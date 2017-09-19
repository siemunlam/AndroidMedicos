package com.siem.siemmedicos.ui.custom;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.util.TypedValue;
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
            AppCompatRadioButton radioButton = new AppCompatRadioButton(getContext());
            radioButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            radioButton.setText(motivo.getDescription(getContext()));
            radioButton.setId(motivo.getValue());
            radioButton.setTextColor(ContextCompat.getColor(mContext, android.R.color.black));
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.textsizeOptions));
            TypedValue typedValue = new TypedValue();
            mContext.getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
            radioButton.setBackgroundResource(typedValue.resourceId);
            addView(radioButton);
        }
    }
}
