package com.siem.siemmedicos.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.siem.siemmedicos.R;
import com.siem.siemmedicos.services.SelectLocationService;
import com.siem.siemmedicos.utils.Utils;

/**
 * Created by Lucas on 7/8/17.
 */

public class ActivateGpsActivity extends ToolbarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final int REQUEST_LOCATION = 1221;

    private GoogleApiClient mGoogleApiClient;
    private PendingResult<LocationSettingsResult> mPendingResult;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest.Builder mLocationSettingsRequest;

    protected void getLocation(){
        if(!Utils.isGPSOn(this)){
            mGoogleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mGoogleApiClient.connect();
            mLocationSetting();
        }else{
            startLocationServices();
        }
    }

    private void startLocationServices() {
        startService(new Intent(ActivateGpsActivity.this, SelectLocationService.class));
    }

    public void mLocationSetting() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        mResult();
    }

    public void mResult() {
        mPendingResult = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mLocationSettingsRequest.build());
        mPendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationServices();
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(ActivateGpsActivity.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            requestActivateGPS();
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        requestActivateGPS();
                        break;
                }
            }

        });
    }

    private void requestActivateGPS() {
        Toast.makeText(ActivateGpsActivity.this, getString(R.string.activateGPS), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationServices();
                        break;

                    case Activity.RESULT_CANCELED:
                        requestActivateGPS();
                        break;

                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
