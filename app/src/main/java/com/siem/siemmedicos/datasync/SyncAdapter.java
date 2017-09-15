package com.siem.siemmedicos.datasync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

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
        Log.i("123456789", "PASOOOOOOOOO");
        if(Utils.isLogued()){
            mPreferences = PreferencesHelper.getInstance();
            String firebaseToken = mPreferences.getFirebaseToken();
            if(!firebaseToken.isEmpty()){
                sendFirebaseToken(firebaseToken);
            }
        }
    }

    private void sendFirebaseToken(String firebaseToken) {
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

}
