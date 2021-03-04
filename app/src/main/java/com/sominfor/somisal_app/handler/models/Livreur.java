package com.sominfor.somisal_app.handler.models;

import java.io.Serializable;

/**
 * Créé par vatsou le 17,février,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class Livreur implements Serializable {
    public static final String TAG = Livreur.class.getSimpleName();
    private String LivColiv;
    private String Livliliv;

    public String getLivColiv() {
        return LivColiv;
    }

    public void setLivColiv(String livColiv) {
        LivColiv = livColiv;
    }

    public String getLivliliv() {
        return Livliliv;
    }

    public void setLivliliv(String livliliv) {
        Livliliv = livliliv;
    }
    @Override
    public String toString() {
        return getLivliliv();
    }

    @Override
    public boolean equals(Object t){
        if(!(t instanceof Livreur)){
            return false;
        }
        Livreur c = (Livreur) t;
        //Compare however you want, ie
        return (c.getLivColiv().equals(this.getLivColiv()));
    }
}
