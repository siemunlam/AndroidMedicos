package com.siem.siemmedicos.model.app;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.siem.siemmedicos.R;
import com.siem.siemmedicos.db.DBWrapper;
import com.siem.siemmedicos.model.googlemapsapi.ResponseDirections;
import com.siem.siemmedicos.utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;

public class AppLocation {

    private int _id;
    private double mLatitude;
    private double mLongitude;
    private float mAccuracy;
    private float mBearing;
    private String mProvider;
    private float mSpeed;
    private long mTimestamp;

    public AppLocation(){

    }

    public AppLocation(Location location){
        setLatitude(location.getLatitude());
        setLongitude(location.getLongitude());
        setAccuracy(location.getAccuracy());
        setBearing(location.getBearing());
        setProvider(location.getProvider());
        setSpeed(location.getSpeed());
        setTime(location.getTime());
    }

    public AppLocation(LatLng lastLatLng, float bearing){
        setLatitude(lastLatLng.latitude);
        setLongitude(lastLatLng.longitude);
        setBearing(bearing);
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public AppLocation(LatLng lastLatLng){
        this(lastLatLng, 0);
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public float getAccuracy() {
        return mAccuracy;
    }

    public void setAccuracy(float accuracy) {
        mAccuracy = accuracy;
    }

    public float getBearing() {
        return mBearing;
    }

    public void setBearing(float bearing) {
        mBearing = bearing;
    }

    public String getProvider() {
        return mProvider;
    }

    public void setProvider(String provider) {
        mProvider = provider;
    }

    public float getSpeed() {
        return mSpeed;
    }

    public void setSpeed(float speed) {
        mSpeed = speed;
    }

    public long getTime() {
        return mTimestamp;
    }

    public void setTime(long timestamp) {
        mTimestamp = timestamp;
    }

    public LatLng getLatLng(){
        return new LatLng(mLatitude, mLongitude);
    }

    public boolean isNullLocation(){
        return mLatitude == 0 && mLongitude == 0;
    }

    public Location getLocation() {
        Location location = new Location(getProvider());
        location.setLatitude(getLatitude());
        location.setLongitude(getLongitude());
        location.setBearing(getBearing());
        location.setAccuracy(getAccuracy());
        location.setSpeed(getSpeed());
        return location;
    }

    public void save(Context context){
        DBWrapper.saveLocation(context, this);
    }

    public void getDirections(Context context, Callback<ResponseDirections> callback) {
        Auxilio auxilio = DBWrapper.getAuxilio(context);
        Call<ResponseDirections> callResponseDirections = RetrofitClient.getMapsGoogleClient().getDirections(mLatitude + "," + mLongitude, auxilio.getLatitude() + "," + auxilio.getLongitude(), context.getString(R.string.keyDirectionsGoogleMaps));
        callResponseDirections.enqueue(callback);
    }
}
