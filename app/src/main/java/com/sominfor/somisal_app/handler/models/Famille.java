package com.sominfor.somisal_app.handler.models;

/**
 * Créé par kguillard le 08,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Gestion de systèmes AS400
 */

public class Famille {
  private String fapcofam;
  private String fapsofam;
  private String  fapdesam;
  private String fapstatu;

    public Famille() {
    }

    public String getFapcofam() {
        return fapcofam;
    }

    public void setFapcofam(String fapcofam) {
        this.fapcofam = fapcofam;
    }

    public String getFapsofam() {
        return fapsofam;
    }

    public void setFapsofam(String fapsofam) {
        this.fapsofam = fapsofam;
    }

    public String getFapdesam() {
        return fapdesam;
    }

    public void setFapdesam(String fapdesam) {
        this.fapdesam = fapdesam;
    }

    public String getFapstatu() {
        return fapstatu;
    }

    public void setFapstatu(String fapstatu) {
        this.fapstatu = fapstatu;
    }

    @Override
    public String toString() {
        return "Famille{" +
                "fapcofam='" + fapcofam + '\'' +
                ", fapsofam='" + fapsofam + '\'' +
                ", fapdesam='" + fapdesam + '\'' +
                ", fapstatu='" + fapstatu + '\'' +
                '}';
    }
}
