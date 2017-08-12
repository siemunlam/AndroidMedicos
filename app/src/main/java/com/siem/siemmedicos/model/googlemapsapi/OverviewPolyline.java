package com.siem.siemmedicos.model.googlemapsapi;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lucas on 11/8/17.
 */

public class OverviewPolyline {

    @SerializedName("points")
    private String mPoints;

    public String getPoints() {
        return mPoints;
    }

    public void setPoints(String points) {
        mPoints = points;
    }
}
