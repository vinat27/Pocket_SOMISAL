package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.DetailDevisAdapter;
import com.sominfor.somisal_app.adapters.DevisAdapter;
import com.sominfor.somisal_app.fragments.CommentairesDevisFullDialog;
import com.sominfor.somisal_app.fragments.FilterProduitFullDialog;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Client;
import com.sominfor.somisal_app.handler.models.DetailDevis;
import com.sominfor.somisal_app.handler.models.Devis;
import com.sominfor.somisal_app.handler.models.Produit;
import com.sominfor.somisal_app.handler.models.ProduitFini;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.utils.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

import static com.sominfor.somisal_app.activities.LoginActivity.protocole;

public class FicheDevisActivity extends AppCompatActivity {
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, apiUrl01, apiUrl02, utilisateurCosoc, utilisateurCoage, nudevText;
    Devis devis, devisInfos;
    RecyclerView RecyclerViewDetailsDevis;
    List<DetailDevis> detailDevisList;
    DetailDevisAdapter detailDevisAdapter;
    TextView TxtClirasoc, TxtDevStatu, TxtDevLimag, TxtDevLiliv, TxtDevVadev, TxtNuDev,TxtDevDaliv;
    public RequestQueue rq;
    ImageView helpTotalTilp;
    DelayedProgressDialog progressDialogInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_devis);
        /**Contrôle de sessions utilisateur**/
        if (!UserSessionManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        /**Instanciation des widgets**/
        RecyclerViewDetailsDevis = findViewById(R.id.RecyclerViewDetailsDevis);
        TxtNuDev = findViewById(R.id.TxtNuDev);
        TxtClirasoc = findViewById(R.id.TxtClirasoc);
        TxtDevStatu = findViewById(R.id.TxtDevStatu);
        TxtDevLimag = findViewById(R.id.TxtDevLimag);
        TxtDevLiliv = findViewById(R.id.TxtDevLiliv);
        TxtDevVadev = findViewById(R.id.TxtDevVadev);
        TxtDevDaliv = findViewById(R.id.TxtDevDaliv);
        helpTotalTilp = findViewById(R.id.helpTotalTilp);
        /**Gestion du menu d'action**/
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Détails devis");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /**Effacer l'ombre sous l'actionBar**/
        getSupportActionBar().setElevation(0);

        serveurNodeController = new ServeurNodeController();
        serveurNode = serveurNodeController.getServeurNodeInfos();
        progressDialogInfo = new DelayedProgressDialog();
        /*URL Récupération de la liste des systèmes*/
        apiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/devis/detailDevis";
        apiUrl02 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/devis/devisByNudev";
        /**Récupération de session utilisateur**/
        utilisateur = UserSessionManager.getInstance(getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();
        utilisateurCosoc = utilisateur.getUtilisateurCosoc();
        utilisateurCoage = utilisateur.getUtilisateurCoage();

        /**Récupération de l'objet devis**/
        Bundle bundle = getIntent().getExtras();
        devis = (Devis) bundle.getSerializable("devis");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        RecyclerViewDetailsDevis.setLayoutManager(linearLayoutManager);
        BigDecimal bd = new BigDecimal(devis.getDevVadev());
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');

        DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);
        formatter.setRoundingMode(RoundingMode.DOWN);
        String vadev = String.format(formatter.format(bd.floatValue()))+" "+devis.getDevlimon().trim();

        helpTotalTilp.setOnClickListener(v -> {
            new SimpleTooltip.Builder(this)
                    .anchorView(v)
                    .text(R.string.helpTotalTilp)
                    .gravity(Gravity.BOTTOM)
                    .dismissOnOutsideTouch(true)
                    .dismissOnInsideTouch(true)
                    .build()
                    .show();
        });

        /**Set Data to Textviews**/
        nudevText = "Devis N°: "+devis.getDevNudev();
        TxtNuDev.setText(nudevText);
        TxtClirasoc.setText(devis.getCliRasoc());
        TxtDevVadev.setText(vadev);

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fromUser = new SimpleDateFormat("dd MMM yyyy");
        String DevDalivFormat = "";

        try {
            DevDalivFormat = fromUser.format(Objects.requireNonNull(myFormat.parse(devis.getDevDaliv())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TxtDevDaliv.setText(DevDalivFormat);

        recupEnteteDevis(apiUrl02, devis.getDevNudev());

        detailDevisList = new ArrayList<>();
        /**Récupération details devis**/
        recupererDetailsDevis(apiUrl01, devis.getDevNudev(), devis.getDevComon());


    }


    // Options Menu (ActionBar Menu)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fiche_devis_activity, menu);
        return true;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.comment_devis:
                /***Filtre sur familles et sous familles**/
                FragmentManager fragmentManager = getSupportFragmentManager();
                CommentairesDevisFullDialog commentairesDevisFullDialog = CommentairesDevisFullDialog.newInstance();
                Bundle args = new Bundle();
                args.putString("devtxnpi", devis.getDevTxhPie());
                args.putString("devtxnen", devis.getDevTxhEnt());
                args.putString("devdextxt", devis.getDexTexte());
                commentairesDevisFullDialog.setArguments(args);
                commentairesDevisFullDialog.show(fragmentManager, ServeurNode.TAG);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /***Récupérer les détails devis**/
    public void recupererDetailsDevis(String api_url, final String devNudev, final String devcomon) {
        RequestQueue requestQueue = new Volley().newRequestQueue(getApplicationContext());
        progressDialogInfo.show(getSupportFragmentManager(), "Loading...");
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {
            try {
                JSONObject jsonObject = new JSONObject(s);
                    /**Commentaires et Post-it***/
                    devis.setDevTxhEnt(jsonObject.getString("DEVTXHEN"));
                    devis.setDevTxhPie(jsonObject.getString("DEVTXHPI"));
                    devis.setDexTexte(jsonObject.getString("DEXTEXTE"));
                    /**Formatage de l'array produit**/
                    JSONArray array= jsonObject.getJSONArray("Produits");
                    for(int i=0;i<array.length();i++) {
                        JSONObject object1 = array.getJSONObject(i);
                        DetailDevis detailDevis = new DetailDevis();
                        detailDevis.setDdvPodev(object1.getInt("DDVPODEV"));
                        detailDevis.setDdvQtdev(object1.getDouble("DDVQTDEV"));
                        detailDevis.setDdvPutar(object1.getDouble("DDVPUTAR"));
                        detailDevis.setDdvTxrem(object1.getDouble("DDVTXREM"));
                        detailDevis.setDdvVarem(object1.getDouble("DDVVAREM"));
                        detailDevis.setDdvCopro(object1.getInt("DDVCOPRO"));
                        detailDevis.setDdvUnvte(object1.getString("DDVUNVTE"));
                        detailDevis.setDdvLipro(object1.getString("PROLIPRO"));
                        detailDevis.setDdvTxnPo(object1.getString("TXNTEXTE"));
                        detailDevis.setDdvNuprm(object1.getInt("DDVNUPRM"));
                        detailDevisList.add(detailDevis);
                        progressDialogInfo.cancel();
                    }
                FragmentManager fragmentManager = getSupportFragmentManager();
                detailDevisAdapter = new DetailDevisAdapter(getApplicationContext(), detailDevisList, fragmentManager);
                RecyclerViewDetailsDevis.setAdapter(detailDevisAdapter);
                RecyclerViewDetailsDevis.setHasFixedSize(true);
            }catch (JSONException e){
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
                param.put("nudev", devNudev);
                param.put("comon", devcomon);
                param.put("cosoc", utilisateurCosoc);
                param.put("coage", utilisateurCoage);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }

    public void recupEnteteDevis(String api_url, final String devNudev) {
        RequestQueue requestQueue = new Volley().newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {
            devisInfos = new Devis();
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonObjectInfo = jsonObject.getJSONObject("FicheDevis");
                devisInfos.setCliRasoc(jsonObjectInfo.getString("CLIRASOC").trim());
                devisInfos.setDevlista(jsonObjectInfo.getString("DEVSTATULIB").trim());
                devisInfos.setDevComag(jsonObjectInfo.getString("DEVCOMAG"));
                devisInfos.setDevLimag(jsonObjectInfo.getString("DEVCOMAGLIB").trim());
                devisInfos.setDevComon(jsonObjectInfo.getString("DEVCOMON"));
                devisInfos.setDevliliv(jsonObjectInfo.getString("DEVLIEUVLIB").trim());


                /**Set values to Textviews**/
                TxtDevLimag.setText(devisInfos.getDevLimag());
                TxtDevLiliv.setText(devisInfos.getDevliliv());
                TxtDevStatu.setText(devisInfos.getDevlista());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, volleyError -> {
            volleyError.printStackTrace();
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

}