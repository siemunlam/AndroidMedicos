package com.siem.siemmedicos.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Locale;

public class DBContentProvider extends ContentProvider {

    public static final int LOCATIONS = 1;
    public static final int LOCATION = 2;
    public static final int INFORMACION_AUXILIO = 3;

    private DataBaseHandler dbHandler;
    private SQLiteDatabase db;
    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String cAuthority = DBContract.CONTENT_AUTHORITY;

    static {
        sUriMatcher.addURI(cAuthority, DBContract.LOCATIONS, LOCATIONS);
        sUriMatcher.addURI(cAuthority, DBContract.LOCATION, LOCATION);
        sUriMatcher.addURI(cAuthority, DBContract.INFORMACION_AUXILIO, INFORMACION_AUXILIO);
    }

    public DBContentProvider() {
    }

    @Override
    public boolean onCreate() {
        dbHandler = DataBaseHandler.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case LOCATIONS:
                return DBContract.MIME_DIR + "/" + DBContract.LOCATIONS;

            case LOCATION:
                return DBContract.MIME_ITEM + "/" + DBContract.LOCATIONS;

            case INFORMACION_AUXILIO:
                return DBContract.MIME_DIR + "/" + DBContract.INFORMACION_AUXILIO;

            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = sUriMatcher.match(uri);
        db = dbHandler.getWritableDatabase();

        switch (match) {
            case LOCATIONS:
                return db.query(
                        DBContract.Locations.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

            case INFORMACION_AUXILIO:
                return db.query(
                        DBContract.InformacionAuxilio.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        int match = sUriMatcher.match(uri);
        db = dbHandler.getWritableDatabase();

        switch (match) {
            case LOCATIONS:
                db.insert(
                        DBContract.Locations.TABLE_NAME,
                        null,
                        contentValues
                );
                notifyChange(DBContract.Locations.CONTENT_URI, null);
                return uri;

            case INFORMACION_AUXILIO:
                db.insert(
                        DBContract.InformacionAuxilio.TABLE_NAME,
                        null,
                        contentValues
                );
                return uri;

            default:
                return uri;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        db = dbHandler.getWritableDatabase();

        switch (match){
            case LOCATIONS:
                return db.delete(
                        DBContract.Locations.TABLE_NAME,
                        selection,
                        selectionArgs
                );

            case LOCATION:
                return db.delete(
                        DBContract.Locations.TABLE_NAME,
                        String.format(Locale.US, "%s = ?", DBContract.Locations._ID),
                        new String[]{Long.toString(ContentUris.parseId(uri))}
                );

            case INFORMACION_AUXILIO:
                return db.delete(
                        DBContract.InformacionAuxilio.TABLE_NAME,
                        selection,
                        selectionArgs
                );

            default:
                return 0;
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        db = dbHandler.getWritableDatabase();

        switch (match) {
            case LOCATIONS:
                return db.update(
                        DBContract.Locations.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );

            case INFORMACION_AUXILIO:
                return db.update(
                        DBContract.InformacionAuxilio.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );

            default:
                return 0;
        }
    }

    private void notifyChange(@NonNull Uri uri, @Nullable ContentObserver observer) {
        if(getContext() != null)
            getContext().getContentResolver().notifyChange(uri, observer);
    }

}
