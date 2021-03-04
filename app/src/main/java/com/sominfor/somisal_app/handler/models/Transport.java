package com.sominfor.somisal_app.handler.models;

import java.io.Serializable;

/**
 * Créé par vatsou le 21,février,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class Transport implements Serializable {
    public static final String TAG = Transport.class.getSimpleName();
    private String TrpCotrp;
    private String TrpLitrp;

    public String getTrpCotrp() {
        return TrpCotrp;
    }

    public void setTrpCotrp(String trpCotrp) {
        TrpCotrp = trpCotrp;
    }

    public String getTrpLitrp() {
        return TrpLitrp;
    }

    public void setTrpLitrp(String trpLitrp) {
        TrpLitrp = trpLitrp;
    }

    @Override
    public String toString() {
        return getTrpLitrp();
    }

    @Override
    public boolean equals(Object t){
        if(!(t instanceof Transport)){
            return false;
        }
        Transport c = (Transport) t;
        //Compare however you want, ie
        return (c.getTrpCotrp().equals(this.getTrpCotrp()));
    }
}
