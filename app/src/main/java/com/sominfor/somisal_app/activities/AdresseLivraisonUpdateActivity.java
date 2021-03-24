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

import java.util.List;

import static com.sominfor.somisal_app.activities.AddCommandeActivity.paysFacturationList;
import static com.sominfor.somisal_app.activities.LoginActivity.protocole;
import static com.sominfor.somisal_app.activities.UpdateCommandeActivity.paysFacturationUpdCdeList;

public class AdresseLivraisonUpdateActivity extends AppCompatActivity {
    MaterialButton BtnNext,BtnPrec;
    TextInputEditText EdtComRasol, EdtComAdr1l, EdtComAdr2l, EdtComCopol, EdtComVilll, EdtComBopol;
    MaterialBetterSpinner MbSpnComCpayl;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, apiUrl01, utilisateurCosoc, utilisateurCoage;
    Commande commande;
    Livreur livreur;
    Pays paysFacturation, paysLivraison, paysLivraisonNotSelected;
    List<Pays> paysLList;
    ApiReceiverMethods apiReceiverMethods;
    PaysSpinnerAdapter paysSpinnerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adresse_livraison_update);
        /**Instanciation des widgets**/
        BtnNext = findViewById(R.id.BtnNext);
        BtnPrec = findViewById(R.id.BtnPrec);
        EdtComRasol = findViewById(R.id.EdtComRasol);
        EdtComAdr1l = findViewById(R.id.EdtComAdr1l);
        EdtComAdr2l = findViewById(R.id.EdtComAdr2l);
        EdtComCopol = findViewById(R.id.EdtComCopol);
        EdtComVilll = findViewById(R.id.EdtComVilll);
        EdtComBopol = findViewById(R.id.EdtComBopol);
        MbSpnComCpayl = findViewById(R.id.MbSpnComCpayl);

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
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Livraison");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**Récupération de session utilisateur**/
        utilisateur = UserSessionManager.getInstance(getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();
        utilisateurCosoc = utilisateur.getUtilisateurCosoc();
        utilisateurCoage = utilisateur.getUtilisateurCoage();

        serveurNodeController = new ServeurNodeController();
        apiReceiverMethods = new ApiReceiverMethods(getApplicationContext());
        /**Récupération du serveur node**/
        serveurNode = serveurNodeController.getServeurNodeInfos();
        /**Récupération des informations envoyées***/
        commande = (Commande) getIntent().getSerializableExtra("commande");

        apiUrl01 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allPays";

        /**Set values to EditTexts**/
        EdtComRasol.setText(commande.getComrasol());
        EdtComAdr1l.setText(commande.getComadr1l());
        EdtComAdr2l.setText(commande.getComadr2l());
        EdtComCopol.setText(commande.getComcopol());
        EdtComVilll.setText(commande.getComvilll());
        EdtComBopol.setText(commande.getCombopol());

        /***Récupération de la liste des lieux de pays**/
        if (paysFacturationUpdCdeList == null){
            paysLList = apiReceiverMethods.recupererListePays(apiUrl01, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        }else {
            paysLList = paysFacturationUpdCdeList;
        }
        paysSpinnerAdapter = new PaysSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, paysLList);
        MbSpnComCpayl.setAdapter(paysSpinnerAdapter);
        /**Set values to spinner**/
        if (!paysLList.isEmpty()){
            /**Set value to MbSpnComCpays**/
            paysLivraisonNotSelected = new Pays();
            paysLivraisonNotSelected.setPaysCopay(commande.getComcpayl());
            int spinnerPosition = paysLList.indexOf(paysLivraisonNotSelected);
            if (spinnerPosition != -1)
            /**Set value to spinnerPays*/
                MbSpnComCpayl.setText(MbSpnComCpayl.getAdapter().getItem(spinnerPosition).toString());
        }
        /***Pays selectionné**/
        MbSpnComCpayl.setOnItemClickListener((parent, view, position, id) -> paysLivraison = paysSpinnerAdapter.getItem(position));

        /**Retour au précédent**/
        BtnPrec.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        /**Bouton Suivant**/
        BtnNext.setOnClickListener(v -> {
            /**Controle de Rasol et Adr1l**/
            if (EdtComRasol.getText().length() != 0){
                if (EdtComAdr1l.getText().length() != 0){
                    if (MbSpnComCpayl.getText().length()!=0){
                        commande.setComrasol(EdtComRasol.getText().toString());
                        commande.setComadr1l(EdtComAdr1l.getText().toString());
                        commande.setComadr2l(EdtComAdr2l.getText().toString());
                        commande.setComcopol(EdtComCopol.getText().toString());
                        commande.setComvilll(EdtComVilll.getText().toString());
                        commande.setCombopol(EdtComBopol.getText().toString());
                        if (null == paysLivraison){
                            commande.setComcpayl(paysLivraisonNotSelected.getPaysCopay());
                        }else{
                            commande.setComcpayl(paysLivraison.getPaysCopay());
                        }

                        Intent i = new Intent(getApplicationContext(), ReglementUpdateActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        i.putExtra("commande", commande);

                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }else{
                        Toast.makeText(this, "Choisissez le pays de livraison", Toast.LENGTH_LONG).show();
                    }


                }else{
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL14), Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL13), Toast.LENGTH_LONG).show();
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