package com.sominfor.somisal_app.handler.models;

import java.io.Serializable;

/**
 * Créé par vatsou le 17,février,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class Magasin implements Serializable {
    public static final String TAG = Magasin.class.getSimpleName();
    private String Magcomag;
    private String Maglimag;

    public String getMagcomag() {
        return Magcomag;
    }

    public void setMagcomag(String magcomag) {
        Magcomag = magcomag;
    }

    public String getMaglimag() {
        return Maglimag;
    }

    public void setMaglimag(String maglimag) {
        Maglimag = maglimag;
    }

    @Override
    public String toString() {
        return getMaglimag();
    }

    @Override
    public boolean equals(Object t){
        if(!(t instanceof Magasin)){
            return false;
        }
        Magasin c = (Magasin) t;
        //Compare however you want, ie
        return (c.getMagcomag().equals(this.getMagcomag())) & (c.getMaglimag().equals(this.getMaglimag()));
    }
}
