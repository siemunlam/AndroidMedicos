package com.siem.siemmedicos.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.ActivityLogoutBinding;
import com.siem.siemmedicos.utils.Constants;
import com.siem.siemmedicos.utils.PreferencesHelper;
import com.siem.siemmedicos.utils.RetrofitClient;
import com.siem.siemmedicos.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lucas on 14/9/17.
 */

public class LogoutActivity extends Activity implements Callback<Object> {

    private ActivityLogoutBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_logout);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                PreferencesHelper preferences = PreferencesHelper.getInstance();
                Call<Object> response = RetrofitClient.getServerClient().logout(preferences.getAuthorization());
                response.enqueue(LogoutActivity.this);
            }
        }, 1000);
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        switch(response.code()){
            case Constants.CODE_SERVER_OK:
                logout();
                break;
            case Constants.CODE_UNAUTHORIZED:
            case Constants.CODE_SERVER_ERROR:
                logout();
                break;
            default:
                serverError();
                break;
        }
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {
        serverError();
    }

    private void logout() {
        Utils.logout(this);
        setResult(Activity.RESULT_OK);
        finish();
    }

    private void serverError() {
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show();
        finish();
    }
}
