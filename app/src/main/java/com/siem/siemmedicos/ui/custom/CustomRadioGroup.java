package com.siem.siemmedicos.ui.custom;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import com.siem.siemmedicos.R;
import com.siem.siemmedicos.utils.ApiConstants;

import java.util.List;

/**
 * Created by Lucas on 18/9/17.
 */

public class CustomRadioGroup extends RadioGroup {

    private Context mContext;

    public CustomRadioGroup(Context context) {
        super(context);
        mContext = context;
    }

    public CustomRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void addRadioButtons(List<ApiConstants.MotivoInasistencia> listMotivos) {
        for (ApiConstants.MotivoInasistencia motivo : listMotivos) {
            AppCompatRadioButton crb = new AppCompatRadioButton(getContext());
            crb.setText(motivo.getDescription(getContext()));
            crb.setId(motivo.getValue());
            crb.setTextColor(ContextCompat.getColor(mContext, android.R.color.black));
            crb.setTextSize(getResources().getDimensionPixelSize(R.dimen.textsizeOptions));
            //TODOOOOOOO
            addView(crb);
        }
    }
}
