package com.siem.siemmedicos.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.siem.siemmedicos.model.app.Auxilio;
import com.siem.siemmedicos.model.app.Motivo;
import com.siem.siemmedicos.model.app.Motivos;

/**
 * Created by Lucas on 21/8/17.
 */

public class DBWrapper {

    public static void cleanAllDB(Context context){
        cleanLocations(context);
        cleanAuxilio(context);
    }

    private static void cleanLocations(Context context) {
        context.getContentResolver().delete(
                DBContract.Locations.CONTENT_URI,
                null,
                null
        );
    }

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
        ContentValues cvInfo = new ContentValues();
        cvInfo.put(DBContract.InformacionAuxilio.COLUMN_NAME_LATITUDE, auxilio.getLatitude());
        cvInfo.put(DBContract.InformacionAuxilio.COLUMN_NAME_LONGITUDE, auxilio.getLongitude());
        cvInfo.put(DBContract.InformacionAuxilio.COLUMN_NAME_DIRECCION, auxilio.getDireccion());
        cvInfo.put(DBContract.InformacionAuxilio.COLUMN_NAME_COLOR_DESCRIPCION, auxilio.getColorDescripcion());
        cvInfo.put(DBContract.InformacionAuxilio.COLUMN_NAME_COLOR_HEXA, auxilio.getColorHexadecimal());
        cvInfo.put(DBContract.InformacionAuxilio.COLUMN_NAME_NOMBRE_PACIENTE, auxilio.getNombrePaciente());
        Uri uri = context.getContentResolver().insert(
                DBContract.InformacionAuxilio.CONTENT_URI,
                cvInfo
        );

        //TODO: Error.... saveMotivos(context, auxilio, ContentUris.parseId(uri));
    }

    private static void saveMotivos(Context context, Auxilio auxilio, long informacionAuxilioId) {
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
                Motivos motivos = getMotivos(context, id);

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
