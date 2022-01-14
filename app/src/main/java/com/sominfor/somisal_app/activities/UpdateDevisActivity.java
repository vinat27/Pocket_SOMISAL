package com.sominfor.somisal_app.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.DelaiReglementSpinnerAdapter;
import com.sominfor.somisal_app.adapters.LivreurSpinnerAdapter;
import com.sominfor.somisal_app.adapters.ModeReglementSpinnerAdapter;
import com.sominfor.somisal_app.fragments.DevisFragment;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Client;
import com.sominfor.somisal_app.handler.models.DelaiReglement;
import com.sominfor.somisal_app.handler.models.Devis;
import com.sominfor.somisal_app.handler.models.GestionParametre;
import com.sominfor.somisal_app.handler.models.LieuVente;
import com.sominfor.somisal_app.handler.models.Livreur;
import com.sominfor.somisal_app.handler.models.Magasin;
import com.sominfor.somisal_app.handler.models.ModeReglement;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.interfaces.VolleyCallBack;
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
import static com.sominfor.somisal_app.fragments.DevisFragment.livreurListDevis;
import static com.sominfor.somisal_app.fragments.HomeFragment.gestionParametresHome;
//TODO Set values to MoReg and Dereg - Recevoir Nacli, Lieuv avec les espaces

public class UpdateDevisActivity extends AppCompatActivity  {

    TextView TxtClirasoc, TxtDevLieuv, TxtDevLimag, TxtDevLiliv, TxtDevDaliv, TxtDevVadev, TxtNudev, TxtDevStatu;
    MaterialButton BtnValider;
    ServeurNodeController serveurNodeController;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage, apiUrl01, apiUrl02, apiUrl03, apiUrl04, apiUrl05, nudev, gszon, gstrn, gslvr;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    List<Livreur> livreurs;
    List<ModeReglement> modeReglements;
    List<DelaiReglement> delaiReglements;
    List<GestionParametre> gestionParametres;
    TextInputEditText EdtDevRfdev, EdtDevDaliv;
    MaterialBetterSpinner MbSpnDevColiv, MbSpnDevMoreg, MbSpnDevDereg;
    Devis devis;
    List<Devis> devisDetail;
    ApiReceiverMethods apiReceiverMethods;
    LivreurSpinnerAdapter livreurSpinnerAdapter;
    ModeReglementSpinnerAdapter modeReglementSpinnerAdapter;
    DelaiReglementSpinnerAdapter delaiReglementSpinnerAdapter;
    DatePickerDialog DpdialogDaliv;
    Livreur livreurNotSelected, livreur;
    ModeReglement modeReglement,modeReglementNotSelected;
    DelaiReglement delaiReglement, delaiReglementNotSelected;
    static UpdateDevisActivity updateDevisActivity;
    DelayedProgressDialog progressDialogInfo;

    /**Nouvelle Instance**/
    public static UpdateDevisActivity getInstance(){
        return  updateDevisActivity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_devis);

        /**Instanciation des widgets**/
        TxtClirasoc = findViewById(R.id.TxtClirasoc);
        TxtDevLieuv = findViewById(R.id.TxtDevLieuv);
        TxtDevLimag = findViewById(R.id.TxtDevLimag);
        //TxtDevLiliv = findViewById(R.id.TxtDevLiliv);
        TxtDevVadev = findViewById(R.id.TxtDevVadev);
        TxtDevDaliv = findViewById(R.id.TxtDevDaliv);
        TxtNudev = findViewById(R.id.TxtNuDev);
        BtnValider = findViewById(R.id.BtnTerminer);
        EdtDevDaliv = findViewById(R.id.EdtDevDaliv);
        EdtDevRfdev = findViewById(R.id.EdtDevRfdev);
        MbSpnDevColiv = findViewById(R.id.MbSpnDevColiv);
        MbSpnDevMoreg = findViewById(R.id.MbSpnDevMoreg);
        MbSpnDevDereg = findViewById(R.id.MbSpnDevDereg);
        TxtDevStatu = findViewById(R.id.TxtDevStatu);

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
        serveurNodeController = new ServeurNodeController();/**Instanciation de l'activité**/
        updateDevisActivity = this;

        /**Récupération du serveur node**/
        serveurNode = serveurNodeController.getServeurNodeInfos();
        apiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/parametre/allColiv";
        apiUrl02 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/parametre/allMoreg";
        apiUrl03 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allDereg";
        apiUrl04 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allChoixBySociete";
        apiUrl05 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/devis/devisByNudev";

        livreurs = new ArrayList<>();
        gestionParametres = new ArrayList<>();
        progressDialogInfo = new DelayedProgressDialog();
        Bundle bundle = getIntent().getExtras();
        devis = (Devis) bundle.getSerializable("devis");

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

        TxtDevDaliv.setText(DevDalivFormat);
        /**valeur H.T.*/
        BigDecimal bd = new BigDecimal(devis.getDevVadev());
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');

        DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);
        formatter.setRoundingMode(RoundingMode.DOWN);
        String vadev = String.format(formatter.format(bd.floatValue()))+" "+devis.getDevlimon().trim();
        TxtDevVadev.setText(vadev);
        /**Set Nudev**/
        nudev = "Devis: #" + devis.getDevNudev();
        TxtNudev.setText(nudev);



        EdtDevDaliv.setText(devis.getDevDaliv());
        EdtDevDaliv.setOnClickListener(v -> {
            DpdialogDaliv.show();
        });



        /**Marquer la date selectionnée dans le champ date livraison**/
        setDateOnEdtDevDaliv();

        /**Vérifier si liste client est vide sinon récupérer***/
        if (livreurListDevis.isEmpty()) {
            /**Récupération de la liste de clients**/
            livreurs = apiReceiverMethods.recupererListeLivreurs(apiUrl01, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        } else {
            livreurs = livreurListDevis;
        }
        livreurSpinnerAdapter = new LivreurSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, livreurs);
        MbSpnDevColiv.setAdapter(livreurSpinnerAdapter);

        /**Récupération des données de gestion paramètres**/
        if (gestionParametresHome.isEmpty()) {
            gestionParametres = apiReceiverMethods.recupererGestParam(apiUrl04, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        } else {
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
            MbSpnDevColiv.setVisibility(View.GONE);
        }

        /**Récupération de la liste des modes de reglemet**/
        if (DevisFragment.modeReglementListDevis.isEmpty()) {
            modeReglements = apiReceiverMethods.recupererModeReglements(apiUrl02, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        } else {
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

        //recupEnteteDevis(apiUrl05, devis.getDevNudev());


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
                                        client.setCliComon(devis.getDevComon());
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
                                        /**Livreur**/
                                        if (!gslvr.equals("N")) {
                                            if (null == livreur) {
                                                i.putExtra("Livreur", livreurNotSelected);
                                            } else {
                                                i.putExtra("Livreur", livreur);
                                            }
                                        } else {
                                            livreur = new Livreur();
                                            livreur.setLivColiv("");
                                            livreur.setLivliliv("");
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
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
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

    public void recupEnteteDevis(String api_url, final String devNudev, final VolleyCallBack volleyCallBack) {
        RequestQueue requestQueue = new Volley().newRequestQueue(getApplicationContext());
        progressDialogInfo.show(getSupportFragmentManager(), "Loading...");
        Devis devisInfo = new Devis();
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {

            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonObjectInfo = jsonObject.getJSONObject("FicheDevis");
                devisInfo.setDevRfdev(jsonObjectInfo.getString("DEVRFDEV").trim());
                devisInfo.setDevMoreg(jsonObjectInfo.getString("DEVMOREG"));
                devisInfo.setDevDereg(jsonObjectInfo.getString("DEVDEREG").trim());
                devisInfo.setDevColiv(jsonObjectInfo.getString("DEVCOLIV").trim());
                devisInfo.setDevliliv(jsonObjectInfo.getString("DEVCOLIVLIB").trim());
                devisInfo.setDevLimag(jsonObjectInfo.getString("DEVCOMAGLIB").trim());
                devisInfo.setDevStatut(jsonObjectInfo.getString("DEVSTATULIB").trim());
                devisInfo.setDevComag(jsonObjectInfo.getString("DEVCOMAG").trim());
                devisInfo.setDevEcova(jsonObjectInfo.getDouble("DEVECOVA"));
                devisInfo.setDevComon(jsonObjectInfo.getString("DEVCOMON"));
                devisInfo.setDevLieuv(jsonObjectInfo.getString("DEVLIEUV").trim());
                devisInfo.setDevTxesc(jsonObjectInfo.getDouble("DEVTXESC"));
                devisInfo.setDevNucli(jsonObjectInfo.getString("DEVNUCLI"));
                devisInfo.setDevNacli(jsonObjectInfo.getString("CLINACLI").trim());

                volleyCallBack.onSuccess(devisInfo);


                TxtDevLimag.setText(devisInfo.getDevLimag());
                TxtDevStatu.setText(devisInfo.getDevStatut());
                EdtDevRfdev.setText(jsonObjectInfo.getString("DEVRFDEV").trim());
                /**Set values to livreur**/
                if (!livreurs.isEmpty()){
                    /**Initialisation livreur**/
                    livreurNotSelected = new Livreur();
                    livreurNotSelected.setLivColiv(devisInfo.getDevColiv());
                    livreurNotSelected.setLivliliv(devisInfo.getDevliliv());
                    int spinnerPosition = livreurs.indexOf(livreurNotSelected);
                    if (spinnerPosition != -1)
                    /**Set value to spinnerLivreur*/
                        MbSpnDevColiv.setText(MbSpnDevColiv.getAdapter().getItem(spinnerPosition).toString());
                }

                /**Set values to ModeRegelemnt**/
                if (!modeReglements.isEmpty()){
                    modeReglementNotSelected = new ModeReglement();
                    modeReglementNotSelected.setCoMoreg(devisInfo.getDevMoreg());
                    int spinnerPosition = modeReglements.indexOf(modeReglementNotSelected);
                    if (spinnerPosition!=-1)
                        MbSpnDevMoreg.setText(MbSpnDevMoreg.getAdapter().getItem(spinnerPosition).toString());
                }

                /**Set Value to Delai*/
                if (!delaiReglements.isEmpty()){
                    delaiReglementNotSelected = new DelaiReglement();
                    delaiReglementNotSelected.setCoDereg(devisInfo.getDevDereg());
                    int spinnerPosition = delaiReglements.indexOf(delaiReglementNotSelected);
                    if (spinnerPosition!=-1)
                        MbSpnDevDereg.setText(MbSpnDevDereg.getAdapter().getItem(spinnerPosition).toString());
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
                param.put("devnudev", devNudev);
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
        recupEnteteDevis(apiUrl05, devis.getDevNudev(), devisDet -> {
            devis.setDevComag(devisDet.getDevComag());
            devis.setDevNucli(devisDet.getDevNucli());
            devis.setDevColieuv(devisDet.getDevLieuv());
            devis.setDevEcova(devisDet.getDevEcova());
            devis.setDevTxesc(devisDet.getDevTxesc());
            devis.setDevComon(devisDet.getDevComon());
            devis.setDevNacli(devisDet.getDevNacli());
        });
    }

}