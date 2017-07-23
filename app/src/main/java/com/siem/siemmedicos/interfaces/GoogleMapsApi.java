package com.siem.siemmedicos.interfaces;

import com.siem.siemmedicos.model.googlemapsapi.ResponseDirections;
import com.siem.siemmedicos.utils.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapsApi {

    @GET(Constants.API_GOOGLEMAPS_DIRECTIONS)
    Call<ResponseDirections> getDirections(
            @Query(Constants.ORIGIN) String origin,
            @Query(Constants.DESTINATION) String destination,
            @Query(Constants.KEY) String key);

}
