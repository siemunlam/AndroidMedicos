package com.siem.siemmedicos.model.app;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lucas on 18/9/17.
 */

public class FinalizarAuxilio {

    @SerializedName("asistencia_realizada")
    private boolean mAsistenciaRealizada;

    /**
     * Asistencia realizada = FALSE
     */
    @SerializedName("motivo_inasistencia")
    private Integer mMotivoInasistencia;

    @SerializedName("observaciones")
    private String mObservaciones;

    /**
     * Asistencia realizada = TRUE
     */
    @SerializedName("categorizacion")
    private Integer mCategorizacion;

    @SerializedName("pacientes")
    private List<Paciente> mListPaciente;


    public boolean isAsistenciaRealizada() {
        return mAsistenciaRealizada;
    }

    public void setAsistenciaRealizada(boolean asistenciaRealizada) {
        mAsistenciaRealizada = asistenciaRealizada;
    }

    public Integer getMotivoInasistencia() {
        return mMotivoInasistencia;
    }

    public void setMotivoInasistencia(Integer motivoInasistencia) {
        mMotivoInasistencia = motivoInasistencia;
    }

    public String getObservaciones() {
        return mObservaciones;
    }

    public void setObservaciones(String observaciones) {
        mObservaciones = observaciones;
    }

    public Integer getCategorizacion() {
        return mCategorizacion;
    }

    public void setCategorizacion(Integer categorizacion) {
        mCategorizacion = categorizacion;
    }

    public List<Paciente> getListPaciente() {
        return mListPaciente;
    }

    public void setListPaciente(List<Paciente> listPaciente) {
        mListPaciente = listPaciente;
    }
}
