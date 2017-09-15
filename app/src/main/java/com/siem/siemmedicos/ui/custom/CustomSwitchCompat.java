package com.siem.siemmedicos.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.siem.siemmedicos.R;

/**
 * Created by Lucas on 14/9/17.
 */

public class CustomSwitchCompat extends RelativeLayout {

    private Context mContext;
    public RelativeLayout mContent;
    private AppCompatTextView mTextview;
    private ToggleButton mToggleButton;
    private OnClickListener mListener;

    public CustomSwitchCompat(Context context) {
        super(context);
        initialice(context, null);
    }

    public CustomSwitchCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialice(context, attrs);
    }

    private void initialice(Context context, AttributeSet attrs){
        inflate(context, R.layout.custom_switch_compat, this);
        mContext = context;
        mContent = (RelativeLayout)findViewById(R.id.content);
        mTextview = (AppCompatTextView)findViewById(R.id.textview);
        mToggleButton = (ToggleButton)findViewById(R.id.toggleButton);
        mListener = new OnClickListener() {
            @Override
            public void onClick(View v) {}
        };

        setAttrs(attrs);

        mContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mToggleButton.setChecked(!mToggleButton.isChecked());

                if(mToggleButton.isChecked()){
                    mToggleButton.setGravity(Gravity.START | Gravity.CENTER);
                }else{
                    mToggleButton.setGravity(Gravity.END | Gravity.CENTER);
                }

                mListener.onClick(v);
            }
        });
    }

    private void setAttrs(AttributeSet attrs) {
        if(attrs != null){
            TypedArray typed = mContext.obtainStyledAttributes(attrs, R.styleable.customComponents);
            mTextview.setText(typed.getString(R.styleable.customComponents_android_text));
            mToggleButton.setChecked(typed.getBoolean(R.styleable.customComponents_android_checked, false));
            typed.recycle();
        }
    }

    public boolean isChecked(){
        return mToggleButton.isChecked();
    }

    public void setOnClick(OnClickListener listener) {
        mListener = listener;
    }
}
