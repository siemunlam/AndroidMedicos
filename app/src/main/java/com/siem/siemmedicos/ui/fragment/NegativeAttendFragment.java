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
                mListener.showButton();
            }
        });

        return view;
    }
}
