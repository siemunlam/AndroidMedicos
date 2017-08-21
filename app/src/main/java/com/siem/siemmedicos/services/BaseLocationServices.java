package com.siem.siemmedicos.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.siem.siemmedicos.R;
import com.siem.siemmedicos.model.app.AppLocation;
import com.siem.siemmedicos.utils.ConfigPreferencesHelper;
import com.siem.siemmedicos.utils.Constants;
import com.siem.siemmedicos.utils.PreferencesHelper;
import com.siem.siemmedicos.utils.Utils;

import java.util.ArrayList;

public class BaseLocationServices extends Service {

    protected static final long TIME_BREAK = 30000;
    protected static final int MEASUREMENT_TO_IGNORE = 3;         // Mediciones a ignorar
    protected static final long MIN_TIME_UPDATES = 1000;          // mS
    protected static final long MIN_DISTANCE_UPDATES = 0;         // meters
    protected final int BEARING_BUFFER_SIZE = 10;

    protected int mMeasurementNumber;                             // Mediciones desde desconexion
    protected PreferencesHelper mPreferences;
    protected ConfigPreferencesHelper mConfigPreferences;
    protected Handler mHandler;
    protected Runnable mRunnable;
    protected AlarmManager mAlarmManager;
    protected ArrayList<Float> mBearingBuffer;
    protected AppLocation mMylocation;

    /**
     * Locations
     */
    protected LocationManager mLocationManager;
    protected MyLocationListener mLocationListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        startForeground(Constants.ID_NOTIFICATION_SERVICE, createBuilderListening().build());
        Utils.setupContentResolver(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationListener = new MyLocationListener();

        mMeasurementNumber = 0;
        mConfigPreferences = ConfigPreferencesHelper.getInstance();
        mAlarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(BaseLocationServices.this, SelectLocationService.class);
                PendingIntent pendingIntent = PendingIntent.getService(BaseLocationServices.this, 0, intent, 0);
                mAlarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + TIME_BREAK, pendingIntent);
                stop();
                stopSelf();
            }
        };
        setRunnable();

        init();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        stop();
    }

    protected void init() {
        if (Utils.isLocationPermissionGranted(BaseLocationServices.this)) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_UPDATES, MIN_DISTANCE_UPDATES, mLocationListener);
        }
    }

    protected void stop() {
        if (Utils.isLocationPermissionGranted(BaseLocationServices.this)) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }

    protected NotificationCompat.Builder createBuilderListening() {
        mPreferences = PreferencesHelper.getInstance();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setWhen(0);
        mBuilder.setContentText(getString(R.string.statusNotification, mPreferences.getDescriptionEstado(this)));
        return mBuilder;
    }

    protected void restartRunnable(){
        mHandler.removeCallbacks(mRunnable);
        setRunnable();
    }

    protected void setRunnable(){
        // Implements in SubClass
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            newLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // required for interface, not used
        }

        @Override
        public void onProviderEnabled(String provider) {
            // required for interface, not used
        }

        @Override
        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {
            // required for interface, not used
        }
    }

    protected void newLocation(Location location) {
        // Implementar en SubClase
    }

    protected void saveInPreferencesLocationData(AppLocation mylocation) {
        mPreferences.setLastLocationTime(mylocation.getTime());
        mPreferences.setLastLocationBearing(mylocation.getBearing());
        mPreferences.setLastLatitude(String.valueOf(mylocation.getLatitude()));
        mPreferences.setLastLongitude(String.valueOf(mylocation.getLongitude()));
        mPreferences.setLastProvider(mylocation.getProvider());
    }
}
