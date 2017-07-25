package com.siem.siemmedicos.model.app;

import com.google.android.gms.maps.model.LatLng;
import com.siem.siemmedicos.utils.PreferencesHelper;

public class LastLocation {

    private PreferencesHelper mPreferences;
    private LatLng mLocation;

    public LastLocation(){
        mPreferences = PreferencesHelper.getInstance();
        Double lat = Double.parseDouble(mPreferences.getLastLatitude("0"));
        Double lng = Double.parseDouble(mPreferences.getLastLongitude("0"));
        mLocation = new LatLng(lat, lng);
    }

    public LatLng getLocation(){
        return mLocation;
    }

    public boolean isNullLocation(){
        return mLocation.latitude == 0 && mLocation.longitude == 0;
    }

}
