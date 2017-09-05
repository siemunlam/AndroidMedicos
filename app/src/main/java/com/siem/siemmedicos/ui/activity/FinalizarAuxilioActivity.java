package com.siem.siemmedicos.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.ActivityFinalizarAuxilioBinding;
import com.siem.siemmedicos.interfaces.ChangeVisibilityButtonListener;
import com.siem.siemmedicos.ui.fragment.AttendFragment;
import com.siem.siemmedicos.ui.fragment.NegativeAttendFragment;
import com.siem.siemmedicos.ui.fragment.PossitiveAttendFragment;

/**
 * Created by Lucas on 3/9/17.
 */

public class FinalizarAuxilioActivity extends ToolbarActivity implements ChangeVisibilityButtonListener {

    private ActivityFinalizarAuxilioBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_finalizar_auxilio);

        setToolbar(true);
        setFragment();

        mBinding.switchAsistioPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment();
                hideButton();
            }
        });
    }

    private void setFragment() {
        FragmentManager fm = getSupportFragmentManager();
        AttendFragment fragment;
        if(mBinding.switchAsistioPaciente.isChecked())
            fragment = new PossitiveAttendFragment();
        else
            fragment = new NegativeAttendFragment();

        fragment.setChangeVisibilityButtonListener(this);
        fm.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    @Override
    public void showButton() {
        if(mBinding.sendButton.getVisibility() != View.VISIBLE){
            mBinding.sendButton.startAnimation(AnimationUtils.loadAnimation(FinalizarAuxilioActivity.this, R.anim.show_center_button));
            mBinding.sendButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideButton(){
        if(mBinding.sendButton.getVisibility() != View.GONE){
            mBinding.sendButton.startAnimation(AnimationUtils.loadAnimation(FinalizarAuxilioActivity.this, R.anim.hide_center_button));
            mBinding.sendButton.setVisibility(View.GONE);
        }
    }

}
