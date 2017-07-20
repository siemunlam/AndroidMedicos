package com.siem.siemmedicos.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.siem.siemmedicos.R;

public class Utils {

    /**
     * Sincronizar el SynAdapter ahora
     * @param context Contexto
     */
    public static void syncNow(Context context) {
        Account mAccount = Utils.getAccount(context);
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);

        ContentResolver.requestSync(
                mAccount,
                context.getString(R.string.content_authority),
                bundle
        );
    }

    /**
     * Crea la cuenta necesaria para el SyncAdapter
     * @param context Contexto
     */
    public static void createSyncAccount(Context context) {
        Account newAccount = getAccount(context);
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        try{
            accountManager.addAccountExplicitly(newAccount, null, null);
        }catch(Exception e){
            Log.i("123456789", "ERROR");
        }
    }

    /**
     * Setea los valores para el content Resolver y agenda una alarma para correr el SyncAdapter
     * @param context Contexto
     */
    public static void setupContentResolver(Context context) {
        /**
         * Content Resolver setup
         */
        Account mAccount = getAccount(context);

        ContentResolver.setIsSyncable(
                mAccount,
                context.getString(R.string.content_authority),
                1);

        ContentResolver.setSyncAutomatically(
                mAccount,
                context.getString(R.string.content_authority),
                false);

        scheduleDataSync(context);
    }

    /**
     * Alarma para correr el SyncAdapter
     * @param context Contexto
     */
    private static void scheduleDataSync(Context context){
        Intent intent = new Intent(context.getString(R.string.broadcast_sync_data));
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        am.cancel(sender);
        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), Constants.SYNC_INTERVAL, sender);
    }

    /**
     * Retorna un Account
     * @param context Contexto
     * @return Account
     */
    public static Account getAccount(Context context){
        return new Account(
                Constants.DEMO_ACCOUNT_NAME,
                context.getString(R.string.account_type));
    }
}
