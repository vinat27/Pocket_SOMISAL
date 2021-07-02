package com.sominfor.somisal_app.handler.models;

/**
 * Créé par vatsou le 01,juillet,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class DelaiLivraison {
    private int dldlv;
    private String nudlv;
    private String lidlv;

    public int getDldlv() {
        return dldlv;
    }

    public void setDldlv(int dldlv) {
        this.dldlv = dldlv;
    }

    public String getNudlv() {
        return nudlv;
    }

    public void setNudlv(String nudlv) {
        this.nudlv = nudlv;
    }

    public String getLidlv() {
        return lidlv;
    }

    public void setLidlv(String lidlv) {
        this.lidlv = lidlv;
    }

    @Override
    public boolean equals(Object t){
        if(!(t instanceof DelaiLivraison)){
            return false;
        }
        DelaiLivraison c = (DelaiLivraison) t;
        //Compare however you want, ie
        return (c.getNudlv().equals(this.getNudlv()));
    }
}
