package com.siem.siemmedicos.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.ActivityLoginBinding;
import com.siem.siemmedicos.model.serverapi.LoginResponse;
import com.siem.siemmedicos.utils.ApiConstants;
import com.siem.siemmedicos.utils.Constants;
import com.siem.siemmedicos.utils.PreferencesHelper;
import com.siem.siemmedicos.utils.RetrofitClient;
import com.siem.siemmedicos.utils.Utils;

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

        mBinding.contentProgress.bringToFront();
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

        setBackgroundImage();
    }

    @Override
    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
        switch(response.code()){
            case Constants.CODE_SERVER_OK:
                LoginResponse loginResponse = response.body();
                ApiConstants.Item estadoNoDisponible = new ApiConstants.NoDisponible();
                mPreferences.setMedicoToken(loginResponse.getToken());
                mPreferences.setValueEstado(estadoNoDisponible.getValue());
                mPreferences.setDescriptionEstado(estadoNoDisponible.getDescription(LoginActivity.this));
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

    private void setBackgroundImage() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background_login, options);
        Bitmap resizedbitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        BitmapDrawable bmpDrawable = new BitmapDrawable(getResources(), resizedbitmap);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBinding.scrollview.setBackground(bmpDrawable);
        }else{
            mBinding.scrollview.setBackgroundDrawable(bmpDrawable);
        }
        bitmap.recycle();
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
            Utils.vibrate(this, listErrorView);
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
        Utils.vibrate(this, listErrorView);
    }

    private void habilitarLoading() {
        mBinding.buttonLogin.setEnabled(false);
        mBinding.edittextPass.setEnabled(false);
        mBinding.edittextUser.setEnabled(false);
        Utils.setTouchable(this, false);
        mBinding.contentProgress.setVisibility(View.VISIBLE);
        Utils.hideSoftKeyboard(this);
    }

    private void deshabilitarLoading() {
        mBinding.buttonLogin.setEnabled(true);
        mBinding.edittextPass.setEnabled(true);
        mBinding.edittextUser.setEnabled(true);
        Utils.setTouchable(this, true);
        mBinding.contentProgress.setVisibility(View.GONE);
    }

    private void controlarMedicoLogueado() {
        mPreferences = PreferencesHelper.getInstance();
        mPreferences.setFirebaseToken(FirebaseInstanceId.getInstance().getToken());
        if(mPreferences.getMedicoToken() != null){
            goToMap();
        }
    }

    private void goToMap() {
        startActivity(new Intent(LoginActivity.this, MapActivity.class));
        finish();
    }
}
