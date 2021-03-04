package com.sominfor.somisal_app.handler.models;

import java.io.Serializable;

/**
 * Créé par vatsou le 19,février,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class Commercial implements Serializable {
    public static final String TAG = Commercial.class.getSimpleName();
    private String CoUscom;
    private String LiUscom;

    public String getCoUscom() {
        return CoUscom;
    }

    public void setCoUscom(String coUscom) {
        CoUscom = coUscom;
    }

    public String getLiUscom() {
        return LiUscom;
    }

    public void setLiUscom(String liUscom) {
        LiUscom = liUscom;
    }

    @Override
    public String toString() {
        return getLiUscom();
    }

    @Override
    public boolean equals(Object t){
        if(!(t instanceof Commercial)){
            return false;
        }
        Commercial c = (Commercial) t;
        //Compare however you want, ie
        return (c.getCoUscom().equals(this.getLiUscom()));
    }
}
