package com.siem.siemmedicos.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.ActivityMapBinding;
import com.siem.siemmedicos.model.app.LastLocation;
import com.siem.siemmedicos.services.SelectLocationService;
import com.siem.siemmedicos.utils.Constants;
import com.siem.siemmedicos.utils.PreferencesHelper;
import com.siem.siemmedicos.utils.Utils;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST = 100;

    private PreferencesHelper mPreferences;
    private ActivityMapBinding mBinding;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_map);
        MapFragment fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);

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

        mBinding.myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                LatLng lastLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, Constants.INITIAL_ZOOM));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.createSyncAccount(getApplicationContext());
        Utils.syncNow(this);
        Utils.setupContentResolver(this);
        if (checkAllPermissions()) {
            checkGpsOn();
            init();
        }
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

        LastLocation lastLocation = new LastLocation();
        if (!lastLocation.isNullLocation())
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLocation.getLocation(), Constants.INITIAL_ZOOM));
        else
            mMap.animateCamera(CameraUpdateFactory.newLatLng(lastLocation.getLocation()));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mBinding.containerExtraData.bringToFront();
        mBinding.containerButtons.bringToFront();
        setearEstado();
    }

    private void checkGpsOn() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            new FragmentDialog().getTextViewDialog(
                    MapActivity.this,
                    "Debe activar el GPS para continuar.",
                    getString(R.string.accept),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    },
                    null,
                    null,
                    false
            ).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean allPermissionGranted = true;
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                allPermissionGranted = false;
            }
        }

        if (allPermissionGranted)
            init();
        else
            checkAllPermissions();
    }

    private void init() {
        startService(new Intent(MapActivity.this, SelectLocationService.class));
        instanceVariables();
    }

    private void instanceVariables() {
        mPreferences = PreferencesHelper.getInstance();
    }

    private void setearEstado() {
        Log.i("123456789", "PASO2");
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        switch (mPreferences.getEstado()){
            case Constants.EN_AUXILIO:
                Log.i("123456789", "PASO3");
                mBinding.containerButtons.setVisibility(View.VISIBLE);
                mBinding.containerExtraData.setVisibility(View.VISIBLE);
                mMap.animateCamera(CameraUpdateFactory.zoomTo(Constants.EMERGENCY_ZOOM));
                lp.setMargins(0, 0, (int) getResources().getDimension(R.dimen.defaultMargin), (int) (getResources().getDimension(R.dimen.defaultMargin) + getResources().getDimension(R.dimen.heightContainerButtons)));
                mBinding.myLocationButton.setLayoutParams(lp);
                break;
            default:
                Log.i("123456789", "PASO4");
                mBinding.containerButtons.setVisibility(View.GONE);
                mBinding.containerExtraData.setVisibility(View.GONE);
                lp.setMargins(0, 0, (int) getResources().getDimension(R.dimen.defaultMargin), (int) getResources().getDimension(R.dimen.defaultMargin));
                mBinding.myLocationButton.setLayoutParams(lp);
                break;
        }
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
