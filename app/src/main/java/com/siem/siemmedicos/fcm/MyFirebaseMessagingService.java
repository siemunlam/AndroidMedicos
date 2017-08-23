package com.siem.siemmedicos.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.siem.siemmedicos.R;
import com.siem.siemmedicos.db.DBWrapper;
import com.siem.siemmedicos.model.app.Auxilio;
import com.siem.siemmedicos.ui.LoginActivity;
import com.siem.siemmedicos.utils.Constants;
import com.siem.siemmedicos.utils.PreferencesHelper;
import com.siem.siemmedicos.utils.Utils;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String KEY_LATITUDE = "Lat";
    private static final String KEY_LONGITUDE = "Lng";
    private static final String KEY_DIRECCION = "Direccion";
    private static final String KEY_PACIENTE = "Paciente";
    private static final String KEY_COLOR_DESCRIPCION = "ColorDescripcion";
    private static final String KEY_COLOR_HEXA = "ColorHexa";
    private static final String KEY_MOTIVOS = "Motivos";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i("123456789", "Llego notificacion");
        PreferencesHelper preferences = PreferencesHelper.getInstance();
        if (remoteMessage.getData().size() > 0 && preferences.getValueEstado() == Constants.Disponible.getValue()) {
            Auxilio auxilio = getAuxilio(remoteMessage.getData());
            DBWrapper.saveAuxilio(this, auxilio);
            preferences.setValueEstado(Constants.EnAuxilio.getValue());
            preferences.setDescriptionEstado(Constants.EnAuxilio.getDescription(this));
            Utils.restarLocationsServices(this);
            sendNotification("Que texto va??");
            sendBroadcast();
        }
    }

    private Auxilio getAuxilio(Map<String, String> data) {
        Auxilio auxilio = new Auxilio();
        auxilio.setLatitude(data.get(KEY_LATITUDE));
        auxilio.setLongitude(data.get(KEY_LONGITUDE));
        auxilio.setDireccion(data.get(KEY_DIRECCION));
        auxilio.setNombrePaciente(data.get(KEY_PACIENTE));
        auxilio.setColorDescripcion(data.get(KEY_COLOR_DESCRIPCION));
        auxilio.setColorHexadecimal(data.get(KEY_COLOR_HEXA));
        auxilio.setMotivos(data.get(KEY_MOTIVOS));

        return auxilio;
    }

    private void sendBroadcast(){
        Intent intent = new Intent(Constants.BROADCAST_NEW_AUXILIO);
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
