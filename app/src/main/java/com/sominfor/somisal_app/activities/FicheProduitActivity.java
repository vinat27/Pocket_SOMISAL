package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Produit;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.utils.UserSessionManager;

public class FicheProduitActivity extends AppCompatActivity {

    Utilisateur utilisateur;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    String systemeAdresse, utilisateurLogin, utilisateurPassword;
    Produit produit;
    TextView TxtProLiPro, TxtProCoPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_produit);

        /**Contrôle de sessions utilisateur**/
        if (!UserSessionManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        /**Gestion du menu d'action**/
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Fiche Produit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /**Effacer l'ombre sous l'actionBar**/
        getSupportActionBar().setElevation(0);

        /**Gestion des écrans - Portrait pour les smartphones et Landscape pour les tablettes**/
        if(!getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        serveurNodeController = new ServeurNodeController();

        /**Instanciation des widgets**/
        TxtProLiPro = findViewById(R.id.TxtProLiPro);
        TxtProCoPro = findViewById(R.id.TxtProCoPro);

        /**Récupération de session utilisateur**/
        utilisateur = UserSessionManager.getInstance(getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();

        /**Récupération de l'objet produit**/
        Bundle bundle = getIntent().getExtras();
        produit = (Produit) bundle.getSerializable("produit");

        /**Initialisation des information s de fiche de stock**/
        TxtProLiPro.setText(produit.getProlipro().trim().toUpperCase());
        TxtProCoPro.setText(String.valueOf(produit.getProcopro()));
    }

    // Options Menu (ActionBar Menu)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fiche_produit_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}