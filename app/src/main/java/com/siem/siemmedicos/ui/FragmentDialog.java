package com.siem.siemmedicos.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.siem.siemmedicos.R;

public class FragmentDialog extends Fragment {

    public Dialog getTextViewDialog(Activity activity,
                                    String textShow,
                                    String acceptText,
                                    DialogInterface.OnClickListener acceptListener,
                                    String cancelText,
                                    DialogInterface.OnClickListener cancelListener,
                                    boolean cancelable){
        View view = View.inflate(activity, R.layout.dialog, null);
        //Typeface mTypeface = Typeface.createFromAsset(activity.getAssets(), "fonts/helvetica_medium.otf");
        final AppCompatTextView mTextView = (AppCompatTextView)view.findViewById(R.id.textview);
        //mTextView.setTypeface(mTypeface);
        mTextView.setText(textShow);
        return new AlertDialog.Builder(activity)
                .setView(view)
                .setPositiveButton(acceptText, acceptListener)
                .setNegativeButton(cancelText, cancelListener)
                .setCancelable(cancelable)
                .create();
    }

}
