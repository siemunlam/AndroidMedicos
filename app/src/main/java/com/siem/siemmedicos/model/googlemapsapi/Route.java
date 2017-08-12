package com.siem.siemmedicos.model.googlemapsapi;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Route {

    @SerializedName("legs")
    private ArrayList<Leg> mLegs;

    @SerializedName("overview_polyline")
    private OverviewPolyline mOverviewPolyline;

    public Leg getLeg(int i) {
        return mLegs.get(i);
    }

    public OverviewPolyline getOverviewPolyline() {
        return mOverviewPolyline;
    }

    public void setOverviewPolyline(OverviewPolyline overviewPolyline) {
        mOverviewPolyline = overviewPolyline;
    }
}
