package com.siem.siemmedicos.model.app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.siem.siemmedicos.R;
import com.siem.siemmedicos.model.googlemapsapi.ResponseDirections;
import com.siem.siemmedicos.utils.Constants;
import com.siem.siemmedicos.utils.Utils;
import com.siem.siemmedicos.utils.maputils.PolyUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Map implements Callback<ResponseDirections> {

    private static final int SIZE_FINISH_MARKER = 125;
    private static final int SIZE_POSITION_MARKER = 230;

    private Marker mPositionMarker;
    private Polyline mPolyline;
    private Location mPreviousLocation;
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

    public void setZoomControlsEnabled(boolean enable){
        mMap.getUiSettings().setZoomControlsEnabled(enable);
    }

    public void addPolyline(List<LatLng> listLatLng) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(25);
        polylineOptions.color(ContextCompat.getColor(mContext, R.color.polyline));
        for (LatLng latLng : listLatLng) {
            polylineOptions.add(latLng);
        }
        if(mPolyline != null){
            mPolyline.remove();
        }
        mPolyline = mMap.addPolyline(polylineOptions);
    }

    public void animateCamera(final CameraUpdate cameraUpdate) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMap.animateCamera(cameraUpdate);
            }
        }, 300);
    }

    public void moveCamera(CameraUpdate cameraUpdate) {
        mMap.moveCamera(cameraUpdate);
    }

    public void addFinishMarker(LatLng latLng) {
        mMap.addMarker(new MarkerOptions()
            .position(latLng)
            .anchor(0, 1f)
            .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.ic_location_auxilio, SIZE_FINISH_MARKER, SIZE_FINISH_MARKER))));
    }

    public void addPositionMarker(Location location){
        if(location.getLatitude() != 0 && location.getLongitude() != 0){
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            float bearing = getBearing(location);
            if(mPositionMarker == null) {
                mPositionMarker = mMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .anchor(0.5f, 0.5f)
                                .rotation(bearing)
                                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(R.drawable.ic_ambulance, SIZE_POSITION_MARKER, SIZE_POSITION_MARKER)))
                                .flat(true));
            }else{
                mPositionMarker.setPosition(latLng);
                mPositionMarker.setRotation(bearing);
            }
            reubicateMap(latLng, bearing);
            mPreviousLocation = location;
        }
    }

    public void addPositionMarker(AppLocation location){
        Location newLocation = new Location("GPS");
        newLocation.setLatitude(location.getLatitude());
        newLocation.setLongitude(location.getLongitude());
        addPositionMarker(newLocation);
    }

    private float getBearing(Location newLocation) {
        if(mPreviousLocation != null){
            return (float) com.google.maps.android.SphericalUtil.computeHeading(new LatLng(mPreviousLocation.getLatitude(), mPreviousLocation.getLongitude()), new LatLng(newLocation.getLatitude(), newLocation.getLongitude()));
        }else{
            return newLocation.getBearing();
        }
    }

    private void reubicateMap(LatLng latLng, float bearing) {
        float zoom = Utils.isInAuxilio() ? Constants.EMERGENCY_ZOOM : Constants.NORMAL_ZOOM;
        CameraPosition camPos = CameraPosition
                .builder(mMap.getCameraPosition())
                .bearing(bearing)
                .zoom(zoom)
                .target(latLng)
                .build();
        animateCamera(CameraUpdateFactory.newCameraPosition(camPos));
    }

    private Bitmap resizeMapIcons(int iconId, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(mContext.getResources(), iconId);
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }

    public void getDirections(AppLocation lastLocation) {
        Log.i("123456789", "Get Directions1");
        if(!lastLocation.isNullLocation()) {
            Log.i("123456789", "Get Directions2");
            lastLocation.getDirections(mContext, this);
        }
    }

    @Override
    public void onResponse(@NonNull Call<ResponseDirections> call, @NonNull Response<ResponseDirections> response) {
        try{
            Log.i("123456789", "onResponse");
            ResponseDirections responseDirections = response.body();
            List<LatLng> listLatLng = PolyUtils.decode(responseDirections.getEncodedPoints());
            addPolyline(listLatLng);
            addFinishMarker(responseDirections.getLastLocation());
        }catch(Exception e){
            Toast.makeText(mContext, mContext.getString(R.string.errorNoRoute), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<ResponseDirections> call, Throwable t) {
        Log.i("123456789", "Failure");
        //Toast.makeText(mContext, mContext.getString(R.string.error), Toast.LENGTH_LONG).show();
    }

    public void controlateInRoute(Location location) {
        if(Utils.isInAuxilio()){
            LatLng lastLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            if(mPolyline != null){
                if (!PolyUtil.isLocationOnPath(lastLatLng, mPolyline.getPoints(), true, 50)) {
                    Toast.makeText(mContext, "Recalculando....", Toast.LENGTH_LONG).show();
                    getDirections(new AppLocation(lastLatLng));
                }else{
                    Toast.makeText(mContext, "Todo bien....", Toast.LENGTH_LONG).show();
                }
            }else{
                getDirections(new AppLocation(lastLatLng));
            }
        }
    }
}
