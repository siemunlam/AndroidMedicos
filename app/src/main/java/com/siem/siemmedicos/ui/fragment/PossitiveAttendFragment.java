package com.siem.siemmedicos.ui.fragment;

import android.animation.Animator;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;

import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.FragmentNegativeAttendBinding;
import com.siem.siemmedicos.databinding.FragmentPossitiveAttendBinding;

/**
 * Created by Lucas on 3/9/17.
 */

public class PossitiveAttendFragment extends AttendFragment {

    private FragmentPossitiveAttendBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_possitive_attend, container, false);
        View view = mBinding.getRoot();

        mBinding.switchBienCategorizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRadiogroupVisibility();
            }
        });

        return view;
    }

    private void setRadiogroupVisibility() {
        if(mBinding.switchBienCategorizado.isChecked()) {
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.hide_top_radiogroup);
            mBinding.radiogroup.setAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mBinding.radiogroup.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }else {
            mBinding.radiogroup.setVisibility(View.VISIBLE);
            mBinding.radiogroup.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.show_top_radiogroup));
        }
    }
}
