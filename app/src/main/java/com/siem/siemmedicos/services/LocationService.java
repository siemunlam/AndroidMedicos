package com.siem.siemmedicos.services;

import android.location.Location;
import android.os.Handler;

import com.siem.siemmedicos.model.app.AppLocation;
import com.siem.siemmedicos.utils.Utils;

public class LocationService extends BaseLocationServices {

    private static final float MULTIPLICATOR_LOCATION_TIMEOUT = 2F;

    private Handler mHandlerLocation;
    private Runnable mRunnableLocation;

    @Override
    public void onCreate(){
        super.onCreate();
        mHandlerLocation = new Handler();
        mRunnableLocation = new Runnable() {
            @Override
            public void run() {
                mMeasurementNumber = 0;
                init();
            }
        };
    }

    @Override
    protected void newLocation(Location location) {
        // Ignore the first n measurements, until the precision stabilizes
        mMeasurementNumber++;
        if (mMeasurementNumber <= MEASUREMENT_TO_IGNORE)
            return;

        Utils.validateLocationTime(location);
        mMylocation = new AppLocation(location);
        restartRunnable();
        stop();

        // Save the location and reset the threshold counters
        saveInPreferencesLocationData(mMylocation);
        mMylocation.save(this);
        mHandlerLocation.postDelayed(mRunnableLocation, mConfigPreferences.getNormalModeTimeTrshld());
    }

    @Override
    protected void setRunnable() {
        mHandler.postDelayed(mRunnable, (long)(mConfigPreferences.getNormalModeTimeTrshld() * MULTIPLICATOR_LOCATION_TIMEOUT));
    }

}
