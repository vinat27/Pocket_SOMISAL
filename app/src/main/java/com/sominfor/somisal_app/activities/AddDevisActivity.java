package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.ClientSpinnerAdapter;
import com.sominfor.somisal_app.adapters.CommercialSpinnerAdapter;
import com.sominfor.somisal_app.adapters.DelaiReglementSpinnerAdapter;
import com.sominfor.somisal_app.adapters.LieuVenteSpinnerAdapter;
import com.sominfor.somisal_app.adapters.LivreurSpinnerAdapter;
import com.sominfor.somisal_app.adapters.MagasinSpinnerAdapter;
import com.sominfor.somisal_app.adapters.ModeReglementSpinnerAdapter;
import com.sominfor.somisal_app.fragments.DevisFragment;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Client;
import com.sominfor.somisal_app.handler.models.Commercial;
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
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
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

public class AddDevisActivity extends AppCompatActivity  {
    /**variables globales**/
    public static final String TAG = AddDevisActivity.class.getSimpleName();
    static AddDevisActivity activityDevisActivity;
    MaterialButton BtnValider;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, apiUrl01, apiUrl02, apiUrl03, apiUrl04, apiUrl05, apiUrl06, apiUrl07,utilisateurCosoc, utilisateurCoage;
    public RequestQueue rq;
    Client client;
    SearchableSpinner SsnDevCliRasoc;
    MaterialBetterSpinner MbSpnCliLieuv, MbSpnDevMag, MbSpnDevColiv, MbSpnDevMoreg, MbSpnDevDereg, MbSpnDevUscom;
    Float DevTxrem, DevTxesc;
    Double DevEcova;
    /**Listes - Lieu de vente, Client, Magasin***/
    List<LieuVente> lieuVentes;
    List<Magasin> magasins;
    List<Client> clients;
    List<Livreur> livreurs;
    List<DelaiReglement> delaiReglements;
    List<ModeReglement> modeReglements;
    List<Commercial> commercialList;
    /**Adapters**/
    LieuVenteSpinnerAdapter lieuVenteSpinnerAdapter;
    ClientSpinnerAdapter clientSpinnerAdapter;
    MagasinSpinnerAdapter magasinSpinnerAdapter;
    LivreurSpinnerAdapter livreurSpinnerAdapter;
    DelaiReglementSpinnerAdapter delaiReglementSpinnerAdapter;
    ModeReglementSpinnerAdapter modeReglementSpinnerAdapter;
    CommercialSpinnerAdapter commercialSpinnerAdapter;

    /**Classes*/
    LieuVente lieuVente;
    Magasin magasin;
    Livreur livreur, livreurNotSelected;
    ModeReglement modeReglement, modeReglementNotSelected;
    DelaiReglement delaiReglement, delaiReglementNotSelected;
    DelayedProgressDialog progressDialogInfo;
    Commercial commercial;

    /**Date Picker**/
    DatePickerDialog DpdialogDadev, DpdialogDaliv;
    TextInputEditText EdtDevdadev,EdtDevDaliv, EdtDevRfdev, EdtDevTxrem, EdtDevTxesc, EdtDevEcova;
    ApiReceiverMethods apiReceiverMethods;

    /**Nouvelle Instance**/
    public static AddDevisActivity getInstance(){
        return   activityDevisActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_devis);

        /**Gestion du menu d'action**/
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Nouveau devis");
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

        /**Instanciation de l'activité**/
        activityDevisActivity = this;

        serveurNodeController = new ServeurNodeController();
        progressDialogInfo = new DelayedProgressDialog();
        /**Récupération du serveur node**/
        serveurNode = serveurNodeController.getServeurNodeInfos();

        /**Initialisation de listes et de la requestQueue**/
        rq = Volley.newRequestQueue(getApplicationContext());
        clients = new ArrayList<>();
        lieuVentes = new ArrayList<>();
        magasins = new ArrayList<>();
        livreurs = new ArrayList<>();
        modeReglements = new ArrayList<>();
        delaiReglements = new ArrayList<>();
        commercialList = new ArrayList<>();
        apiReceiverMethods = new ApiReceiverMethods(getApplicationContext());
        DevTxesc = 0.0f;
        DevTxrem = 0.0f;
        DevEcova = 0.0;

        /**URL Récupération de la liste des clients**/
        apiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/allClient";
        apiUrl02 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/parametre/allLivth";
        apiUrl03 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/parametre/allComag";
        apiUrl04 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/parametre/allColiv";
        apiUrl05 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/parametre/allDereg";
        apiUrl06 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/parametre/allMoreg";
        apiUrl07 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/parametre/allUscom";

        /**Instanciation des widgets**/
        SsnDevCliRasoc = findViewById(R.id.MbSpnCliRasoc);
        BtnValider = findViewById(R.id.BtnValider);
        MbSpnCliLieuv = findViewById(R.id.MbSpnCliLieuv);
        MbSpnDevMag = findViewById(R.id.MbSpnDevMag);
        MbSpnDevColiv = findViewById(R.id.MbSpnDevColiv);
        EdtDevdadev = findViewById(R.id.EdtDevDadev);
        EdtDevDaliv = findViewById(R.id.EdtDevDaliv);
        MbSpnDevMoreg = findViewById(R.id.MbSpnDevMoreg);
        MbSpnDevDereg = findViewById(R.id.MbSpnDevDereg);
        MbSpnDevUscom = findViewById(R.id.MbSpnDevUscom);
        EdtDevRfdev = findViewById(R.id.EdtDevRfdev);
        EdtDevTxrem = findViewById(R.id.EdtDevTxrem);
        EdtDevTxesc = findViewById(R.id.EdtDevTxesc);
        EdtDevEcova = findViewById(R.id.EdtDevEcova);

        /**Récupération de la liste de clients**/
        if (DevisFragment.clientListDevis == null){
            clients = apiReceiverMethods.recupererListeClients(apiUrl01,systemeAdresse,utilisateurLogin,utilisateurPassword,utilisateurCosoc, utilisateurCoage);
        }else{
            clients = DevisFragment.clientListDevis;
        }
        clientSpinnerAdapter = new ClientSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, clients);
        SsnDevCliRasoc.setAdapter(clientSpinnerAdapter);

        /***Récupération de la liste des lieux de vente**/
        lieuVentes = apiReceiverMethods.recupererListeLieuv(apiUrl02,systemeAdresse,utilisateurLogin,utilisateurPassword,utilisateurCosoc, utilisateurCoage);
        lieuVenteSpinnerAdapter = new LieuVenteSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, lieuVentes);
        MbSpnCliLieuv.setAdapter(lieuVenteSpinnerAdapter);

        /**Récupération de la liste des magasins**/
        magasins = apiReceiverMethods.recupererListeMagasins(apiUrl03, systemeAdresse, utilisateurLogin, utilisateurPassword,utilisateurCosoc, utilisateurCoage);
        magasinSpinnerAdapter = new MagasinSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, magasins);
        MbSpnDevMag.setAdapter(magasinSpinnerAdapter);

        /**Récupération de la liste des livreurs**/
        if (DevisFragment.livreurListDevis.isEmpty()){
            livreurs = apiReceiverMethods.recupererListeLivreurs(apiUrl04, systemeAdresse, utilisateurLogin, utilisateurPassword,utilisateurCosoc, utilisateurCoage);
        }else{
            livreurs = DevisFragment.livreurListDevis;
        }
        livreurSpinnerAdapter = new LivreurSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, livreurs);
        MbSpnDevColiv.setAdapter(livreurSpinnerAdapter);

        /**Récupération de la liste des modes de reglemet**/
        if (DevisFragment.modeReglementListDevis.isEmpty()){
            modeReglements = apiReceiverMethods.recupererModeReglements(apiUrl06, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        }else{
            modeReglements = DevisFragment.modeReglementListDevis;
        }
        modeReglementSpinnerAdapter = new ModeReglementSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, modeReglements);
        MbSpnDevMoreg.setAdapter(modeReglementSpinnerAdapter);

        /**Récupération de la liste des delais de reglemet**/
        if (DevisFragment.modeReglementListDevis.isEmpty()){
            delaiReglements = apiReceiverMethods.recupererDelaiReglements(apiUrl05, systemeAdresse, utilisateurLogin, utilisateurPassword,utilisateurCosoc, utilisateurCoage);
        }else{
            delaiReglements = DevisFragment.delaiReglementListDevis;
        }
        delaiReglementSpinnerAdapter = new DelaiReglementSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, delaiReglements);
        MbSpnDevDereg.setAdapter(delaiReglementSpinnerAdapter);

        /**Récupération de la liste des commerciaux**/
        commercialList = apiReceiverMethods.recupererCommerciaux(apiUrl07, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        commercialSpinnerAdapter = new CommercialSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, commercialList);
        MbSpnDevUscom.setAdapter(commercialSpinnerAdapter);

        /**Initialiser les dates de devis et de livraison à la date courante**/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        EdtDevdadev.setText(sdf.format(new Date()));
        EdtDevDaliv.setText(sdf.format(new Date()));
        /**Champ de date, selection de date  DatePicker**/
        EdtDevdadev.setOnClickListener(v -> DpdialogDadev.show());
        /**Marquer la date selectionnée dans le champ date devis**/
        setDateOnEdtDevDadev();
        EdtDevDaliv.setOnClickListener(v -> {
            DpdialogDaliv.show();
        });
        /**Marquer la date selectionnée dans le champ date livraison**/
        setDateOnEdtDevDaliv();

        /**Controles de selections**/
        /**selection Client**/
        SsnDevCliRasoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /**Récupération Client**/
                client = clientSpinnerAdapter.getItem(position);
                /**Initialiser des listes **/
                if (!client.getCliColiv().equals("")) {
                    /**Initialisation livreur**/
                    livreurNotSelected = new Livreur();
                    livreurNotSelected.setLivColiv(client.getCliColiv());
                    livreurNotSelected.setLivliliv(client.getCliLiliv());
                    int spinnerPosition = livreurs.indexOf(livreurNotSelected);
                    if (spinnerPosition != -1)
                    /**Set value to spinnerLivreur*/
                    MbSpnDevColiv.setText(MbSpnDevColiv.getAdapter().getItem(spinnerPosition).toString());
                }
                /**Initialisation Délai de règlement **/
                if (!client.getCliDereg().equals("")) {
                    /**Initialisation délai de reglment**/
                    delaiReglementNotSelected = new DelaiReglement();
                    delaiReglementNotSelected.setCoDereg(client.getCliDereg());
                    int spinnerPosition = delaiReglements.indexOf(delaiReglementNotSelected);
                    if (spinnerPosition!=-1)
                    /**Set value to spinnerDelai*/
                    MbSpnDevDereg.setText(MbSpnDevDereg.getAdapter().getItem(spinnerPosition).toString());
                }

                /**Initialisation Mode de règlement **/
                if (!client.getCliMoreg().equals("")) {
                    /**Initialisation délai de reglment**/
                    modeReglementNotSelected = new ModeReglement();
                    modeReglementNotSelected.setCoMoreg(client.getCliMoreg());
                    int spinnerPosition = modeReglements.indexOf(modeReglementNotSelected);
                    /**Set value to spinnerMode*/
                    if (spinnerPosition!=-1)
                    MbSpnDevMoreg.setText(MbSpnDevMoreg.getAdapter().getItem(spinnerPosition).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /**
         * Selection Lieu de vente**/
        MbSpnCliLieuv.setOnItemClickListener((adapterView, view, i, l) -> {
            lieuVente = lieuVenteSpinnerAdapter.getItem(i);
        });

        /**
         * Selection Magasin**/
        MbSpnDevMag.setOnItemClickListener((adapterView, view, i, l) -> {
            magasin = magasinSpinnerAdapter.getItem(i);
        });

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

        /**
         * Selection Commercial**/
        MbSpnDevUscom.setOnItemClickListener((parent, view, position, id) -> commercial = commercialSpinnerAdapter.getItem(position));

        /**A la validation***/
        BtnValider.setOnClickListener(v -> {
            if (SsnDevCliRasoc.getSelectedItem()!=null){
                if (MbSpnCliLieuv.length() !=0){
                    if (MbSpnDevMag.length() != 0){
                        if (EdtDevDaliv.getText().length() != 0){
                            if (MbSpnDevMoreg.length()!=0){
                                if (MbSpnDevDereg.length()!=0){
                                    /**Comparaison des dates**/
                                    try {
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                        Date currentDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
                                        Date dateDevis = simpleDateFormat.parse(EdtDevdadev.getText().toString());
                                        Date dateDevisLivraison = simpleDateFormat.parse(EdtDevDaliv.getText().toString());
                                        assert dateDevis != null;
                                        if (dateDevis.compareTo(currentDate) >= 0){
                                           /**Date devis correcte - Comparaison à la date de livraison**/
                                           if (dateDevisLivraison.compareTo(dateDevis) >= 0){
                                               Devis devis = new Devis();
                                               devis.setDevDadev(EdtDevdadev.getText().toString());
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
                                               /**Remise globale**/
                                               if (!EdtDevTxrem.getText().toString().equals(""))
                                                   DevTxrem = Float.parseFloat(Objects.requireNonNull(EdtDevTxrem.getText()).toString());
                                               /**Escompte**/
                                               if (!EdtDevTxesc.getText().toString().equals(""))
                                                   DevTxesc = Float.parseFloat(Objects.requireNonNull(EdtDevTxesc.getText()).toString());

                                               /**Eco-Participation*/
                                               if (!EdtDevEcova.getText().toString().equals(""))
                                                   DevEcova = Double.parseDouble(Objects.requireNonNull(EdtDevEcova.getText()).toString());

                                               /***Commercial**/
                                               if (null == commercial){
                                                   devis.setDevUscom("");
                                               }else{
                                                   devis.setDevUscom(commercial.getCoUscom());
                                               }
                                               devis.setDevTxrem(DevTxrem);
                                               devis.setDevTxesc(DevTxesc);
                                               devis.setDevEcova(DevEcova);
                                               Intent i = new Intent(AddDevisActivity.this, AddProduitDevisActivity.class);
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
                                        }else{
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL08), Toast.LENGTH_LONG).show();
                                        }
                                    }catch (ParseException e){
                                        e.printStackTrace();
                                    }
                                }else {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL06), Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL07), Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL01), Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL02), Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL03), Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL04), Toast.LENGTH_LONG).show();
            }

        });

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


    /**Attribuer la date selectionnée au champ de date**/
    public void setDateOnEdtDevDadev(){
        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DpdialogDadev = new DatePickerDialog(AddDevisActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            EdtDevdadev.setText(dateFormat.format(newDate.getTime()));
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    /**Attribuer la date selectionnée au champ de date de livraison**/
    public void setDateOnEdtDevDaliv(){
        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DpdialogDaliv = new DatePickerDialog(AddDevisActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            EdtDevDaliv.setText(dateFormat.format(newDate.getTime()));
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


}