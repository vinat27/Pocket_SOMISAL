package com.sominfor.somisal_app.handler.models;

import java.io.Serializable;

/**
 * Créé par vatsou le 16,février,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class LieuVente implements Serializable {
    public static final String TAG = LieuVente.class.getSimpleName();
    private String Colieuv;
    private String Lilieuv;

    public LieuVente(){}

    public String getColieuv() {
        return Colieuv;
    }

    public void setColieuv(String colieuv) {
        Colieuv = colieuv;
    }

    public String getLilieuv() {
        return Lilieuv;
    }

    public void setLilieuv(String lilieuv) {
        Lilieuv = lilieuv;
    }

    @Override
    public String toString() {
        return getLilieuv();
    }

    @Override
    public boolean equals(Object t){
        if(!(t instanceof LieuVente)){
            return false;
        }
        LieuVente c = (LieuVente) t;
        //Compare however you want, ie
        return (c.getColieuv().equals(this.getColieuv())) & (c.getLilieuv().equals(this.getLilieuv()));
    }
}
