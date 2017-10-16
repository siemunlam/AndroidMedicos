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
        mBinding.containerDetallesAuxilio.setDatos(mAuxilio);
        if(!mAuxilio.getNombrePaciente().isEmpty())
            mBinding.textviewPaciente.setText(mAuxilio.getNombrePaciente());
        else
            mBinding.textviewPaciente.setText(getString(R.string.noEspecificado));
        mBinding.textviewMotivos.setText(mAuxilio.getParsedMotivos());
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
