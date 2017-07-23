package com.siem.siemmedicos.model.googlemapsapi;

import com.google.gson.annotations.SerializedName;

public class Step {

    @SerializedName("start_location")
    private Location mStartLocation;

    @SerializedName("end_location")
    private Location mEndLocation;

}
