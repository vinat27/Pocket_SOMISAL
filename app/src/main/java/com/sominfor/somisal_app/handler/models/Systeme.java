package com.sominfor.somisal_app.handler.models;

/**
 * Créé par vatsou le 06,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Gestion de systèmes AS400
 */
public class Systeme {
    public static final String TAG = Systeme.class.getSimpleName();
    public static final String TABLE = "SYSTEME";

    public static final String KEY_SystemeFiliale = "systemeFiliale";
    public static final String KEY_SystemeAdresse = "systemeAdresse";

    private String systemeFiliale;
    private String systemeAdresse;
    private String systemeCosoc;
    private String systemeCoage;

    public String getSystemeFiliale() {
        return systemeFiliale;
    }

    public void setSystemeFiliale(String systemeFiliale) {
        this.systemeFiliale = systemeFiliale;
    }

    public String getSystemeAdresse() {
        return systemeAdresse;
    }

    public void setSystemeAdresse(String systemeAdresse) {
        this.systemeAdresse = systemeAdresse;
    }

    public String getSystemeCosoc() {
        return systemeCosoc;
    }

    public void setSystemeCosoc(String systemeCosoc) {
        this.systemeCosoc = systemeCosoc;
    }

    public String getSystemeCoage() {
        return systemeCoage;
    }

    public void setSystemeCoage(String systemeCoage) {
        this.systemeCoage = systemeCoage;
    }

    @Override
    public String toString() {
        return getSystemeFiliale();
    }
}
