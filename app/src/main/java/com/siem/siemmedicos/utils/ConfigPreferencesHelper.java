package com.siem.siemmedicos.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.siem.siemmedicos.Application;

public class ConfigPreferencesHelper {

    private static final String KEY_INTENSIVE_MODE_TIME_TRSHLD = "INTENSIVE_MODE_TIME_TRSHLD";
    private static final int DEFAULT_INTENSIVE_MODE_TIME_TRSHLD = 15000;

    private static final String KEY_INTENSIVE_MODE_DIST_TRSHLD = "INTENSIVE_MODE_DIST_TRSHLD";
    private static final int DEFAULT_INTENSIVE_MODE_DIST_TRSHLD = 40;

    private static final String KEY_INTENSIVE_MODE_BEAR_TRSHLD = "INTENSIVE_MODE_BEAR_TRSHLD";
    private static final int DEFAULT_INTENSIVE_MODE_BEAR_TRSHLD = 30;

    private static final String KEY_INTENSIVE_MODE_BEAR_MIN_SPEED = "INTENSIVE_MODE_BEAR_MIN_SPEED";
    private static final float DEFAULT_INTENSIVE_MODE_BEAR_MIN_SPEED = 10.0f;

    private static final String KEY_NORMAL_MODE_TIME_TRSHLD = "NORMAL_MODE_TIME_TRSHLD";
    private static final int DEFAULT_NORMAL_MODE_TIME_TRSHLD = Constants.ONE_MINUTE;

    private static ConfigPreferencesHelper mInstance = null;
    private SharedPreferences mPreferences;

    private ConfigPreferencesHelper(Context context) {
        mPreferences = context.getSharedPreferences(Constants.NAME_SHAREDPREFERENCES, Context.MODE_PRIVATE);
    }

    public static ConfigPreferencesHelper getInstance(){
        if(mInstance == null){
            synchronized (PreferencesHelper.class) {
                if(mInstance == null){
                    mInstance = new ConfigPreferencesHelper(Application.getInstance());
                }
            }
        }
        return mInstance;
    }

    // KEY_INTENSIVE_MODE_TIME_TRSHLD
    public int getIntensiveModeTimeTrshld(){
        return mPreferences.getInt(KEY_INTENSIVE_MODE_TIME_TRSHLD, DEFAULT_INTENSIVE_MODE_TIME_TRSHLD);
    }

    public void setIntensiveModeTimeTrshld(int value){
        mPreferences.edit().putInt(KEY_INTENSIVE_MODE_TIME_TRSHLD, value).apply();
    }


    // KEY_INTENSIVE_MODE_DIST_TRSHLD
    public int getIntensiveModeDistTrshld(){
        return mPreferences.getInt(KEY_INTENSIVE_MODE_DIST_TRSHLD, DEFAULT_INTENSIVE_MODE_DIST_TRSHLD);
    }

    public void setIntensiveModeDistTrshld(int value){
        mPreferences.edit().putInt(KEY_INTENSIVE_MODE_DIST_TRSHLD, value).apply();
    }


    // KEY_INTENSIVE_MODE_BEAR_TRSHLD
    public int getIntensiveModeBearTrshld(){
        return mPreferences.getInt(KEY_INTENSIVE_MODE_BEAR_TRSHLD, DEFAULT_INTENSIVE_MODE_BEAR_TRSHLD);
    }

    public void setIntensiveModeBearTrshld(int value){
        mPreferences.edit().putInt(KEY_INTENSIVE_MODE_BEAR_TRSHLD, value).apply();
    }


    // KEY_INTENSIVE_MODE_BEAR_MIN_SPEED
    public float getIntensiveModeBearMinSpeed(){
        return mPreferences.getFloat(KEY_INTENSIVE_MODE_BEAR_MIN_SPEED, DEFAULT_INTENSIVE_MODE_BEAR_MIN_SPEED);
    }

    public void setIntensiveModeBearMinSpeed(float value){
        mPreferences.edit().putFloat(KEY_INTENSIVE_MODE_BEAR_MIN_SPEED, value).apply();
    }

    // KEY_NORMAL_MODE_TIME_TRSHLD
    public int getNormalModeTimeTrshld(){
        return mPreferences.getInt(KEY_NORMAL_MODE_TIME_TRSHLD, DEFAULT_NORMAL_MODE_TIME_TRSHLD);
    }

    public void setNormalModeTimeTrshld(int value){
        mPreferences.edit().putInt(KEY_NORMAL_MODE_TIME_TRSHLD, value).apply();
    }

}
