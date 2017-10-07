package com.siem.siemmedicos.model.app;


/**
 * Created by lucas on 8/25/17.
 */

public class Motivo {

    private static final String FORMAT_MOTIVO = "- %1$s %2$s";

    private String mMotivo;

    public Motivo(){

    }

    public Motivo(String key, String value){
        mMotivo = String.format(FORMAT_MOTIVO, key, value);
    }

    public void setMotivo(String motivo) {
        this.mMotivo = motivo;
    }

    public String getMotivo() {
        return mMotivo;
    }

    @Override
    public String toString(){
        return getMotivo();
    }
}
