package com.siem.siemmedicos.ui;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.icu.text.LocaleDisplayNames;
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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.ActivityMapBinding;
import com.siem.siemmedicos.model.app.LastLocation;
import com.siem.siemmedicos.model.googlemapsapi.ResponseDirections;
import com.siem.siemmedicos.model.googlemapsapi.Step;
import com.siem.siemmedicos.services.SelectLocationService;
import com.siem.siemmedicos.utils.Constants;
import com.siem.siemmedicos.utils.PreferencesHelper;
import com.siem.siemmedicos.utils.RetrofitClient;
import com.siem.siemmedicos.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, Callback<ResponseDirections> {

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
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Utils.getPassiveLocation(MapActivity.this), Constants.INITIAL_ZOOM));
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
                ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(FirebaseInstanceId.getInstance().getToken(), FirebaseInstanceId.getInstance().getToken());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, FirebaseInstanceId.getInstance().getToken(), Toast.LENGTH_LONG).show();
                return true;
            case R.id.menuLogout:
                Utils.logout();
                Intent intent = new Intent(MapActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LastLocation lastLocation = new LastLocation(Utils.getPassiveLocation(MapActivity.this));
        if (!lastLocation.isNullLocation())
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLocation.getLocation(), Constants.INITIAL_ZOOM));
        else
            mMap.animateCamera(CameraUpdateFactory.newLatLng(lastLocation.getLocation()));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }mMap.setMyLocationEnabled(false);
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
                LastLocation lastLocation = new LastLocation(Utils.getPassiveLocation(MapActivity.this));
                lastLocation.getDirections(this, this);
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

    @Override
    public void onResponse(@NonNull Call<ResponseDirections> call, @NonNull Response<ResponseDirections> response) {
        try{
            ResponseDirections responseDirections = response.body();
            ArrayList<Step> steps = responseDirections.getSteps();
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.add(steps.get(0).getStartLocation());
            for (Step step : steps) {
                polylineOptions.add(step.getEndLocation());
            }
            polylineOptions.width(25);
            polylineOptions.color(ContextCompat.getColor(MapActivity.this, R.color.polyline));
            Polyline line = mMap.addPolyline(polylineOptions);
        }catch(Exception e){
            Toast.makeText(MapActivity.this, getString(R.string.error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<ResponseDirections> call, Throwable t) {
        Toast.makeText(MapActivity.this, getString(R.string.error), Toast.LENGTH_LONG).show();
    }
}
