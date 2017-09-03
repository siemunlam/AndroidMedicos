package com.siem.siemmedicos.ui.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

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
public class DetalleAuxilioActivity extends AppCompatActivity implements OnStreetViewPanoramaReadyCallback {

    private ActivityDetallesAuxilioBinding mBinding;
    private Auxilio mAuxilio;
    private Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detalles_auxilio);
        setToolbar();
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

    private void setDatos() {
        mAuxilio = DBWrapper.getAuxilio(this);
        mBinding.containerDetallesAuxilio.setDatos(mAuxilio);
        mBinding.textviewPaciente.setText(mAuxilio.getNombrePaciente());
        mBinding.textviewMotivos.setText(mAuxilio.getParsedMotivos());
    }

    private void setToolbar() {
        setSupportActionBar(mBinding.appBarLayout.getToolbar());
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }else
            mBinding.appBarLayout.setVisibility(View.GONE);
        mBinding.appBarLayout.setText(getString(R.string.name));
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        streetViewPanorama.setPosition(new LatLng(Double.parseDouble(mAuxilio.getLatitude()), Double.parseDouble(mAuxilio.getLongitude())));

        streetViewPanorama.setUserNavigationEnabled(false);
        streetViewPanorama.setZoomGesturesEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        supportFinishAfterTransition();
        Utils.addFinishTransitionAnimation(this);
    }
}
