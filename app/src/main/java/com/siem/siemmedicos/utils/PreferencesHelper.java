package com.siem.siemmedicos.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.siem.siemmedicos.Application;

public class PreferencesHelper {

    private static final String KEY_LAST_LATITUDE = "LAST_LOCATION_LATITUDE";
    private static final String KEY_LAST_LONGITUDE = "LAST_LOCATION_LONGITUDE";
    private static final String KEY_LAST_LOCATION_TIME = "LAST_LOCATION_TIME";
    private static final String KEY_LAST_BEARING = "LAST_LOCATION_BEARING";
    private static final String KEY_LAST_PROVIDER = "LAST_LOCATION_PROVIDER";

    private static PreferencesHelper mInstance = null;
    private SharedPreferences mPreferences;

    private PreferencesHelper(Context context) {
        mPreferences = context.getSharedPreferences(Constants.NAME_SHAREDPREFERENCES, Context.MODE_PRIVATE);
    }

    public static PreferencesHelper getInstance(){
        if(mInstance == null){
            synchronized (PreferencesHelper.class) {
                if(mInstance == null){
                    mInstance = new PreferencesHelper(Application.getInstance());
                }
            }
        }
        return mInstance;
    }

    // KEY_LAST_LATITUDE
    public String getLastLatitude(){
        return mPreferences.getString(KEY_LAST_LATITUDE, null);
    }

    public String getLastLatitude(String defaultValue){
        return mPreferences.getString(KEY_LAST_LATITUDE, defaultValue);
    }

    public void setLastLatitude(String value){
        mPreferences.edit().putString(KEY_LAST_LATITUDE, value).apply();
    }


    // KEY_LAST_LONGITUDE
    public String getLastLongitude(){
        return mPreferences.getString(KEY_LAST_LONGITUDE, null);
    }

    public String getLastLongitude(String defaultValue){
        return mPreferences.getString(KEY_LAST_LONGITUDE, defaultValue);
    }

    public void setLastLongitude(String value){
        mPreferences.edit().putString(KEY_LAST_LONGITUDE, value).apply();
    }


    // KEY_LAST_LOCATION_TIME
    public long getLastLocationTime(){
        return mPreferences.getLong(KEY_LAST_LOCATION_TIME, 0);
    }

    public void setLastLocationTime(long value){
        mPreferences.edit().putLong(KEY_LAST_LOCATION_TIME, value).apply();
    }


    // KEY_LAST_BEARING
    public float getLastLocationBearing(float defaultValue){
        return mPreferences.getFloat(KEY_LAST_BEARING, defaultValue);
    }

    public void setLastLocationBearing(float value){
        mPreferences.edit().putFloat(KEY_LAST_BEARING, value).apply();
    }


    // KEY_LAST_PROVIDER
    public String getLastProvider(String defaultValue){
        return mPreferences.getString(KEY_LAST_PROVIDER, defaultValue);
    }

    public void setLastProvider(String value){
        mPreferences.edit().putString(KEY_LAST_PROVIDER, value).apply();
    }

}
