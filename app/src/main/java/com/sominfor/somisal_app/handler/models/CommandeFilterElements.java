package com.sominfor.somisal_app.handler.models;

/**
 * Créé par vatsou le 28,juin,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class CommandeFilterElements {
    private String dateInf;
    private String dateSup;
    private String commandeStatut;

    public String getDateInf() {
        return dateInf;
    }

    public void setDateInf(String dateInf) {
        this.dateInf = dateInf;
    }

    public String getDateSup() {
        return dateSup;
    }

    public void setDateSup(String dateSup) {
        this.dateSup = dateSup;
    }

    public String getCommandeStatut() {
        return commandeStatut;
    }

    public void setCommandeStatut(String commandeStatut) {
        this.commandeStatut = commandeStatut;
    }
}
