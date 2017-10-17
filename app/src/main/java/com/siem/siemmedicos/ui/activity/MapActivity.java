package com.siem.siemmedicos.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.ActivityMapBinding;
import com.siem.siemmedicos.db.DBContract;
import com.siem.siemmedicos.db.DBWrapper;
import com.siem.siemmedicos.model.app.AppLocation;
import com.siem.siemmedicos.model.app.Auxilio;
import com.siem.siemmedicos.model.app.Map;
import com.siem.siemmedicos.ui.custom.CustomFragmentDialog;
import com.siem.siemmedicos.utils.ApiConstants;
import com.siem.siemmedicos.utils.Constants;
import com.siem.siemmedicos.utils.PreferencesHelper;
import com.siem.siemmedicos.utils.RetrofitClient;
import com.siem.siemmedicos.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends ActivateGpsActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST = 100;
    private static final int LOGOUT_ACTIVITY = 105;
    private static final int FINALIZAR_AUXILIO = 110;
    private static final int DETALLE_AUXILIO = 115;

    private BroadcastReceiver mBroadcastReceiver;
    private ContentObserver mLocationObserver;
    private ContentObserver mAuxilioObserver;
    private PreferencesHelper mPreferencesHelper;
    private ActivityMapBinding mBinding;
    private Typeface mTypeface;
    private Map myMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_map);
        setToolbar(false);
        mTypeface = Typeface.createFromAsset(getAssets(), Constants.PRIMARY_FONT);
        MapFragment fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
        instanceVariables();

        mBinding.buttonUnlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CustomFragmentDialog().getTextViewDialog(
                        MapActivity.this,
                        getString(R.string.confirmUnlink),
                        getString(R.string.accept),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                desvincularAuxilio();
                            }
                        },
                        getString(R.string.cancel),
                        null,
                        false
                ).show();
            }
        });

        mBinding.buttonFinalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.startActivityWithTransitionForResult(MapActivity.this, new Intent(MapActivity.this, FinalizarAuxilioActivity.class), FINALIZAR_AUXILIO);
            }
        });

        mBinding.myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myLocationClicked();
            }
        });

        mBinding.containerDetallesAuxilio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.startActivityWithTransitionForResult(MapActivity.this, new Intent(MapActivity.this, DetalleAuxilioActivity.class), DETALLE_AUXILIO);
            }
        });
        setTypeface();
    }

    @Override
    public void onBackPressed(){
        supportFinishAfterTransition();
        Utils.addDefaultFinishTransitionAnimation(this);
    }

    private void setTypeface() {
        mBinding.buttonUnlink.setTypeface(mTypeface);
        mBinding.buttonFinalize.setTypeface(mTypeface);
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

    @Override
    public void onDestroy(){
        super.onDestroy();
        myMap.onDestroy();
    }

    private void registerBroadcastReceiver() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.BROADCAST_NEW_AUXILIO)) {
                    Toast.makeText(MapActivity.this, getString(R.string.asignNuevoAuxilio), Toast.LENGTH_LONG).show();
                    setearEstado();
                    invalidateOptionsMenu();
                }else if(intent.getAction().equals(Constants.BROADCAST_CANCEL_AUXILIO)){
                    Toast.makeText(MapActivity.this, getString(R.string.cancelAuxilio), Toast.LENGTH_LONG).show();
                    changeEstadoAuxilio();
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.BROADCAST_NEW_AUXILIO);
        filter.addAction(Constants.BROADCAST_CANCEL_AUXILIO);
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(this);
        bm.registerReceiver(mBroadcastReceiver, filter);
    }

    private void registerContentObserver() {
        getContentResolver().registerContentObserver(DBContract.Locations.CONTENT_URI, true, mLocationObserver);
        getContentResolver().registerContentObserver(DBContract.InformacionAuxilio.CONTENT_URI, true, mAuxilioObserver);
    }

    private void unregisterBroadcastReceiver(){
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(this);
        bm.unregisterReceiver(mBroadcastReceiver);
    }

    private void unregisterContentObserver() {
        if (mLocationObserver != null)
            getContentResolver().unregisterContentObserver(mLocationObserver);

        if(mAuxilioObserver != null)
            getContentResolver().unregisterContentObserver(mAuxilioObserver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mPreferencesHelper.getValueEstado() != new ApiConstants.EnAuxilio().getValue()){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_map, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Utils.syncNow(MapActivity.this);
        switch (item.getItemId()) {
            case R.id.menuUpdateStatus:
                if(mPreferencesHelper.isSendLocation()){
                    new CustomFragmentDialog().getRadioButtonsEstadoDialog(this, getString(R.string.accept), true).show();
                }else{
                    Toast.makeText(MapActivity.this, getString(R.string.errorNoLocation), Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.menuLogout:
                startActivityForResult(new Intent(MapActivity.this, LogoutActivity.class), LOGOUT_ACTIVITY);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode){
            case LOGOUT_ACTIVITY:
                if(resultCode == Activity.RESULT_OK){
                    Intent intentLogin = new Intent(MapActivity.this, LoginActivity.class);
                    intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intentLogin);
                    finish();
                }
                break;

            case FINALIZAR_AUXILIO:
                if(resultCode == Activity.RESULT_OK){
                    int code = intent.getIntExtra(FinalizarAuxilioActivity.KEY_RESPONSE_CODE, 0);
                    switch(code){
                        case Constants.CODE_SERVER_OK:
                        case Constants.CODE_201:
                        case Constants.CODE_BAD_REQUEST:
                        case Constants.CODE_MEDICO_NOT_ASIGNED:
                            changeEstadoAuxilio();
                            break;

                        case Constants.CODE_UNAUTHORIZED:
                            Utils.logout(MapActivity.this);
                            finish();
                            break;
                    }
                }
                break;

            case DETALLE_AUXILIO:
                if(resultCode == Activity.RESULT_OK){
                    int code = intent.getIntExtra(DetalleAuxilioActivity.KEY_CANCEL_AUXILIO, 0);
                    switch(code){
                        case DetalleAuxilioActivity.CODE_CANCEL_AUXILIO:
                            changeEstadoAuxilio();
                            break;
                    }
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap.setMap(googleMap);
        myMap.setZoomControlsEnabled(false);

        mBinding.cardDetallesAuxilio.bringToFront();
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
        if(myMap.isReady())
            setearEstado();
        getLocation();
    }

    private void myLocationClicked(){
        AppLocation lastLocation = new AppLocation(Utils.getPassiveLocation(MapActivity.this));
        if(!lastLocation.isNullLocation()){
            addMarker(lastLocation.getLocation());
        }

        if(!mPreferencesHelper.isSendLocation()){
            lastLocation.save(this);
        }
    }

    private void instanceVariables() {
        myMap = new Map(this);
        mPreferencesHelper = PreferencesHelper.getInstance();
        mLocationObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            public void onChange(boolean selfChange) {
                newLocation();
            }
        };

        mAuxilioObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            public void onChange(boolean selfChange) {
                determinateFinalizeButtonVisibility(DBWrapper.getAuxilio(MapActivity.this));
            }
        };
    }

    private void newLocation() {
        Location location = Utils.getLastLocationSaved(this);
        addMarker(location);
    }

    private void addMarker(Location location) {
        if(location != null){
            myMap.addPositionMarker(location);
            myMap.controlateInRoute(location);
        }
    }

    private void setearEstado() {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        switch (mPreferencesHelper.getValueEstado()){
            case ApiConstants.EnAuxilio.value:
                AppLocation lastLocation = new AppLocation(Utils.getPassiveLocation(MapActivity.this));
                myMap.getDirections(lastLocation);
                myMap.addPositionMarker(lastLocation);
                Auxilio auxilio = DBWrapper.getAuxilio(this);
                mBinding.containerDetallesAuxilio.setDatos(auxilio, false);
                mBinding.containerButtons.setVisibility(View.VISIBLE);
                mBinding.cardDetallesAuxilio.setVisibility(View.VISIBLE);
                lp.setMargins(0, 0, (int) getResources().getDimension(R.dimen.defaultMargin), (int) (getResources().getDimension(R.dimen.defaultMargin) + getResources().getDimension(R.dimen.heightContainerButtons)));
                mBinding.myLocationButton.setLayoutParams(lp);
                determinateFinalizeButtonVisibility(auxilio);
                break;
            default:
                mBinding.containerButtons.setVisibility(View.GONE);
                mBinding.cardDetallesAuxilio.setVisibility(View.GONE);
                lp.setMargins(0, 0, (int) getResources().getDimension(R.dimen.defaultMargin), (int) getResources().getDimension(R.dimen.defaultMargin));
                mBinding.myLocationButton.setLayoutParams(lp);
                invalidateOptionsMenu();
                myMap.cleanPolyline();
                break;
        }
    }

    private void determinateFinalizeButtonVisibility(Auxilio auxilio) {
        if(auxilio.getIdEstado() == new ApiConstants.EnLugar().getValue() ||
                auxilio.getIdEstado() == new ApiConstants.EnTraslado().getValue()){
            if(mBinding.buttonFinalize.getVisibility() != View.VISIBLE){
                Animation animation = AnimationUtils.loadAnimation(MapActivity.this, R.anim.slide_in_right_without_transparency);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mBinding.buttonFinalize.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                mBinding.buttonFinalize.setAnimation(animation);
                mBinding.buttonFinalize.startAnimation(animation);
            }
        }else{
            mBinding.buttonFinalize.setVisibility(View.GONE);
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

    private void desvincularAuxilio() {
        Call<Object> response = RetrofitClient.getServerClient().desvincularAuxilio(mPreferencesHelper.getAuthorization());
        response.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                switch(response.code()){
                    case Constants.CODE_SERVER_OK:
                    case Constants.CODE_BAD_REQUEST:
                    case Constants.CODE_MEDICO_NOT_ASIGNED:
                        changeEstadoAuxilio();
                        break;
                    case Constants.CODE_UNAUTHORIZED:
                        Utils.logout(MapActivity.this);
                        finish();
                        break;
                    default:
                        Toast.makeText(MapActivity.this, getString(R.string.errorUnlink), Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(MapActivity.this, getString(R.string.errorUnlink), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void changeEstadoAuxilio() {
        //Cuando te desvinculas del auxilio o cuando finalizas el auxilio
        Utils.updateEstado(MapActivity.this, new ApiConstants.NoDisponible());
        myMap.cleanMapAuxilio();
        setearEstado();
        myLocationClicked();
    }
}
