package com.siem.siemmedicos.interfaces;

import com.siem.siemmedicos.model.serverapi.LoginResponse;
import com.siem.siemmedicos.utils.Constants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ServerApi {

    @FormUrlEncoded
    @POST(Constants.API_LOGIN)
    Call<LoginResponse> login(
            @Field(Constants.USERNAME) String username,
            @Field(Constants.PASSWORD) String password);

    @PUT(Constants.API_LOGOUT)
    Call<Object> logout(
            @Header(Constants.AUTHORIZATION) String authorization);

    @FormUrlEncoded
    @PUT(Constants.API_UPDATE_FCM)
    Call<Object> updateFCM(
            @Header(Constants.AUTHORIZATION) String authorization,
            @Field(Constants.FCM_CODE) String fcm_code);

    @FormUrlEncoded
    @PUT(Constants.API_UPDATE_UBICACION)
    Call<Object> updateUbicacion(
            @Header(Constants.AUTHORIZATION) String authorization,
            @Field(Constants.LATITUD) String latitud,
            @Field(Constants.LONGITUD) String longitud,
            @Field(Constants.TIMESTAMP) String timestamp);

    @FormUrlEncoded
    @PUT(Constants.API_UPDATE_ESTADO_MEDICO)
    Call<Object> updateEstadoMedico(
            @Header(Constants.AUTHORIZATION) String authorization,
            @Field(Constants.ESTADO) int estado);

    @GET(Constants.API_DESVINCULAR_AUXILIO)
    Call<Object> desvincularAuxilio(
            @Header(Constants.AUTHORIZATION) String authorization);

    @POST(Constants.API_FINALIZAR_AUXILIO)
    Call<Object> finalizarAuxilio(
            @Header(Constants.AUTHORIZATION) String authorization,
            @Body String data);

}
