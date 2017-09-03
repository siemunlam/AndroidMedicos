package com.siem.siemmedicos.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.ActivityFinalizarAuxilioBinding;
import com.siem.siemmedicos.ui.fragment.NegativeAttendFragment;
import com.siem.siemmedicos.ui.fragment.PossitiveAttendFragment;

/**
 * Created by Lucas on 3/9/17.
 */

public class FinalizarAuxilioActivity extends AppCompatActivity {

    private ActivityFinalizarAuxilioBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_finalizar_auxilio);

        setToolbar();
        setFragment();

        mBinding.switchAsistioPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment();
            }
        });
    }

    private void setToolbar() {
        setSupportActionBar(mBinding.appBarLayout.getToolbar());
        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle("");
        else
            mBinding.appBarLayout.setVisibility(View.GONE);
        mBinding.appBarLayout.setText(getString(R.string.name));
    }

    private void setFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment;
        if(mBinding.switchAsistioPaciente.isChecked())
            fragment = new PossitiveAttendFragment();
        else
            fragment = new NegativeAttendFragment();

        fm.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

}
