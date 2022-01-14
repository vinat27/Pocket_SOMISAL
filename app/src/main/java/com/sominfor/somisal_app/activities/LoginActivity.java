package com.sominfor.somisal_app.activities;
/**
 * Fichier : LoginActivity.java
 * Auteur : ATSOU Koffi Vincent
 * Description: Gestion Authentification
 * Date Création: 06 Janvier 2021
 * **/

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.SystemeAdapter;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Systeme;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.utils.UserSessionManager;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    ArrayList<Systeme> systemes;
    SystemeAdapter systemeAdapter;
    MaterialBetterSpinner materialDesignSpinner;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    TextInputEditText EdtLogin, EdtPassword;
    Button BtnConnexion;
    String ApiUrl01, ApiUrl02, systemeFiliale, systemeAdresse, systemeCosoc, systemeCoage;
    ImageView server_settings;
    RequestQueue requestQueue;
    boolean statutServeur;
    ConstraintLayout constraintLayout;
    public static String protocole = "http";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**Controle orientation***/
        if(!getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**Instanciation des widgets**/
        materialDesignSpinner = findViewById(R.id.MbSpnSystem);
        EdtLogin = findViewById(R.id.EdtLogin);
        EdtPassword = findViewById(R.id.EdtPassword);
        BtnConnexion = findViewById(R.id.BtnConnexion);
        constraintLayout = findViewById(R.id.Ctl01);
        server_settings = findViewById(R.id.server_settings);
        serveurNodeController = new ServeurNodeController();
        systemes = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        server_settings.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        });



        /**controle connexion au serveur Node**/
        statutServeur = serveurNodeController.checkIfIsExist();
        if (!statutServeur){
            /***Le serveur n'a pas été configuré**/
            Snackbar snackbar=  Snackbar.make(constraintLayout,getResources().getString(R.string.login_activity_Snackbar01_NoServeur), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getResources().getString(R.string.login_activity_Snackbar01_Action), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            /**Ecran des paramètres***/
                            Intent intent = new Intent(LoginActivity.this, SettingsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            snackbar.setActionTextColor(Color.WHITE);
            snackbar.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.red));
            snackbar.show();
        }else{
            /**Récupération des informations serveur**/
            serveurNode = serveurNodeController.getServeurNodeInfos();
            /*URL Récupération de la liste des systèmes*/
            ApiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/administration/systeme";
            /*URL Authentification*/
            ApiUrl02 = protocole+"://"+serveurNode.getServeurNodeIp()+"/administration/login";
            /**Récupération de la liste des serveurs AS400**/
            getListSystemes(ApiUrl01);

            /**
             * Récupération de l'élément sélectionné
             * **/
            materialDesignSpinner.setOnItemClickListener((adapterView, view, i, l) -> {
                Systeme systeme = systemeAdapter.getItem(i);
                assert systeme != null;
                systemeFiliale = systeme.getSystemeFiliale();
                systemeAdresse = systeme.getSystemeAdresse();
                systemeCosoc = systeme.getSystemeCosoc();
                systemeCoage = systeme.getSystemeCoage();
            });
            /**Validation par l'utilisateur**/
            BtnConnexion.setOnClickListener(view -> {
                if(EdtLogin.getText().length() !=0 && EdtPassword.getText().length()!=0 && systemeFiliale != null){
                    String login = EdtLogin.getText().toString();
                    String password = EdtPassword.getText().toString();
                    /**Envoie d'informations au serveur Node**/
                    authentifier(ApiUrl02,systemeAdresse,login,password,systemeCosoc,systemeCoage);
                }else{
                    Toast.makeText(getApplicationContext(),"Remplissez tous les champs!",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }



    /**
     * @param apiUrl
     * Cette méthode permet de récupérer la liste des systèmes AS400 depuis un fichier JSON
     */
    public void getListSystemes(String apiUrl){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, apiUrl,  null, jsonArray -> {
            for (int i=0; i<jsonArray.length(); i++){
                Systeme systeme = new Systeme();
                try{
                    /**Récupération de la liste**/
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    systeme.setSystemeFiliale(jsonObject.getString("filiale"));
                    systeme.setSystemeAdresse(jsonObject.getString("systeme"));
                    systeme.setSystemeCosoc(jsonObject.getString("cosoc"));
                    systeme.setSystemeCoage(jsonObject.getString("coage"));
                    //Ajout dans la liste d'objets
                    systemes.add(systeme);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            systemeAdapter = new SystemeAdapter(LoginActivity.this, android.R.layout.simple_spinner_item, systemes);
            materialDesignSpinner.setAdapter(systemeAdapter);
        }, (VolleyError error) -> {
            error.printStackTrace();
            /**Erreur connexion**/
            Snackbar snackbar =  Snackbar.make(constraintLayout,getResources().getString(R.string.login_activity_Snackbar01_NoConnexion), Snackbar.LENGTH_LONG);
            snackbar.setBackgroundTint(ContextCompat.getColor(getApplicationContext(),R.color.red));
            snackbar.show();
        });
        requestQueue.add(jsonArrayRequest);
    }

    /**
     *
     * @param api_url l'url de récupération des information du fichier JSON
     * @param systeme le système choisi
     * @param login le login as400 de l'utilisateur
     * @param password le mot de passe de l'utilisateur
     *
     */
    public void authentifier(String api_url, final String systeme, final String login, final String password, final String systemeCosoc, final String systemeCoage){
        RequestQueue requestQueue = new Volley().newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, api_url, s -> {
            Utilisateur utilisateur = new Utilisateur();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("succes") == "true"){
                    //Authentification réussie
                    Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                    /**Formatage de l'objet utilisateur**/
                    JSONObject jsonObjectInfo = jsonObject.getJSONObject("Utilisateur");
                    utilisateur.setUtilisateurFiliale(jsonObjectInfo.getString("filiale"));
                    utilisateur.setUtilisateurSysteme(systeme);
                    utilisateur.setUtilisateurLogin(login);
                    utilisateur.setUtilisateurPassword(password);
                    utilisateur.setUtilisateurCosoc(jsonObjectInfo.getString("cosoc"));
                    utilisateur.setUtilisateurCoage(jsonObjectInfo.getString("coage"));
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    //Enregistrer la session utilisateur
                    UserSessionManager.getInstance(getApplicationContext()).userLogin(utilisateur.getUtilisateurLogin(),utilisateur.getUtilisateurSysteme(),utilisateur.getUtilisateurFiliale(), utilisateur.getUtilisateurPassword(), utilisateur.getUtilisateurCosoc(), utilisateur.getUtilisateurCoage());
                    //Démarrer l'activité pour le profil
                    finish();
                    startActivity(intent);
                }else{
                    /**Echec authentification**/
                    Snackbar snackbar=  Snackbar.make(constraintLayout,getResources().getString(R.string.login_activity_Snackbar03_NoAuthentication), Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(ContextCompat.getColor(getApplicationContext(),R.color.red));
                    snackbar.show();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }, Throwable::printStackTrace)
        {
            /**Paramètres envoyés**/
            protected Map<String,String> getParams(){
                Map<String, String> param = new HashMap<>();
                param.put("login", login);
                param.put("systeme",systeme);
                param.put("password", password);
                param.put("cosoc", systemeCosoc);
                param.put("coage", systemeCoage);

                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

}