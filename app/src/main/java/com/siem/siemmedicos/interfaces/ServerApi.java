package com.siem.siemmedicos.interfaces;

import com.siem.siemmedicos.model.serverapi.LoginResponse;
import com.siem.siemmedicos.model.serverapi.LogoutResponse;
import com.siem.siemmedicos.utils.Constants;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ServerApi {

    @FormUrlEncoded
    @POST(Constants.API_LOGIN)
    Call<LoginResponse> login(
            @Field("username") String username,
            @Field("password") String password);

    @PUT(Constants.API_LOGOUT)
    Call<LogoutResponse> logout(
            @Header("authorization") String authorization);

}
