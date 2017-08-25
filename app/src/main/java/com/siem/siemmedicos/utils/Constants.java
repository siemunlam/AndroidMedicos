package com.siem.siemmedicos.utils;

import android.Manifest;
import android.content.Context;

import com.siem.siemmedicos.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constants {

    /**
     * Maps
     */
    public static final float INITIAL_ZOOM = 1.0F;
    public static final float NORMAL_ZOOM = 16.0F;
    public static final float EMERGENCY_ZOOM = 17.5F;

    /**
     * Fonts
     */
    public static final String PRIMARY_FONT = "fonts/rounded_elegance.ttf";

    /**
     * Estados
     */
    public static class Disponible {
        private static int value = 1;

        public static int getValue() {
            return value;
        }

        public static String getDescription(Context context) {
            return context.getString(R.string.medicoEstadoDisponible);
        }
    }

    public static class NoDisponible {
        private static int value = 2;

        public static int getValue() {
            return value;
        }

        public static String getDescription(Context context) {
            return context.getString(R.string.medicoEstadoNoDisponible);
        }
    }

    public static class EnAuxilio {
        public static final int value = 3;

        public static int getValue() {
            return value;
        }

        public static String getDescription(Context context) {
            return context.getString(R.string.medicoEstadoEnAuxilio);
        }
    }

    /**
     * SyncAdapter
     */
    public static final int ONE_MINUTE = 60 * 1000;
    public static final long SYNC_INTERVAL = ONE_MINUTE * 5;
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
     * Push Notifications Keys
     */
    public static final String KEY_LATITUDE = "Lat";
    public static final String KEY_LONGITUDE = "Lng";
    public static final String KEY_DIRECCION = "Direccion";
    public static final String KEY_PACIENTE = "Paciente";
    public static final String KEY_COLOR_DESCRIPCION = "ColorDescripcion";
    public static final String KEY_COLOR_HEXA = "ColorHexa";
    public static final String KEY_MOTIVOS = "Motivos";
    public static final String KEY_MOTIVO = "Motivo";

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
     * Calls API Google
     */
    public static final String API_GOOGLEMAPS_DIRECTIONS = "maps/api/directions/json";
    public static final String ORIGIN = "origin";
    public static final String DESTINATION = "destination";
    public static final String KEY = "key";

    /**
     * Calls API Server
     */
    public static final int CODE_SERVER_OK = 200;
    public static final int CODE_BAD_REQUEST = 400;
    public static final String API_LOGIN = "auth/token/";
    public static final String API_LOGOUT = "medicos/logout/";

    /**
     * Receivers
     */
    public static final String BROADCAST_NEW_AUXILIO = "com.siem.siemmedicos.new_auxilio";

}
