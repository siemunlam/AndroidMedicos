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
    private Integer mDni;

    @SerializedName("edad")
    private Integer mEdad;

    @SerializedName("diagnostico")
    private String mDiagnostico;

    @SerializedName("trasladado")
    private boolean mTrasladado;

    public Paciente(){
        mTrasladado = false;
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

    public Integer getDni() {
        return mDni;
    }

    public void setDni(Integer dni) {
        mDni = dni;
    }

    public Integer getEdad() {
        return mEdad;
    }

    public void setEdad(Integer edad) {
        mEdad = edad;
    }

    public boolean isTrasladado() {
        return mTrasladado;
    }

    public void setTrasladado(boolean trasladado) {
        mTrasladado = trasladado;
    }

    public String getDiagnostico() {
        return mDiagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        mDiagnostico = diagnostico;
    }
}
