package com.siem.siemmedicos.db;

import android.content.Context;

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

        context.getContentResolver().delete(
                DBContract.InformacionAuxilio.CONTENT_URI,
                null,
                null
        );
    }

}
