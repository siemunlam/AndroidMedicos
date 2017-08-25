package com.siem.siemmedicos.model.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 8/25/17.
 */

public class Motivos {

    private List<Motivo> mListMotivos;

    public void addMotivo(Motivo motivo){
        if (mListMotivos == null)
            mListMotivos = new ArrayList<>();

        mListMotivos.add(motivo);
    }

    public List<Motivo> getListMotivos() {
        return mListMotivos;
    }

    public void setListMotivos(List<Motivo> mListMotivos) {
        this.mListMotivos = mListMotivos;
    }
}
