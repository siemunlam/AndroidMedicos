package com.siem.siemmedicos.services;

import android.location.Location;
import android.util.Log;

import com.siem.siemmedicos.model.app.AppLocation;
import com.siem.siemmedicos.utils.Utils;

import java.util.ArrayList;

public class IntensiveLocationService extends BaseLocationServices {

    private static final String ZERO = "0";
    protected static final long LOCATION_TIMEOUT = 60000;     // 2 min

    @Override
    protected void newLocation(Location location) {
        // Ignore the first n measurements, until the precision stabilizes
        mMeasurementNumber++;
        if (mMeasurementNumber <= MEASUREMENT_TO_IGNORE)
            return;

        Utils.validateLocationTime(location);
        mMylocation = new AppLocation(location);
        restartRunnable();
        setDataInBearingBuffer(mMylocation);

        // Only save locations after a threshold (time, distance or bearing)
        int TIME_THRESHOLD = mConfigPreferences.getIntensiveModeTimeTrshld();               // mS
        int DISTANCE_THRESHOLD = mConfigPreferences.getIntensiveModeDistTrshld();           // meters
        int BEARING_THRESHOLD = mConfigPreferences.getIntensiveModeBearTrshld();            // Degrees
        float BEARING_MIN_SPEED = mConfigPreferences.getIntensiveModeBearMinSpeed() / 3.6f; // km/h converted to m/s

        long lastLocationTime = mPreferences.getLastLocationTime();
        float lastLocationBearing = mPreferences.getLastLocationBearing(mMylocation.getBearing());
        double lastLatitude =  Double.parseDouble(mPreferences.getLastLatitude(ZERO));
        double lastLongitude = Double.parseDouble(mPreferences.getLastLongitude(ZERO));

        float bearingDiff = getBearingDiff(mMylocation, lastLocationBearing, BEARING_MIN_SPEED, BEARING_THRESHOLD);
        float distanceDiff = getDistanceDiff(mMylocation, lastLatitude, lastLongitude);
        long timeDiff = mMylocation.getTime() - lastLocationTime;

        if (timeDiff < TIME_THRESHOLD &&
                (bearingDiff < BEARING_THRESHOLD || mMylocation.getSpeed() < BEARING_MIN_SPEED) &&
                distanceDiff < DISTANCE_THRESHOLD){
            return;
        }

        // Save the location and reset the threshold counters
        saveInPreferencesLocationData(mMylocation);
        mMylocation.save(this);
    }

    private void setDataInBearingBuffer(AppLocation mylocation) {
        if(mBearingBuffer == null){
            mBearingBuffer = new ArrayList<>();
            for (int i = 0; i < BEARING_BUFFER_SIZE; i++) {
                mBearingBuffer.add(mylocation.getBearing());
            }
        } else {
            mBearingBuffer.remove(0);
            mBearingBuffer.add(mylocation.getBearing());
        }
    }

    private float getBearingDiff(AppLocation mylocation, float lastLocationBearing, float BEARING_MIN_SPEED, int BEARING_THRESHOLD) {
        float bearingDiff = 0.0f;
        if (mylocation.getBearing() != 0.0) {
            if(mylocation.getSpeed() >= BEARING_MIN_SPEED) {
                //Recorro la lista, salteando el ultimo, ya que es igual a mylocation.getBearing()
                for (int i = mBearingBuffer.size() - 2; i >= 0; i--) {
                    bearingDiff = Math.abs(mylocation.getBearing() - mBearingBuffer.get(i));
                    if (bearingDiff >= BEARING_THRESHOLD) {
                        //Si la diferencia es mayor, seteo los valores con el actual
                        for (int j = 0; j < BEARING_BUFFER_SIZE; j++) {
                            mBearingBuffer.set(j, mylocation.getBearing());
                        }
                        break;
                    }
                }
            }
        } else {
            mylocation.setBearing(lastLocationBearing);
        }

        return bearingDiff;
    }

    private float getDistanceDiff(AppLocation mylocation, double lastLatitude, double lastLongitude) {
        float[] results = new float[1];
        Location.distanceBetween(
                lastLatitude,
                lastLongitude,
                mylocation.getLatitude(),
                mylocation.getLongitude(),
                results);

        return results[0];
    }

    @Override
    protected void setRunnable() {
        mHandler.postDelayed(mRunnable, LOCATION_TIMEOUT);
    }

}
