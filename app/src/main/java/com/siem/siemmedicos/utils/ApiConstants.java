package com.siem.siemmedicos.utils;

import android.content.Context;

import com.siem.siemmedicos.R;

/**
 * Created by Lucas on 18/9/17.
 */

public class ApiConstants {

    /**
     * Estados
     */
    public interface Estado{
        int getValue();
        String getDescription(Context context);
    }

    public static class Disponible implements Estado{
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

    public static class NoDisponible implements Estado{
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

    public static class EnAuxilio implements Estado{
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
    public interface MotivoInasistencia{
        int getValue();
        String getDescription(Context context);
    }

    public static class Otro implements MotivoInasistencia{
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

    public static class UbicacionIncorrecta implements MotivoInasistencia{
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

    public static class SinRespuesta implements MotivoInasistencia{
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

    public static class YaTrasladado implements MotivoInasistencia{
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

}
