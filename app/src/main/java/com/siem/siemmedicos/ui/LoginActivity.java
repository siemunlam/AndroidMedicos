package com.siem.siemmedicos.ui;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.ActivityLoginBinding;
import com.siem.siemmedicos.utils.PreferencesHelper;

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
                mPreferences.setMedicoId("1");
                startActivity(new Intent(LoginActivity.this, MapActivity.class));
                finish();
            }
        });
    }

    private void controlarMedicoLogueado() {
        mPreferences = PreferencesHelper.getInstance();
        Log.i("123456789", "Token: " + FirebaseInstanceId.getInstance().getToken()); //TODO: Borrar
        if(mPreferences.getMedicoId() != null){
            startActivity(new Intent(LoginActivity.this, MapActivity.class));
            finish();
        }
    }
}
