package com.siem.siemmedicos.ui.custom;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.siem.siemmedicos.R;
import com.siem.siemmedicos.utils.ApiConstants;
import com.siem.siemmedicos.utils.Constants;
import com.siem.siemmedicos.utils.PreferencesHelper;
import com.siem.siemmedicos.utils.RetrofitClient;
import com.siem.siemmedicos.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomFragmentDialog extends Fragment {

    private PreferencesHelper mPreferencesHelper;

    public Dialog getTextViewDialog(Activity activity,
                                    String textShow,
                                    String acceptText,
                                    DialogInterface.OnClickListener acceptListener,
                                    String cancelText,
                                    DialogInterface.OnClickListener cancelListener,
                                    boolean cancelable){
        View view = View.inflate(activity, R.layout.custom_dialog_text, null);
        Typeface mTypeface = Typeface.createFromAsset(activity.getAssets(), Constants.PRIMARY_FONT);
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

    public Dialog getRadioButtonsEstadoDialog(final Activity activity,
                                              final String acceptText,
                                              boolean cancelable){
        mPreferencesHelper = PreferencesHelper.getInstance();
        Typeface mTypeface = Typeface.createFromAsset(activity.getAssets(), Constants.PRIMARY_FONT);
        View view = View.inflate(activity, R.layout.custom_dialog_radiobuttons, null);
        final RadioGroup mRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        final AppCompatRadioButton radioButtonDisponible = new AppCompatRadioButton(activity);
        ApiConstants.Item disponible = new ApiConstants.Disponible();
        radioButtonDisponible.setText(disponible.getDescription(activity));
        radioButtonDisponible.setTypeface(mTypeface);
        radioButtonDisponible.setId(disponible.getValue());
        radioButtonDisponible.setChecked(mPreferencesHelper.getValueEstado() == disponible.getValue());

        final AppCompatRadioButton radioButtonNoDisponible = new AppCompatRadioButton(activity);
        ApiConstants.Item noDisponible = new ApiConstants.NoDisponible();
        radioButtonNoDisponible.setText(noDisponible.getDescription(activity));
        radioButtonNoDisponible.setTypeface(mTypeface);
        radioButtonNoDisponible.setId(noDisponible.getValue());
        radioButtonNoDisponible.setChecked(mPreferencesHelper.getValueEstado() == noDisponible.getValue());

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
                if(mPreferencesHelper.getValueEstado() != mRadioGroup.getCheckedRadioButtonId()){
                    updateEstado(activity, mRadioGroup.getCheckedRadioButtonId());
                }
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

    private void updateEstado(final Activity activity, final int checkedRadioButtonId) {
        Call<Object> response = RetrofitClient.getServerClient().updateEstadoMedico(mPreferencesHelper.getAuthorization(), checkedRadioButtonId);
        response.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                switch(response.code()){
                    case Constants.CODE_SERVER_OK:
                        mPreferencesHelper.setValueEstado(checkedRadioButtonId);
                        mPreferencesHelper.setDescriptionEstado(Utils.getDescriptionEstado(activity, checkedRadioButtonId));
                        Utils.restarLocationsServices(activity);
                        break;
                    case Constants.CODE_UNAUTHORIZED:
                        Utils.logout(activity);
                        activity.finish();
                        break;
                    default:
                        Toast.makeText(activity, activity.getString(R.string.errorUpdateEstado), Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(activity, activity.getString(R.string.errorUpdateEstado), Toast.LENGTH_LONG).show();
            }
        });
    }

}
