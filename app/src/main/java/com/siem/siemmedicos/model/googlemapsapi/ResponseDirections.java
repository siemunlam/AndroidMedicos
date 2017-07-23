package com.siem.siemmedicos.model.googlemapsapi;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseDirections {

    @SerializedName("routes")
    private ArrayList<Route> mRoutes;

    @SerializedName("status")
    private String mStatus;

}
