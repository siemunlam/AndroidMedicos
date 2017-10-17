package com.siem.siemmedicos.db;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.siem.siemmedicos.model.app.AppLocation;
import com.siem.siemmedicos.model.app.Auxilio;
import com.siem.siemmedicos.model.app.Motivo;
import com.siem.siemmedicos.model.app.Motivos;
import com.siem.siemmedicos.utils.ApiConstants;
import com.siem.siemmedicos.utils.Constants;
import com.siem.siemmedicos.utils.Utils;

import java.util.Date;

/**
 * Created by Lucas on 21/8/17.
 */

public class DBWrapper {

    public static void cleanAllDB(Context context){
        cleanLocations(context);
        cleanAuxilio(context);
    }

    /**
     * Locations
     */
    public static void cleanLocations(Context context) {
        context.getContentResolver().delete(
                DBContract.Locations.CONTENT_URI,
                null,
                null
        );
    }

    public static void deleteLocation(Context context, int id){
        context.getContentResolver().delete(
                DBContract.Locations.CONTENT_URI,
                DBContract.Locations._ID + " = ? ",
                new String[]{ String.valueOf(id) }
        );
    }

    public static AppLocation getLastLocation(Context context){
        Cursor cursor = context.getContentResolver().query(
                DBContract.Locations.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        AppLocation location = null;
        if(cursor != null){
            if(cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndex(DBContract.Locations._ID));
                String latitude = cursor.getString(cursor.getColumnIndex(DBContract.Locations.COLUMN_NAME_LATITUDE));
                String longitude = cursor.getString(cursor.getColumnIndex(DBContract.Locations.COLUMN_NAME_LONGITUDE));
                String bearing = cursor.getString(cursor.getColumnIndex(DBContract.Locations.COLUMN_NAME_BEARING));
                String accuracy = cursor.getString(cursor.getColumnIndex(DBContract.Locations.COLUMN_NAME_ACCURACY));
                String time = cursor.getString(cursor.getColumnIndex(DBContract.Locations.COLUMN_NAME_TIMESTAMP_LOC));
                String speed = cursor.getString(cursor.getColumnIndex(DBContract.Locations.COLUMN_NAME_SPEED));
                String provider = cursor.getString(cursor.getColumnIndex(DBContract.Locations.COLUMN_NAME_PROVIDER));
                LatLng latLng = new LatLng(
                        Double.parseDouble(latitude),
                        Double.parseDouble(longitude)
                );

                location = new AppLocation(latLng, Float.parseFloat(bearing));
                location.setId(id);
                location.setAccuracy(Float.parseFloat(accuracy));
                location.setTime(Long.parseLong(time));
                location.setSpeed(Float.parseFloat(speed));
                location.setProvider(provider);
            }
            cursor.close();
        }

        return location;
    }

    public static void saveLocation(Context context, AppLocation location){
        AppLocation lastLocation = getLastLocation(context);
        float bearing = (lastLocation != null ? Utils.getBearing(lastLocation.getLatitude(), lastLocation.getLongitude(), location.getLatitude(), location.getLongitude()) : location.getBearing());
        Date now = new Date();
        ContentValues values = new ContentValues();
        values.put(DBContract.Locations.COLUMN_NAME_LATITUDE, String.valueOf(location.getLatitude()));
        values.put(DBContract.Locations.COLUMN_NAME_LONGITUDE, String.valueOf(location.getLongitude()));
        values.put(DBContract.Locations.COLUMN_NAME_ACCURACY, String.valueOf(location.getAccuracy()));
        values.put(DBContract.Locations.COLUMN_NAME_TIMESTAMP_SAVE, String.valueOf(now.getTime()));
        values.put(DBContract.Locations.COLUMN_NAME_TIME_SAVE, Constants.DATE_COMPLET_FORMAT.format(now));
        values.put(DBContract.Locations.COLUMN_NAME_TIMESTAMP_LOC, String.valueOf(location.getTime()));
        values.put(DBContract.Locations.COLUMN_NAME_TIME_LOC, Constants.DATE_COMPLET_FORMAT.format(new Date(location.getTime())));
        values.put(DBContract.Locations.COLUMN_NAME_SPEED, String.valueOf(location.getSpeed()));
        values.put(DBContract.Locations.COLUMN_NAME_PROVIDER, location.getProvider());
        values.put(DBContract.Locations.COLUMN_NAME_BEARING, String.valueOf(bearing));
        cleanLocations(context);
        context.getContentResolver().insert(DBContract.Locations.CONTENT_URI, values);
    }

    /**
     * Auxilio
     */
    public static void cleanAuxilio(Context context) {
        context.getContentResolver().delete(
                DBContract.InformacionAuxilio.CONTENT_URI,
                null,
                null
        );

        cleanMotivosAuxilio(context);
    }

    private static void cleanMotivosAuxilio(Context context) {
        context.getContentResolver().delete(
                DBContract.MotivoAuxilio.CONTENT_URI,
                null,
                null
        );
    }

    public static void saveAuxilio(Context context, Auxilio auxilio){
        cleanAuxilio(context);
        ContentValues cvInfo = new ContentValues();
        cvInfo.put(DBContract.InformacionAuxilio.COLUMN_NAME_LATITUDE, auxilio.getLatitude());
        cvInfo.put(DBContract.InformacionAuxilio.COLUMN_NAME_LONGITUDE, auxilio.getLongitude());
        cvInfo.put(DBContract.InformacionAuxilio.COLUMN_NAME_DIRECCION, auxilio.getDireccion());
        cvInfo.put(DBContract.InformacionAuxilio.COLUMN_NAME_COLOR_DESCRIPCION, auxilio.getColorDescripcion());
        cvInfo.put(DBContract.InformacionAuxilio.COLUMN_NAME_COLOR_HEXA, auxilio.getColorHexadecimal());
        cvInfo.put(DBContract.InformacionAuxilio.COLUMN_NAME_NOMBRE_PACIENTE, auxilio.getNombrePaciente());
        cvInfo.put(DBContract.InformacionAuxilio.COLUMN_NAME_SEXO_PACIENTE, auxilio.getSexo());
        cvInfo.put(DBContract.InformacionAuxilio.COLUMN_NAME_OBSERVACIONES, auxilio.getObservaciones());
        cvInfo.put(DBContract.InformacionAuxilio.COLUMN_NAME_REFERENCIA, auxilio.getReferencia());
        cvInfo.put(DBContract.InformacionAuxilio.COLUMN_NAME_ID_ESTADO, new ApiConstants.EnCamino().getValue());
        Uri uri = context.getContentResolver().insert(
                DBContract.InformacionAuxilio.CONTENT_URI,
                cvInfo
        );

        saveMotivos(context, auxilio, ContentUris.parseId(uri));
    }

    public static void updateEstadoAuxilio(Context context, ApiConstants.Item estado){
        ContentValues cvInfo = new ContentValues();
        cvInfo.put(DBContract.InformacionAuxilio.COLUMN_NAME_ID_ESTADO, estado.getValue());
        context.getContentResolver().update(
                DBContract.InformacionAuxilio.CONTENT_URI,
                cvInfo,
                null,
                null
        );
    }

    private static void saveMotivos(Context context, Auxilio auxilio, long informacionAuxilioId) {
        if(auxilio.getMotivos() == null || auxilio.getMotivos().getListMotivos() == null)
            return;
        for (Motivo motivo : auxilio.getMotivos().getListMotivos()) {
            ContentValues cvMotivos = new ContentValues();
            cvMotivos.put(DBContract.MotivoAuxilio.COLUMN_NAME_ID_AUXILIO, informacionAuxilioId);
            cvMotivos.put(DBContract.MotivoAuxilio.COLUMN_NAME_MOTIVO, motivo.getMotivo());
            context.getContentResolver().insert(
                    DBContract.MotivoAuxilio.CONTENT_URI,
                    cvMotivos
            );
        }
    }

    public static Auxilio getAuxilio(Context context){
        Cursor cursor = context.getContentResolver().query(
                DBContract.InformacionAuxilio.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        Auxilio auxilio = new Auxilio();
        if(cursor != null){
            if(cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndex(DBContract.InformacionAuxilio._ID));
                String latitude = cursor.getString(cursor.getColumnIndex(DBContract.InformacionAuxilio.COLUMN_NAME_LATITUDE));
                String longitude = cursor.getString(cursor.getColumnIndex(DBContract.InformacionAuxilio.COLUMN_NAME_LONGITUDE));
                String direccion = cursor.getString(cursor.getColumnIndex(DBContract.InformacionAuxilio.COLUMN_NAME_DIRECCION));
                String colorDescripcion = cursor.getString(cursor.getColumnIndex(DBContract.InformacionAuxilio.COLUMN_NAME_COLOR_DESCRIPCION));
                String colorHexadecimal = cursor.getString(cursor.getColumnIndex(DBContract.InformacionAuxilio.COLUMN_NAME_COLOR_HEXA));
                String nombrePaciente = cursor.getString(cursor.getColumnIndex(DBContract.InformacionAuxilio.COLUMN_NAME_NOMBRE_PACIENTE));
                String sexoPaciente = cursor.getString(cursor.getColumnIndex(DBContract.InformacionAuxilio.COLUMN_NAME_SEXO_PACIENTE));
                String observaciones = cursor.getString(cursor.getColumnIndex(DBContract.InformacionAuxilio.COLUMN_NAME_OBSERVACIONES));
                String referencia = cursor.getString(cursor.getColumnIndex(DBContract.InformacionAuxilio.COLUMN_NAME_REFERENCIA));
                int idEstado = cursor.getInt(cursor.getColumnIndex(DBContract.InformacionAuxilio.COLUMN_NAME_ID_ESTADO));
                Motivos motivos = getMotivos(context, id);

                auxilio.setLatitude(latitude);
                auxilio.setLongitude(longitude);
                auxilio.setDireccion(direccion);
                auxilio.setColorDescripcion(colorDescripcion);
                auxilio.setColorHexadecimal(colorHexadecimal);
                auxilio.setNombrePaciente(nombrePaciente);
                auxilio.setSexo(sexoPaciente);
                auxilio.setObservaciones(observaciones);
                auxilio.setReferencia(referencia);
                auxilio.setMotivos(motivos);
                auxilio.setIdEstado(idEstado);
            }
            cursor.close();
        }

        return auxilio;
    }

    public static Motivos getMotivos(Context context, long informacionAuxilioId){
        Cursor cursor = context.getContentResolver().query(
                DBContract.MotivoAuxilio.CONTENT_URI,
                null,
                DBContract.MotivoAuxilio.COLUMN_NAME_ID_AUXILIO + " = ? ",
                new String[]{ String.valueOf(informacionAuxilioId) },
                null
        );

        Motivos motivos = new Motivos();
        if(cursor != null){
            while(cursor.moveToNext()){
                Motivo motivo = new Motivo();
                String descripcionMotivo = cursor.getString(cursor.getColumnIndex(DBContract.MotivoAuxilio.COLUMN_NAME_MOTIVO));

                motivo.setMotivo(descripcionMotivo);
                motivos.addMotivo(motivo);
            }
            cursor.close();
        }

        return motivos;
    }

}
