package com.siem.siemmedicos.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.siem.siemmedicos.BuildConfig;
import com.siem.siemmedicos.interfaces.GoogleMapsApi;
import com.siem.siemmedicos.interfaces.ServerApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static GoogleMapsApi retrofitMapsGoogle = null;
    private static ServerApi retrofitServerApi = null;

    private static Gson getGson(){
        return new GsonBuilder()
                .setLenient()
                .create();
    }

    public static GoogleMapsApi getMapsGoogleClient() {
        if (retrofitMapsGoogle == null) {
            Gson gson = getGson();

            retrofitMapsGoogle = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_MAPSGOOGLE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(GoogleMapsApi.class);
        }
        return retrofitMapsGoogle;
    }

    public static ServerApi getServerClient() {
        if (retrofitServerApi == null) {
            Gson gson = getGson();

            retrofitServerApi = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(ServerApi.class);
        }
        return retrofitServerApi;
    }
}