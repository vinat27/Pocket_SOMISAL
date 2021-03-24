package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.CommercialSpinnerAdapter;
import com.sominfor.somisal_app.adapters.DelaiReglementSpinnerAdapter;
import com.sominfor.somisal_app.adapters.ModeReglementSpinnerAdapter;
import com.sominfor.somisal_app.adapters.PaysSpinnerAdapter;
import com.sominfor.somisal_app.fragments.DevisFragment;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Client;
import com.sominfor.somisal_app.handler.models.Commande;
import com.sominfor.somisal_app.handler.models.Commercial;
import com.sominfor.somisal_app.handler.models.DelaiReglement;
import com.sominfor.somisal_app.handler.models.LieuVente;
import com.sominfor.somisal_app.handler.models.Livreur;
import com.sominfor.somisal_app.handler.models.Magasin;
import com.sominfor.somisal_app.handler.models.ModeReglement;
import com.sominfor.somisal_app.handler.models.Pays;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Tournee;
import com.sominfor.somisal_app.handler.models.Transport;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.utils.ApiReceiverMethods;
import com.sominfor.somisal_app.utils.UserSessionManager;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.List;

import static com.sominfor.somisal_app.activities.AddCommandeActivity.delaiReglements;
import static com.sominfor.somisal_app.activities.AddCommandeActivity.modeReglements;
import static com.sominfor.somisal_app.activities.AddCommandeActivity.paysFacturationList;
import static com.sominfor.somisal_app.activities.AddCommandeActivity.uscomList;
import static com.sominfor.somisal_app.activities.LoginActivity.protocole;

public class ReglementActivity extends AppCompatActivity {
    MaterialButton BtnNext,BtnPrec;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, apiUrl01, apiUrl02, apiUrl03, utilisateurCosoc, utilisateurCoage;
    Client client;
    LieuVente lieuVente;
    Magasin magasin;
    Tournee tournee;
    Transport transport;
    Commande commande;
    Livreur livreur;
    Pays paysFacturation, paysLivraison;
    ModeReglement modeReglement, modeReglementNotSelected;
    DelaiReglement delaiReglement, delaiReglementNotSelected;
    Commercial commercial;
    ApiReceiverMethods apiReceiverMethods;
    MaterialBetterSpinner MbSpnComMoreg, MbSpnComDereg, MbSpnComUscom;
    TextInputEditText EdtComTxrem, EdtComTxesc, EdtComEcova, EdtComTelep, EdtComIdcor;

    List<ModeReglement> modeReglementList;
    List<DelaiReglement> delaiReglementList;
    List<Commercial> commercialList;

    static ReglementActivity reglementActivity;


    ModeReglementSpinnerAdapter modeReglementSpinnerAdapter;
    DelaiReglementSpinnerAdapter delaiReglementSpinnerAdapter;
    CommercialSpinnerAdapter commercialSpinnerAdapter;

    /**Nouvelle Instance**/
    public static ReglementActivity getInstance(){
        return  reglementActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reglement);
        /**Instanciation des widgets**/
        BtnNext = findViewById(R.id.BtnNext);
        BtnPrec = findViewById(R.id.BtnPrec);

        /**Instanciation de l'activité**/
        reglementActivity = this;

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
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Règlement");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EdtComTxrem = findViewById(R.id.EdtComTxrem);
        EdtComTxesc = findViewById(R.id.EdtComTxesc);
        EdtComEcova = findViewById(R.id.EdtComEcova);
        EdtComTelep = findViewById(R.id.EdtComTelep);
        EdtComIdcor = findViewById(R.id.EdtComIdcor);
        MbSpnComMoreg = findViewById(R.id.MbSpnComMoreg);
        MbSpnComDereg = findViewById(R.id.MbSpnComDereg);
        MbSpnComUscom = findViewById(R.id.MbSpnComUscom);

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
        apiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/parametre/allMoreg";
        apiUrl02 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/parametre/allDereg";
        apiUrl03 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/parametre/allUscom";
        /**Récupération des informations envoyées***/
        client = (Client) getIntent().getSerializableExtra("client");
        lieuVente = (LieuVente) getIntent().getSerializableExtra("lieuvente");
        magasin = (Magasin) getIntent().getSerializableExtra("magasin");
        tournee = (Tournee) getIntent().getSerializableExtra("tournee");
        transport = (Transport) getIntent().getSerializableExtra("transport");
        commande = (Commande) getIntent().getSerializableExtra("commande");
        livreur = (Livreur) getIntent().getSerializableExtra("livreur");
        paysFacturation = (Pays) getIntent().getSerializableExtra("pays");
        paysLivraison = (Pays) getIntent().getSerializableExtra("paysLivraison");

        /***Récupération de la liste des modes de reglement**/
        if (modeReglements == null){
            modeReglementList = apiReceiverMethods.recupererModeReglements(apiUrl01, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        }else {
            modeReglementList = modeReglements;
        }
        modeReglementSpinnerAdapter = new ModeReglementSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, modeReglementList);
        MbSpnComMoreg.setAdapter(modeReglementSpinnerAdapter);


        /**Récupération de la liste des delais de reglemet**/
        if (delaiReglements.isEmpty()){
            delaiReglementList = apiReceiverMethods.recupererDelaiReglements(apiUrl02, systemeAdresse, utilisateurLogin, utilisateurPassword,utilisateurCosoc, utilisateurCoage);
        }else{
            delaiReglementList = delaiReglements;
        }
        delaiReglementSpinnerAdapter = new DelaiReglementSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, delaiReglementList);
        MbSpnComDereg.setAdapter(delaiReglementSpinnerAdapter);

        if (uscomList.isEmpty()){
            commercialList = apiReceiverMethods.recupererCommerciaux(apiUrl03, systemeAdresse, utilisateurLogin, utilisateurPassword,utilisateurCosoc, utilisateurCoage);
        }else{
            commercialList = uscomList;
        }
        commercialSpinnerAdapter = new CommercialSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, commercialList);
        MbSpnComUscom.setAdapter(commercialSpinnerAdapter);


        /**Initialisation Délai de règlement **/
        if (!delaiReglementList.isEmpty()) {
            /**Initialisation délai de reglment**/
            delaiReglementNotSelected = new DelaiReglement();
            delaiReglementNotSelected.setCoDereg(client.getCliDereg());
            int spinnerPosition = delaiReglementList.indexOf(delaiReglementNotSelected);
            if (spinnerPosition!=-1)
            /**Set value to spinnerDelai*/
                MbSpnComDereg.setText(MbSpnComDereg.getAdapter().getItem(spinnerPosition).toString());
        }

        /**Initialisation Mode de règlement **/
        if (!modeReglementList.isEmpty()) {
            /**Initialisation délai de reglment**/
            modeReglementNotSelected = new ModeReglement();
            modeReglementNotSelected.setCoMoreg(client.getCliMoreg());
            int spinnerPosition = modeReglementList.indexOf(modeReglementNotSelected);
            /**Set value to spinnerMode*/
            if (spinnerPosition!=-1)
                MbSpnComMoreg.setText(MbSpnComMoreg.getAdapter().getItem(spinnerPosition).toString());
        }

        MbSpnComDereg.setOnItemClickListener((parent, view, position, id) -> delaiReglement = delaiReglementSpinnerAdapter.getItem(position));
        MbSpnComMoreg.setOnItemClickListener((parent, view, position, id) -> modeReglement = modeReglementSpinnerAdapter.getItem(position));
        MbSpnComUscom.setOnItemClickListener((parent, view, position, id) -> commercial = commercialSpinnerAdapter.getItem(position));

        /**Retour au précédent**/
        BtnPrec.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
        /**Bouton Suivant**/
        BtnNext.setOnClickListener(v -> {
            if (MbSpnComMoreg.length()!=0){
                if (MbSpnComDereg.length()!=0){
                    /*Taux remise**/
                    if (EdtComTxrem.getText().toString().equals("")){
                        commande.setComtxrem(0.00);
                    }else{
                        commande.setComtxrem(Double.valueOf(EdtComTxrem.getText().toString()));
                    }
                    /**Escompte**/
                    if (EdtComTxesc.getText().toString().equals("")){
                        commande.setComtxesc(0.00);
                    }else{
                        commande.setComtxesc(Double.valueOf(EdtComTxesc.getText().toString()));
                    }
                    /**Ecova*/
                    if (EdtComEcova.getText().toString().equals("")){
                        commande.setComecova(0.00);
                    }else{
                        commande.setComecova(Double.valueOf(EdtComEcova.getText().toString()));
                    }
                    Intent i = new Intent(getApplicationContext(), AddProduitCommandeActivity.class);
                    i.putExtra("client", client);
                    i.putExtra("lieuvente", lieuVente);
                    i.putExtra("magasin", magasin);
                    i.putExtra("commande", commande);
                    i.putExtra("tournee", tournee);
                    i.putExtra("transport", transport);
                    i.putExtra("livreur", livreur);
                    i.putExtra("pays", paysFacturation);
                    i.putExtra("paysLivraison", paysLivraison);
                    if (null == commercial){
                        Commercial commercialNotSelected = new Commercial();
                        commercialNotSelected.setCoUscom("");
                        i.putExtra("commercial", commercialNotSelected);
                    }else{
                        i.putExtra("commercial", commercial);
                    }

                    if (null == modeReglement){
                        i.putExtra("modeReglement", modeReglementNotSelected);
                    }else{
                        i.putExtra("modeReglement", modeReglement);
                    }

                    if (null == delaiReglement){
                        i.putExtra("delaiReglement", delaiReglementNotSelected);
                    }else{
                        i.putExtra("delaiReglement", delaiReglement);
                    }

                    finish();
                    startActivity(i);

                }else{
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL06), Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL07), Toast.LENGTH_LONG).show();
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