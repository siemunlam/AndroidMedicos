package com.siem.siemmedicos.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.siem.siemmedicos.model.app.Auxilio;

/**
 * Created by Lucas on 21/8/17.
 */

public class DBWrapper {

    public static void cleanAllDB(Context context){
        context.getContentResolver().delete(
                DBContract.Locations.CONTENT_URI,
                null,
                null
        );

        cleanAuxilio(context);
    }

    public static void cleanAuxilio(Context context) {
        context.getContentResolver().delete(
                DBContract.InformacionAuxilio.CONTENT_URI,
                null,
                null
        );
    }

    public static void saveAuxilio(Context context, Auxilio auxilio){
        ContentValues cv = new ContentValues();
        cv.put(DBContract.InformacionAuxilio.COLUMN_NAME_LATITUDE, auxilio.getLatitude());
        cv.put(DBContract.InformacionAuxilio.COLUMN_NAME_LONGITUDE, auxilio.getLongitude());
        cv.put(DBContract.InformacionAuxilio.COLUMN_NAME_DIRECCION, auxilio.getDireccion());
        cv.put(DBContract.InformacionAuxilio.COLUMN_NAME_COLOR_DESCRIPCION, auxilio.getColorDescripcion());
        cv.put(DBContract.InformacionAuxilio.COLUMN_NAME_COLOR_HEXA, auxilio.getColorHexadecimal());
        cv.put(DBContract.InformacionAuxilio.COLUMN_NAME_NOMBRE_PACIENTE, auxilio.getNombrePaciente());
        cv.put(DBContract.InformacionAuxilio.COLUMN_NAME_MOTIVOS, auxilio.getMotivos());
        context.getContentResolver().insert(
                DBContract.InformacionAuxilio.CONTENT_URI,
                cv
        );
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
                String latitude = cursor.getString(cursor.getColumnIndex(DBContract.InformacionAuxilio.COLUMN_NAME_LATITUDE));
                String longitude = cursor.getString(cursor.getColumnIndex(DBContract.InformacionAuxilio.COLUMN_NAME_LONGITUDE));
                String direccion = cursor.getString(cursor.getColumnIndex(DBContract.InformacionAuxilio.COLUMN_NAME_DIRECCION));
                String colorDescripcion = cursor.getString(cursor.getColumnIndex(DBContract.InformacionAuxilio.COLUMN_NAME_COLOR_DESCRIPCION));
                String colorHexadecimal = cursor.getString(cursor.getColumnIndex(DBContract.InformacionAuxilio.COLUMN_NAME_COLOR_HEXA));
                String nombrePaciente = cursor.getString(cursor.getColumnIndex(DBContract.InformacionAuxilio.COLUMN_NAME_NOMBRE_PACIENTE));
                String motivos = cursor.getString(cursor.getColumnIndex(DBContract.InformacionAuxilio.COLUMN_NAME_MOTIVOS));

                auxilio.setLatitude(latitude);
                auxilio.setLongitude(longitude);
                auxilio.setDireccion(direccion);
                auxilio.setColorDescripcion(colorDescripcion);
                auxilio.setColorHexadecimal(colorHexadecimal);
                auxilio.setNombrePaciente(nombrePaciente);
                auxilio.setMotivos(motivos);
            }
            cursor.close();
        }

        return auxilio;
    }

}
