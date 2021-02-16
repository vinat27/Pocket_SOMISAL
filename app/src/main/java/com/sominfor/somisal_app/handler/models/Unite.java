package com.sominfor.somisal_app.handler.models;

import java.util.Objects;

/**
 * Créé par vatsou le 24,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class Unite {
    private String UniteCode;
    private String UniteLibelle;
    private float UniteCoef;

    public Unite(){}

    public Unite(String uniteCode, String uniteLibelle) {
        UniteCode = uniteCode;
        UniteLibelle = uniteLibelle;
    }

    public String getUniteCode() {
        return UniteCode;
    }

    public void setUniteCode(String uniteCode) {
        UniteCode = uniteCode;
    }

    public String getUniteLibelle() {
        return UniteLibelle;
    }

    public void setUniteLibelle(String uniteLibelle) {
        UniteLibelle = uniteLibelle;
    }

    public float getUniteCoef() {
        return UniteCoef;
    }

    public void setUniteCoef(float uniteCoef) {
        UniteCoef = uniteCoef;
    }

    @Override
    public String toString() {
        return getUniteLibelle();
    }

    @Override
    public boolean equals(Object t){
        if(!(t instanceof Unite)){
            return false;
        }
        Unite c = (Unite) t;
        //Compare however you want, ie
        return (c.getUniteLibelle().equals(this.getUniteLibelle())) & (c.getUniteCode().equals(this.getUniteCode()));
    }
}
