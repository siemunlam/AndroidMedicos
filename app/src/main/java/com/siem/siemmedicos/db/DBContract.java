package com.siem.siemmedicos.db;

import android.net.Uri;
import android.provider.BaseColumns;

import com.siem.siemmedicos.BuildConfig;

public class DBContract {

    /**
     * Database constants
     */
    public static final String COMMA_SEP = ",";
    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String PRIMARY_KEY = " PRIMARY KEY";
    public static final String AUTOINCREMENT = " AUTOINCREMENT";
    public static final String NOT_NULL = " NOT NULL";
    public static final String DEFAULT = " DEFAULT";
    public static final String NULL = " NULL";

    /**
     * Content constants
     */
    public static final String CONTENT_AUTHORITY = BuildConfig.APPLICATION_ID + ".provider";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Path constants
     */
    public static final String LOCATIONS = "locations";
    public static final String LOCATION = LOCATIONS + "/#";
    public static final String INFORMACION_AUXILIO = "informacion_auxilio";
    public static final String MOTIVO_AUXILIO = "motivo_auxilio";

    /**
     * Tipos MIME
     */
    public static final String MIME_DIR = "vnd.android.cursor.dir";
    public static final String MIME_ITEM = "vnd.android.cursor.item";

    public DBContract() {
    }

    public static abstract class Locations implements BaseColumns {
        public static final String TABLE_NAME = "locations";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendEncodedPath(LOCATIONS).build();

        public static final Uri ITEM_URI = BASE_CONTENT_URI.buildUpon()
                .appendEncodedPath(LOCATION).build();

        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_ACCURACY = "accuracy";
        public static final String COLUMN_NAME_TIMESTAMP_SAVE = "timestamp_save";
        public static final String COLUMN_NAME_TIME_SAVE = "time_save";
        public static final String COLUMN_NAME_TIMESTAMP_LOC = "timestamp_loc";
        public static final String COLUMN_NAME_TIME_LOC = "time_loc";
        public static final String COLUMN_NAME_SPEED = "speed";
        public static final String COLUMN_NAME_PROVIDER = "provider";
        public static final String COLUMN_NAME_BEARING = "bearing";
    }

    public static abstract class InformacionAuxilio implements BaseColumns {
        public static final String TABLE_NAME = "informacion_auxilio";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendEncodedPath(INFORMACION_AUXILIO).build();

        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_DIRECCION = "direccion";
        public static final String COLUMN_NAME_COLOR_DESCRIPCION = "color_descripcion";
        public static final String COLUMN_NAME_COLOR_HEXA = "color_hexa";
        public static final String COLUMN_NAME_NOMBRE_PACIENTE = "nombre_paciente";
        public static final String COLUMN_NAME_SEXO_PACIENTE = "sexo_paciente";
        public static final String COLUMN_NAME_OBSERVACIONES = "observaciones";
        public static final String COLUMN_NAME_REFERENCIA = "referencia";
        public static final String COLUMN_NAME_CONTACTO = "contacto";
        public static final String COLUMN_NAME_ID_ESTADO = "id_estado";
    }

    public static abstract class MotivoAuxilio implements BaseColumns {
        public static final String TABLE_NAME = "motivo_auxilio";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendEncodedPath(MOTIVO_AUXILIO).build();

        public static final String COLUMN_NAME_ID_AUXILIO = "id_auxilio";
        public static final String COLUMN_NAME_MOTIVO = "motivo";
    }
}
