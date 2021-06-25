package com.sominfor.somisal_app.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.LivreurSpinnerAdapter;
import com.sominfor.somisal_app.adapters.TourneeSpinnerAdapter;
import com.sominfor.somisal_app.adapters.TransportSpinnerAdapter;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Client;
import com.sominfor.somisal_app.handler.models.Commande;
import com.sominfor.somisal_app.handler.models.Commercial;
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
import static com.sominfor.somisal_app.fragments.CommandeFragment.livreurListCdeFragment;
import static com.sominfor.somisal_app.fragments.CommandeFragment.tourneeListCdeFragment;
import static com.sominfor.somisal_app.fragments.CommandeFragment.transportListCdeFragment;
import static com.sominfor.somisal_app.fragments.HomeFragment.gestionParametresHome;

public class UpdateCommandeActivity extends AppCompatActivity {
    public static final String TAG = UpdateCommandeActivity.class.getSimpleName();
    public RequestQueue rq;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    Tournee tournee, tourneeNotSelected;
    Livreur livreur, livreurNotSelected;
    Transport transport, transportNotSelected;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, apiUrl01, apiUrl02, apiUrl03, gszon, gstrn, apiUrl11, apiUrl07, apiUrl08, apiUrl09, apiUrl10, utilisateurCosoc, utilisateurCoage;
    ApiReceiverMethods apiReceiverMethods;
    Commande commande;
    TextView TxtComRasoc, TxtComLieuv, TxtComNucom, TxtComLimag, TxtComDacom, TxtComLiliv, TxtComVacom;
    MaterialButton BtnNext;
    MaterialBetterSpinner MbSpnComCotrn, MbSpnComColiv, MbSpnComCotrp;
    TextInputEditText  EdtComDaliv, EdtComNamar;
    DatePickerDialog DpComDaliv;
    List<Tournee> tournees;
    List<Livreur> livreurs;
    List<Transport> transports;
    List<GestionParametre> gestionParametres;

    public static List<Pays> paysFacturationUpdCdeList;
    public static List<DelaiReglement> delaiReglementsUpdCde;
    public static List<ModeReglement> modeReglementsUpdCde;
    public static List<Commercial> uscomListUpdCde;
    /**
     * Liste des adapters
     **/
    TourneeSpinnerAdapter tourneeSpinnerAdapter;
    TransportSpinnerAdapter transportSpinnerAdapter;
    LivreurSpinnerAdapter livreurSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_commande);

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
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Actualisation Commande");
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
        gestionParametres = new ArrayList<>();
        /**Récupération du serveur node**/
        serveurNode = serveurNodeController.getServeurNodeInfos();
        /**Récupération de commandes*/
        Bundle bundle = getIntent().getExtras();
        commande = (Commande) bundle.getSerializable("commande");
        /**Initialisation de listes et de la requestQueue**/
        rq = Volley.newRequestQueue(getApplicationContext());
        paysFacturationUpdCdeList = new ArrayList<>();
        delaiReglementsUpdCde = new ArrayList<>();
        modeReglementsUpdCde = new ArrayList<>();
        uscomListUpdCde =  new ArrayList<>();
        apiUrl01 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allCotrn";
        apiUrl02 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allColiv";
        apiUrl03 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allCotrp";
        apiUrl07 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allPays";
        apiUrl08 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/parametre/allMoreg";
        apiUrl09 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/parametre/allDereg";
        apiUrl10 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/parametre/allUscom";
        apiUrl11 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allChoixBySociete";
        /**Instanciation des widgets**/
        TxtComRasoc = findViewById(R.id.TxtComRasoc);
        TxtComLieuv = findViewById(R.id.TxtComLieuv);
        TxtComNucom = findViewById(R.id.TxtComNucom);
        TxtComLimag = findViewById(R.id.TxtComLimag);
        TxtComDacom = findViewById(R.id.TxtComDacom);
        TxtComLiliv = findViewById(R.id.TxtComLiliv);
        TxtComVacom = findViewById(R.id.TxtComVacom);
        EdtComNamar = findViewById(R.id.EdtComNamar);
        MbSpnComCotrn = findViewById(R.id.MbSpnComCotrn);
        EdtComDaliv = findViewById(R.id.EdtComDaliv);
        MbSpnComColiv = findViewById(R.id.MbSpnComColiv);
        MbSpnComCotrp = findViewById(R.id.MbSpnComCotrp);
        BtnNext = findViewById(R.id.BtnTerminer);

        /**Set values to Textviews*/
        TxtComRasoc.setText(commande.getComrasoc());
        TxtComLieuv.setText(commande.getComlilieuv());
        TxtComNucom.setText(commande.getComnucom());
        TxtComLimag.setText(commande.getComlimag());
        @SuppressLint("DefaultLocale") String vacom = String.format("%.2f", commande.getComvacom()) + " " + commande.getComlimon().trim();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fromUser = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String ComDacomFormat = "";

        try {
            ComDacomFormat = fromUser.format(Objects.requireNonNull(myFormat.parse(commande.getComdacom())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TxtComDacom.setText(ComDacomFormat);
        TxtComLiliv.setText(commande.getComliliv());
        TxtComVacom.setText(vacom);
        EdtComNamar.setText(commande.getComnamar());
        EdtComDaliv.setText(commande.getComdaliv());
        EdtComDaliv.setOnClickListener(v -> {
            DpComDaliv.show();
        });
        /**Marquer la date selectionnée dans le champ date livraison**/
        setDateOnEdtDevDaliv();
        /**Récupération des données de gestion paramètres**/
        if(gestionParametresHome.isEmpty()){
            gestionParametres = apiReceiverMethods.recupererGestParam(apiUrl11,systemeAdresse,utilisateurLogin,utilisateurPassword,utilisateurCosoc, utilisateurCoage);
        }else{
            gestionParametres = gestionParametresHome;
        }
        /**Gestion de zone géographie**/
        gszon = gestionParametres.get(0).getDatas();
        /**Gestion de tournée**/
        gstrn = gestionParametres.get(1).getDatas();

        /**Gestion tournée**/
        if (gstrn.equals("N")){
            MbSpnComCotrn.setVisibility(View.GONE);

        }
        /***Récupération des listes de tournée, livreurs, transports***/
        /**--Tournées--**/
        if (tourneeListCdeFragment.isEmpty()) {
            tournees = apiReceiverMethods.recupererListeTournees(apiUrl01, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        } else {
            tournees = tourneeListCdeFragment;
        }
        tourneeSpinnerAdapter = new TourneeSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, tournees);
        MbSpnComCotrn.setAdapter(tourneeSpinnerAdapter);
        /**Livreurs**/
        if (livreurListCdeFragment.size()==0) {
            /**Récupération de la liste de livreurs**/
            livreurs = apiReceiverMethods.recupererListeLivreurs(apiUrl02, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        } else {
            livreurs = livreurListCdeFragment;
        }
        livreurSpinnerAdapter = new LivreurSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, livreurs);
        MbSpnComColiv.setAdapter(livreurSpinnerAdapter);
        /**Transports*/
        if (transportListCdeFragment == null) {
            /**Récupération de la liste de transports**/
            transports = apiReceiverMethods.recupererListeTransport(apiUrl03, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        } else {
            transports = transportListCdeFragment;
        }
        transportSpinnerAdapter = new TransportSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, transports);
        MbSpnComCotrp.setAdapter(transportSpinnerAdapter);
        /***Récupération des listes pays, règlement**/
        paysFacturationUpdCdeList = apiReceiverMethods.recupererListePays(apiUrl07, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        modeReglementsUpdCde = apiReceiverMethods.recupererModeReglements(apiUrl08, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        delaiReglementsUpdCde = apiReceiverMethods.recupererDelaiReglements(apiUrl09, systemeAdresse, utilisateurLogin, utilisateurPassword,utilisateurCosoc, utilisateurCoage);
        uscomListUpdCde = apiReceiverMethods.recupererCommerciaux(apiUrl10, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        /**Set values to spinners**/
        /**Set values to tournees**/
        if (!tournees.isEmpty()) {
            /**Initialisation tournee**/
            tourneeNotSelected = new Tournee();
            tourneeNotSelected.setTrnCotrn(commande.getComcotrn());
            int spinnerPosition = tournees.indexOf(tourneeNotSelected);
            if (spinnerPosition != -1)
            /**Set value to spinnerTournee*/
                MbSpnComCotrn.setText(MbSpnComCotrn.getAdapter().getItem(spinnerPosition).toString());
        }
        /**Set values to livreurs**/
        if (!livreurs.isEmpty()) {
            /**Initialisation livreur**/
            livreurNotSelected = new Livreur();
            livreurNotSelected.setLivColiv(commande.getComcoliv());
            int spinnerPosition = livreurs.indexOf(livreurNotSelected);
            if (spinnerPosition != -1)
            /**Set value to spinnerLivreur*/
                MbSpnComColiv.setText(MbSpnComColiv.getAdapter().getItem(spinnerPosition).toString());
        }
        /**Set values to transport*/
        if (!transports.isEmpty()) {
            transportNotSelected = new Transport();
            transportNotSelected.setTrpCotrp(commande.getComcotrp());
            int spinnerPosition = transports.indexOf(transportNotSelected);
            if (spinnerPosition != -1)
            /**Set value to spinnerTransport*/
                MbSpnComCotrp.setText(MbSpnComCotrp.getAdapter().getItem(spinnerPosition).toString());
        }

        /***Selection tournees**/
        MbSpnComCotrn.setOnItemClickListener((parent, view, position, id) -> {
            tournee = tourneeSpinnerAdapter.getItem(position);
        });
        /**Selection livreurs**/
        MbSpnComColiv.setOnItemClickListener((parent, view, position, id) -> {
            livreur = livreurSpinnerAdapter.getItem(position);
        });
        /***Selection transports**/
        MbSpnComCotrp.setOnItemClickListener((parent, view, position, id) -> {
            transport = transportSpinnerAdapter.getItem(position);
        });

        BtnNext.setOnClickListener(v -> {
            if (MbSpnComColiv.getText().length() != 0) {
                if (MbSpnComCotrn.getText().length() != 0) {
                    /**Comparaison des dates**/
                    try {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        Date currentDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
                        Date dateCommande = simpleDateFormat.parse(commande.getComdacom());
                        Date dateLivraison = simpleDateFormat.parse(EdtComDaliv.getText().toString());
                        assert dateCommande != null;

                            /**Date devis correcte - Comparaison à la date de livraison**/
                            if (dateLivraison.compareTo(currentDate) >= 0) {
                                Intent i = new Intent(getApplicationContext(), AdresseLivraisonUpdateActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                if (null == tournee) {
                                   commande.setComcotrn(tourneeNotSelected.getTrnCotrn());
                                } else {
                                    commande.setComcotrn(tournee.getTrnCotrn());
                                }

                                if (!gstrn.equals("N")){
                                    if (null == tournee) {
                                        commande.setComcotrn(tourneeNotSelected.getTrnCotrn());
                                    } else {
                                        commande.setComcotrn(tournee.getTrnCotrn());
                                    }
                                }else{
                                    commande.setComcotrn("");
                                }
                                /**Livreur**/
                                if (null == livreur) {
                                  commande.setComcoliv(livreurNotSelected.getLivColiv());
                                } else {
                                   commande.setComcoliv(livreur.getLivColiv());
                                }

                                /**Transport**/
                                if (null == transport) {
                                   commande.setComcotrp(transportNotSelected.getTrpCotrp());
                                } else {
                                    commande.setComcotrp(transport.getTrpCotrp());
                                }
                               commande.setComdaliv(EdtComDaliv.getText().toString());
                                commande.setComnamar(EdtComNamar.getText().toString());
                                i.putExtra("commande", commande);
                                startActivity(i);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL09), Toast.LENGTH_LONG).show();
                            }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL05), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL01), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Attribuer la date selectionnée au champ de date de livraison
     **/
    public void setDateOnEdtDevDaliv() {
        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DpComDaliv = new DatePickerDialog(UpdateCommandeActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            EdtComDaliv.setText(dateFormat.format(newDate.getTime()));
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
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