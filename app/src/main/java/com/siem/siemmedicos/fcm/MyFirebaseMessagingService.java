package com.siem.siemmedicos.fcm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.siem.siemmedicos.R;
import com.siem.siemmedicos.db.DBWrapper;
import com.siem.siemmedicos.model.app.Auxilio;
import com.siem.siemmedicos.model.app.Motivo;
import com.siem.siemmedicos.model.app.Motivos;
import com.siem.siemmedicos.services.UpdateEstadoService;
import com.siem.siemmedicos.ui.activity.LoginActivity;
import com.siem.siemmedicos.utils.ApiConstants;
import com.siem.siemmedicos.utils.Constants;
import com.siem.siemmedicos.utils.PreferencesHelper;
import com.siem.siemmedicos.utils.Utils;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

import static com.siem.siemmedicos.utils.Constants.KEY_CODE;
import static com.siem.siemmedicos.utils.Constants.KEY_COLOR_DESCRIPCION;
import static com.siem.siemmedicos.utils.Constants.KEY_COLOR_HEXA;
import static com.siem.siemmedicos.utils.Constants.KEY_DIRECCION;
import static com.siem.siemmedicos.utils.Constants.KEY_LATITUDE;
import static com.siem.siemmedicos.utils.Constants.KEY_LONGITUDE;
import static com.siem.siemmedicos.utils.Constants.KEY_MOTIVOS;
import static com.siem.siemmedicos.utils.Constants.KEY_PACIENTE;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final long TIME_UPDATE_ESTADO = 3000;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        PreferencesHelper preferencesHelper = PreferencesHelper.getInstance();
        //Log.i("123456789", "ACA 1: " + remoteMessage.getData());
        //Log.i("123456789", "ACA 2: " + remoteMessage.getData().size() + " - " + preferencesHelper.getValueEstado() + " - " + preferencesHelper.getDescriptionEstado(this));
        if (remoteMessage.getData().size() > 0 && preferencesHelper.getValueEstado() == new ApiConstants.Disponible().getValue()) {
            Auxilio auxilio = getAuxilio(remoteMessage.getData());
            if(auxilio != null){
                DBWrapper.saveAuxilio(this, auxilio);
                Utils.updateEstado(this, new ApiConstants.EnAuxilio());
                sendNotification(getString(R.string.descripcionAuxilio, auxilio.getColorDescripcion()));
                sendBroadcast(Constants.BROADCAST_NEW_AUXILIO);
            }
        }else if(remoteMessage.getData().size() > 0 && preferencesHelper.getValueEstado() == new ApiConstants.EnAuxilio().getValue()){
            try{
                Map<String, String> data = remoteMessage.getData();
                int code = Integer.parseInt(data.get(KEY_CODE));
                if(code == Constants.CODE_CANCEL_AUXILIO){
                    sendBroadcast(Constants.BROADCAST_CANCEL_AUXILIO);
                    //prepareAlarmUpdateEstado();
                }
            }catch(Exception e){}
        }
    }

    private void prepareAlarmUpdateEstado() {
        Intent intent = new Intent(MyFirebaseMessagingService.this, UpdateEstadoService.class);
        PendingIntent pendingIntent = PendingIntent.getService(MyFirebaseMessagingService.this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + TIME_UPDATE_ESTADO, pendingIntent);
    }

    private Auxilio getAuxilio(Map<String, String> data) {
        Auxilio auxilio = new Auxilio();
        auxilio.setLatitude(data.get(KEY_LATITUDE));
        auxilio.setLongitude(data.get(KEY_LONGITUDE));
        auxilio.setDireccion(data.get(KEY_DIRECCION));
        auxilio.setNombrePaciente(data.get(KEY_PACIENTE));
        auxilio.setColorDescripcion(data.get(KEY_COLOR_DESCRIPCION));
        auxilio.setColorHexadecimal(data.get(KEY_COLOR_HEXA));

        try{
            JSONObject jsonObject = new JSONObject(data.get(KEY_MOTIVOS));
            Motivos motivos = new Motivos();
            Iterator<?> permisos = jsonObject.keys();
            while(permisos.hasNext() ){
                String key = (String)permisos.next();
                String value = jsonObject.getString(key);
                motivos.addMotivo(new Motivo(key, value));
            }
            auxilio.setMotivos(motivos);
        }catch(Exception e){
            //TODO: Exception
        }

        return auxilio;
    }

    private void sendBroadcast(String broadcast){
        Intent intent = new Intent(broadcast);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        long[] vibrate = {1000, 1000, 1000, 1000};
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_notification);
        mBuilder.setContentTitle(getString(R.string.nuevoAuxilio));
        mBuilder.setContentText(messageBody);
        mBuilder.setAutoCancel(true);
        mBuilder.setSound(defaultSoundUri);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setVibrate(vibrate);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mBuilder.build());
    }

}
