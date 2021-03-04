package com.sominfor.somisal_app.handler.models;

import java.io.Serializable;

/**
 * Créé par vatsou le 18,février,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class DelaiReglement implements Serializable {
    public static final String TAG = DelaiReglement.class.getSimpleName();
    private String CoDereg;
    private String LiDereg;

    public String getCoDereg() {
        return CoDereg;
    }

    public void setCoDereg(String coDereg) {
        CoDereg = coDereg;
    }

    public String getLiDereg() {
        return LiDereg;
    }

    public void setLiDereg(String liDereg) {
        LiDereg = liDereg;
    }

    @Override
    public String toString() {
        return getLiDereg();
    }

    @Override
    public boolean equals(Object t){
        if(!(t instanceof DelaiReglement)){
            return false;
        }
        DelaiReglement c = (DelaiReglement) t;
        //Compare however you want, ie
        return (c.getCoDereg().equals(this.getCoDereg()));
    }
}
