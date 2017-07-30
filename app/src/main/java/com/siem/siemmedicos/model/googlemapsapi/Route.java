package com.siem.siemmedicos.model.googlemapsapi;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Route {

    @SerializedName("legs")
    private ArrayList<Leg> mLegs;

    public Leg getLeg(int i) {
        return mLegs.get(i);
    }
}
