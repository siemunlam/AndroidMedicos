package com.siem.siemmedicos.ui;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.ActivityLoginBinding;
import com.siem.siemmedicos.model.serverapi.LoginResponse;
import com.siem.siemmedicos.utils.Constants;
import com.siem.siemmedicos.utils.PreferencesHelper;
import com.siem.siemmedicos.utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {

    private PreferencesHelper mPreferences;
    private ActivityLoginBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controlarMedicoLogueado();
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        mBinding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Animation shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.vibrate);
                //mBinding.textinputlayoutUser.startAnimation(shake);
                Call<LoginResponse> call = RetrofitClient.getServerClient().login(mBinding.edittextUser.getText().toString(), mBinding.edittextPass.getText().toString());
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        switch(response.code()){
                            case Constants.CODE_SERVER_OK:
                                goToMap();
                                break;
                            case Constants.CODE_BAD_REQUEST:
                                Toast.makeText(LoginActivity.this, "User o pass incorrecto", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                error();
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        error();
                    }
                });
            }
        });
    }

    private void error() {
        Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show();
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
