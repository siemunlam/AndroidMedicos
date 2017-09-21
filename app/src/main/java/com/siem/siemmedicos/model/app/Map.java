package com.siem.siemmedicos.model.app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import java.util.Locale;

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
    private TextToSpeech mTextToSpeech;

    public Map(Context context){
        mContext = context;

        mTextToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    mTextToSpeech.setLanguage(Locale.getDefault());
                }
            }
        });
    }

    public void setMap(GoogleMap map) {
        mMap = map;

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(false);
    }

    public void onDestroy(){
        if(mTextToSpeech != null){
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }
    }

    public boolean isReady(){
        return mMap != null;
    }

    public void setZoomControlsEnabled(boolean enable){
        mMap.getUiSettings().setZoomControlsEnabled(enable);
        mMap.getUiSettings().setMapToolbarEnabled(enable);
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
            float bearing = Utils.getBearing(mPreviousLocation, location);
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
        if(!lastLocation.isNullLocation()) {
            lastLocation.getDirections(mContext, this);
        }
    }

    @Override
    public void onResponse(@NonNull Call<ResponseDirections> call, @NonNull Response<ResponseDirections> response) {
        try{
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
        Toast.makeText(mContext, mContext.getString(R.string.error), Toast.LENGTH_LONG).show();
    }

    public void controlateInRoute(Location location) {
        if(Utils.isInAuxilio()){
            LatLng lastLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            if(mPolyline != null){
                if (!PolyUtil.isLocationOnPath(lastLatLng, mPolyline.getPoints(), true, 50)) {
                    String text = mContext.getString(R.string.recalculate);
                    Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
                    speak(text);
                    getDirections(new AppLocation(lastLatLng));
                }
            }else{
                getDirections(new AppLocation(lastLatLng));
            }
        }
    }

    private void speak(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTextToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void cleanMapAuxilio(){
        mMap.clear();
        if(mPreviousLocation != null){
            addPositionMarker(mPreviousLocation);
            mPositionMarker = null;
        }
    }
}
