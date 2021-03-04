package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.DelaiReglementSpinnerAdapter;
import com.sominfor.somisal_app.adapters.LivreurSpinnerAdapter;
import com.sominfor.somisal_app.adapters.ModeReglementSpinnerAdapter;
import com.sominfor.somisal_app.fragments.ClientFragment;
import com.sominfor.somisal_app.fragments.DevisFragment;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Client;
import com.sominfor.somisal_app.handler.models.DelaiReglement;
import com.sominfor.somisal_app.handler.models.Devis;
import com.sominfor.somisal_app.handler.models.LieuVente;
import com.sominfor.somisal_app.handler.models.Livreur;
import com.sominfor.somisal_app.handler.models.Magasin;
import com.sominfor.somisal_app.handler.models.ModeReglement;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.utils.ApiReceiverMethods;
import com.sominfor.somisal_app.utils.UserSessionManager;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.sominfor.somisal_app.activities.LoginActivity.protocole;
import static com.sominfor.somisal_app.fragments.DevisFragment.livreurListDevis;
//TODO Set values to MoReg and Dereg - Recevoir Nacli, Lieuv avec les espaces

public class UpdateDevisActivity extends AppCompatActivity {

    TextView TxtClirasoc, TxtDevLieuv, TxtDevLimag, TxtDevLiliv, TxtDevDaliv, TxtDevVadev, TxtDevNudev;
    MaterialButton BtnValider;
    ServeurNodeController serveurNodeController;
    String systemeAdresse, utilisateurLogin, utilisateurPassword,utilisateurCosoc, utilisateurCoage, apiUrl01,apiUrl02,apiUrl03;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    List<Livreur> livreurs;
    List<ModeReglement> modeReglements;
    List<DelaiReglement> delaiReglements;
    TextInputEditText EdtDevRfdev, EdtDevDaliv;
    MaterialBetterSpinner MbSpnDevColiv, MbSpnDevMoreg, MbSpnDevDereg;
    Devis devis;
    ApiReceiverMethods apiReceiverMethods;
    LivreurSpinnerAdapter livreurSpinnerAdapter;
    ModeReglementSpinnerAdapter modeReglementSpinnerAdapter;
    DelaiReglementSpinnerAdapter delaiReglementSpinnerAdapter;
    DatePickerDialog DpdialogDaliv;
    Livreur livreurNotSelected, livreur;
    ModeReglement modeReglement,modeReglementNotSelected;
    DelaiReglement delaiReglement, delaiReglementNotSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_devis);

        /**Instanciation des widgets**/
        TxtClirasoc = findViewById(R.id.TxtClirasoc);
        TxtDevLieuv = findViewById(R.id.TxtDevLieuv);
        TxtDevNudev = findViewById(R.id.TxtDevNudev);
        TxtDevLimag = findViewById(R.id.TxtDevLimag);
        TxtDevLiliv = findViewById(R.id.TxtDevLiliv);
        TxtDevVadev = findViewById(R.id.TxtDevVadev);
        TxtDevDaliv = findViewById(R.id.TxtDevDaliv);
        BtnValider = findViewById(R.id.BtnTerminer);
        EdtDevDaliv = findViewById(R.id.EdtDevDaliv);
        EdtDevRfdev = findViewById(R.id.EdtDevRfdev);
        MbSpnDevColiv = findViewById(R.id.MbSpnDevColiv);
        MbSpnDevMoreg = findViewById(R.id.MbSpnDevMoreg);
        MbSpnDevDereg = findViewById(R.id.MbSpnDevDereg);

        /**Gestion du menu d'action**/
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Actualisation devis");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /**Effacer l'ombre sous l'actionBar**/
        getSupportActionBar().setElevation(0);

        /**Récupération de session utilisateur**/
        utilisateur = UserSessionManager.getInstance(getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();
        utilisateurCosoc = utilisateur.getUtilisateurCosoc();
        utilisateurCoage = utilisateur.getUtilisateurCoage();
        apiReceiverMethods = new ApiReceiverMethods(getApplicationContext());
        serveurNodeController = new ServeurNodeController();
        /**Récupération du serveur node**/
        serveurNode = serveurNodeController.getServeurNodeInfos();
        apiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/parametre/allColiv";
        apiUrl02 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/parametre/allMoreg";
        apiUrl03 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/parametre/allDereg";

        livreurs = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        devis = (Devis) bundle.getSerializable("devis");
        @SuppressLint("DefaultLocale") String vadev = String.format("%.2f", devis.getDevVadev())+" "+devis.getDevComon().trim();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fromUser = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String DevDadevFormat = "";
        String DevDalivFormat = "";

        try {
            DevDadevFormat = fromUser.format(Objects.requireNonNull(myFormat.parse(devis.getDevDadev())));
            DevDalivFormat = fromUser.format(Objects.requireNonNull(myFormat.parse(devis.getDevDaliv())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /**Set Data to Textviews**/
        TxtClirasoc.setText(devis.getCliRasoc());
        TxtDevLieuv.setText(devis.getDevLieuv());
        TxtDevNudev.setText(devis.getDevNudev());
        TxtDevLimag.setText(devis.getDevLimag());
        TxtDevLiliv.setText(devis.getDevColiv());
        TxtDevDaliv.setText(DevDalivFormat);
        TxtDevVadev.setText(vadev);
        EdtDevRfdev.setText(devis.getDevRfdev());
        EdtDevDaliv.setText(devis.getDevDaliv());

        EdtDevDaliv.setOnClickListener(v -> {
            DpdialogDaliv.show();
        });
        /**Marquer la date selectionnée dans le champ date livraison**/
        setDateOnEdtDevDaliv();

        /**Vérifier si liste client est vide sinon récupérer***/
        if(livreurListDevis ==null){
            /**Récupération de la liste de clients**/
            livreurs = apiReceiverMethods.recupererListeLivreurs(apiUrl01,systemeAdresse,utilisateurLogin,utilisateurPassword,utilisateurCosoc, utilisateurCoage);
        }else{
            livreurs = livreurListDevis;
        }
        livreurSpinnerAdapter = new LivreurSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, livreurs);
        MbSpnDevColiv.setAdapter(livreurSpinnerAdapter);

        /**Récupération de la liste des modes de reglemet**/
        if (DevisFragment.modeReglementListDevis.isEmpty()){
            modeReglements = apiReceiverMethods.recupererModeReglements(apiUrl02, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        }else{
            modeReglements = DevisFragment.modeReglementListDevis;
        }
        modeReglementSpinnerAdapter = new ModeReglementSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, modeReglements);
        MbSpnDevMoreg.setAdapter(modeReglementSpinnerAdapter);

        /**Récupération de la liste des delais de reglemet**/
        if (DevisFragment.modeReglementListDevis.isEmpty()){
            delaiReglements = apiReceiverMethods.recupererDelaiReglements(apiUrl03, systemeAdresse, utilisateurLogin, utilisateurPassword,utilisateurCosoc, utilisateurCoage);
        }else{
            delaiReglements = DevisFragment.delaiReglementListDevis;
        }
        delaiReglementSpinnerAdapter = new DelaiReglementSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, delaiReglements);
        MbSpnDevDereg.setAdapter(delaiReglementSpinnerAdapter);

        /**Set values to livreur**/
        if (!livreurs.isEmpty()){
            /**Initialisation livreur**/
            livreurNotSelected = new Livreur();
            livreurNotSelected.setLivColiv(devis.getDevColiv());
            livreurNotSelected.setLivliliv(devis.getDevliliv());
            int spinnerPosition = livreurs.indexOf(livreurNotSelected);
            if (spinnerPosition != -1)
            /**Set value to spinnerLivreur*/
                MbSpnDevColiv.setText(MbSpnDevColiv.getAdapter().getItem(spinnerPosition).toString());
        }

        /**Set values to ModeRegelemnt**/
        if (!modeReglements.isEmpty()){
            modeReglementNotSelected = new ModeReglement();
            modeReglementNotSelected.setCoMoreg(devis.getDevMoreg());
            int spinnerPosition = modeReglements.indexOf(modeReglementNotSelected);
            if (spinnerPosition!=-1)
                MbSpnDevMoreg.setText(MbSpnDevMoreg.getAdapter().getItem(spinnerPosition).toString());
        }

        /**Set Value to Delai*/
        if (!delaiReglements.isEmpty()){
            delaiReglementNotSelected = new DelaiReglement();
            delaiReglementNotSelected.setCoDereg(devis.getDevDereg());
            int spinnerPosition = delaiReglements.indexOf(delaiReglementNotSelected);
            if (spinnerPosition!=-1)
                MbSpnDevDereg.setText(MbSpnDevDereg.getAdapter().getItem(spinnerPosition).toString());
        }


        /**
         * Selection Livreur*/
        MbSpnDevColiv.setOnItemClickListener((adapterView, view, i, l) -> {
            livreur = livreurSpinnerAdapter.getItem(i);
        });

        /**
         * Sélection Règlement
         */
        MbSpnDevMoreg.setOnItemClickListener((parent, view, position, id) -> modeReglement = modeReglementSpinnerAdapter.getItem(position));
        /**
         * Selection Delai Règlement
         */
        MbSpnDevDereg.setOnItemClickListener((parent, view, position, id) -> delaiReglement = delaiReglementSpinnerAdapter.getItem(position));

        /**Au clic du bouton suivant***/
        BtnValider.setOnClickListener(v -> {
                    if (MbSpnDevColiv.length() !=0){
                        if (EdtDevDaliv.getText().length() != 0){
                            try {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                Date currentDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
                                Date dateDevis = simpleDateFormat.parse(devis.getDevDadev());
                                Date dateDevisLivraison = simpleDateFormat.parse(EdtDevDaliv.getText().toString());
                                assert dateDevis != null;
                                    /**Date devis correcte - Comparaison à la date de livraison**/
                                    if (dateDevisLivraison.compareTo(dateDevis) >= 0){

                                        devis.setDevRfdev(EdtDevRfdev.getText().toString());
                                        devis.setDevDaliv(EdtDevDaliv.getText().toString());
                                        /**Vérification si élément sélectionné ou pas**/
                                        if (null == modeReglement){
                                            devis.setDevMoreg(modeReglementNotSelected.getCoMoreg());
                                        }else{
                                            devis.setDevMoreg(modeReglement.getCoMoreg());
                                        }
                                        /**Vérification si délai selectionné vide ou pas**/
                                        if (null == delaiReglement){
                                            devis.setDevDereg(delaiReglementNotSelected.getCoDereg());
                                        }else{
                                            devis.setDevDereg(delaiReglement.getCoDereg());
                                        }
                                        /***Set client values**/
                                        Client client = new Client();
                                        client.setCliNucli(devis.getDevNucli());
                                        client.setCliNacli(devis.getDevNacli());
                                        client.setCliLiComon(devis.getDevComon());
                                        /**Set Value to lieuv**/
                                        LieuVente lieuVente = new LieuVente();
                                        lieuVente.setColieuv(devis.getDevColieuv());
                                        lieuVente.setLilieuv(devis.getDevLieuv());

                                        /**Magasin**/
                                        Magasin magasin = new Magasin();
                                        magasin.setMagcomag(devis.getDevComag());
                                        magasin.setMaglimag(devis.getDevLimag());

                                        Intent i = new Intent(UpdateDevisActivity.this, UpdateProduitDevisActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        i.putExtra("client", client);
                                        i.putExtra("Lieuvente", lieuVente);
                                        if (null == livreur){
                                            i.putExtra("Livreur", livreurNotSelected);
                                        }else{
                                            i.putExtra("Livreur", livreur);
                                        }
                                        i.putExtra("Magasin", magasin);
                                        i.putExtra("devis", devis);
                                        startActivity(i);
                                    }else {
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL09), Toast.LENGTH_LONG).show();
                                    }

                            }catch (ParseException e){
                                e.printStackTrace();
                            }

                        } else{
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL01), Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL11), Toast.LENGTH_LONG).show();
                    }
        });
    }

    /**Attribuer la date selectionnée au champ de date de livraison**/
    public void setDateOnEdtDevDaliv(){
        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DpdialogDaliv = new DatePickerDialog(UpdateDevisActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            EdtDevDaliv.setText(dateFormat.format(newDate.getTime()));
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}