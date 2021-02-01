package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.DetailCommandeAdapter;
import com.sominfor.somisal_app.adapters.DetailDevisAdapter;
import com.sominfor.somisal_app.fragments.CommentairesDevisFullDialog;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Commande;
import com.sominfor.somisal_app.handler.models.DetailCommande;
import com.sominfor.somisal_app.handler.models.DetailDevis;
import com.sominfor.somisal_app.handler.models.Devis;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.utils.UserSessionManager;

import java.util.ArrayList;
import java.util.List;

public class CommandeDetailsActivity extends AppCompatActivity {
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, ApiUrl01;
    Commande commande;
    RecyclerView RecyclerViewDetailsCommandes;
    List<DetailCommande> detailCommandeList;
    DetailCommandeAdapter detailCommandeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande_details);

        /**Contrôle de sessions utilisateur**/
        if (!UserSessionManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        /**Gestion du menu d'action**/
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Détails commande");
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
        commande = (Commande) bundle.getSerializable("commande");

        RecyclerViewDetailsCommandes = findViewById(R.id.RecyclerViewDetailsCommandes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        RecyclerViewDetailsCommandes.setLayoutManager(linearLayoutManager);


        initData();
        setRecyclerview();
    }

    // Options Menu (ActionBar Menu)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fiche_commande_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.comment_commande:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setRecyclerview(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        detailCommandeAdapter = new DetailCommandeAdapter(getApplicationContext(), detailCommandeList, fragmentManager);
        RecyclerViewDetailsCommandes.setAdapter(detailCommandeAdapter);
        RecyclerViewDetailsCommandes.setHasFixedSize(true);
    }

    public void initData(){
        detailCommandeList = new ArrayList<>();

        detailCommandeList.add(new DetailCommande(3154, 573, "K", "00000001", 1000,5.000, 12.47, 0.00, 0.00, 0.00, 27.69, "AMANDE EFFILEE BLANCHIE 5 KG", ""));
        detailCommandeList.add(new DetailCommande(3154, 573, "K", "00000002", 1000,5.000, 12.47, 0.00, 0.00, 0.00, 27.69, "AMANDE EFFILEE BLANCHIE 5 KG", ""));
    }

}