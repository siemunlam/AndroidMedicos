package com.siem.siemmedicos.ui.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.ActivityChangePasswordBinding;
import com.siem.siemmedicos.utils.Constants;
import com.siem.siemmedicos.utils.PreferencesHelper;
import com.siem.siemmedicos.utils.RetrofitClient;
import com.siem.siemmedicos.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends ToolbarActivity implements Callback<Object>{

    private ActivityChangePasswordBinding mBinding;
    private Typeface mTypeface;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        setToolbar(true);
        mTypeface = Typeface.createFromAsset(getAssets(), Constants.PRIMARY_FONT);

        mBinding.titleChangePassword.setTypeface(mTypeface);
        mBinding.edittextOldPassword.setTypeface(mTypeface);
        mBinding.edittextNewPassword.setTypeface(mTypeface);
        mBinding.edittextReNewPassword.setTypeface(mTypeface);

        mBinding.changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Regex de password
                if(isFullComplete() && isSamePassword()){
                    habilitarLoading();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            PreferencesHelper preferences = PreferencesHelper.getInstance();
                            String oldPass = mBinding.edittextOldPassword.getText().toString();
                            String newPass = mBinding.edittextNewPassword.getText().toString();
                            Call<Object> call = RetrofitClient.getServerClient().changePassword(preferences.getAuthorization(), oldPass, newPass, newPass);
                            call.enqueue(ChangePasswordActivity.this);
                        }
                    }, 100);
                }
            }
        });
    }

    private boolean isFullComplete() {
        String oldPass = mBinding.edittextOldPassword.getText().toString();
        String newPass = mBinding.edittextNewPassword.getText().toString();
        String reNewPass = mBinding.edittextReNewPassword.getText().toString();
        List<View> listErrorView = new ArrayList<>();
        if(oldPass.isEmpty())
            listErrorView.add(mBinding.textinputOldPassword);
        if(newPass.isEmpty())
            listErrorView.add(mBinding.textinputNewPassword);
        if(reNewPass.isEmpty())
            listErrorView.add(mBinding.textinputReNewPassword);

        errorFields(listErrorView, getString(R.string.errorEmptyFields));
        return listErrorView.size() == 0;
    }

    private boolean isSamePassword() {
        String newPass = mBinding.edittextNewPassword.getText().toString();
        String reNewPass = mBinding.edittextReNewPassword.getText().toString();

        List<View> listErrorView = new ArrayList<>();
        if(!newPass.equals(reNewPass)){
            listErrorView.add(mBinding.textinputNewPassword);
            listErrorView.add(mBinding.textinputReNewPassword);
        }

        errorFields(listErrorView, getString(R.string.errorNewPassword));
        return listErrorView.size() == 0;
    }

    private void errorFields(List<View> listErrorView, String error) {
        if(listErrorView.size() > 0){
            mToast = Toast.makeText(ChangePasswordActivity.this, error, Toast.LENGTH_LONG);
            mToast.show();
            Utils.vibrate(this, listErrorView);
        }
    }

    private void habilitarLoading() {
        mBinding.changeButton.setEnabled(false);
        mBinding.edittextOldPassword.setEnabled(false);
        mBinding.edittextNewPassword.setEnabled(false);
        mBinding.edittextReNewPassword.setEnabled(false);
        Utils.setTouchable(this, false);
        mBinding.contentProgress.setVisibility(View.VISIBLE);
        Utils.hideSoftKeyboard(this);
    }

    private void deshabilitarLoading() {
        mBinding.changeButton.setEnabled(true);
        mBinding.edittextOldPassword.setEnabled(true);
        mBinding.edittextNewPassword.setEnabled(true);
        mBinding.edittextReNewPassword.setEnabled(true);
        Utils.setTouchable(this, true);
        mBinding.contentProgress.setVisibility(View.GONE);
    }

    private void serverError(){
        deshabilitarLoading();
        mToast = Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG);
        mToast.show();
    }

    /**
     * Callback<Object>
     */
    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        switch(response.code()){
            case Constants.CODE_SERVER_OK:
                Toast.makeText(this, getString(R.string.changePasswordCorrectly), Toast.LENGTH_SHORT).show();
                finish();
                break;

            case Constants.CODE_BAD_REQUEST:
                deshabilitarLoading();
                List<View> listErrorView = new ArrayList<>();
                listErrorView.add(mBinding.textinputOldPassword);
                errorFields(listErrorView, getString(R.string.wrongPassword));
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
}
