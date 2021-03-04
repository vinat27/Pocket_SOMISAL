package com.sominfor.somisal_app.handler.models;

import java.io.Serializable;

/**
 * Créé par vatsou le 20,février,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class Tournee implements Serializable {
    public static final String TAG = Tournee.class.getSimpleName();
    private String TrnCotrn;
    private String TrnLitrn;

    public String getTrnCotrn() {
        return TrnCotrn;
    }

    public void setTrnCotrn(String trnCotrn) {
        TrnCotrn = trnCotrn;
    }

    public String getTrnLitrn() {
        return TrnLitrn;
    }

    public void setTrnLitrn(String trnLitrn) {
        TrnLitrn = trnLitrn;
    }

    @Override
    public String toString() {
        return getTrnLitrn();
    }

    @Override
    public boolean equals(Object t){
        if(!(t instanceof Tournee)){
            return false;
        }
        Tournee c = (Tournee) t;
        //Compare however you want, ie
        return (c.getTrnCotrn().equals(this.getTrnCotrn()));
    }
}
