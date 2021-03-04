package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.CommandeProduitAdapter;
import com.sominfor.somisal_app.adapters.DetailsDevisProduitsAdapter;
import com.sominfor.somisal_app.fragments.ClientFragment;
import com.sominfor.somisal_app.fragments.CommandeAddProduitFullDialog;
import com.sominfor.somisal_app.fragments.CommandeFragment;
import com.sominfor.somisal_app.fragments.DevisAddProduitFullDialog;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.DetailCommande;
import com.sominfor.somisal_app.handler.models.DetailDevis;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.interfaces.CommandeProduitsListener;
import com.sominfor.somisal_app.utils.UserSessionManager;

import java.util.ArrayList;
import java.util.List;

public class AddProduitCommandeActivity extends AppCompatActivity implements CommandeProduitsListener {
    FragmentManager fragmentManager;
    AppCompatActivity activity;
    FloatingActionButton fab_add_commande_details;
    LinearLayout Lnr01;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage;
    TextView TxtComNucom, TxtComRasoc, TxtComLieuv, TxtComStatu, TxtComComag, TxtComColiv, TxtComDaliv, TxtComVacom;
    RecyclerView recyclerViewDetailsCommande;
    CommandeProduitAdapter commandeProduitAdapter;
    List<DetailCommande> detailCommandeList = new ArrayList<>();

    @Override
    public void onDataReceived(DetailCommande detailCommande) {
        Lnr01.setVisibility(View.GONE);
        int spinnerPosition = detailCommandeList.indexOf(detailCommande);
        if (spinnerPosition != -1){
            detailCommandeList.set(spinnerPosition, detailCommande);
            commandeProduitAdapter.notifyItemChanged(spinnerPosition);
        }else{
            detailCommandeList.add(detailCommande);
            commandeProduitAdapter.notifyItemInserted(detailCommandeList.size());
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produit_commande);


        /**Contrôle de sessions utilisateur**/
        if (!UserSessionManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        /**Gestion du menu d'action**/
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Détails Commande");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /**Effacer l'ombre sous l'actionBar**/
        getSupportActionBar().setElevation(0);

        /**Instanciation des widgets**/
        recyclerViewDetailsCommande = findViewById(R.id.RecyclerViewDetailsCommande);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerViewDetailsCommande.setLayoutManager(linearLayoutManager);
        TxtComNucom = findViewById(R.id.TxtComNucom);
        TxtComRasoc = findViewById(R.id.TxtComRasoc);
        TxtComLieuv = findViewById(R.id.TxtComLieuv);
        TxtComStatu = findViewById(R.id.TxtComStatu);
        TxtComComag = findViewById(R.id.TxtComComag);
        TxtComColiv = findViewById(R.id.TxtComColiv);
        TxtComDaliv = findViewById(R.id.TxtComDaliv);
        TxtComVacom = findViewById(R.id.TxtComVacom);
        //BtnValider = findViewById(R.id.BtnValider);
        Lnr01 = findViewById(R.id.Lnr01);
        fab_add_commande_details = findViewById(R.id.fab_add_commande_details);

        serveurNodeController = new ServeurNodeController();
        serveurNode = serveurNodeController.getServeurNodeInfos();

        /**Récupération de session utilisateur**/
        utilisateur = UserSessionManager.getInstance(getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();
        utilisateurCosoc = utilisateur.getUtilisateurCosoc();
        utilisateurCoage = utilisateur.getUtilisateurCoage();

        FragmentManager fragmentManager = getSupportFragmentManager();
        commandeProduitAdapter = new CommandeProduitAdapter(getApplicationContext(), detailCommandeList, fragmentManager);
        recyclerViewDetailsCommande.setAdapter(commandeProduitAdapter);

        if (detailCommandeList.isEmpty()){
            Lnr01.setVisibility(View.VISIBLE);
        }else{
            Lnr01.setVisibility(View.GONE);
        }

        /**Ajout de produits**/
        fab_add_commande_details.setOnClickListener(v -> {
            openAddProduitDialog();
        });
    }

    // Options Menu (ActionBar Menu)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_add_commande_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            startActivity(new Intent(this, DashboardActivity.class));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**Ajout de produit**/

    private void openAddProduitDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        CommandeAddProduitFullDialog commandeAddProduitFullDialog = CommandeAddProduitFullDialog.newInstance();
        //devisPostItFullDialog.setTargetFragment(devisPostItFullDialog, 100);
        commandeAddProduitFullDialog.show(fragmentManager, ServeurNode.TAG);
    }

    public void doPositiveClick(DetailCommande dd) {
        int removeIndex = detailCommandeList.indexOf(dd);
        detailCommandeList.remove(removeIndex);
        commandeProduitAdapter.notifyItemRemoved(removeIndex);
    }

    public void doNegativeClick(DetailCommande dd) {
    }
}