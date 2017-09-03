package com.siem.siemmedicos.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.ActivityFinalizarAuxilioBinding;

/**
 * Created by Lucas on 3/9/17.
 */

public class FinalizarAuxilioActivity extends AppCompatActivity {

    private ActivityFinalizarAuxilioBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_finalizar_auxilio);

    }

}
