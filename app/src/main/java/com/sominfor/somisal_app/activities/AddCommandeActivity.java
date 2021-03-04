package com.sominfor.somisal_app.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
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
import com.sominfor.somisal_app.fragments.ClientFragment;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Client;
import com.sominfor.somisal_app.handler.models.LieuVente;
import com.sominfor.somisal_app.handler.models.Livreur;
import com.sominfor.somisal_app.handler.models.Magasin;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Tournee;
import com.sominfor.somisal_app.handler.models.Transport;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.utils.ApiReceiverMethods;
import com.sominfor.somisal_app.utils.UserSessionManager;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.sominfor.somisal_app.activities.LoginActivity.protocole;

public class AddCommandeActivity extends AppCompatActivity {
    public static final String TAG = AddCommandeActivity.class.getSimpleName();
    /***Liste des spinners**/
    List<Client> clientList;
    List<LieuVente> lieuVentes;
    List<Magasin> magasins;
    List<Tournee> tournees;
    List<Livreur> livreurs;
    List<Transport> transports;

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

    /**
     * Classes d'Objets
     **/
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    Client client;
    LieuVente lieuVente;
    Magasin magasin;
    Tournee tournee;
    Livreur livreur;
    Transport transport;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, apiUrl01, apiUrl02, apiUrl03, apiUrl04, apiUrl05, apiUrl06,utilisateurCosoc, utilisateurCoage;
    public RequestQueue rq;

    ApiReceiverMethods apiReceiverMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_commande);

        /****Contrôle de sessions utilisateur**/
        if (!UserSessionManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
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
        apiReceiverMethods = new ApiReceiverMethods(getApplicationContext());
        magasins = new ArrayList<>();

        /**URL Récupération de la liste des clients**/
        apiUrl01 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/allClient";
        apiUrl02 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allLivth";
        apiUrl03 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allComag";
        apiUrl04 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allCotrn";
        apiUrl05 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allColiv";
        apiUrl06 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allCotrp";

        /**Instanciation des widgets**/
        SsnComCliRasoc = findViewById(R.id.MbSpnComRasoc);
        BtnNext = findViewById(R.id.BtnNext);
        MbSpnCliLieuv = findViewById(R.id.MbSpnCliLieuv);
        MbSpnComComag = findViewById(R.id.MbSpnComComag);
        MbSpnComCotrn = findViewById(R.id.MbSpnComCotrn);
        MbSpnComColiv = findViewById(R.id.MbSpnComColiv);
        MbSpnComCotrp = findViewById(R.id.MbSpnComCotrp);
        EdtComDacom = findViewById(R.id.EdtComDacom);
        EdtComDaliv = findViewById(R.id.EdtComDaliv);
        EdtComNamar = findViewById(R.id.EdtComNamar);

        /**Récupération de la liste de clients**/
        clientList = ClientFragment.clients;
        clientSpinnerAdapter = new ClientSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, clientList);
        SsnComCliRasoc.setAdapter(clientSpinnerAdapter);

        /***Récupération de la liste des lieux de vente**/
        lieuVentes = apiReceiverMethods.recupererListeLieuv(apiUrl02, systemeAdresse, utilisateurLogin, utilisateurPassword,  utilisateurCosoc, utilisateurCoage);
        lieuVenteSpinnerAdapter = new LieuVenteSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, lieuVentes);
        MbSpnCliLieuv.setAdapter(lieuVenteSpinnerAdapter);

        /**Récupération de la liste des magasins**/
        magasins = apiReceiverMethods.recupererListeMagasins(apiUrl03, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        magasinSpinnerAdapter = new MagasinSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, magasins);
        MbSpnComComag.setAdapter(magasinSpinnerAdapter);

        /**Récupération de la liste des magasins**/
        tournees = apiReceiverMethods.recupererListeTournees(apiUrl04, systemeAdresse, utilisateurLogin, utilisateurPassword,  utilisateurCosoc, utilisateurCoage);
        tourneeSpinnerAdapter = new TourneeSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, tournees);
        MbSpnComCotrn.setAdapter(tourneeSpinnerAdapter);

        /**Récupération de la liste des livreurs**/
        livreurs = apiReceiverMethods.recupererListeLivreurs(apiUrl05, systemeAdresse, utilisateurLogin, utilisateurPassword,  utilisateurCosoc, utilisateurCoage);
        livreurSpinnerAdapter = new LivreurSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, livreurs);
        MbSpnComColiv.setAdapter(livreurSpinnerAdapter);

        /**Récupération de la liste des transports**/
        transports = apiReceiverMethods.recupererListeTransport(apiUrl06, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        transportSpinnerAdapter = new TransportSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, transports);
        MbSpnComCotrp.setAdapter(transportSpinnerAdapter);

        /**Date commande**/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        EdtComDacom.setText(sdf.format(new Date()));
        /**Date de livraison**/
        EdtComDaliv.setText(sdf.format(new Date()));

        /**Champ de date, selection de date  DatePicker**/
        EdtComDacom.setOnClickListener(v -> DpComDacom.show());
        /**Marquer la date selectionnée dans le champ date devis**/
        setDateOnEdtComDacom();
        /**Date de livraison**/
        EdtComDaliv.setOnClickListener(v -> DpComDaliv.show());
        setDateOnEdtComDaliv();

        /**Controles de selections**/
        /**selection Client**/
        SsnComCliRasoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /**Récupération Client**/
                client = clientSpinnerAdapter.getItem(position);
                /**Initialiser des listes **/
                if (!client.getCliCotrn().equals("")) {
                    /**Initialisation Tournée**/
                    Tournee tournee = new Tournee();
                    tournee.setTrnLitrn(client.getCliLitrn());
                    tournee.setTrnCotrn(client.getCliCotrn());
                    int spinnerPosition = tournees.indexOf(tournee);
                    if (spinnerPosition != -1)
                    /**Set value to spinnerLivreur*/
                        MbSpnComCotrn.setText(MbSpnComCotrn.getAdapter().getItem(spinnerPosition).toString());
                }

                /**Initialiser des listes **/
                if (!client.getCliColiv().equals("")) {
                    /**Initialisation livreur**/
                    Livreur livreur = new Livreur();
                    livreur.setLivColiv(client.getCliColiv());
                    livreur.setLivliliv(client.getCliLiliv());
                    int spinnerPosition = livreurs.indexOf(livreur);
                    if (spinnerPosition != -1)
                    /**Set value to spinnerLivreur*/
                        MbSpnComColiv.setText(MbSpnComColiv.getAdapter().getItem(spinnerPosition).toString());
                }

                /**Initialiser des listes **/
                if (!client.getCliCotrp().equals("")) {
                    /**Initialisation livreur**/
                    Transport transport = new Transport();
                    transport.setTrpCotrp(client.getCliCotrp());
                    transport.setTrpLitrp(client.getCliLitrp());
                    int spinnerPosition = transports.indexOf(transport);
                    if (spinnerPosition != -1)
                    /**Set value to spinnerLivreur*/
                        MbSpnComCotrp.setText(MbSpnComCotrp.getAdapter().getItem(spinnerPosition).toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**Récupération sélection lieu de ventes**/
        MbSpnCliLieuv.setOnItemClickListener((parent, view, position, id) -> lieuVente = lieuVenteSpinnerAdapter.getItem(position));

        /**Récupération sélection magasin**/
        MbSpnComComag.setOnItemClickListener((parent, view, position, id) -> magasin = magasinSpinnerAdapter.getItem(position));

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

        BtnNext.setOnClickListener(v -> {
            if (SsnComCliRasoc.getSelectedItem() != null) {
                if (MbSpnCliLieuv.length() != 0) {
                    if (MbSpnComComag.length() != 0) {
                        if (MbSpnComColiv.getText().length() != 0) {
                            if (MbSpnComCotrn.getText().length() != 0) {
                                Intent i = new Intent(getApplicationContext(), AdresseFacturationActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                i.putExtra("client", client);
                                i.putExtra("Lieuvente", lieuVente);
                                i.putExtra("Magasin", magasin);
                                i.putExtra("ComDacom", EdtComDacom.getText().toString());
                                i.putExtra("Tournee", tournee);
                                i.putExtra("ComDaliv", EdtComDaliv.getText().toString());
                                i.putExtra("Transport", transport);
                                i.putExtra("ComNamar", EdtComNamar.getText().toString());
                                startActivity(i);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL05), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL01), Toast.LENGTH_LONG).show();
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

}