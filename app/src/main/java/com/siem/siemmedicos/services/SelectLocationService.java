package com.siem.siemmedicos.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.siem.siemmedicos.utils.Utils;

public class SelectLocationService extends Service {

    private Intent mLocationServiceIntent;
    private Intent mIntensiveLocationServiceIntent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stopLocationsServices();

        if(Utils.locationIntensiveMode(SelectLocationService.this))
            startService(mIntensiveLocationServiceIntent);
        else
            startService(mLocationServiceIntent);

        return START_STICKY;
    }

    private void stopLocationsServices() {
        mLocationServiceIntent = new Intent(SelectLocationService.this, LocationService.class);
        mIntensiveLocationServiceIntent = new Intent(SelectLocationService.this, IntensiveLocationService.class);
        stopForeground(true);
        stopService(mLocationServiceIntent);
        stopService(mIntensiveLocationServiceIntent);
    }

}
