package com.siem.siemmedicos.model;

/**
 * Created by Lucas on 4/9/17.
 */

public class ModelObject {

    private String mTitle;
    private int mLayoutResId;

    public ModelObject(String title, int layoutResId) {
        mTitle = title;
        mLayoutResId = layoutResId;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}
