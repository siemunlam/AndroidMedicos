package com.siem.siemmedicos.model.app;

import android.content.ContentValues;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.siem.siemmedicos.db.DBContract;
import com.siem.siemmedicos.utils.Constants;

import java.util.Date;

public class AppLocation {

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

    public boolean isNullLocation(){
        return mLatitude == 0 && mLongitude == 0;
    }

    public void save(Context context){
        Log.i("123456789", "New valid location");
        Date now = new Date();
        ContentValues values = new ContentValues();
        values.put(DBContract.Locations.COLUMN_NAME_LATITUDE, String.valueOf(getLatitude()));
        values.put(DBContract.Locations.COLUMN_NAME_LONGITUDE, String.valueOf(getLongitude()));
        values.put(DBContract.Locations.COLUMN_NAME_ACCURACY, String.valueOf(getAccuracy()));
        values.put(DBContract.Locations.COLUMN_NAME_TIMESTAMP_SAVE, String.valueOf(now.getTime()));
        values.put(DBContract.Locations.COLUMN_NAME_TIME_SAVE, Constants.DATE_COMPLET_FORMAT.format(now));
        values.put(DBContract.Locations.COLUMN_NAME_TIMESTAMP_LOC, String.valueOf(getTime()));
        values.put(DBContract.Locations.COLUMN_NAME_TIME_LOC, Constants.DATE_COMPLET_FORMAT.format(new Date(getTime())));
        values.put(DBContract.Locations.COLUMN_NAME_SPEED, String.valueOf(getSpeed()));
        values.put(DBContract.Locations.COLUMN_NAME_PROVIDER, getProvider());
        values.put(DBContract.Locations.COLUMN_NAME_BEARING, String.valueOf(getBearing()));
        context.getContentResolver().insert(DBContract.Locations.CONTENT_URI, values);
    }

}
