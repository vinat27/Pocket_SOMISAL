package com.sominfor.somisal_app.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.ClientSpinnerAdapter;
import com.sominfor.somisal_app.adapters.LieuVenteSpinnerAdapter;
import com.sominfor.somisal_app.adapters.LivreurSpinnerAdapter;
import com.sominfor.somisal_app.adapters.MagasinSpinnerAdapter;
import com.sominfor.somisal_app.adapters.TourneeSpinnerAdapter;
import com.sominfor.somisal_app.adapters.TransportSpinnerAdapter;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Client;
import com.sominfor.somisal_app.handler.models.Commande;
import com.sominfor.somisal_app.handler.models.Commercial;
import com.sominfor.somisal_app.handler.models.DelaiLivraison;
import com.sominfor.somisal_app.handler.models.DelaiReglement;
import com.sominfor.somisal_app.handler.models.GestionParametre;
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
import com.sominfor.somisal_app.utils.AsyncTaskExecutorService;
import com.sominfor.somisal_app.utils.UserSessionManager;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.sominfor.somisal_app.activities.LoginActivity.protocole;
import static com.sominfor.somisal_app.fragments.CommandeFragment.clientListCde;
import static com.sominfor.somisal_app.fragments.CommandeFragment.delaiLivraisons;
import static com.sominfor.somisal_app.fragments.CommandeFragment.livreurListCdeFragment;
import static com.sominfor.somisal_app.fragments.CommandeFragment.magasinsCdeFragment;
import static com.sominfor.somisal_app.fragments.HomeFragment.gestionParametresHome;

public class AddCommandeActivity extends AppCompatActivity {
    public static final String TAG = AddCommandeActivity.class.getSimpleName();
    /***Liste des spinners**/
    List<Client> clientList;
    List<LieuVente> lieuVentes;
    List<Magasin> magasins;
    List<Tournee> tournees;
    List<Livreur> livreurs;
    List<Transport> transports;
    List<GestionParametre> gestionParametres;
    public static List<Pays> paysFacturationList;
    public static List<DelaiReglement> delaiReglements;
    public static List<ModeReglement> modeReglements;
    public static List<Commercial> uscomList;
    List<DelaiLivraison> delaiLivraisonList;
    /***Adapters**/
    ClientSpinnerAdapter clientSpinnerAdapter;
    LieuVenteSpinnerAdapter lieuVenteSpinnerAdapter;
    MagasinSpinnerAdapter magasinSpinnerAdapter;
    TourneeSpinnerAdapter tourneeSpinnerAdapter;
    LivreurSpinnerAdapter livreurSpinnerAdapter;
    TransportSpinnerAdapter transportSpinnerAdapter;
    /**
     * Widgets
     **/
    SearchableSpinner SsnComCliRasoc;
    MaterialButton BtnNext;
    MaterialBetterSpinner MbSpnCliLieuv, MbSpnComComag, MbSpnComCotrn, MbSpnComColiv, MbSpnComCotrp;
    TextInputEditText EdtComDacom, EdtComDaliv, EdtComNamar;
    DatePickerDialog DpComDacom, DpComDaliv;
    TextView TxtDlvlv;

    /**
     * Classes d'Objets
     **/
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    Client client,clientInfos;
    LieuVente lieuVente;
    Magasin magasin, magasinNotSelected;
    Tournee tournee, tourneeNotSelected;
    Livreur livreur, livreurNotSelected;
    Transport transport, transportNotSelected;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, apiUrl01, apiUrl02, apiUrl03, apiUrl04, apiUrl05, apiUrl06, apiUrl07, apiUrl08, apiUrl09, apiUrl10, apiUrl11, apiUrl12, apiUrl13, gszon, gslvr, gstrn, utilisateurCosoc, utilisateurCoage;
    Commande commande;
    Pays pays;
    public RequestQueue rq;
    ApiReceiverMethods apiReceiverMethods;
    DelayedProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_commande);

        /****Contrôle de sessions utilisateur**/
        if (!UserSessionManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        /**Controle orientation***/
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        /**Gestion du menu d'action**/
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Nouvelle Commande");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**Récupération de session utilisateur**/
        utilisateur = UserSessionManager.getInstance(getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();
        utilisateurCosoc = utilisateur.getUtilisateurCosoc();
        utilisateurCoage = utilisateur.getUtilisateurCoage();
        serveurNodeController = new ServeurNodeController();
        /**Récupération du serveur node**/
        serveurNode = serveurNodeController.getServeurNodeInfos();

        /**Initialisation de listes et de la requestQueue**/
        rq = Volley.newRequestQueue(getApplicationContext());
        clientList = new ArrayList<>();
        lieuVentes = new ArrayList<>();
        delaiLivraisonList = new ArrayList<>();
        apiReceiverMethods = new ApiReceiverMethods(getApplicationContext());
        magasins = new ArrayList<>();
        paysFacturationList = new ArrayList<>();
        delaiReglements = new ArrayList<>();
        modeReglements = new ArrayList<>();
        uscomList = new ArrayList<>();
        gestionParametres = new ArrayList<>();
        tourneeNotSelected = new Tournee();
        magasinNotSelected = new Magasin();
        progressDialog = new DelayedProgressDialog();


        /**URL Récupération de la liste des clients**/
        apiUrl01 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/client/allClientSansRgp";
        apiUrl02 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allLivth";
        apiUrl03 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allComag";
        apiUrl04 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allCotrn";
        apiUrl05 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allColiv";
        apiUrl06 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allCotrp";
        apiUrl07 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allPays";
        apiUrl08 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allMoreg";
        apiUrl09 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allDereg";
        apiUrl10 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allUscom";
        apiUrl11 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allChoixBySociete";
        apiUrl12 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allDlv";
        apiUrl13 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/client/infosByClient";

        /**Instanciation des widgets**/
        SsnComCliRasoc = findViewById(R.id.MbSpnComRasoc);
        BtnNext = findViewById(R.id.BtnNext);
        MbSpnCliLieuv = findViewById(R.id.MbSpnCliLieuv);
        MbSpnComComag = findViewById(R.id.MbSpnComComag);
        MbSpnComCotrn = findViewById(R.id.MbSpnComCotrn);
        MbSpnComColiv = findViewById(R.id.MbSpnComColiv);
        MbSpnComCotrp = findViewById(R.id.MbSpnComCotrp);
        EdtComDaliv = findViewById(R.id.EdtComDaliv);
        EdtComNamar = findViewById(R.id.EdtComNamar);
        TxtDlvlv = findViewById(R.id.TxtDlvlv);

        /**Récupération des données de gestion paramètres**/
        if(gestionParametresHome.isEmpty()){
            gestionParametres = apiReceiverMethods.recupererGestParam(apiUrl11,systemeAdresse,utilisateurLogin,utilisateurPassword,utilisateurCosoc, utilisateurCoage);
        }else{
            gestionParametres = gestionParametresHome;
        }
        /**Gestion de zone géographie**/
        gszon = gestionParametres.get(0).getDatas();
        /**Gestion de tournée**/
        gstrn = gestionParametres.get(2).getDatas();
        /**Gestion de livreur**/
        gslvr = gestionParametres.get(1).getDatas();

        /**Gestion tournée**/
        if (gstrn.equals("N")){
            MbSpnComCotrn.setVisibility(View.GONE);
        }

        /**Gestion livreur**/
        if (gslvr.equals("N")){
            MbSpnComColiv.setVisibility(View.GONE);
        }
        /**Récupération de la liste de clients**/
        clientList = apiReceiverMethods.recupererListeClientsSansRegroupeur(apiUrl01, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        clientSpinnerAdapter = new ClientSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, clientList);
        SsnComCliRasoc.setAdapter(clientSpinnerAdapter);



        if (livreurListCdeFragment.size()==0){
            livreurs = apiReceiverMethods.recupererListeLivreurs(apiUrl05, systemeAdresse, utilisateurLogin, utilisateurPassword,  utilisateurCosoc, utilisateurCoage);
        }else{
            livreurs = livreurListCdeFragment;
        }
        /**Récupération de la liste des livreurs**/
        livreurSpinnerAdapter = new LivreurSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, livreurs);
        MbSpnComColiv.setAdapter(livreurSpinnerAdapter);

        /**Récupération de la liste des magasins**/
        if (magasinsCdeFragment.isEmpty()){
            magasins = apiReceiverMethods.recupererListeMagasins(apiUrl03, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        }else{
            magasins = magasinsCdeFragment;
        }
        magasinSpinnerAdapter = new MagasinSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, magasins);
        MbSpnComComag.setAdapter(magasinSpinnerAdapter);

        /**Exécution en background - Popularisation des spinners (Combo boxes)**/
        DelayedProgressDialog pgDialog = new DelayedProgressDialog();
        pgDialog.show(getSupportFragmentManager(), "Load");
        pgDialog.setCancelable(false);
        new Thread(() -> {

            try {
                Thread.sleep(4000);
                SpinnerTask spinnerTask = new SpinnerTask();
                spinnerTask.execute();

                // Now start your activity
                pgDialog.cancel();

            } catch (InterruptedException e) {
                e.printStackTrace();
                pgDialog.cancel();
            }
        }).start();

        /**Date commande**/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        /**Date de livraison**/
        EdtComDaliv.setText(sdf.format(new Date()));

        /**Champ de date, selection de date  DatePicker**/
        //EdtComDacom.setOnClickListener(v -> DpComDacom.show());
        /**Marquer la date selectionnée dans le champ date devis**/
        //setDateOnEdtComDacom();
        /**Date de livraison**/
        EdtComDaliv.setOnClickListener(v -> DpComDaliv.show());
        setDateOnEdtComDaliv();

        /**Controles de selections**/
        /**selection Client**/
        SsnComCliRasoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               infosbyClient(apiUrl13, clientSpinnerAdapter.getItem(position).getCliNucli());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        /**Récupération sélection lieu de ventes**/
        MbSpnCliLieuv.setOnItemClickListener((parent, view, position, id) -> {
            lieuVente = lieuVenteSpinnerAdapter.getItem(position);
            /**Récupération de magasin par défaut**/
            if (!lieuVente.getComag().equals("")){
                /**Inititalisation magasin**/
                magasinNotSelected = new Magasin();
                magasinNotSelected.setMagcomag(lieuVente.getComag());

                int spinnerPosition = magasins.indexOf(magasinNotSelected);
                if (spinnerPosition != -1){
                    /**Set value to magasin spinner**/
                    MbSpnComComag.setText(MbSpnComComag.getAdapter().getItem(spinnerPosition).toString());
                    magasinNotSelected.setMaglimag(MbSpnComComag.getAdapter().getItem(spinnerPosition).toString());
                    if (!gszon.equals("N") && !client.getCliZogeo().equals("")){
                        String codlv = magasinNotSelected.getMagcomag().trim()+" "+client.getCliZogeo();
                        DelaiLivraison delaiLivraison = new DelaiLivraison();
                        delaiLivraison.setNudlv(codlv);
                        int listPosition = delaiLivraisonList.indexOf(delaiLivraison);
                        if (listPosition != -1){
                            String dateLivraisonSouhaitee = addDaysToDateLivraison(delaiLivraisonList.get(listPosition).getDldlv());
                            EdtComDaliv.setText(dateLivraisonSouhaitee);
                            String dlvDlv = "Delai: "+delaiLivraisonList.get(listPosition).getDldlv()+" jour(s)";
                            TxtDlvlv.setText(dlvDlv);
                        }
                    }
                }

            }
        });
        /**Récupération magasin***/
        MbSpnComComag.setOnItemClickListener((parent, view, position, id) ->{
            magasin = magasinSpinnerAdapter.getItem(position);
            if (!gszon.equals("N") && !client.getCliZogeo().equals("")){
                String codlv = magasin.getMagcomag().trim()+" "+client.getCliZogeo();
                DelaiLivraison delaiLivraison = new DelaiLivraison();
                delaiLivraison.setNudlv(codlv);
                int listPosition = delaiLivraisons.indexOf(delaiLivraison);
                if (listPosition != -1){
                    String dateLivraisonSouhaitee = addDaysToDateLivraison(delaiLivraisonList.get(listPosition).getDldlv());
                    EdtComDaliv.setText(dateLivraisonSouhaitee);
                    String dlvDlv = "Delai: "+delaiLivraisonList.get(listPosition).getDldlv()+" jour(s)";
                    TxtDlvlv.setText(dlvDlv);
                }
            }
        });

        /**Récupération sélection tournée**/
        MbSpnComCotrn.setOnItemClickListener((parent, view, position, id) -> {
            tournee = tourneeSpinnerAdapter.getItem(position);
        });

        /**Récupération selection livreur**/
        MbSpnComColiv.setOnItemClickListener((parent, view, position, id) -> {
            livreur = livreurSpinnerAdapter.getItem(position);
        });

        /**Récupération sélection transport**/
        MbSpnComCotrp.setOnItemClickListener((parent, view, position, id) -> transport = transportSpinnerAdapter.getItem(position));
        /***Bouton Suivant**/
        BtnNext.setOnClickListener(v -> {
            if (SsnComCliRasoc.getSelectedItem() != null) {
                if (MbSpnCliLieuv.length() != 0) {
                    if (MbSpnComComag.length() != 0) {
                                /**Comparaison des dates**/
                                try {
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                    Date dateCommande = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
                                    Date dateLivraison = simpleDateFormat.parse(EdtComDaliv.getText().toString());
                                    assert dateCommande != null;
                                        /**Date devis correcte - Comparaison à la date de livraison**/
                                        if (dateLivraison.compareTo(dateCommande) >= 0) {
                                            Intent i = new Intent(getApplicationContext(), AdresseLivraisonActivity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                            i.putExtra("client", client);
                                            i.putExtra("lieuvente", lieuVente);
                                            /***Magasin**/
                                            if (null == magasin){
                                                i.putExtra("magasin", magasinNotSelected);
                                            }else{
                                                i.putExtra("magasin", magasin);
                                            }
                                            /***Tournée***/
                                            if (!gstrn.equals("N")) {
                                                if (null == tournee) {
                                                    i.putExtra("tournee", tourneeNotSelected);
                                                } else {
                                                    i.putExtra("tournee", tournee);
                                                }
                                            } else {
                                                tournee = new Tournee();
                                                tournee.setTrnCotrn("");
                                                tournee.setTrnLitrn("");
                                                i.putExtra("tournee", tournee);
                                            }
                                            /**Livreur**/
                                            if (!gslvr.equals("N")) {
                                            if (null == livreur) {
                                                i.putExtra("livreur", livreurNotSelected);
                                            } else {
                                                i.putExtra("livreur", livreur);
                                            }
                                            }else{
                                                livreur = new Livreur();
                                                livreur.setLivColiv("");
                                                livreur.setLivliliv("");
                                                i.putExtra("livreur", livreur);
                                            }
                                            /**Transport**/
                                            if (null == transport) {
                                                i.putExtra("transport", transportNotSelected);
                                            } else {
                                                i.putExtra("transport", transport);
                                            }
                                            /**Set values to Commande**/
                                            commande = new Commande();
                                            /**Set values to object**/
                                            commande.setComdacom(simpleDateFormat.format(new Date()));
                                            commande.setComdaliv(EdtComDaliv.getText().toString());
                                            commande.setComnamar(EdtComNamar.getText().toString());
                                            commande.setComrasoc(client.getCliRasoc());
                                            commande.setComadre1(client.getCliAdre1());
                                            commande.setComadre2(client.getCliAdre2());
                                            commande.setComcopos(client.getCliCopos());
                                            commande.setComville(client.getCliVille());
                                            commande.setCombopos(client.getCliBopos());
                                            /**Gestion Zone Géographique**/
                                            if (gszon.equals("N")) {
                                                commande.setComzogeo("");
                                            } else {
                                                commande.setComzogeo(client.getCliZogeo());
                                            }
                                            i.putExtra("commande", commande);
                                            /**Set values to Pays**/
                                            pays = new Pays();
                                            pays.setPaysCopay(client.getCliCpays());
                                            i.putExtra("pays", pays);
                                            if (gstrn.equals("N")){
                                                startActivity(i);
                                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                            }else{
                                                if (MbSpnComCotrn.getText().length() != 0){
                                                    startActivity(i);
                                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                }else{
                                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL05), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL09), Toast.LENGTH_LONG).show();
                                        }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL02), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL03), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL04), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!getResources().getBoolean(R.bool.isTablet)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
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

    /**
     * Attribuer la date selectionnée au champ de date
     **/
    public void setDateOnEdtComDacom() {
        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DpComDacom = new DatePickerDialog(AddCommandeActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            EdtComDacom.setText(dateFormat.format(newDate.getTime()));
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Attribuer la date selectionnée au champ de date
     **/
    public void setDateOnEdtComDaliv() {
        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DpComDaliv = new DatePickerDialog(AddCommandeActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            EdtComDaliv.setText(dateFormat.format(newDate.getTime()));
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private class SpinnerTask extends AsyncTaskExecutorService<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void avoid) {
            lieuVentes = apiReceiverMethods.recupererListeLieuv(apiUrl02, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);

            tournees = apiReceiverMethods.recupererListeTournees(apiUrl04, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
           /**Récupération de la liste des transports**/
            transports = apiReceiverMethods.recupererListeTransport(apiUrl06, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
            /***Récupération de la liste des lieux de vente**/
            paysFacturationList = apiReceiverMethods.recupererListePays(apiUrl07, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
            modeReglements = apiReceiverMethods.recupererModeReglements(apiUrl08, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
            delaiReglements = apiReceiverMethods.recupererDelaiReglements(apiUrl09, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
            uscomList = apiReceiverMethods.recupererCommerciaux(apiUrl10, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
            delaiLivraisonList = apiReceiverMethods.recupererDlv(apiUrl12, systemeAdresse, utilisateurLogin, utilisateurPassword,  utilisateurCosoc, utilisateurCoage);
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            // Now start your activity
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    /**Récupération de la liste des transports**/
                    transportSpinnerAdapter = new TransportSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, transports);
                    MbSpnComCotrp.setAdapter(transportSpinnerAdapter);
                    lieuVenteSpinnerAdapter = new LieuVenteSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, lieuVentes);
                    MbSpnCliLieuv.setAdapter(lieuVenteSpinnerAdapter);

                    tourneeSpinnerAdapter = new TourneeSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, tournees);
                    MbSpnComCotrn.setAdapter(tourneeSpinnerAdapter);

                }
            });
        }
    }

    /**Add days to dateLivraison**/
    public String addDaysToDateLivraison(int daysNbr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        // convert date to calendar
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        // Add days to calendar
        c.add(Calendar.DATE, daysNbr);
        String dateInString = sdf.format(c.getTime());
        return dateInString;
    }

    public void infosbyClient(String api_url, final String clinucli) {
        RequestQueue requestQueue = new Volley().newRequestQueue(getApplicationContext());
        progressDialog.show(getSupportFragmentManager(), "Loading...");
        client = new Client();
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonObjectInfo = jsonObject.getJSONObject("FicheClient");

                client.setCliNucli(jsonObjectInfo.getString("CLINUCLI"));
                client.setCliNacli(jsonObjectInfo.getString("CLINACLI"));
                client.setCliRasoc(jsonObjectInfo.getString("CLIRASOC").trim());
                client.setCliAdre1(jsonObjectInfo.getString("CLIADRE1").trim());
                client.setCliAdre2(jsonObjectInfo.getString("CLIADRE2").trim());
                client.setCliBopos(jsonObjectInfo.getString("CLIBOPOS").trim());
                client.setCliCopos(jsonObjectInfo.getString("CLICOPOS").trim());
                client.setCliVille(jsonObjectInfo.getString("CLIVILLE").trim());
                client.setCliCpays(jsonObjectInfo.getString("CLICPAYS").trim());
                client.setCliLiNacli(jsonObjectInfo.getString("LIBNACLI").trim());
                client.setCliLiComon(jsonObjectInfo.getString("LIBCOMON").trim());
                client.setCliColiv(jsonObjectInfo.getString("CLICOLIV").trim());
                client.setCliLiliv(jsonObjectInfo.getString("LIBCOLIV").trim());
                client.setCliDereg(jsonObjectInfo.getString("CLIDEREG").trim());
                client.setCliMoreg(jsonObjectInfo.getString("CLIMOREG").trim());
                client.setCliCotrn(jsonObjectInfo.getString("CLICOTRN").trim());
                client.setCliLitrn(jsonObjectInfo.getString("LIBCOTRN").trim());
                client.setCliCotrp(jsonObjectInfo.getString("CLICOTRP").trim());
                client.setCliLitrp(jsonObjectInfo.getString("LIBCOTRP").trim());
                client.setClililivth(jsonObjectInfo.getString("LIBLIVTH").trim());
                client.setCliRasol(jsonObjectInfo.getString("CLIRASOL").trim());
                client.setCliAdr1l(jsonObjectInfo.getString("CLIADR1L").trim());
                client.setCliAdr2l(jsonObjectInfo.getString("CLIADR2L").trim());
                client.setCliCopol(jsonObjectInfo.getString("CLICOPOL").trim());
                client.setCliVilll(jsonObjectInfo.getString("CLIVILLL").trim());
                client.setCliBopol(jsonObjectInfo.getString("CLIBOPOL").trim());
                client.setCliCpayl(jsonObjectInfo.getString("CLICPAYL").trim());
                client.setCliNacpx(jsonObjectInfo.getString("CLINACPX"));
                client.setCliCpgen(jsonObjectInfo.getString("CLICPGEN").trim());
                client.setCliCpaux(jsonObjectInfo.getString("CLICPAUX").trim());
                client.setCliComon(jsonObjectInfo.getString("CLICOMON").trim());
                client.setCliZogeo(jsonObjectInfo.getString("CLIZOGEO"));
                client.setCliMtplf(jsonObjectInfo.getDouble("CLIMTPLF"));

                /**Initialiser des listes **/
                if (!client.getCliCotrp().equals("")) {
                    /**Initialisation livreur**/
                    transportNotSelected = new Transport();
                    transportNotSelected.setTrpCotrp(client.getCliCotrp());
                    transportNotSelected.setTrpLitrp(client.getCliLitrp());
                    int spinnerPosition = transports.indexOf(transportNotSelected);
                    if (spinnerPosition != -1)
                    /**Set value to spinnerLivreur*/
                        MbSpnComCotrp.setText(MbSpnComCotrp.getAdapter().getItem(spinnerPosition).toString());
                }
                /**Initialiser des listes **/
                if (!client.getCliCotrn().equals("")) {
                    /**Initialisation Tournée**/
                    tourneeNotSelected.setTrnLitrn(client.getCliLitrn());
                    tourneeNotSelected.setTrnCotrn(client.getCliCotrn());
                    int spinnerPosition = tournees.indexOf(tourneeNotSelected);
                    if (spinnerPosition != -1)
                    /**Set value to spinnerLivreur*/
                        MbSpnComCotrn.setText(MbSpnComCotrn.getAdapter().getItem(spinnerPosition).toString());
                }

                /**Initialiser des listes **/
                if (!client.getCliColiv().equals("")) {
                    /**Initialisation livreur**/
                    livreurNotSelected = new Livreur();
                    livreurNotSelected.setLivColiv(client.getCliColiv());
                    livreurNotSelected.setLivliliv(client.getCliLiliv());
                    int spinnerPosition = livreurs.indexOf(livreurNotSelected);
                    if (spinnerPosition != -1)
                    /**Set value to spinnerLivreur*/
                        MbSpnComColiv.setText(MbSpnComColiv.getAdapter().getItem(spinnerPosition).toString());
                }
                progressDialog.cancel();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, volleyError -> {
            volleyError.printStackTrace();
            progressDialog.cancel();
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("systeme",systemeAdresse);
                param.put("login",utilisateurLogin);
                param.put("password",utilisateurPassword);
                param.put("cosoc", utilisateurCosoc);
                param.put("coage", utilisateurCoage);
                param.put("nucli", clinucli);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }

}