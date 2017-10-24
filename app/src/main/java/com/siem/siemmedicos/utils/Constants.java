package com.siem.siemmedicos.utils;

import android.Manifest;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constants {

    /**
     * Notification ID
     */
    public static final int NOTIFICATION_ID = 1010;

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
    public static final String LOGO_FONT = "fonts/logo_font.ttf";

    /**
     * SyncAdapter
     */
    public static final int ONE_MINUTE = 60 * 1000;
    public static final long SYNC_INTERVAL = ONE_MINUTE / 2;
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
    public static final String KEY_LATITUDE = "lat";
    public static final String KEY_LONGITUDE = "long";
    public static final String KEY_DIRECCION = "direccion";
    public static final String KEY_PACIENTE = "paciente";
    public static final String KEY_COLOR_DESCRIPCION = "colorDescripcion";
    public static final String KEY_COLOR_HEXA = "colorHexa";
    public static final String KEY_MOTIVOS = "motivos";
    public static final String KEY_SEXO = "sexo";
    public static final String KEY_OBSERVACIONES = "observaciones";
    public static final String KEY_REFERENCIA = "referencia";
    public static final String KEY_CONTACTO = "contacto";

    public static final int CODE_CANCEL_AUXILIO = 25;
    public static final String KEY_CODE = "code";

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
     * API Server
     */
    public static final int CODE_SERVER_OK = 200;
    public static final int CODE_201 = 201;
    public static final int CODE_MEDICO_NOT_ASIGNED = 208;
    public static final int CODE_BAD_REQUEST = 400;
    public static final int CODE_UNAUTHORIZED = 401;
    public static final int CODE_SERVER_ERROR = 500;
    public static final String API_LOGIN = "auth/token/";
    public static final String API_LOGOUT = "medicos/logout/";
    public static final String API_UPDATE_FCM = "medicos/fcmUpdate/";
    public static final String API_UPDATE_UBICACION = "medicos/ubicacionUpdate/";
    public static final String API_UPDATE_ESTADO_MEDICO = "medicos/estadoUpdate/";
    public static final String API_DESVINCULAR_AUXILIO = "medicos/desvincularAsignacion/";
    public static final String API_FINALIZAR_AUXILIO = "medicos/finalizarAsignacion/";
    public static final String API_UPDATE_ESTADO_ASIGNACION = "medicos/estadoAsignacionUpdate/";

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String AUTHORIZATION = "authorization";
    public static final String FCM_CODE = "fcm_code";
    public static final String LATITUD = "latitud_gps";
    public static final String LONGITUD = "longitud_gps";
    public static final String TIMESTAMP = "timestamp_gps";
    public static final String ESTADO = "estado";

    /**
     * Receivers
     */
    public static final String BROADCAST_NEW_AUXILIO = "com.siem.siemmedicos.new_auxilio";
    public static final String BROADCAST_CANCEL_AUXILIO = "com.siem.siemmedicos.cancel_auxilio";

    /**
     * Cambio de estado de la asignacion
     */
    public static final int MIN_DISTANCE_CHANGE_TO_ENLUGAR = 200;
    public static final int MIN_DISTANCE_CHANGE_TO_ENTRASLADO = 400;

}
