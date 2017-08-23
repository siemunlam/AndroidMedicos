package com.siem.siemmedicos.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.siem.siemmedicos.R;
import com.siem.siemmedicos.utils.Constants;

/**
 * Created by Lucas on 19/8/17.
 */

public class CustomEdittextLogin extends RelativeLayout {

    private Context mContext;
    private Typeface mTypeface;
    private AppCompatImageView mImage;
    private AppCompatEditText mEdittext;

    public CustomEdittextLogin(Context context) {
        super(context);
        initialice(context, null);
    }

    public CustomEdittextLogin(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialice(context, attrs);
    }

    private void initialice(Context context, AttributeSet attrs){
        inflate(context, R.layout.custom_edittext_login, this);
        mContext = context;
        mImage = (AppCompatImageView)findViewById(R.id.image);
        mEdittext = (AppCompatEditText)findViewById(R.id.edittext);
        mTypeface = Typeface.createFromAsset(context.getAssets(), Constants.PRIMARY_FONT);

        setAttrs(attrs);
    }

    private void setAttrs(AttributeSet attrs) {
        if(attrs != null){
            TypedArray typed = mContext.obtainStyledAttributes(attrs, R.styleable.customComponents);
            mImage.setImageDrawable(typed.getDrawable(R.styleable.customComponents_android_src));
            mEdittext.setHint(typed.getString(R.styleable.customComponents_android_hint));
            mEdittext.setInputType(typed.getInt(R.styleable.customComponents_android_inputType, 0));
            mEdittext.setTypeface(mTypeface);
            typed.recycle();
        }
    }

    public String getText(){
        return mEdittext.getText().toString();
    }

}
