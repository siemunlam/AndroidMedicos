package com.siem.siemmedicos.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.FragmentNegativeAttendBinding;
import com.siem.siemmedicos.model.app.FinalizarAuxilio;
import com.siem.siemmedicos.utils.ApiConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 3/9/17.
 */

public class NegativeAttendFragment extends AttendFragment {

    private FragmentNegativeAttendBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_negative_attend, container, false);
        View view = mBinding.getRoot();

        mBinding.radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(mListener != null)
                    mListener.showButton();
            }
        });

        setRadiogroupData();

        return view;
    }

    private void setRadiogroupData() {
        List<ApiConstants.Item> listMotivos = new ArrayList<>();
        listMotivos.add(new ApiConstants.UbicacionIncorrecta());
        listMotivos.add(new ApiConstants.SinRespuesta());
        listMotivos.add(new ApiConstants.YaTrasladado());
        listMotivos.add(new ApiConstants.Otro());

        mBinding.radiogroup.addRadioButtons(listMotivos);
    }

    @Override
    public FinalizarAuxilio getFinalizarAuxilio(){
        FinalizarAuxilio finalizarAuxilio = new FinalizarAuxilio();
        finalizarAuxilio.setMotivoInasistencia(mBinding.radiogroup.getCheckedRadioButtonId());
        if(!mBinding.edittextObservaciones.getText().toString().isEmpty())
            finalizarAuxilio.setObservaciones(mBinding.edittextObservaciones.getText().toString());
        return finalizarAuxilio;
    }
}
