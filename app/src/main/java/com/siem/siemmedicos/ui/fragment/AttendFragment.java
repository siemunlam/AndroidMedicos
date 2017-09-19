package com.siem.siemmedicos.ui.fragment;

import android.support.v4.app.Fragment;

import com.siem.siemmedicos.interfaces.ChangeVisibilityButtonListener;
import com.siem.siemmedicos.model.app.FinalizarAuxilio;

/**
 * Created by Lucas on 3/9/17.
 */

public class AttendFragment extends Fragment {

    protected ChangeVisibilityButtonListener mListener;

    public void setChangeVisibilityButtonListener(ChangeVisibilityButtonListener listener){
        mListener = listener;
    }

    public FinalizarAuxilio getFinalizarAuxilio(){
        return new FinalizarAuxilio();
    }

}
