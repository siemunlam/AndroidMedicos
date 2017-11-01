package com.siem.siemmedicos.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.WindowManager;
import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.ActivityChangePasswordBinding;

public class ChangePasswordActivity extends ToolbarActivity {

    private ActivityChangePasswordBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);

    }

    /*private void habilitarLoading() {
        mBinding.buttonLogin.setEnabled(false);
        mBinding.edittextPass.setEnabled(false);
        mBinding.edittextUser.setEnabled(false);
        setTouchable(false);
        mBinding.progress.setVisibility(View.VISIBLE);
    }

    private void deshabilitarLoading() {
        mBinding.buttonLogin.setEnabled(true);
        mBinding.edittextPass.setEnabled(true);
        mBinding.edittextUser.setEnabled(true);
        setTouchable(true);
        mBinding.progress.setVisibility(View.GONE);
    }*/

    private void setTouchable(boolean touchable) {
        if(touchable){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }else{
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}
