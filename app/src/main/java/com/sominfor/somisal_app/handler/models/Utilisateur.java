package com.sominfor.somisal_app.handler.models;

/**
 * Créé par vatsou le 07,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Modèle de classe Utilisateur
 */
public class Utilisateur {
    public static final String TAG = Utilisateur.class.getSimpleName();
    public static final String TABLE = "UTILISATEUR";

    private String utilisateurLogin;
    private String utilisateurPassword;
    private String utilisateurSysteme;
    private String utilisateurFiliale;
    private String utilisateurCosoc;
    private String utilisateurCoage;

    public String getUtilisateurLogin() {
        return utilisateurLogin;
    }

    public void setUtilisateurLogin(String utilisateurLogin) {
        this.utilisateurLogin = utilisateurLogin;
    }

    public String getUtilisateurPassword() {
        return utilisateurPassword;
    }

    public void setUtilisateurPassword(String utilisateurPassword) {
        this.utilisateurPassword = utilisateurPassword;
    }

    public String getUtilisateurSysteme() {
        return utilisateurSysteme;
    }

    public void setUtilisateurSysteme(String utilisateurSysteme) {
        this.utilisateurSysteme = utilisateurSysteme;
    }

    public String getUtilisateurFiliale() {
        return utilisateurFiliale;
    }

    public void setUtilisateurFiliale(String utilisateurFiliale) {
        this.utilisateurFiliale = utilisateurFiliale;
    }

    public String getUtilisateurCosoc() {
        return utilisateurCosoc;
    }

    public void setUtilisateurCosoc(String utilisateurCosoc) {
        this.utilisateurCosoc = utilisateurCosoc;
    }

    public String getUtilisateurCoage() {
        return utilisateurCoage;
    }

    public void setUtilisateurCoage(String utilisateurCoage) {
        this.utilisateurCoage = utilisateurCoage;
    }
}
