package com.siem.siemmedicos.model.app;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lucas on 10/9/17.
 */

public class Paciente {

    @SerializedName("nombre")
    private String mNombre;

    @SerializedName("apellido")
    private String mApellido;

    @SerializedName("dni")
    private String mDni;

    @SerializedName("edad")
    private String mEdad;

    @SerializedName("diagnostico")
    private String mDiagnostico;

    public Paciente(){
        mNombre = "";
        mApellido = "";
        mDni = "";
        mEdad = "";
        mDiagnostico = "";
    }

    public String getNombre() {
        return mNombre;
    }

    public void setNombre(String nombre) {
        mNombre = nombre;
    }

    public String getApellido() {
        return mApellido;
    }

    public void setApellido(String apellido) {
        mApellido = apellido;
    }

    public String getDni() {
        return mDni;
    }

    public void setDni(String dni) {
        mDni = dni;
    }

    public String getEdad() {
        return mEdad;
    }

    public void setEdad(String edad) {
        mEdad = edad;
    }

    public String getDiagnostico() {
        return mDiagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        mDiagnostico = diagnostico;
    }
}
