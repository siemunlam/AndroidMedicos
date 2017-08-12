package com.siem.siemmedicos.model.googlemapsapi;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseDirections {

    @SerializedName("routes")
    private ArrayList<Route> mRoutes;

    @SerializedName("status")
    private String mStatus;

    public String getEncodedPoints(){
        return mRoutes.get(0).getOverviewPolyline().getPoints();
    }

    public Route getRoute(int i) {
        return mRoutes.get(i);
    }

    public ArrayList<Step> getSteps(){
        Route route = getRoute(0);
        Leg leg = route.getLeg(0);
        return leg.getSteps();
    }

    public LatLng getFirstLocation(){
        return getSteps().get(0).getStartLocation();
    }

    public LatLng getLastLocation(){
        return getSteps().get(getSteps().size() - 1).getEndLocation();
    }

}
