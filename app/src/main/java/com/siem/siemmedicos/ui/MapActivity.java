package com.siem.siemmedicos.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.ActivityMapBinding;
import com.siem.siemmedicos.services.SelectLocationService;
import com.siem.siemmedicos.utils.Constants;
import com.siem.siemmedicos.utils.Utils;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST = 100;

    private ActivityMapBinding mBinding;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_map);
        mBinding.containerExtraData.bringToFront();
        MapFragment fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);

        if(checkAllPermissions()){
            init();
        }

        mBinding.buttonUnlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FragmentDialog().getTextViewDialog(MapActivity.this, getString(R.string.confirmUnlink), getString(R.string.accept), null, getString(R.string.cancel), null, false).show();
            }
        });

        mBinding.buttonFinalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FragmentDialog().getTextViewDialog(MapActivity.this, getString(R.string.confirmFinalize), getString(R.string.accept), null, getString(R.string.cancel), null, false).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.createSyncAccount(getApplicationContext());
        Utils.syncNow(this);
        Utils.setupContentResolver(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuUpdateStatus:
                return true;
            case R.id.menuLogout:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean allPermissionGranted = true;
        for (int i = 0; i < permissions.length; i++) {
            if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                allPermissionGranted = false;
            }
        }

        if(allPermissionGranted)
            init();
        else
            checkAllPermissions();
    }

    private void init(){
        startService(new Intent(MapActivity.this, SelectLocationService.class));
    }

    private boolean checkAllPermissions() {
        ArrayList<String> deniedPermissions = new ArrayList<>();
        for (String permission : Constants.permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
            }
        }

        if (deniedPermissions.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    deniedPermissions.toArray(new String[deniedPermissions.size()]),
                    PERMISSIONS_REQUEST);
            return false;
        }
        return true;
    }
}
