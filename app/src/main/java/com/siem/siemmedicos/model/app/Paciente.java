package com.siem.siemmedicos.model.app;

/**
 * Created by Lucas on 10/9/17.
 */

public class Paciente {

    private String mNombre;
    private String mApellido;
    private String mDni;
    private String mEdad;
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
