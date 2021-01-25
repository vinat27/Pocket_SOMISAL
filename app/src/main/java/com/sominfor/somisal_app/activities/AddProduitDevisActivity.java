package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.DetailDevisAdapter;
import com.sominfor.somisal_app.adapters.DetailsDevisProduitsAdapter;
import com.sominfor.somisal_app.fragments.DevisAddProduitFullDialog;
import com.sominfor.somisal_app.fragments.DevisPostItFullDialog;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.DetailDevis;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.interfaces.DevisProduitsListener;
import com.sominfor.somisal_app.utils.UserSessionManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddProduitDevisActivity extends AppCompatActivity implements DevisProduitsListener {
    TextView TxtDevnudev, TxtClirasoc, TxtDevLieuv, TxtDevStatu, TxtDevLimag, TxtDevLiliv, TxtDevDaliv, TxtDevVadev;
    MaterialButton BtnValider;
    FloatingActionButton fab_add_devis_details;
    LinearLayout Lnr01;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    String systemeAdresse, utilisateurLogin, utilisateurPassword;
    RecyclerView RecyclerViewDetailsDevis;
    DetailsDevisProduitsAdapter detailsDevisProduitsAdapter;
    List<DetailDevis> detailDevisList = new ArrayList<>();

    @Override
    public void onDataReceived(DetailDevis detailDevis) {
        Lnr01.setVisibility(View.GONE);
        detailDevisList.add(detailDevis);
        detailsDevisProduitsAdapter.notifyItemInserted(detailDevisList.size());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produit_devis);

        /**Gestion du menu d'action**/
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Détails devis");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /**Effacer l'ombre sous l'actionBar**/
        getSupportActionBar().setElevation(0);

        /**Instanciation des widgets**/
        RecyclerViewDetailsDevis = findViewById(R.id.RecyclerViewDetailsDevis);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        RecyclerViewDetailsDevis.setLayoutManager(linearLayoutManager);
        TxtDevnudev = findViewById(R.id.TxtDevNudev);
        TxtClirasoc = findViewById(R.id.TxtClirasoc);
        TxtDevLieuv = findViewById(R.id.TxtDevLieuv);
        TxtDevStatu = findViewById(R.id.TxtDevStatu);
        TxtDevLimag = findViewById(R.id.TxtDevLimag);
        TxtDevLiliv = findViewById(R.id.TxtDevLiliv);
        TxtDevDaliv = findViewById(R.id.TxtDevDaliv);
        TxtDevVadev = findViewById(R.id.TxtDevVadev);
        BtnValider = findViewById(R.id.BtnValider);
        Lnr01 = findViewById(R.id.Lnr01);
        fab_add_devis_details = findViewById(R.id.fab_add_devis_details);

        serveurNodeController = new ServeurNodeController();
        serveurNode = serveurNodeController.getServeurNodeInfos();

        /**Récupération de session utilisateur**/
        utilisateur = UserSessionManager.getInstance(getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();

        FragmentManager fragmentManager = getSupportFragmentManager();
        detailsDevisProduitsAdapter = new DetailsDevisProduitsAdapter(getApplicationContext(), detailDevisList, fragmentManager);
        RecyclerViewDetailsDevis.setAdapter(detailsDevisProduitsAdapter);

        if (detailDevisList.isEmpty()){
            Lnr01.setVisibility(View.VISIBLE);
        }else{
            Lnr01.setVisibility(View.GONE);
        }

        /**Ajout de produits**/
        fab_add_devis_details.setOnClickListener(v -> {
            openAddProduitDialog();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void initData(){
        //detailDevisList = new ArrayList<>();
        //detailDevisList.add(new DetailDevis(3154,573,"K",1000,5.000,12.47,0.00,0.00,24.94,"AMANDE EFFILEE BLANCHIE 5 KG",""));
        //detailDevisList.add(new DetailDevis(4,483,"P",2000,5.000,1.67,0.00,0.00, 3.34, "BOITE FER SPECIAL PIZZA", ""));
    }

    /**Ajout de produit**/

    private void openAddProduitDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DevisAddProduitFullDialog devisAddProduitFullDialog = DevisAddProduitFullDialog.newInstance();
        //devisPostItFullDialog.setTargetFragment(devisPostItFullDialog, 100);
        devisAddProduitFullDialog.show(fragmentManager, ServeurNode.TAG);
    }

}