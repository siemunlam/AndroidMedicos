package com.siem.siemmedicos.model.app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.siem.siemmedicos.R;
import com.siem.siemmedicos.model.googlemapsapi.ResponseDirections;
import com.siem.siemmedicos.model.googlemapsapi.Step;
import com.siem.siemmedicos.ui.MapActivity;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Map implements Callback<ResponseDirections> {

    private Marker mPositionMarker;
    private Polyline mPolyline;
    private ArrayList<LatLng> mListLatLng;
    private Context mContext;
    private GoogleMap mMap;

    public Map(Context context){
        mContext = context;
        mListLatLng = new ArrayList<>();
    }

    public void setMap(GoogleMap map) {
        mMap = map;

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(false);
    }

    public void addPolyline(ArrayList<LatLng> listLatLng) {
        mListLatLng = listLatLng;
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(25);
        polylineOptions.color(ContextCompat.getColor(mContext, R.color.polyline));
        for (LatLng latLng : listLatLng) {
            polylineOptions.add(latLng);
        }
        mPolyline = mMap.addPolyline(polylineOptions);
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

    public void addPositionMarker(LatLng latLng){
        if(mPositionMarker == null) {
            mPositionMarker = mMap.addMarker(
                    new MarkerOptions()
                    .position(latLng));
        }else{
            mPositionMarker.setPosition(latLng);
        }
    }

    private Bitmap resizeMapIcons(int iconId, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(mContext.getResources(), iconId);
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }

    public void getDirections(LastLocation lastLocation) {
        lastLocation.getDirections(mContext, this);
    }

    @Override
    public void onResponse(@NonNull Call<ResponseDirections> call, @NonNull Response<ResponseDirections> response) {
        try{
            ResponseDirections responseDirections = response.body();
            ArrayList<Step> steps = responseDirections.getSteps();
            ArrayList<LatLng> listLatLng = new ArrayList<>();
            listLatLng.add(responseDirections.getFirstLocation());
            for (Step step : steps) {
                listLatLng.add(step.getEndLocation());
            }
            addPolyline(listLatLng);
            addFinishMarker(responseDirections.getLastLocation());
        }catch(Exception e){
            Toast.makeText(mContext, mContext.getString(R.string.error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<ResponseDirections> call, Throwable t) {
        Toast.makeText(mContext, mContext.getString(R.string.error), Toast.LENGTH_LONG).show();
    }

    public void controlateInRoute(LatLng lastLatLng) {
        /*if(!PolyUtil.isLocationOnPath(lastLatLng, mListLatLng, true)){
            Toast.makeText(mContext, "Recalculando....", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(mContext, "En ruta", Toast.LENGTH_LONG).show();
        }*/
    }
}
