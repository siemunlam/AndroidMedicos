package com.siem.siemmedicos.model.app;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.siem.siemmedicos.R;
import com.siem.siemmedicos.model.googlemapsapi.ResponseDirections;
import com.siem.siemmedicos.utils.PreferencesHelper;
import com.siem.siemmedicos.utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LastLocation {

    private PreferencesHelper mPreferences;
    private LatLng mLocation;

    public LastLocation(){
        mPreferences = PreferencesHelper.getInstance();
        Double lat = Double.parseDouble(mPreferences.getLastLatitude("0"));
        Double lng = Double.parseDouble(mPreferences.getLastLongitude("0"));
        mLocation = new LatLng(lat, lng);
    }

    public LastLocation(LatLng lastLatLng){
        mLocation = lastLatLng;
    }

    public LatLng getLocation(){
        return mLocation;
    }

    public boolean isNullLocation(){
        return mLocation.latitude == 0 && mLocation.longitude == 0;
    }

    public void getDirections(Context context, Callback<ResponseDirections> callback) {
        PreferencesHelper preferences = PreferencesHelper.getInstance();
        String latitude = preferences.getLatitudeAuxilio();
        String longitude = preferences.getLongitudeAuxilio();
        Call<ResponseDirections> callResponseDirections = RetrofitClient.getMapsGoogleClient().getDirections(mLocation.latitude + "," + mLocation.longitude, latitude + "," + longitude, context.getString(R.string.keyDirectionsGoogleMaps));
        callResponseDirections.enqueue(callback);
    }
}
