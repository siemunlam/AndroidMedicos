package com.siem.siemmedicos.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.siem.siemmedicos.db.DBWrapper;
import com.siem.siemmedicos.model.app.AppLocation;
import com.siem.siemmedicos.model.app.Auxilio;
import com.siem.siemmedicos.utils.ApiConstants;
import com.siem.siemmedicos.utils.PreferencesHelper;
import com.siem.siemmedicos.utils.Utils;

import static com.siem.siemmedicos.utils.Constants.MIN_DISTANCE_CHANGE_TO_ENLUGAR;
import static com.siem.siemmedicos.utils.Constants.MIN_DISTANCE_CHANGE_TO_ENTRASLADO;

/**
 * Created by lucas on 10/10/17.
 */
public class DeterminarEstadoAsignacionTask extends AsyncTask<AppLocation, Void, Void> {

    private Context mContext;
    private PreferencesHelper mPreferencesHelper;

    public DeterminarEstadoAsignacionTask(Context context){
        mContext = context;
        mPreferencesHelper = PreferencesHelper.getInstance();
    }

    @Override
    protected Void doInBackground(AppLocation... params) {
        AppLocation location = params[0];
        Auxilio auxilio = DBWrapper.getAuxilio(mContext);
        if(auxilio.getLatitude() != null && auxilio.getLongitude() != null && auxilio.getIdEstado() != new ApiConstants.EnTraslado().getValue()){
            double distancia = Utils.distanceCoordinatesInMeters(
                                        location.getLatitude(),
                                        location.getLongitude(),
                                        Double.parseDouble(auxilio.getLatitude()),
                                        Double.parseDouble(auxilio.getLongitude()));
            Log.i("123456789AA", "Distancia: " + distancia + " - Estado: " + auxilio.getIdEstado());
            if(auxilio.getIdEstado() == new ApiConstants.EnCamino().getValue() && distancia < MIN_DISTANCE_CHANGE_TO_ENLUGAR){
                //Change to en lugar
                DBWrapper.updateEstadoAuxilio(mContext, new ApiConstants.EnLugar());
                setSendEstadoAsignacion();
            }else if(auxilio.getIdEstado() == new ApiConstants.EnLugar().getValue() && distancia > MIN_DISTANCE_CHANGE_TO_ENTRASLADO){
                //Change to en traslado
                DBWrapper.updateEstadoAuxilio(mContext, new ApiConstants.EnTraslado());
                setSendEstadoAsignacion();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}

    private void setSendEstadoAsignacion() {
        mPreferencesHelper.setSendEstadoAsignacion(true);
        Utils.restarLocationsServices(mContext);
        Utils.syncNow(mContext);
    }
}
