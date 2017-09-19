package com.siem.siemmedicos.utils;

import android.content.Context;

import com.siem.siemmedicos.R;

/**
 * Created by Lucas on 18/9/17.
 */

public class ApiConstants {

    public interface Item{
        int getValue();
        String getDescription(Context context);
    }

    /**
     * Estados
     */
    public static class Disponible implements Item{
        private static int value = 1;

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public String getDescription(Context context) {
            return context.getString(R.string.medicoEstadoDisponible);
        }
    }

    public static class NoDisponible implements Item{
        private static int value = 2;

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public String getDescription(Context context) {
            return context.getString(R.string.medicoEstadoNoDisponible);
        }
    }

    public static class EnAuxilio implements Item{
        public static final int value = 3;

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public String getDescription(Context context) {
            return context.getString(R.string.medicoEstadoEnAuxilio);
        }
    }


    /**
     * Motivo inasistencia
     */
    public static class Otro implements Item{
        private static int value = 0;

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public String getDescription(Context context) {
            return context.getString(R.string.otroMotivo);
        }
    }

    public static class UbicacionIncorrecta implements Item{
        private static int value = 1;

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public String getDescription(Context context) {
            return context.getString(R.string.ubicacionIncorrecta);
        }
    }

    public static class SinRespuesta implements Item{
        private static int value = 2;

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public String getDescription(Context context) {
            return context.getString(R.string.sinRespuesta);
        }
    }

    public static class YaTrasladado implements Item{
        private static int value = 3;

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public String getDescription(Context context) {
            return context.getString(R.string.yaTrasladado);
        }
    }


    /**
     * Categorizacion
     */
    public static class BienCategorizado implements Item{
        private static int value = 0;

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public String getDescription(Context context) {
            return "";
        }
    }

    public static class Subcategorizado implements Item{
        private static int value = 1;

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public String getDescription(Context context) {
            return context.getString(R.string.subcategorizado);
        }
    }

    public static class Supercategorizado implements Item{
        private static int value = 2;

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public String getDescription(Context context) {
            return context.getString(R.string.supercategorizado);
        }
    }

}
