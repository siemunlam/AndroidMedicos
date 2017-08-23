package com.siem.siemmedicos.model.app;

/**
 * Created by Lucas on 22/8/17.
 */

public class Auxilio {

    private String mLatitude;
    private String mLongitude;
    private String mDireccion;
    private String mColorDescripcion;
    private String mColorHexadecimal;
    private String mNombrePaciente;
    private String mMotivos;

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public String getDireccion() {
        return mDireccion;
    }

    public void setDireccion(String MDireccion) {
        this.mDireccion = MDireccion;
    }

    public String getColorDescripcion() {
        return mColorDescripcion;
    }

    public void setColorDescripcion(String colorDescripcion) {
        mColorDescripcion = colorDescripcion;
    }

    public String getColorHexadecimal() {
        return mColorHexadecimal;
    }

    public void setColorHexadecimal(String colorHexadecimal) {
        mColorHexadecimal = colorHexadecimal;
    }

    public String getNombrePaciente() {
        return mNombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        mNombrePaciente = nombrePaciente;
    }

    public String getMotivos() {
        return mMotivos;
    }

    public void setMotivos(String motivos) {
        mMotivos = motivos;
    }
}