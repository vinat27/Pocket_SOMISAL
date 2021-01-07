package com.sominfor.somisal_app.activities;
/**
 * Fichier : LoginActivity.java
 * Auteur : ATSOU Koffi Vincent
 * Description: Gestion Authentification
 * Date Création: 06 Janvier 2021
 * **/

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SpinnerAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.SystemeAdapter;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Systeme;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    ArrayList<Systeme> systemes;
    SystemeAdapter systemeAdapter;
    MaterialBetterSpinner materialDesignSpinner;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    TextInputEditText EdtLogin, EdtPassword;
    Button BtnConnexion;
    String ApiUrl01;
    RequestQueue requestQueue;
    public static String protocole = "http";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /**Controle orientation de l'écran
         * Paysage pour les tablettes
         * Portrait pour les smartphones**/
        if(!getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        /**Instanciation des widgets**/
        materialDesignSpinner = findViewById(R.id.MbSpnSystem);
        EdtLogin = findViewById(R.id.EdtLogin);
        EdtPassword = findViewById(R.id.EdtPassword);
        BtnConnexion = findViewById(R.id.BtnConnexion);
        serveurNodeController = new ServeurNodeController();
        systemes = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        /**Récupération des informations serveur**/
        serveurNode = serveurNodeController.getServeurNodeInfos();
        ApiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/administration/systeme";
        /**Récupération de la liste des serveurs AS400**/
        getListSystemes(ApiUrl01);
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
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    systeme.setSystemeFiliale(jsonObject.getString("filiale"));
                    systeme.setSystemeAdresse(jsonObject.getString("systeme"));
                    //Ajout dans la liste d'objets
                    systemes.add(systeme);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            systemeAdapter = new SystemeAdapter(LoginActivity.this, android.R.layout.simple_spinner_item, systemes);
            materialDesignSpinner.setAdapter(systemeAdapter);
        }, error -> error.printStackTrace());

        requestQueue.add(jsonArrayRequest);
    }

    // Options Menu (ActionBar Menu)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_login, menu);
        return true;
    }

    // When Options Menu is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Gestion de menus
        int id = item.getItemId();
        if (id == R.id.handleSystem){
            /**Ecran des paramètres***/
            Intent intent = new Intent(LoginActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}