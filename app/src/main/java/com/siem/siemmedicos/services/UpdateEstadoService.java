package com.siem.siemmedicos.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.siem.siemmedicos.utils.ApiConstants;
import com.siem.siemmedicos.utils.Constants;
import com.siem.siemmedicos.utils.PreferencesHelper;
import com.siem.siemmedicos.utils.RetrofitClient;
import com.siem.siemmedicos.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lucas on 10/9/17.
 */

public class UpdateEstadoService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateEstado();
        return START_NOT_STICKY;
    }

    private void updateEstado() {
        PreferencesHelper preferencesHelper = PreferencesHelper.getInstance();
        Call<Object> response = RetrofitClient.getServerClient().updateEstadoMedico(preferencesHelper.getAuthorization(), new ApiConstants.Disponible().getValue());
        response.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                switch(response.code()){
                    case Constants.CODE_SERVER_OK:
                        Utils.updateEstado(UpdateEstadoService.this, new ApiConstants.Disponible());
                        Utils.restarLocationsServices(UpdateEstadoService.this);
                        Log.i("123456789", "*** Actualizado estado a Disponible");
                        break;
                    case Constants.CODE_UNAUTHORIZED:
                        Utils.logout(UpdateEstadoService.this);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {}
        });
    }
}
