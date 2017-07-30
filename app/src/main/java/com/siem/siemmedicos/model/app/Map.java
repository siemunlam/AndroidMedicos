package com.siem.siemmedicos.model.app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.siem.siemmedicos.R;

/**
 * Created by Lucas on 30/7/17.
 */

public class Map {

    private Context mContext;
    private GoogleMap mMap;

    public Map(Context context){
        mContext = context;
    }

    public void setMap(GoogleMap map) {
        mMap = map;

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(false);
    }

    public void addPolyline(PolylineOptions polylineOptions) {
        Polyline line = mMap.addPolyline(polylineOptions);
    }

    public void animateCamera(CameraUpdate cameraUpdate) {
        mMap.animateCamera(cameraUpdate);
    }

    public void addFinishMarker(LatLng latLng) {
        mMap.addMarker(new MarkerOptions()
            .position(latLng)
            .anchor(0, 1f)
            .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.ic_location_auxilio, 125, 125))));
    }

    private Bitmap resizeMapIcons(int iconId, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(mContext.getResources(), iconId);
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }
}
