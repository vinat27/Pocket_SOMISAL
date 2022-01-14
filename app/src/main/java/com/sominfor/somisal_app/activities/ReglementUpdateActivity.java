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
import com.sominfor.somisal_app.adapters.CommercialSpinnerAdapter;
import com.sominfor.somisal_app.adapters.DelaiReglementSpinnerAdapter;
import com.sominfor.somisal_app.adapters.ModeReglementSpinnerAdapter;
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
import static com.sominfor.somisal_app.activities.AddCommandeActivity.uscomList;
import static com.sominfor.somisal_app.activities.LoginActivity.protocole;
import static com.sominfor.somisal_app.activities.UpdateCommandeActivity.delaiReglementsUpdCde;
import static com.sominfor.somisal_app.activities.UpdateCommandeActivity.modeReglementsUpdCde;
import static com.sominfor.somisal_app.activities.UpdateCommandeActivity.uscomListUpdCde;

public class ReglementUpdateActivity extends AppCompatActivity {
    MaterialButton BtnNext,BtnPrec;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, apiUrl01, apiUrl02, apiUrl03, utilisateurCosoc, utilisateurCoage;
    Commande commande;
    ModeReglement modeReglement, modeReglementNotSelected;
    DelaiReglement delaiReglement, delaiReglementNotSelected;
    Commercial commercial, commercialNotSelected;
    ApiReceiverMethods apiReceiverMethods;
    MaterialBetterSpinner MbSpnComMoreg, MbSpnComDereg, MbSpnComUscom;
    TextInputEditText EdtComTxrem, EdtComTxesc, EdtComEcova, EdtComTelep, EdtComIdcor;

    List<ModeReglement> modeReglementList;
    List<DelaiReglement> delaiReglementList;
    List<Commercial> commercialList;

    static ReglementUpdateActivity reglementUpdateActivity;


    ModeReglementSpinnerAdapter modeReglementSpinnerAdapter;
    DelaiReglementSpinnerAdapter delaiReglementSpinnerAdapter;
    CommercialSpinnerAdapter commercialSpinnerAdapter;

    /**Nouvelle Instance**/
    public static ReglementUpdateActivity getInstance(){
        return  reglementUpdateActivity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reglement_update);
        /**Instanciation des widgets**/
        BtnNext = findViewById(R.id.BtnNext);
        BtnPrec = findViewById(R.id.BtnPrec);

        /**Instanciation de l'activité**/
        reglementUpdateActivity = this;

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
        commande = (Commande) getIntent().getSerializableExtra("commande");
        commercial = new Commercial();
        /**Initialisation des Edittext*/
        EdtComTxrem.setText(String.valueOf(commande.getComtxrem()));
        EdtComTxesc.setText(String.valueOf(commande.getComtxesc()));
        EdtComEcova.setText(String.valueOf(commande.getComecova()));

        /***Récupération de la liste des modes de reglement**/
        if (modeReglementsUpdCde == null){
            modeReglementList = apiReceiverMethods.recupererModeReglements(apiUrl01, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        }else {
            modeReglementList = modeReglementsUpdCde;
        }
        modeReglementSpinnerAdapter = new ModeReglementSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, modeReglementList);
        MbSpnComMoreg.setAdapter(modeReglementSpinnerAdapter);


        /**Récupération de la liste des delais de reglemet**/
        if (delaiReglementsUpdCde.isEmpty()){
            delaiReglementList = apiReceiverMethods.recupererDelaiReglements(apiUrl02, systemeAdresse, utilisateurLogin, utilisateurPassword,utilisateurCosoc, utilisateurCoage);
        }else{
            delaiReglementList = delaiReglementsUpdCde;
        }
        delaiReglementSpinnerAdapter = new DelaiReglementSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, delaiReglementList);
        MbSpnComDereg.setAdapter(delaiReglementSpinnerAdapter);

        if (uscomListUpdCde.isEmpty()){
            commercialList = apiReceiverMethods.recupererCommerciaux(apiUrl03, systemeAdresse, utilisateurLogin, utilisateurPassword,utilisateurCosoc, utilisateurCoage);
        }else{
            commercialList = uscomListUpdCde;
        }
        commercialSpinnerAdapter = new CommercialSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, commercialList);
        MbSpnComUscom.setAdapter(commercialSpinnerAdapter);

        /**Initialisation Délai de règlement **/
        if (!delaiReglementList.isEmpty()) {
            /**Initialisation délai de reglment**/
            delaiReglementNotSelected = new DelaiReglement();
            delaiReglementNotSelected.setCoDereg(commande.getComdereg());
            int spinnerPosition = delaiReglementList.indexOf(delaiReglementNotSelected);
            if (spinnerPosition!=-1)
            /**Set value to spinnerDelai*/
                MbSpnComDereg.setText(MbSpnComDereg.getAdapter().getItem(spinnerPosition).toString());
        }

        /**Initialisation Commercial **/
        if (!commercialList.isEmpty()) {
            /**Initialisation délai de reglment**/
            commercialNotSelected = new Commercial();
            commercialNotSelected.setCoUscom(commande.getComuscom());
            int spinnerPosition = commercialList.indexOf(commercialNotSelected);
            if (spinnerPosition!=-1)
            /**Set value to spinnerDelai*/
                MbSpnComUscom.setText(MbSpnComUscom.getAdapter().getItem(spinnerPosition).toString());
        }

        /**Initialisation Mode de règlement **/
        if (!modeReglementList.isEmpty()) {
            /**Initialisation délai de reglment**/
            modeReglementNotSelected = new ModeReglement();
            modeReglementNotSelected.setCoMoreg(commande.getCommoreg());
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
                    if (null == modeReglement){
                       commande.setCommoreg(modeReglementNotSelected.getCoMoreg());
                    }else{
                       commande.setCommoreg(modeReglement.getCoMoreg());
                    }

                    if (null == delaiReglement){
                       commande.setComdereg(delaiReglementNotSelected.getCoDereg());
                    }else{
                        commande.setComdereg(delaiReglement.getCoDereg());
                    }
                    if (null == commercial){
                        commande.setComuscom("");
                    }else{
                        commande.setComuscom(commercial.getCoUscom());
                    }
                    Intent i = new Intent(getApplicationContext(), UpdateProduitCommandeActivity.class);
                    i.putExtra("commande", commande);
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