package com.siem.siemmedicos.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.siem.siemmedicos.R;

/**
 * Created by Lucas on 3/9/17.
 */

public class PossitiveAttendFragment extends AttendFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_possitive_attend, container, false);

        return view;
    }
}
