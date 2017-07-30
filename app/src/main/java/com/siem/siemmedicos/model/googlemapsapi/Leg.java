package com.siem.siemmedicos.model.googlemapsapi;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Leg {

    @SerializedName("distance")
    private Distance mDistance;

    @SerializedName("duration")
    private Duration mDuration;

    @SerializedName("steps")
    private ArrayList<Step> mSteps;

    public ArrayList<Step> getSteps() {
        return mSteps;
    }
}
