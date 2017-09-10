package com.siem.siemmedicos.model.app;

/**
 * Created by Lucas on 10/9/17.
 */

public class Paciente {

    private String mNombre;
    private String mApellido;
    private String mDni;
    private String mFechaNacimiento;
    private String mObservaciones;

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

    public String getFechaNacimiento() {
        return mFechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        mFechaNacimiento = fechaNacimiento;
    }

    public String getObservaciones() {
        return mObservaciones;
    }

    public void setObservaciones(String observaciones) {
        mObservaciones = observaciones;
    }
}
