package com.sominfor.somisal_app.handler.models;

/**
 * Créé par vatsou le 30,juin,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class StatutCommande {
    private String codSta;
    private String libSta;

    public StatutCommande(String codSta, String libSta) {
        this.codSta = codSta;
        this.libSta = libSta;
    }

    public String getCodSta() {
        return codSta;
    }

    public void setCodSta(String codSta) {
        this.codSta = codSta;
    }

    public String getLibSta() {
        return libSta;
    }

    public void setLibSta(String libSta) {
        this.libSta = libSta;
    }

    @Override
    public String toString() {
        return libSta;
    }
}
