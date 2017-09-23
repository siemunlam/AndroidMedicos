package com.siem.siemmedicos.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.ActivityFinalizarAuxilioBinding;
import com.siem.siemmedicos.interfaces.ChangeVisibilityButtonListener;
import com.siem.siemmedicos.model.app.FinalizarAuxilio;
import com.siem.siemmedicos.ui.fragment.AttendFragment;
import com.siem.siemmedicos.ui.fragment.NegativeAttendFragment;
import com.siem.siemmedicos.ui.fragment.PossitiveAttendFragment;
import com.siem.siemmedicos.utils.Constants;
import com.siem.siemmedicos.utils.PreferencesHelper;
import com.siem.siemmedicos.utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lucas on 3/9/17.
 */

public class FinalizarAuxilioActivity extends ToolbarActivity implements ChangeVisibilityButtonListener {

    private static final String KEY_STATE_SAVED = "STATE_SAVED";
    public static final String KEY_RESPONSE_CODE = "RESPONSE_CODE";

    private ActivityFinalizarAuxilioBinding mBinding;
    private PreferencesHelper mPreferencesHelper;
    private FinalizarAuxilio mFinalizarAuxilio;
    private AttendFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_finalizar_auxilio);

        setToolbar(true);
        setFragment();

        mPreferencesHelper = PreferencesHelper.getInstance();
        mBinding.switchAsistioPaciente.setOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment();
                hideButton();
            }
        });

        mBinding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFragment != null){
                    mFinalizarAuxilio = mFragment.getFinalizarAuxilio();
                    mFinalizarAuxilio.setAsistenciaRealizada(mBinding.switchAsistioPaciente.isChecked());
                    Gson gson = new Gson();
                    finalizarAuxilio(gson.toJson(mFinalizarAuxilio));
                }
            }
        });

        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_STATE_SAVED)){
            finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_STATE_SAVED, true);
    }

    @Override
    public void showButton() {
        if(mBinding.sendButton.getVisibility() != View.VISIBLE){
            mBinding.sendButton.startAnimation(AnimationUtils.loadAnimation(FinalizarAuxilioActivity.this, R.anim.show_center));
            mBinding.sendButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideButton(){
        if(mBinding.sendButton.getVisibility() != View.GONE){
            mBinding.sendButton.startAnimation(AnimationUtils.loadAnimation(FinalizarAuxilioActivity.this, R.anim.hide_center));
            mBinding.sendButton.setVisibility(View.GONE);
        }
    }

    private void finalizarAuxilio(String finalizarAuxilioString) {
        Call<Object> response = RetrofitClient.getServerClient().finalizarAuxilio(mPreferencesHelper.getAuthorization(), finalizarAuxilioString);
        response.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                switch(response.code()){
                    case Constants.CODE_SERVER_OK:
                    case Constants.CODE_BAD_REQUEST:
                    case Constants.CODE_UNAUTHORIZED:
                        Intent intent = new Intent();
                        intent.putExtra(KEY_RESPONSE_CODE, response.code());
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                        break;
                    default:
                        Toast.makeText(FinalizarAuxilioActivity.this, getString(R.string.errorFinalizarAuxilio), Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(FinalizarAuxilioActivity.this, getString(R.string.errorFinalizarAuxilio), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setFragment() {
        FragmentManager fm = getSupportFragmentManager();
        if(mBinding.switchAsistioPaciente.isChecked())
            mFragment = new PossitiveAttendFragment();
        else
            mFragment = new NegativeAttendFragment();

        mFragment.setChangeVisibilityButtonListener(this);
        fm.beginTransaction().replace(R.id.fragmentContainer, mFragment).commit();
    }

}
