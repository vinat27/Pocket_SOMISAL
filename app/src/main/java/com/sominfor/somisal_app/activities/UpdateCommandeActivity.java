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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
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
import com.sominfor.somisal_app.interfaces.VolleyCallBackCommande;
import com.sominfor.somisal_app.utils.ApiReceiverMethods;
import com.sominfor.somisal_app.utils.UserSessionManager;
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
    String systemeAdresse, utilisateurLogin, utilisateurPassword, apiUrl01, apiUrl02, apiUrl03, apiUrl04, gszon, gstrn,gslvr, apiUrl11, apiUrl07, apiUrl08, apiUrl09, apiUrl10, utilisateurCosoc, utilisateurCoage;
    ApiReceiverMethods apiReceiverMethods;
    Commande commande, commandeInfos;
    TextView TxtComRasoc, TxtComLieuv, TxtComNucom, TxtComLimag, TxtComDacom, TxtComLiliv, TxtComVacom;
    MaterialButton BtnNext;
    MaterialBetterSpinner MbSpnComCotrn, MbSpnComColiv, MbSpnComCotrp;
    TextInputEditText  EdtComDaliv, EdtComNamar;
    DatePickerDialog DpComDaliv;
    List<Tournee> tournees;
    List<Livreur> livreurs;
    List<Transport> transports;
    List<GestionParametre> gestionParametres;
    DelayedProgressDialog progressDialogInfo;

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
        progressDialogInfo = new DelayedProgressDialog();
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
        apiUrl04 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/commande/commandeByNucom";
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
        //TxtComLiliv = findViewById(R.id.TxtComLiliv);
        TxtComVacom = findViewById(R.id.TxtComVacom);
        EdtComNamar = findViewById(R.id.EdtComNamar);
        MbSpnComCotrn = findViewById(R.id.MbSpnComCotrn);
        EdtComDaliv = findViewById(R.id.EdtComDaliv);
        MbSpnComColiv = findViewById(R.id.MbSpnComColiv);
        MbSpnComCotrp = findViewById(R.id.MbSpnComCotrp);
        BtnNext = findViewById(R.id.BtnTerminer);

        /**Set values to Textviews*/
        TxtComRasoc.setText(commande.getComrasoc());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fromUser = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String ComDacomFormat = "";

        try {
            ComDacomFormat = fromUser.format(Objects.requireNonNull(myFormat.parse(commande.getComdacom())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TxtComDacom.setText(ComDacomFormat);
        BigDecimal bd = new BigDecimal(commande.getComvacom());
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');

        DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);
        formatter.setRoundingMode(RoundingMode.DOWN);
        String wvacom = formatter.format(bd.floatValue()) + " " + commande.getComlimon();
        TxtComVacom.setText(wvacom);
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
        gstrn = gestionParametres.get(2).getDatas();

        /**Gestion livreur**/
        gslvr = gestionParametres.get(1).getDatas();

        /**Gestion livreur**/
        if (gslvr.equals("N")) {
            MbSpnComColiv.setVisibility(View.GONE);
        }

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
                                if (!gslvr.equals("N")){
                                    if (null == livreur) {
                                        commande.setComcoliv(livreurNotSelected.getLivColiv());
                                    } else {
                                        commande.setComcoliv(livreur.getLivColiv());
                                    }
                                }else{
                                    commande.setComcoliv("");
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

    /**Récupération des informations En-tête commande**/
    public void recupererEnteteCommande(String api_url, final String comNucom, VolleyCallBackCommande callBackCommande){
        RequestQueue requestQueue = new Volley().newRequestQueue(getApplicationContext());
        progressDialogInfo.show(getSupportFragmentManager(), "Loading...");
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {
            commandeInfos = new Commande();
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonObjectInfo = jsonObject.getJSONObject("FicheCommande");
                commandeInfos.setComlimag(jsonObjectInfo.getString("LIBCOMAG").trim());
                commandeInfos.setComcomag(jsonObjectInfo.getString("COMCOMAG"));
                commandeInfos.setComnamar(jsonObjectInfo.getString("COMNAMAR").trim());
                commandeInfos.setComnucli(jsonObjectInfo.getString("COMNUCLI"));
                commandeInfos.setComcoliv(jsonObjectInfo.getString("COMCOLIV").trim());
                commandeInfos.setComcotrp(jsonObjectInfo.getString("COMCOTRP"));
                commandeInfos.setComcotrn(jsonObjectInfo.getString("COMCOTRN").trim());
                commandeInfos.setComlitrn(jsonObjectInfo.getString("LIBCOTRN").trim());
                commandeInfos.setComcomon(jsonObjectInfo.getString("COMCOMON"));
                commandeInfos.setComlieuv(jsonObjectInfo.getString("COMLIEUV"));
                commandeInfos.setComlimon(jsonObjectInfo.getString("LIBCOMON").trim());
                commandeInfos.setComlilieuv(jsonObjectInfo.getString("LIBLIEUV").trim());
                commandeInfos.setComrasol(jsonObjectInfo.getString("COMRASOL").trim());
                commandeInfos.setComadr1l(jsonObjectInfo.getString("COMADR1L").trim());
                commandeInfos.setComadr2l(jsonObjectInfo.getString("COMADR2L").trim());
                commandeInfos.setComcopol(jsonObjectInfo.getString("COMCOPOL").trim());
                commandeInfos.setComvilll(jsonObjectInfo.getString("COMVILLL").trim());
                commandeInfos.setCombopol(jsonObjectInfo.getString("COMBOPOL").trim());
                commandeInfos.setComcpayl(jsonObjectInfo.getString("COMCPAYL").trim());
                commandeInfos.setComdereg(jsonObjectInfo.getString("COMDEREG"));
                commandeInfos.setCommoreg(jsonObjectInfo.getString("COMMOREG"));
                commandeInfos.setComtxrem(jsonObjectInfo.getDouble("COMTXREM"));
                commandeInfos.setComtxesc(jsonObjectInfo.getDouble("COMTXESC"));
                commandeInfos.setComecova(jsonObjectInfo.getDouble("COMECOVA"));
                commandeInfos.setComuscom(jsonObjectInfo.getString("COMUSCOM").trim());
                commandeInfos.setComnacli(jsonObjectInfo.getString("CLINACLI").trim());
                commandeInfos.setComadre1(jsonObjectInfo.getString("COMADRE1").trim());
                commandeInfos.setComadre2(jsonObjectInfo.getString("COMADRE2").trim());
                commandeInfos.setCombopos(jsonObjectInfo.getString("COMBOPOS").trim());
                commandeInfos.setComcopos(jsonObjectInfo.getString("COMCOPOS").trim());
                commandeInfos.setComville(jsonObjectInfo.getString("COMVILLE").trim());
                commandeInfos.setComcpays(jsonObjectInfo.getString("COMCPAYS").trim());

                callBackCommande.onSuccess(commandeInfos);
                TxtComLieuv.setText(commande.getComlilieuv());
                TxtComNucom.setText(commande.getComnucom());
                TxtComLimag.setText(commande.getComlimag());
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
                progressDialogInfo.cancel();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, volleyError -> {
            volleyError.printStackTrace();
            progressDialogInfo.cancel();
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("systeme",systemeAdresse);
                param.put("login",utilisateurLogin);
                param.put("password",utilisateurPassword);
                param.put("cosoc", utilisateurCosoc);
                param.put("coage", utilisateurCoage);
                param.put("nucom", comNucom);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }

    public void onResume(){
        super.onResume();
        recupererEnteteCommande(apiUrl04, commande.getComnucom(), commandeDet -> {
            commande.setComcomag(commandeDet.getComcomag());
            commande.setComnamar(commandeDet.getComnamar());
            commande.setComcoliv(commandeDet.getComcoliv());
            commande.setComcotrp(commandeDet.getComcotrp().trim());
            commande.setComcomon(commandeDet.getComcomon());
            commande.setComlimon(commandeDet.getComlimon());
            commande.setComlimag(commandeDet.getComlimag().trim());
            commande.setComrasol(commandeDet.getComrasol());
            commande.setComadr1l(commandeDet.getComadr1l());
            commande.setComadr2l(commandeDet.getComadr2l());
            commande.setComcopol(commandeDet.getComcopol());
            commande.setComvilll(commandeDet.getComvilll());
            commande.setCombopol(commandeDet.getCombopol());
            commande.setComcpayl(commandeDet.getComcpayl());
            commande.setComdereg(commandeDet.getComdereg());
            commande.setCommoreg(commandeDet.getCommoreg());
            commande.setComtxrem(commandeDet.getComtxrem());
            commande.setComtxesc(commandeDet.getComtxesc());
            commande.setComecova(commandeDet.getComecova());
            commande.setComuscom(commandeDet.getComuscom());
            commande.setComnacli(commandeDet.getComnacli());
            commande.setComlieuv(commandeDet.getComlieuv());
            commande.setComnucli(commandeDet.getComnucli());
            commande.setComadre1(commandeDet.getComadre1());
            commande.setComadre2(commandeDet.getComadre2());
            commande.setCombopos(commandeDet.getCombopos());
            commande.setComcopos(commandeDet.getComcopos());
            commande.setComville(commandeDet.getComville());
            commande.setComcpays(commandeDet.getComcpays());
        });
    }
}