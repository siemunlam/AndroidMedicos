package com.siem.siemmedicos.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.FragmentPossitiveAttendBinding;
import com.siem.siemmedicos.interfaces.PossitiveChangeVisibilityButtonListener;
import com.siem.siemmedicos.model.app.FinalizarAuxilio;
import com.siem.siemmedicos.model.app.Paciente;
import com.siem.siemmedicos.ui.custom.CustomPagerAdapter;
import com.siem.siemmedicos.utils.ApiConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 3/9/17.
 */

public class PossitiveAttendFragment extends AttendFragment implements
        ViewPager.OnPageChangeListener,
        PossitiveChangeVisibilityButtonListener {

    private static final int FIRST_ITEM_SELECTED = 0;
    private static final int RADIOGROUP_NO_SELECTED = -1;

    private FragmentPossitiveAttendBinding mBinding;
    private CustomPagerAdapter mAdapter;
    private List<ImageView> mListDots;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_possitive_attend, container, false);
        View view = mBinding.getRoot();

        mAdapter = new CustomPagerAdapter(getActivity(), this);
        mBinding.pager.setAdapter(mAdapter);
        mBinding.pager.setCurrentItem(FIRST_ITEM_SELECTED);
        mBinding.pager.addOnPageChangeListener(this);
        setUiPageViewController();

        mBinding.switchBienCategorizado.setOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRadiogroupVisibility();
                controlateSendButton();
            }
        });

        mBinding.agregarPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paciente paciente = new Paciente();
                mAdapter.addPaciente(paciente);
                addDot();
                mBinding.pager.setCurrentItem(mListDots.size() - 1);
                controlateRemoverPaciente();
                controlateSendButton();
            }
        });

        mBinding.removerPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListDots.size() > 1){
                    int position = mBinding.pager.getCurrentItem();
                    removeDot(position);
                    mAdapter.removePaciente(position);
                    mBinding.pager.setCurrentItem(position > 0 ? position - 1 : 0);
                    if(position == 0)
                        onPageSelected(0);
                    controlateRemoverPaciente();
                    controlateSendButton();
                }
            }
        });

        setRadiogroupData();

        mBinding.radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                controlateSendButton();
            }
        });

        return view;
    }

    private void setRadiogroupData() {
        List<ApiConstants.Item> listCategorizacion = new ArrayList<>();
        listCategorizacion.add(new ApiConstants.Supercategorizado());
        listCategorizacion.add(new ApiConstants.Subcategorizado());

        mBinding.radiogroup.addRadioButtons(listCategorizacion);
    }

    private void setRadiogroupVisibility() {
        if (mBinding.switchBienCategorizado.isChecked()) {
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.hide_top_radiogroup);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    mBinding.radiogroup.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            mBinding.radiogroup.setAnimation(animation);
            mBinding.radiogroup.startAnimation(animation);
        } else {
            mBinding.radiogroup.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.show_top_radiogroup);
            mBinding.radiogroup.setAnimation(animation);
            mBinding.radiogroup.startAnimation(animation);
        }
    }

    private void setUiPageViewController() {
        mListDots = new ArrayList<>();
        addDot();
        mListDots.get(FIRST_ITEM_SELECTED).setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.selecteditem_dot));
    }

    private void addDot(){
        mListDots.add(new ImageView(getContext()));
        int lastAdded = mListDots.size() - 1;
        mListDots.get(lastAdded).setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.nonselecteditem_dot));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(4, 0, 4, 0);
        mBinding.viewPagerCountDots.addView(mListDots.get(lastAdded), params);
    }

    private void removeDot(int position){
        mListDots.remove(position);
        mBinding.viewPagerCountDots.removeViewAt(position);
    }

    private void controlateRemoverPaciente() {
        if(mListDots.size() > 1)
            showRemovePaciente();
        else
            hideRemoverPaciente();
    }

    private void hideRemoverPaciente(){
        if(mBinding.removerPaciente.getVisibility() != View.GONE){
            mBinding.removerPaciente.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.hide_center));
            mBinding.removerPaciente.setVisibility(View.GONE);
        }
    }

    private void showRemovePaciente(){
        if(mBinding.removerPaciente.getVisibility() != View.VISIBLE){
            mBinding.removerPaciente.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.show_center));
            mBinding.removerPaciente.setVisibility(View.VISIBLE);
        }
    }

    /**
     * PossitiveChangeVisibilityButtonListener
     */
    @Override
    public void controlateSendButton(){
        if(mListener == null)
            return;
        if(!mBinding.switchBienCategorizado.isChecked() &&
                mBinding.radiogroup.getCheckedRadioButtonId() == RADIOGROUP_NO_SELECTED){
            mListener.hideButton();
            return;
        }else if(!mAdapter.haveData()){
            mListener.hideButton();
            return;
        }

        mListener.showButton();
    }

    /**
     * ViewPager.OnPageChangeListener
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < mListDots.size(); i++) {
            mListDots.get(i).setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.nonselecteditem_dot));
        }
        mListDots.get(position).setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public FinalizarAuxilio getFinalizarAuxilio(){
        FinalizarAuxilio finalizarAuxilio = new FinalizarAuxilio();
        finalizarAuxilio.setCategorizacion(mBinding.switchBienCategorizado.isChecked() ? new ApiConstants.BienCategorizado().getValue() : mBinding.radiogroup.getCheckedRadioButtonId());
        finalizarAuxilio.setListPaciente(mAdapter.getListData());
        return finalizarAuxilio;
    }
}
