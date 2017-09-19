package com.siem.siemmedicos.ui.fragment;

import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.FragmentNegativeAttendBinding;
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

        mBinding.borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "ID: " + mBinding.radiogroup
                        .getCheckedRadioButtonId(), Toast.LENGTH_LONG).show();
            }
        });

        setRadiogroupData();

        return view;
    }

    private void setRadiogroupData() {
        List<ApiConstants.MotivoInasistencia> listMotivos = new ArrayList<>();
        listMotivos.add(new ApiConstants.UbicacionIncorrecta());
        listMotivos.add(new ApiConstants.SinRespuesta());
        listMotivos.add(new ApiConstants.YaTrasladado());
        listMotivos.add(new ApiConstants.Otro());

        mBinding.radiogroup.addRadioButtons(listMotivos);
    }
}
