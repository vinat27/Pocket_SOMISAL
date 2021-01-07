package com.sominfor.somisal_app.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.sominfor.somisal_app.activities.LoginActivity;
import com.sominfor.somisal_app.handler.models.Utilisateur;

/**
 * Créé par vatsou le 07,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Gestion de session utilisateur
 */
public class UserSessionManager  {
    //Constantes
    private static final String SHARED_PREF_NAME = "SomisalSession";
    public static final String KEY_LOGIN = "key_login";
    public static final String KEY_SYSTEME = "key_systeme";
    public static final String KEY_FILIALE = "key_filiale";
    public static final String KEY_PASSWORD = "key_password";
    public static final String KEY_COSOC = "key_cosoc";
    public static final String KEY_COAGE = "key_coage";
    private static UserSessionManager mInstance;
    private static Context mCtx;

    private UserSessionManager(Context context) {
        mCtx = context;
    }

    public static synchronized UserSessionManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UserSessionManager(context);
        }
        return mInstance;
    }

    /**Isertion des informations dans la session**/
    public void userLogin(String login, String systeme, String filiale, String password, String cosoc, String coage) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LOGIN, login);
        editor.putString(KEY_FILIALE, filiale);
        editor.putString(KEY_SYSTEME, systeme);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_COSOC, cosoc);
        editor.putString(KEY_COAGE, coage);
        editor.apply();
    }

   /**Vérifier si l'utilisateur est déjà loggé ou pas**/
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LOGIN,null) != null;
    }

    /**Déconnexion utilisateur**/
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        /**Retour vers le login**/
        Intent i = new Intent(mCtx, LoginActivity.class);
        //Fermer tous les écrans
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mCtx.startActivity(i);
    }

    /**Récupération des informations utilisateur**/
    public Utilisateur getUtilisateurDetail(){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        /**Utilisation de hashmap pour enregistrer l'utilisateur**/
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUtilisateurLogin(sharedPreferences.getString(KEY_LOGIN,null));
        utilisateur.setUtilisateurFiliale(sharedPreferences.getString(KEY_FILIALE,null));
        utilisateur.setUtilisateurSysteme(sharedPreferences.getString(KEY_SYSTEME,null));
        utilisateur.setUtilisateurPassword(sharedPreferences.getString(KEY_PASSWORD,null));
        utilisateur.setUtilisateurCosoc(sharedPreferences.getString(KEY_COSOC, null));
        utilisateur.setUtilisateurCoage(sharedPreferences.getString(KEY_COAGE, null));
        return utilisateur;
    }
}
