package com.siem.siemmedicos.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.google.gson.Gson;
import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.ActivityFinalizarAuxilioBinding;
import com.siem.siemmedicos.interfaces.ChangeVisibilityButtonListener;
import com.siem.siemmedicos.model.app.FinalizarAuxilio;
import com.siem.siemmedicos.ui.fragment.AttendFragment;
import com.siem.siemmedicos.ui.fragment.NegativeAttendFragment;
import com.siem.siemmedicos.ui.fragment.PossitiveAttendFragment;

/**
 * Created by Lucas on 3/9/17.
 */

public class FinalizarAuxilioActivity extends ToolbarActivity implements ChangeVisibilityButtonListener {

    private ActivityFinalizarAuxilioBinding mBinding;
    private FinalizarAuxilio mFinalizarAuxilio;
    private AttendFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_finalizar_auxilio);

        setToolbar(true);
        setFragment();

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
                    String finalizarAuxilioString = gson.toJson(mFinalizarAuxilio);
                }
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

}
