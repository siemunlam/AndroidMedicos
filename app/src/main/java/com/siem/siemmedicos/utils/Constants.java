package com.siem.siemmedicos.utils;

import android.Manifest;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constants {

    /**
     * Maps
     */
    public static final float INITIAL_ZOOM = 16.0F;
    public static final float EMERGENCY_ZOOM = 19.0F;

    /**
     * Estados
     */
    public static final int EN_ESPERA = 0;
    public static final int EN_AUXILIO = 1;
    public static final int EN_AUXILIO_CASUAL = 2;
    public static final int NO_DISPONIBLE = 3;

    /**
     * SyncAdapter
     */
    public static final int ONE_MINUTE = 60 * 1000;
    public static final long SYNC_INTERVAL = ONE_MINUTE;
    public static final String DEMO_ACCOUNT_NAME = "demo@siemmedicos.com";

    /**
     * Shared preferences constants
     */
    static final String NAME_SHAREDPREFERENCES = "SIEM_MEDICOS_PREFERENCES";

    /**
     * Notification Services in Foreground
     */
    public static final int ID_NOTIFICATION_SERVICE = 1001;

    /**
     * Permissions
     */
    public static final String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    /**
     * Date
     */
    public static final SimpleDateFormat DATE_COMPLET_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    public static final SimpleDateFormat DATE_USER_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);

    /**
     * Calls API
     */
    public static final String API_GOOGLEMAPS_DIRECTIONS = "maps/api/directions/json";
    public static final String ORIGIN = "origin";
    public static final String DESTINATION = "destination";
    public static final String KEY = "key";

    /**
     * Receivers
     */
    public static final String BROADCAST_NEW_AUXILIO = "com.siem.siemmedicos.new_auxilio";

}