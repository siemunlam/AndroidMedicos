package com.siem.siemmedicos.ui;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.iid.FirebaseInstanceId;
import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.ActivityMapBinding;
import com.siem.siemmedicos.db.DBContract;
import com.siem.siemmedicos.model.app.AppLocation;
import com.siem.siemmedicos.model.app.Map;
import com.siem.siemmedicos.utils.Constants;
import com.siem.siemmedicos.utils.PreferencesHelper;
import com.siem.siemmedicos.utils.Utils;

import java.util.ArrayList;

public class MapActivity extends ActivateGpsActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST = 100;

    private BroadcastReceiver mNewAuxilioBroadcastReceiver;
    private ContentObserver mObserver;
    private PreferencesHelper mPreferences;
    private ActivityMapBinding mBinding;
    private Map myMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_map);
        MapFragment fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
        instanceVariables();

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
                myLocationClicked();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        registerBroadcastReceiver();
        registerContentObserver();
        Utils.createSyncAccount(getApplicationContext());
        Utils.syncNow(this);
        Utils.setupContentResolver(this);
        if (checkAllPermissions()) {
            init();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterBroadcastReceiver();
        unregisterContentObserver();
    }

    private void registerBroadcastReceiver() {
        mNewAuxilioBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.BROADCAST_NEW_AUXILIO)) {
                    Toast.makeText(MapActivity.this, getString(R.string.asignNuevoAuxilio), Toast.LENGTH_LONG).show();
                    setearEstado();
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.BROADCAST_NEW_AUXILIO);
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(this);
        bm.registerReceiver(mNewAuxilioBroadcastReceiver, filter);
    }

    private void registerContentObserver() {
        getContentResolver().registerContentObserver(DBContract.Locations.CONTENT_URI, true, mObserver);
    }

    private void unregisterBroadcastReceiver(){
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(this);
        bm.unregisterReceiver(mNewAuxilioBroadcastReceiver);
    }

    private void unregisterContentObserver() {
        if (mObserver != null)
            getContentResolver().unregisterContentObserver(mObserver);
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
        myMap.setMap(googleMap);
        myMap.setZoomControlsEnabled(false);

        mBinding.containerExtraData.bringToFront();
        mBinding.containerButtons.bringToFront();
        setearEstado();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                myLocationClicked();
            }
        }, 2000);
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
        getLocation();
    }

    private void myLocationClicked(){
        AppLocation lastLocation = new AppLocation(Utils.getPassiveLocation(MapActivity.this));
        if(!lastLocation.isNullLocation()){
            //myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLocation.getLatLng(), Constants.NORMAL_ZOOM));
            //myMap.addPositionMarker(lastLocation);
            addMarker(lastLocation.getLocation());
        }
    }

    private void instanceVariables() {
        myMap = new Map(this);
        mPreferences = PreferencesHelper.getInstance();
        mObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            public void onChange(boolean selfChange) {
                newLocation();
            }
        };
    }

    private void newLocation() {
        Log.i("123456789", "New position location");
        Location location = Utils.getLastLocationSaved(this);
        addMarker(location);
    }

    private void addMarker(Location location) {
        if(location != null){
            Log.i("123456789", "New marker location");
            myMap.addPositionMarker(location);
            myMap.controlateInRoute(location);
        }
    }

    private void setearEstado() {
        Log.i("123456789", "PASO2");
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        switch (mPreferences.getEstado()){
            case Constants.EN_AUXILIO:
                Log.i("123456789", "PASO3");
                AppLocation lastLocation = new AppLocation(Utils.getPassiveLocation(MapActivity.this));
                myMap.getDirections(lastLocation);
                myMap.addPositionMarker(lastLocation);
                mBinding.containerButtons.setVisibility(View.VISIBLE);
                mBinding.containerExtraData.setVisibility(View.VISIBLE);
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
