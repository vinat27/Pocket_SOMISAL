package com.sominfor.somisal_app.handler.models;

import java.io.Serializable;

/**
 * Créé par vatsou le 10,février,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class Valrem implements Serializable {
    private String tarUnvte;
    private int tarCofvt;
    private Double tarPriun;
    private Double remVarem;
    private Double remTxrem;

    public Valrem(){}

    public String getTarUnvte() {
        return tarUnvte;
    }

    public void setTarUnvte(String tarUnvte) {
        this.tarUnvte = tarUnvte;
    }

    public int getTarCofvt() {
        return tarCofvt;
    }

    public void setTarCofvt(int tarCofvt) {
        this.tarCofvt = tarCofvt;
    }

    public Double getTarPriun() {
        return tarPriun;
    }

    public void setTarPriun(Double tarPriun) {
        this.tarPriun = tarPriun;
    }

    public Double getRemVarem() {
        return remVarem;
    }

    public void setRemVarem(Double remVarem) {
        this.remVarem = remVarem;
    }

    public Double getRemTxrem() {
        return remTxrem;
    }

    public void setRemTxrem(Double remTxrem) {
        this.remTxrem = remTxrem;
    }
}
