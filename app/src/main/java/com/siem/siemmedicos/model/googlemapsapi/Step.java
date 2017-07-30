package com.siem.siemmedicos.model.googlemapsapi;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class Step {

    @SerializedName("start_location")
    private Location mStartLocation;

    @SerializedName("end_location")
    private Location mEndLocation;

    public LatLng getStartLocation() {
        return new LatLng(Double.parseDouble(mStartLocation.getLatitud()), Double.parseDouble(mStartLocation.getLongitud()));
    }

    public LatLng getEndLocation() {
        return new LatLng(Double.parseDouble(mEndLocation.getLatitud()), Double.parseDouble(mEndLocation.getLongitud()));
    }
}
