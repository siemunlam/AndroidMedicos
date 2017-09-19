package com.siem.siemmedicos.model.app;

import java.util.List;

/**
 * Created by Lucas on 18/9/17.
 */

public class FinalizarAuxilio {

    private boolean mAsistenciaRealizada;

    /**
     * Asistencia realizada = FALSE
     */
    private int mMotivoInasistencia;
    private String mObservaciones;

    /**
     * Asistencia realizada = TRUE
     */
    private int mCategorizacion;
    private List<Paciente> mListPaciente;


    public boolean isAsistenciaRealizada() {
        return mAsistenciaRealizada;
    }

    public void setAsistenciaRealizada(boolean asistenciaRealizada) {
        mAsistenciaRealizada = asistenciaRealizada;
    }

    public int getMotivoInasistencia() {
        return mMotivoInasistencia;
    }

    public void setMotivoInasistencia(int motivoInasistencia) {
        mMotivoInasistencia = motivoInasistencia;
    }

    public String getObservaciones() {
        return mObservaciones;
    }

    public void setObservaciones(String observaciones) {
        mObservaciones = observaciones;
    }

    public int getCategorizacion() {
        return mCategorizacion;
    }

    public void setCategorizacion(int categorizacion) {
        mCategorizacion = categorizacion;
    }

    public List<Paciente> getListPaciente() {
        return mListPaciente;
    }

    public void setListPaciente(List<Paciente> listPaciente) {
        mListPaciente = listPaciente;
    }
}
