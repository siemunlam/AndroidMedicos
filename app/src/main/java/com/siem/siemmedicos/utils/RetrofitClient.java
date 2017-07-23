package com.siem.siemmedicos.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.siem.siemmedicos.BuildConfig;
import com.siem.siemmedicos.interfaces.GoogleMapsApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static GoogleMapsApi retrofitMapsGoogle = null;

    public static GoogleMapsApi getMapsGoogleClient() {
        if (retrofitMapsGoogle == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofitMapsGoogle = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_MAPSGOOGLE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(GoogleMapsApi.class);
        }
        return retrofitMapsGoogle;
    }
}