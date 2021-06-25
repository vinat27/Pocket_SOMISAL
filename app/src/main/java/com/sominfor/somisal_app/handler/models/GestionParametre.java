package com.sominfor.somisal_app.handler.models;

/**
 * Créé par vatsou le 23,juin,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class GestionParametre {
    private String copar;
    private String datas;

    public String getCopar() {
        return copar;
    }

    public void setCopar(String copar) {
        this.copar = copar;
    }

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return getDatas();
    }
}
