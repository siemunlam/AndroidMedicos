package com.siem.siemmedicos.model.app;

import android.text.TextUtils;

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
    private String mSexo;
    private String mObservaciones;
    private String mReferencia;
    private String mContacto;
    private Motivos mMotivos;
    private int mIdEstado;

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

    public String getSexo() {
        return mSexo;
    }

    public void setSexo(String sexo) {
        mSexo = sexo;
    }

    public String getObservaciones() {
        return mObservaciones;
    }

    public void setObservaciones(String observaciones) {
        mObservaciones = observaciones;
    }

    public String getReferencia() {
        return mReferencia;
    }

    public void setReferencia(String referencia) {
        mReferencia = referencia;
    }

    public String getContacto() {
        return mContacto;
    }

    public void setContacto(String contacto) {
        mContacto = contacto;
    }

    public Motivos getMotivos() {
        return mMotivos;
    }

    public void setMotivos(Motivos motivos) {
        mMotivos = motivos;
    }

    public int getIdEstado() {
        return mIdEstado;
    }

    public void setIdEstado(int idEstado) {
        mIdEstado =idEstado;
    }

    public String getParsedMotivos(){
        if(getMotivos().getListMotivos() != null)
            return TextUtils.join("\n", getMotivos().getListMotivos());
        else
            return "";
    }
}
