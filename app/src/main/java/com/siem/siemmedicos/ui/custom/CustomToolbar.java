package com.siem.siemmedicos.ui.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import com.siem.siemmedicos.R;
import com.siem.siemmedicos.utils.Constants;

/**
 * Created by Lucas on 1/9/17.
 */

public class CustomToolbar extends AppBarLayout {

    private Typeface mTypefaceLogo;
    private AppCompatTextView mToolbarTitle;
    private Toolbar mToolbar;

    public CustomToolbar(Context context) {
        super(context);
        initialice(context, null);
    }

    public CustomToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialice(context, attrs);
    }

    private void initialice(Context context, AttributeSet attrs){
        inflate(context, R.layout.custom_toolbar, this);
        mTypefaceLogo = Typeface.createFromAsset(context.getAssets(), Constants.LOGO_FONT);
        mToolbarTitle = (AppCompatTextView)findViewById(R.id.toolbarTitle);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);

        mToolbarTitle.setTypeface(mTypefaceLogo);
    }

    public Toolbar getToolbar(){
        return mToolbar;
    }

    public void setText(String text){
        mToolbarTitle.setText(text);
    }

}
