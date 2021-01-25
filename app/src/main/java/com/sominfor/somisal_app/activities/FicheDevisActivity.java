package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.DetailDevisAdapter;
import com.sominfor.somisal_app.adapters.DevisAdapter;
import com.sominfor.somisal_app.fragments.CommentairesDevisFullDialog;
import com.sominfor.somisal_app.fragments.FilterProduitFullDialog;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.DetailDevis;
import com.sominfor.somisal_app.handler.models.Devis;
import com.sominfor.somisal_app.handler.models.Produit;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.utils.UserSessionManager;

import java.util.ArrayList;
import java.util.List;

public class FicheDevisActivity extends AppCompatActivity {
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, ApiUrl01;
    Devis devis;
    RecyclerView RecyclerViewDetailsDevis;
    List<DetailDevis> detailDevisList;
    DetailDevisAdapter detailDevisAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_devis);
        /**Contrôle de sessions utilisateur**/
        if (!UserSessionManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        /**Gestion du menu d'action**/
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Détails devis");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /**Effacer l'ombre sous l'actionBar**/
        getSupportActionBar().setElevation(0);


        serveurNodeController = new ServeurNodeController();
        serveurNode = serveurNodeController.getServeurNodeInfos();

        /**Récupération de session utilisateur**/
        utilisateur = UserSessionManager.getInstance(getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();

        /**Récupération de l'objet devis**/
        Bundle bundle = getIntent().getExtras();
        devis = (Devis) bundle.getSerializable("devis");

        RecyclerViewDetailsDevis = findViewById(R.id.RecyclerViewDetailsDevis);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        RecyclerViewDetailsDevis.setLayoutManager(linearLayoutManager);

        initData();
        setRecyclerview();

    }


    // Options Menu (ActionBar Menu)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fiche_devis_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.comment_devis:
                /***Filtre sur familles et sous familles**/
                FragmentManager fragmentManager = getSupportFragmentManager();
                CommentairesDevisFullDialog commentairesDevisFullDialog = CommentairesDevisFullDialog.newInstance();
                Bundle args = new Bundle();
                args.putString("devtxnpi", "RAS");
                args.putString("devtxnen", "");
                commentairesDevisFullDialog.setArguments(args);
                commentairesDevisFullDialog.show(fragmentManager, ServeurNode.TAG);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setRecyclerview(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        detailDevisAdapter = new DetailDevisAdapter(getApplicationContext(), detailDevisList, fragmentManager);
        RecyclerViewDetailsDevis.setAdapter(detailDevisAdapter);
        RecyclerViewDetailsDevis.setHasFixedSize(true);
    }

    public void initData(){
        detailDevisList = new ArrayList<>();

        detailDevisList.add(new DetailDevis(3154,573,"K",1000,5.000,12.47,0.00,0.00,24.94,"AMANDE EFFILEE BLANCHIE 5 KG",""));
        detailDevisList.add(new DetailDevis(4,483,"P",2000,5.000,1.67,0.00,0.00, 3.34, "BOITE FER SPECIAL PIZZA", ""));
    }
}