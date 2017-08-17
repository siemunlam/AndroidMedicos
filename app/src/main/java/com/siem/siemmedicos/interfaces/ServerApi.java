package com.siem.siemmedicos.interfaces;

import com.siem.siemmedicos.model.serverapi.LoginResponse;
import com.siem.siemmedicos.utils.Constants;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ServerApi {

    @FormUrlEncoded
    @POST(Constants.API_LOGIN)
    Call<LoginResponse> login(
            @Field("username") String username,
            @Field("password") String password);

}
