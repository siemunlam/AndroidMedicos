package com.siem.siemmedicos.datasync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.siem.siemmedicos.db.DBWrapper;
import com.siem.siemmedicos.model.app.AppLocation;
import com.siem.siemmedicos.model.app.Auxilio;
import com.siem.siemmedicos.utils.Constants;
import com.siem.siemmedicos.utils.PreferencesHelper;
import com.siem.siemmedicos.utils.RetrofitClient;
import com.siem.siemmedicos.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private Context mContext;
    private PreferencesHelper mPreferences;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        if(Utils.isLogued()){
            mPreferences = PreferencesHelper.getInstance();
            sendEstadoAsignacion();
            sendFirebaseToken();
            sendLocation();
        }
    }

    private void sendEstadoAsignacion() {
        boolean isSend = mPreferences.isSendEstadoAsignacion();
        if(isSend){
            Auxilio auxilio = DBWrapper.getAuxilio(mContext);
            final int idEstado = auxilio.getIdEstado();
            Call<Object> response = RetrofitClient.getServerClient().updateEstadoAsignacion(mPreferences.getAuthorization(), idEstado);
            response.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    switch(response.code()){
                        case Constants.CODE_SERVER_OK:
                            Auxilio auxilio = DBWrapper.getAuxilio(mContext);
                            if(idEstado == auxilio.getIdEstado())
                                mPreferences.cleanSendEstadoAsignacion();
                            break;
                        case Constants.CODE_UNAUTHORIZED:
                            Utils.logout(mContext);
                            break;
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {}
            });
        }
    }

    private void sendFirebaseToken() {
        String firebaseToken = mPreferences.getFirebaseToken();
        if(firebaseToken.isEmpty()){
            return;
        }
        Call<Object> response = RetrofitClient.getServerClient().updateFCM(mPreferences.getAuthorization(), firebaseToken);
        response.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                switch(response.code()){
                    case Constants.CODE_SERVER_OK:
                        mPreferences.cleanFirebaseToken();
                        break;
                    case Constants.CODE_UNAUTHORIZED:
                        Utils.logout(mContext);
                        break;
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {}
        });
    }

    private void sendLocation() {
        final AppLocation lastLocation = DBWrapper.getLastLocation(mContext);
        if(lastLocation != null){
            Call<Object> response = RetrofitClient.getServerClient().updateUbicacion(mPreferences.getAuthorization(),
                    String.valueOf(lastLocation.getLatitude()),
                    String.valueOf(lastLocation.getLongitude()),
                    String.valueOf(lastLocation.getTime()));
            response.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    switch(response.code()){
                        case Constants.CODE_SERVER_OK:
                            DBWrapper.deleteLocation(mContext, lastLocation.getId());
                            break;
                        case Constants.CODE_UNAUTHORIZED:
                            Utils.logout(mContext);
                            break;
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {}
            });
        }
    }

}
