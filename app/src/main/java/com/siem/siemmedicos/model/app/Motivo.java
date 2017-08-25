package com.siem.siemmedicos.model.app;

import com.google.gson.annotations.SerializedName;

import static com.siem.siemmedicos.utils.Constants.KEY_MOTIVO;

/**
 * Created by lucas on 8/25/17.
 */

public class Motivo {

    @SerializedName(KEY_MOTIVO)
    private String mMotivo;

    public void setMotivo(String motivo) {
        this.mMotivo = motivo;
    }

    public String getMotivo() {
        return mMotivo;
    }
}
