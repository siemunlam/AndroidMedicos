package com.siem.siemmedicos.model.googlemapsapi;

import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("lat")
    private String mLatitud;

    @SerializedName("lng")
    private String mLongitud;

    public String getLatitud() {
        return mLatitud;
    }

    public String getLongitud() {
        return mLongitud;
    }
}
