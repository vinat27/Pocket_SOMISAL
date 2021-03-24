package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.PaysSpinnerAdapter;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Client;
import com.sominfor.somisal_app.handler.models.Commande;
import com.sominfor.somisal_app.handler.models.LieuVente;
import com.sominfor.somisal_app.handler.models.Livreur;
import com.sominfor.somisal_app.handler.models.Magasin;
import com.sominfor.somisal_app.handler.models.Pays;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Tournee;
import com.sominfor.somisal_app.handler.models.Transport;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.utils.ApiReceiverMethods;
import com.sominfor.somisal_app.utils.UserSessionManager;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;

import static com.sominfor.somisal_app.activities.AddCommandeActivity.paysFacturationList;
import static com.sominfor.somisal_app.activities.LoginActivity.protocole;
import static com.sominfor.somisal_app.activities.UpdateCommandeActivity.paysFacturationUpdCdeList;

public class AdresseFacturationUpdateActivity extends AppCompatActivity {
    Commande commande;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, apiUrl01, utilisateurCosoc, utilisateurCoage;
    Pays pays, paysNotSelected;
    Livreur livreur;

    String Comdacom, Comdaliv, ComNamar;
    MaterialButton BtnNext,BtnPrec;
    TextInputEditText EdtComRasoc, EdtComAdre1, EdtComAdre2, EdtComCopos, EdtComVille, EdtComBopos;
    MaterialBetterSpinner MbSpnComCpays;
    PaysSpinnerAdapter paysSpinnerAdapter;
    ApiReceiverMethods apiReceiverMethods;
    List<Pays> paysList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adresse_facturation_update);
        /**Contrôle de sessions utilisateur**/
        if (!UserSessionManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        /**Controle orientation***/
        if(!getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        /**Gestion du menu d'action**/
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Facturation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**Récupération de session utilisateur**/
        utilisateur = UserSessionManager.getInstance(getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();
        utilisateurCosoc = utilisateur.getUtilisateurCosoc();
        utilisateurCoage = utilisateur.getUtilisateurCoage();
        commande = new Commande();

        serveurNodeController = new ServeurNodeController();
        /**Récupération du serveur node**/
        serveurNode = serveurNodeController.getServeurNodeInfos();

        /**URL Récupération de la liste des clients**/
        apiUrl01 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allPays";

        /**Récupération des informations envoyées***/
        commande = (Commande) getIntent().getSerializableExtra("commande");

        /**Initialisation des listes**/
        paysList = new ArrayList<>();
        apiReceiverMethods = new ApiReceiverMethods(getApplicationContext());

        /**Instanciation des widgets**/
        BtnNext = findViewById(R.id.BtnNext);
        BtnPrec = findViewById(R.id.BtnPrec);
        EdtComRasoc = findViewById(R.id.EdtComRasoc);
        EdtComAdre1 = findViewById(R.id.EdtComAdre1);
        EdtComAdre2 = findViewById(R.id.EdtComAdre2);
        EdtComCopos = findViewById(R.id.EdtComCopos);
        EdtComVille = findViewById(R.id.EdtComVille);
        EdtComBopos = findViewById(R.id.EdtComBopos);
        MbSpnComCpays = findViewById(R.id.MbSpnComCpays);

        /**Set values to EditTexts**/
        EdtComRasoc.setText(commande.getComrasoc());
        EdtComAdre1.setText(commande.getComadre1());
        EdtComAdre2.setText(commande.getComadre2());
        EdtComCopos.setText(commande.getComcopos());
        EdtComVille.setText(commande.getComville());
        EdtComBopos.setText(commande.getCombopos());

        /***Récupération de la liste des lieux de vente**/
        if (paysFacturationUpdCdeList == null){
            paysList = apiReceiverMethods.recupererListePays(apiUrl01, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        }else {
            paysList = paysFacturationUpdCdeList;
        }
        paysSpinnerAdapter = new PaysSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, paysList);
        MbSpnComCpays.setAdapter(paysSpinnerAdapter);

        if (!paysList.isEmpty()){
            /**Set value to MbSpnComCpays**/
            paysNotSelected = new Pays();
            paysNotSelected.setPaysCopay(commande.getComcpays());
            int spinnerPosition = paysList.indexOf(paysNotSelected);
            if (spinnerPosition != -1)
            /**Set value to spinnerPays*/
                MbSpnComCpays.setText(MbSpnComCpays.getAdapter().getItem(spinnerPosition).toString());
        }
        MbSpnComCpays.setOnItemClickListener((parent, view, position, id) -> pays = paysSpinnerAdapter.getItem(position));

        /**Retour au précédent**/
        BtnPrec.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        /**Bouton Suivant**/
        BtnNext.setOnClickListener(v -> {
                    if (MbSpnComCpays.getText().length() != 0) {
                        commande.setComrasoc(EdtComRasoc.getText().toString());
                        commande.setComadre1(EdtComAdre1.getText().toString());
                        commande.setComadre2(EdtComAdre2.getText().toString());
                        commande.setComcopos(EdtComCopos.getText().toString());
                        commande.setComville(EdtComVille.getText().toString());
                        commande.setCombopos(EdtComBopos.getText().toString());
                        if (null == pays){
                            commande.setComcpays(paysNotSelected.getPaysCopay());
                        }else{
                            commande.setComcpays(pays.getPaysCopay());
                        }

                        Intent i = new Intent(getApplicationContext(), AdresseLivraisonUpdateActivity.class);
                        i.putExtra("commande", commande);
                        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }else{
                        Toast.makeText(this, "Choisissez le pays de facturation", Toast.LENGTH_LONG).show();
                    }

        });
    }

    // Options Menu (ActionBar Menu)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_commande_activity, menu);
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