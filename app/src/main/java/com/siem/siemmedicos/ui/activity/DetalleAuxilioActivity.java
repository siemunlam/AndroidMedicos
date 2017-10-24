package com.siem.siemmedicos.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.siem.siemmedicos.R;
import com.siem.siemmedicos.databinding.ActivityDetallesAuxilioBinding;
import com.siem.siemmedicos.db.DBWrapper;
import com.siem.siemmedicos.model.app.Auxilio;
import com.siem.siemmedicos.utils.Constants;
import com.siem.siemmedicos.utils.Utils;

/**
 * Created by Lucas on 22/8/17.
 */
public class DetalleAuxilioActivity extends ToolbarActivity implements OnStreetViewPanoramaReadyCallback {

    private static final String MASCULINO = "M";
    private static final String FEMENINO = "F";
    public static final String KEY_CANCEL_AUXILIO = "CANCEL_AUXILIO";
    public static final int CODE_CANCEL_AUXILIO = 200;

    private ActivityDetallesAuxilioBinding mBinding;
    private BroadcastReceiver mBroadcastReceiver;
    private Auxilio mAuxilio;
    private Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detalles_auxilio);
        setToolbar(true);
        StreetViewPanoramaFragment streetViewPanoramaFragment = (StreetViewPanoramaFragment) getFragmentManager().findFragmentById(R.id.streetViewPanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
        mTypeface = Typeface.createFromAsset(getAssets(), Constants.PRIMARY_FONT);
        mBinding.scrollview.setVerticalScrollBarEnabled(false);
        mBinding.textviewPacienteTitle.setTypeface(mTypeface);
        mBinding.textviewPaciente.setTypeface(mTypeface);
        mBinding.textviewMotivosTitle.setTypeface(mTypeface);
        mBinding.textviewMotivos.setTypeface(mTypeface);
        mBinding.textviewSexoTitle.setTypeface(mTypeface);
        mBinding.textviewSexo.setTypeface(mTypeface);
        mBinding.textviewObservacionesTitle.setTypeface(mTypeface);
        mBinding.textviewObservaciones.setTypeface(mTypeface);
        mBinding.textviewContactoTitle.setTypeface(mTypeface);
        mBinding.textviewContacto.setTypeface(mTypeface);

        setDatos();
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        try{
            streetViewPanorama.setPosition(new LatLng(Double.parseDouble(mAuxilio.getLatitude()), Double.parseDouble(mAuxilio.getLongitude())));

            streetViewPanorama.setUserNavigationEnabled(false);
            streetViewPanorama.setZoomGesturesEnabled(false);
        }catch(Exception e){
            mBinding.contentStreetviewPanorama.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerBroadcastReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterBroadcastReceiver();
    }

    private void setDatos() {
        mAuxilio = DBWrapper.getAuxilio(this);

        //Datos
        mBinding.containerDetallesAuxilio.setDatos(mAuxilio, true);

        //Sexo
        if(mAuxilio.getSexo().isEmpty()){
            mBinding.textviewSexo.setVisibility(View.GONE);
            mBinding.textviewSexoTitle.setVisibility(View.GONE);
        }else{
            mBinding.textviewSexo.setVisibility(View.VISIBLE);
            mBinding.textviewSexoTitle.setVisibility(View.VISIBLE);
            mBinding.textviewSexo.setText(getSexo());
        }

        //Nombre paciente
        mBinding.textviewPaciente.setText(getNombrePaciente());

        //Contacto
        if(mAuxilio.getContacto().isEmpty()){
            mBinding.textviewContacto.setVisibility(View.GONE);
            mBinding.textviewContactoTitle.setVisibility(View.GONE);
        }else{
            mBinding.textviewContacto.setVisibility(View.VISIBLE);
            mBinding.textviewContactoTitle.setVisibility(View.VISIBLE);
            mBinding.textviewContacto.setText(mAuxilio.getContacto());
            boolean isDouble;
            try{
                Double.parseDouble(mAuxilio.getContacto());
                isDouble = true;
            }catch(Exception e){
                isDouble = false;
            }
            dialClickNumber(isDouble);
        }

        //Motivos
        mBinding.textviewMotivos.setText(mAuxilio.getParsedMotivos());

        //Observaciones
        if(mAuxilio.getObservaciones().isEmpty()){
            mBinding.textviewObservaciones.setVisibility(View.GONE);
            mBinding.textviewObservacionesTitle.setVisibility(View.GONE);
        }else{
            mBinding.textviewObservaciones.setVisibility(View.VISIBLE);
            mBinding.textviewObservacionesTitle.setVisibility(View.VISIBLE);
            mBinding.textviewObservaciones.setText(getObservaciones());
        }
    }

    private void dialClickNumber(boolean isDouble) {
        if(isDouble){
            SpannableString content = new SpannableString(mAuxilio.getContacto());
            content.setSpan(new UnderlineSpan(), 0, mAuxilio.getContacto().length(), 0);
            mBinding.textviewContacto.setText(content);
            mBinding.textviewContacto.setTextAppearance(this, R.style.linkTheme);
            mBinding.textviewContacto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.dialNumber(DetalleAuxilioActivity.this, mAuxilio.getContacto());
                }
            });
        }
    }

    private String getSexo() {
        if(!mAuxilio.getSexo().isEmpty()){
            switch(mAuxilio.getSexo()){
                case MASCULINO:
                    return getString(R.string.masculino);

                case FEMENINO:
                    return getString(R.string.femenino);

                default:
                    return getString(R.string.noEspecificado);
            }
        } else
            return getString(R.string.noEspecificado);
    }

    private String getNombrePaciente() {
        if(!mAuxilio.getNombrePaciente().isEmpty())
            return mAuxilio.getNombrePaciente();
        else
            return getString(R.string.noEspecificado);
    }

    private String getObservaciones() {
        return mAuxilio.getObservaciones();
    }

    private void registerBroadcastReceiver() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Constants.BROADCAST_CANCEL_AUXILIO)){
                    Toast.makeText(DetalleAuxilioActivity.this, getString(R.string.cancelAuxilio), Toast.LENGTH_LONG).show();
                    cancelAuxilio();
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.BROADCAST_CANCEL_AUXILIO);
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(this);
        bm.registerReceiver(mBroadcastReceiver, filter);
    }

    private void unregisterBroadcastReceiver(){
        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(this);
        bm.unregisterReceiver(mBroadcastReceiver);
    }

    private void cancelAuxilio() {
        Intent intent = new Intent();
        intent.putExtra(KEY_CANCEL_AUXILIO, CODE_CANCEL_AUXILIO);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
