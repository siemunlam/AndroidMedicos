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
    private static final String KEY_LATITUDE_AUXILIO = "KEY_LATITUDE_AUXILIO";
    private static final String KEY_LONGITUDE_AUXILIO = "KEY_LONGITUDE_AUXILIO";
    private static final String KEY_MEDICO_TOKEN = "KEY_MEDICO_TOKEN";
    private static final String KEY_ESTADO = "KEY_ESTADO";

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

    public void cleanLastLatitude(){
        mPreferences.edit().remove(KEY_LAST_LATITUDE).apply();
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

    public void cleanLastLongitude(){
        mPreferences.edit().remove(KEY_LAST_LONGITUDE).apply();
    }


    // KEY_LAST_LOCATION_TIME
    public long getLastLocationTime(){
        return mPreferences.getLong(KEY_LAST_LOCATION_TIME, 0);
    }

    public void setLastLocationTime(long value){
        mPreferences.edit().putLong(KEY_LAST_LOCATION_TIME, value).apply();
    }

    public void cleanLastLocationTime(){
        mPreferences.edit().remove(KEY_LAST_LOCATION_TIME).apply();
    }


    // KEY_LAST_BEARING
    public float getLastLocationBearing(float defaultValue){
        return mPreferences.getFloat(KEY_LAST_BEARING, defaultValue);
    }

    public void setLastLocationBearing(float value){
        mPreferences.edit().putFloat(KEY_LAST_BEARING, value).apply();
    }

    public void cleanLastLocationBearing(){
        mPreferences.edit().remove(KEY_LAST_BEARING).apply();
    }


    // KEY_LAST_PROVIDER
    public String getLastProvider(String defaultValue){
        return mPreferences.getString(KEY_LAST_PROVIDER, defaultValue);
    }

    public void setLastProvider(String value){
        mPreferences.edit().putString(KEY_LAST_PROVIDER, value).apply();
    }

    public void cleanLastProvider(){
        mPreferences.edit().remove(KEY_LAST_PROVIDER).apply();
    }


    //KEY_LATITUDE_AUXILIO
    public String getLatitudeAuxilio(){
        return mPreferences.getString(KEY_LATITUDE_AUXILIO, null);
    }

    public void setLatitudeAuxilio(String value){
        mPreferences.edit().putString(KEY_LATITUDE_AUXILIO, value).apply();
    }

    public void cleanLatitudeAuxilio(){
        mPreferences.edit().remove(KEY_LATITUDE_AUXILIO).apply();
    }


    //KEY_LONGITUDE_AUXILIO
    public String getLongitudeAuxilio(){
        return mPreferences.getString(KEY_LONGITUDE_AUXILIO, null);
    }

    public void setLongitudeAuxilio(String value){
        mPreferences.edit().putString(KEY_LONGITUDE_AUXILIO, value).apply();
    }

    public void cleanLongitudeAuxilio(){
        mPreferences.edit().remove(KEY_LONGITUDE_AUXILIO).apply();
    }


    //KEY_MEDICO_TOKEN
    public String getMedicoToken(){
        return mPreferences.getString(KEY_MEDICO_TOKEN, null);
    }

    public void setMedicoToken(String value){
        mPreferences.edit().putString(KEY_MEDICO_TOKEN, value).apply();
    }

    public void cleanMedicoToken(){
        mPreferences.edit().remove(KEY_MEDICO_TOKEN).apply();
    }


    //KEY_ESTADO
    public int getEstado(){
        return mPreferences.getInt(KEY_ESTADO, Constants.EN_ESPERA);
    }

    public void setEstado(int estado){
        mPreferences.edit().putInt(KEY_ESTADO, estado).apply();
    }

    public void cleanEstado(){
        mPreferences.edit().remove(KEY_ESTADO).apply();
    }

}
