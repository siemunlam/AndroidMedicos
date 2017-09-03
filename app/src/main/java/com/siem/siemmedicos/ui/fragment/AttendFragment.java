package com.siem.siemmedicos.ui.fragment;

import android.support.v4.app.Fragment;

import com.siem.siemmedicos.interfaces.ChangeVisibilityButtonListener;

/**
 * Created by Lucas on 3/9/17.
 */

public class AttendFragment extends Fragment {

    protected ChangeVisibilityButtonListener mListener;

    public void setChangeVisibilityButtonListener(ChangeVisibilityButtonListener listener){
        mListener = listener;
    }

}
