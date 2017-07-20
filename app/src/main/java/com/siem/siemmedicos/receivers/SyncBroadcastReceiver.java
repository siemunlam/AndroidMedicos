package com.siem.siemmedicos.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.siem.siemmedicos.R;
import com.siem.siemmedicos.utils.Utils;

/**
 * Created by diegoanfuso on 24/08/16.
 */
public class SyncBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(context.getString(R.string.broadcast_sync_data))) {
            Utils.syncNow(context);
        }
    }
}
