package com.siem.siemmedicos.utils;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;
import com.siem.siemmedicos.R;
import com.siem.siemmedicos.db.DBContract;
import com.siem.siemmedicos.db.DBWrapper;
import com.siem.siemmedicos.model.app.AppLocation;
import com.siem.siemmedicos.services.IntensiveLocationService;
import com.siem.siemmedicos.services.LocationService;
import com.siem.siemmedicos.services.SelectLocationService;
import com.siem.siemmedicos.task.DeterminarEstadoAsignacionTask;

import java.util.Calendar;
import java.util.TimeZone;

public class Utils {

    public static void logout(Context context){
        //TODO: Corroborar que este todo borrado
        PreferencesHelper preferences = PreferencesHelper.getInstance();
        preferences.cleanDescriptionEstado();
        preferences.cleanValueEstado();
        preferences.cleanLastLatitude();
        preferences.cleanLastLocationBearing();
        preferences.cleanLastLocationTime();
        preferences.cleanLastLongitude();
        preferences.cleanLastProvider();
        preferences.cleanMedicoToken();
        preferences.cleanFirebaseToken();
        preferences.cleanSendEstadoAsignacion();
        preferences.cleanSendLocation();
        DBWrapper.cleanAllDB(context);
    }

    /**
     * Comprobar si el medico esta logueado
     * @return boolean logueado o no
     */
    public static boolean isLogued(){
        PreferencesHelper preferences = PreferencesHelper.getInstance();
        return preferences.getMedicoToken() != null;
    }

    /**
     * Para saber si debe correr el servicio de Location en modo intensivo
     * @param context Contexto
     * @return true si debe correr en modo intensivo
     */
    public static boolean locationIntensiveMode(Context context){
        return isCharging(context);
    }

    /**
     * Para saber si el celular se esta cargando
     * @param context Contexto
     * @return true if charging
     */
    private static boolean isCharging(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if(intent == null)
            return false;
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB;
    }

    /**
     * Chequea si el timestamp de la ubicacion es coherente. Si es mas viejo que 1 mes desde
     * la fecha actual lo reemplaza por el timestamp del sistema.
     *
     */
    public static void validateLocationTime(Location location){
        long locationTimestamp = location.getTime();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.add(Calendar.MONTH, -1);
        if(locationTimestamp < calendar.getTime().getTime()){
            location.setTime(System.currentTimeMillis());
        }
    }

    /**
     * Para saber si tiene los permisos de Location habilitados
     * @param context Contexto
     * @return true si los permisos estan activos
     */
    public static boolean isLocationPermissionGranted(Context context){
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Sincronizar el SynAdapter ahora
     * @param context Contexto
     */
    public static void syncNow(Context context) {
        Account mAccount = Utils.getAccount(context);
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);

        ContentResolver.requestSync(
                mAccount,
                context.getString(R.string.content_authority),
                bundle
        );
    }

    /**
     * Crea la cuenta necesaria para el SyncAdapter
     * @param context Contexto
     */
    public static void createSyncAccount(Context context) {
        Account newAccount = getAccount(context);
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        try{
            accountManager.addAccountExplicitly(newAccount, null, null);
        }catch(Exception e){

        }
    }

    /**
     * Setea los valores para el content Resolver y agenda una alarma para correr el SyncAdapter
     * @param context Contexto
     */
    public static void setupContentResolver(Context context) {
        /**
         * Content Resolver setup
         */
        Account mAccount = getAccount(context);

        ContentResolver.setIsSyncable(
                mAccount,
                context.getString(R.string.content_authority),
                1);

        ContentResolver.setSyncAutomatically(
                mAccount,
                context.getString(R.string.content_authority),
                false);

        scheduleDataSync(context);
    }

    /**
     * Alarma para correr el SyncAdapter
     * @param context Contexto
     */
    private static void scheduleDataSync(Context context){
        Intent intent = new Intent(context.getString(R.string.broadcast_sync_data));
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        am.cancel(sender);
        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), Constants.SYNC_INTERVAL, sender);
    }

    /**
     * Retorna un Account
     * @param context Contexto
     * @return Account
     */
    public static Account getAccount(Context context){
        return new Account(
                Constants.DEMO_ACCOUNT_NAME,
                context.getString(R.string.account_type));
    }

    /**
     * Retorna la ubicacion
     * @return LatLng
     */
    public static LatLng getPassiveLocation(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return new LatLng(0, 0);
        }
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        if(lastLocation != null)
            return new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        else
            return new LatLng(0, 0);
    }

    public static float getBearing(Location previousLocation, Location newLocation) {
        if(previousLocation != null){
            return getBearing(previousLocation.getLatitude(), previousLocation.getLongitude(), newLocation.getLatitude(), newLocation.getLongitude());
        }else{
            return newLocation.getBearing();
        }
    }

    public static float getBearing(double previousLatitude, double previousLongitude, double newLatitude, double newLongitude) {
        return (float) com.google.maps.android.SphericalUtil.computeHeading(new LatLng(previousLatitude, previousLongitude), new LatLng(newLatitude, newLongitude));
    }

    public static Location getLastLocationSaved(Context context){
        Location location = null;
        Cursor cursor = context.getContentResolver().query(
                DBContract.Locations.CONTENT_URI,
                null,
                null,
                null,
                DBContract.Locations.COLUMN_NAME_TIMESTAMP_LOC + " DESC LIMIT 1");
        if(cursor != null){
            if(cursor.moveToNext()){
                Double lat = cursor.getDouble(cursor.getColumnIndex(DBContract.Locations.COLUMN_NAME_LATITUDE));
                Double lng = cursor.getDouble(cursor.getColumnIndex(DBContract.Locations.COLUMN_NAME_LONGITUDE));
                String bearing = cursor.getString(cursor.getColumnIndex(DBContract.Locations.COLUMN_NAME_BEARING));
                String provider = cursor.getString(cursor.getColumnIndex(DBContract.Locations.COLUMN_NAME_PROVIDER));

                location = new Location(provider);
                location.setLatitude(lat);
                location.setLongitude(lng);
                location.setBearing(Float.parseFloat(bearing));
            }
            cursor.close();
        }
        return location;
    }

    public static boolean isGPSOn(final Activity activity) {
        LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean isInAuxilio(){
        PreferencesHelper preferences = PreferencesHelper.getInstance();
        return preferences.getValueEstado() == new ApiConstants.EnAuxilio().getValue();
    }

    public static void updateEstado(Context context, ApiConstants.Item estado){
        PreferencesHelper preferences = PreferencesHelper.getInstance();
        preferences.setValueEstado(estado.getValue());
        preferences.setDescriptionEstado(estado.getDescription(context));
        Utils.restarLocationsServices(context);
    }

    public static void restarLocationsServices(Context context){
        context.stopService(new Intent(context, IntensiveLocationService.class));
        context.stopService(new Intent(context, LocationService.class));
        context.startService(new Intent(context, SelectLocationService.class));
    }

    public static String getDescriptionEstadoMedico(Context context, int estado) {
        ApiConstants.Item disponible = new ApiConstants.Disponible();
        if(disponible.getValue() == estado)
            return disponible.getDescription(context);

        ApiConstants.Item noDisponible = new ApiConstants.NoDisponible();
        if(noDisponible.getValue() == estado)
            return noDisponible.getDescription(context);

        ApiConstants.Item enAuxilio = new ApiConstants.EnAuxilio();
        if(enAuxilio.getValue() == estado)
            return enAuxilio.getDescription(context);
        return null;
    }

    public static String getEstadoAuxilio(Context context, int idEstado){
        ApiConstants.Item enCamino = new ApiConstants.EnCamino();
        if(idEstado == enCamino.getValue())
            return enCamino.getDescription(context);

        ApiConstants.Item enLugar = new ApiConstants.EnLugar();
        if(idEstado == enLugar.getValue())
            return enLugar.getDescription(context);

        ApiConstants.Item enTraslado = new ApiConstants.EnTraslado();
        if(idEstado == enTraslado.getValue())
            return enTraslado.getDescription(context);

        return null;
    }

    public static void addStartTransitionAnimation(Activity activity){
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void addFinishTransitionAnimation(Activity activity){
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public static void addDefaultFinishTransitionAnimation(Activity activity){
        activity.overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }

    public static void startActivityWithTransition(Activity activity, Intent intent) {
        activity.startActivity(intent);
        addStartTransitionAnimation(activity);
    }

    public static void startActivityWithTransitionForResult(Activity activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
        addStartTransitionAnimation(activity);
    }

    public static void determinateEstadoAsignacion(Context context, AppLocation location) {
        PreferencesHelper preferencesHelper = PreferencesHelper.getInstance();
        if(preferencesHelper.getValueEstado() == new ApiConstants.EnAuxilio().getValue())
            new DeterminarEstadoAsignacionTask(context).execute(location);
    }

    public static double distanceCoordinatesInMeters(double lat1, double lng1, double lat2, double lng2) {
        double radioTierra = 6371;//en kilómetros
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));

        return radioTierra * va2 * 1000;
    }
}
