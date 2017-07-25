package com.siem.siemmedicos.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Field;

public class DataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DBNAME = "siemmedicosdb";
    private static DataBaseHandler mInstance = null;

    /**
     * Delete tables
     */
    public static final String SQL_DELETE_LOCATIONS =
            "DROP TABLE IF EXISTS " + DBContract.Locations.TABLE_NAME;

    /**
     * Create tables
     */
    private static final String SQL_CREATE_LOCATIONS =
            "CREATE TABLE " + DBContract.Locations.TABLE_NAME + "("
                    + DBContract.Locations._ID + DBContract.INTEGER_TYPE + DBContract.PRIMARY_KEY + DBContract.AUTOINCREMENT + DBContract.COMMA_SEP
                    + DBContract.Locations.COLUMN_NAME_LATITUDE + DBContract.TEXT_TYPE + DBContract.COMMA_SEP
                    + DBContract.Locations.COLUMN_NAME_LONGITUDE + DBContract.TEXT_TYPE + DBContract.COMMA_SEP
                    + DBContract.Locations.COLUMN_NAME_ACCURACY + DBContract.TEXT_TYPE + DBContract.COMMA_SEP
                    + DBContract.Locations.COLUMN_NAME_TIMESTAMP_SAVE + DBContract.TEXT_TYPE + DBContract.COMMA_SEP
                    + DBContract.Locations.COLUMN_NAME_TIME_SAVE + DBContract.TEXT_TYPE + DBContract.COMMA_SEP
                    + DBContract.Locations.COLUMN_NAME_TIMESTAMP_LOC + DBContract.TEXT_TYPE + DBContract.COMMA_SEP
                    + DBContract.Locations.COLUMN_NAME_TIME_LOC + DBContract.TEXT_TYPE + DBContract.COMMA_SEP
                    + DBContract.Locations.COLUMN_NAME_SPEED + DBContract.TEXT_TYPE + DBContract.COMMA_SEP
                    + DBContract.Locations.COLUMN_NAME_PROVIDER + DBContract.TEXT_TYPE + DBContract.COMMA_SEP
                    + DBContract.Locations.COLUMN_NAME_BEARING + DBContract.TEXT_TYPE + ") ";


    private DataBaseHandler(Context context) {
        super(context, DBNAME, null, DATABASE_VERSION);
    }

    public static DataBaseHandler getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DataBaseHandler(ctx.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            execSQLFields(sqLiteDatabase, "SQL_CREATE");
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        recreateDB(sqLiteDatabase);
    }

    public void recreateDB(SQLiteDatabase sqLiteDatabase){
        try {
            execSQLFields(sqLiteDatabase, "SQL_DELETE");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(sqLiteDatabase);
    }

    private void execSQLFields(SQLiteDatabase db, String fieldsPrefix) {
        for(Field field : getClass().getDeclaredFields()) {
            if (field.getName().contains(fieldsPrefix)) {
                try {
                    db.execSQL((String) field.get(this));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
