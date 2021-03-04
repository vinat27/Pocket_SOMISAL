package com.sominfor.somisal_app.handler.models;

import java.io.Serializable;

/**
 * Créé par vatsou le 19,février,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class ModeReglement implements Serializable {
    public static final String TAG = ModeReglement.class.getSimpleName();
    private String CoMoreg;
    private String LiMoreg;

    public String getCoMoreg() {
        return CoMoreg;
    }

    public void setCoMoreg(String coMoreg) {
        CoMoreg = coMoreg;
    }

    public String getLiMoreg() {
        return LiMoreg;
    }

    public void setLiMoreg(String liMoreg) {
        LiMoreg = liMoreg;
    }
    @Override
    public String toString() {
        return getLiMoreg();
    }

    @Override
    public boolean equals(Object t){
        if(!(t instanceof ModeReglement)){
            return false;
        }
        ModeReglement c = (ModeReglement) t;
        //Compare however you want, ie
        return (c.getCoMoreg().equals(this.getCoMoreg()));
    }

}
