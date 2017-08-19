package com.siem.siemmedicos.ui;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.firebase.database.Transaction;
import com.google.firebase.iid.FirebaseInstanceId;
import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.ActivityLoginBinding;
import com.siem.siemmedicos.model.serverapi.LoginResponse;
import com.siem.siemmedicos.utils.Constants;
import com.siem.siemmedicos.utils.PreferencesHelper;
import com.siem.siemmedicos.utils.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity implements Callback<LoginResponse> {

    private PreferencesHelper mPreferences;
    private ActivityLoginBinding mBinding;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controlarMedicoLogueado();
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        mBinding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoginFullComplete()){
                    habilitarLoading();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String user = mBinding.edittextUser.getText();
                            String pass = mBinding.edittextPass.getText();
                            Call<LoginResponse> call = RetrofitClient.getServerClient().login(user, pass);
                            call.enqueue(LoginActivity.this);
                        }
                    }, 100);
                }
            }
        });
    }

    @Override
    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
        switch(response.code()){
            case Constants.CODE_SERVER_OK:
                goToMap();
                break;
            case Constants.CODE_BAD_REQUEST:
                loginError();
                break;
            default:
                serverError();
                break;
        }
    }

    @Override
    public void onFailure(Call<LoginResponse> call, Throwable t) {
        serverError();
    }

    private boolean isLoginFullComplete() {
        String user = mBinding.edittextUser.getText();
        String pass = mBinding.edittextPass.getText();
        List<View> listErrorView = new ArrayList<>();
        if(user.isEmpty())
            listErrorView.add(mBinding.edittextUser);
        if(pass.isEmpty())
            listErrorView.add(mBinding.edittextPass);

        missingFields(listErrorView);
        return listErrorView.size() <= 0;
    }

    private void missingFields(List<View> listErrorView) {
        if(listErrorView.size() > 0){
            mToast = Toast.makeText(LoginActivity.this, getString(R.string.errorEmptyFields), Toast.LENGTH_LONG);
            mToast.show();
            vibrate(listErrorView);
        }
    }

    private void vibrate(List<View> listErrorView) {
        for (View view : listErrorView) {
            Animation shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.vibrate);
            view.startAnimation(shake);
        }
    }

    private void serverError(){
        deshabilitarLoading();
        mToast = Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG);
        mToast.show();
    }

    private void loginError() {
        deshabilitarLoading();
        mToast = Toast.makeText(LoginActivity.this, getString(R.string.errorLogin), Toast.LENGTH_LONG);
        mToast.show();
        List<View> listErrorView = new ArrayList<>();
        listErrorView.add(mBinding.edittextUser);
        listErrorView.add(mBinding.edittextPass);
        vibrate(listErrorView);
    }

    private void habilitarLoading() {
        mBinding.buttonLogin.setEnabled(false);
        mBinding.edittextPass.setEnabled(false);
        mBinding.edittextUser.setEnabled(false);
        mBinding.progressContent.setVisibility(View.VISIBLE);
    }

    private void deshabilitarLoading() {
        mBinding.buttonLogin.setEnabled(true);
        mBinding.edittextPass.setEnabled(true);
        mBinding.edittextUser.setEnabled(true);
        mBinding.progressContent.setVisibility(View.GONE);
    }

    private void controlarMedicoLogueado() {
        mPreferences = PreferencesHelper.getInstance();
        Log.i("123456789", "Token: " + FirebaseInstanceId.getInstance().getToken()); //TODO: Borrar
        if(mPreferences.getMedicoToken() != null){
            goToMap();
        }
    }

    private void goToMap() {
        startActivity(new Intent(LoginActivity.this, MapActivity.class));
        finish();
    }
}
