package com.siem.siemmedicos.utils;

import android.Manifest;

public class Constants {

    /**
     * SyncAdapter
     */
    public static final int ONE_MINUTE = 60 * 1000;
    public static final long SYNC_INTERVAL = ONE_MINUTE;
    public static final String DEMO_ACCOUNT_NAME = "demo@siemmedicos.com";

    /**
     * Permissions
     */
    public static final String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    /**
     * Calls API
     */
    public static final String API_GOOGLEMAPS_DIRECTIONS = "maps/api/directions/json";
    public static final String ORIGIN = "origin";
    public static final String DESTINATION = "destination";
    public static final String KEY = "key";

}
