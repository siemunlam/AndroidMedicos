package com.siem.siemmedicos.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.siem.siemmedicos.R;
import com.siem.siemmedicos.utils.Constants;
import com.siem.siemmedicos.utils.PreferencesHelper;
import com.siem.siemmedicos.utils.Utils;

public class FragmentDialog extends Fragment {

    public Dialog getTextViewDialog(Activity activity,
                                    String textShow,
                                    String acceptText,
                                    DialogInterface.OnClickListener acceptListener,
                                    String cancelText,
                                    DialogInterface.OnClickListener cancelListener,
                                    boolean cancelable){
        View view = View.inflate(activity, R.layout.dialog_text, null);
        Typeface mTypeface = Typeface.createFromAsset(activity.getAssets(), "fonts/rounded_elegance.ttf");
        final AppCompatTextView mTextView = (AppCompatTextView)view.findViewById(R.id.textview);
        mTextView.setTypeface(mTypeface);
        mTextView.setText(textShow);
        return new AlertDialog.Builder(activity)
                .setView(view)
                .setPositiveButton(acceptText, acceptListener)
                .setNegativeButton(cancelText, cancelListener)
                .setCancelable(cancelable)
                .create();
    }

    public Dialog getRadioButtonsDialog(final Activity activity,
                                        String acceptText,
                                        boolean cancelable){
        final PreferencesHelper preferencesHelper = PreferencesHelper.getInstance();
        Typeface mTypeface = Typeface.createFromAsset(activity.getAssets(), "fonts/rounded_elegance.ttf");
        View view = View.inflate(activity, R.layout.dialog_radiobuttons, null);
        final RadioGroup mRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        final AppCompatRadioButton radioButtonDisponible = new AppCompatRadioButton(activity);
        radioButtonDisponible.setText(Constants.Disponible.getDescription(activity));
        radioButtonDisponible.setTypeface(mTypeface);
        radioButtonDisponible.setId(Constants.Disponible.getValue());
        radioButtonDisponible.setChecked(preferencesHelper.getValueEstado() == Constants.Disponible.getValue());

        final AppCompatRadioButton radioButtonNoDisponible = new AppCompatRadioButton(activity);
        radioButtonNoDisponible.setText(Constants.NoDisponible.getDescription(activity));
        radioButtonNoDisponible.setTypeface(mTypeface);
        radioButtonNoDisponible.setId(Constants.NoDisponible.getValue());
        radioButtonNoDisponible.setChecked(preferencesHelper.getValueEstado() == Constants.NoDisponible.getValue());

        radioButtonDisponible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButtonNoDisponible.setChecked(false);
                radioButtonDisponible.setChecked(true);
            }
        });
        radioButtonNoDisponible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButtonDisponible.setChecked(false);
                radioButtonNoDisponible.setChecked(true);
            }
        });

        DialogInterface.OnClickListener acceptListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("123456789", "ACA: "+mRadioGroup.getCheckedRadioButtonId());
                //TODO: API CALLL Cambio de estado
                preferencesHelper.setValueEstado(mRadioGroup.getCheckedRadioButtonId());
                preferencesHelper.setDescriptionEstado(Utils.getDescriptionEstado(activity, mRadioGroup.getCheckedRadioButtonId()));
                Utils.restarLocationsServices(activity);
            }
        };

        mRadioGroup.addView(radioButtonDisponible);
        mRadioGroup.addView(radioButtonNoDisponible);
        return new AlertDialog.Builder(activity)
                .setView(view)
                .setPositiveButton(acceptText, acceptListener)
                .setCancelable(cancelable)
                .create();
    }

}
