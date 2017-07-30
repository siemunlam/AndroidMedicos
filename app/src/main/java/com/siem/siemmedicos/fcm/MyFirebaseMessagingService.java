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
import com.siem.siemmedicos.services.IntensiveLocationService;
import com.siem.siemmedicos.services.LocationService;
import com.siem.siemmedicos.services.SelectLocationService;
import com.siem.siemmedicos.ui.LoginActivity;
import com.siem.siemmedicos.utils.Constants;
import com.siem.siemmedicos.utils.PreferencesHelper;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String KEY_LATITUDE = "Lat";
    private static final String KEY_LONGITUDE = "Lng";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i("123456789", "Llego notificacion");
        PreferencesHelper preferences = PreferencesHelper.getInstance();
        if (remoteMessage.getData().size() > 0 && preferences.getEstado() == Constants.EN_ESPERA) {
            Log.i("123456789", "Paso1");
            preferences.setLatitudeAuxilio(remoteMessage.getData().get(KEY_LATITUDE));
            preferences.setLongitudeAuxilio(remoteMessage.getData().get(KEY_LONGITUDE));
            preferences.setEstado(Constants.EN_AUXILIO);
            stopService(new Intent(this, IntensiveLocationService.class));
            stopService(new Intent(this, LocationService.class));
            startService(new Intent(this, SelectLocationService.class));
            sendNotification("Que texto va??");
            sendBroadcast();
        }
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
