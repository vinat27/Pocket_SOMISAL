package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.DevisAdapter;
import com.sominfor.somisal_app.adapters.DevisSoldesAdapter;
import com.sominfor.somisal_app.fragments.FiltresDevisDialog;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.CommandeFilterElements;
import com.sominfor.somisal_app.handler.models.Devis;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.interfaces.DevisFilterListener;
import com.sominfor.somisal_app.utils.ApiReceiverMethods;
import com.sominfor.somisal_app.utils.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.sominfor.somisal_app.activities.LoginActivity.protocole;

public class DevisSoldesActivity extends AppCompatActivity implements DevisFilterListener {

    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, apiUrl01, utilisateurCosoc, utilisateurCoage, devStatut;
    RecyclerView recyclerViewDevisSoldes;
    public RequestQueue rq;
    DelayedProgressDialog progressDialogInfo;
    ApiReceiverMethods apiReceiverMethods;
    FrameLayout frameLayout;
    List<Devis> devisSoldesList;
    DevisSoldesAdapter devisSoldesAdapter;
    private MenuItem mSearchItem;
    private SearchView sv;
    FloatingActionButton fab_filter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**Controle orientation***/
        if(!getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devis_soldes);

        /**Contrôle de sessions utilisateur**/
        if (!UserSessionManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        rq = Volley.newRequestQueue(this);
        devisSoldesList = new ArrayList<>();
        recyclerViewDevisSoldes = findViewById(R.id.RecyclerViewDevisSoldes);
        frameLayout = findViewById(R.id.frameLayout);
        fab_filter = findViewById(R.id.Fab_Filter);
        devStatut = "S";

        /**Gestion du menu d'action**/
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Devis Soldés");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /**Effacer l'ombre sous l'actionBar**/
        getSupportActionBar().setElevation(0);

        progressDialogInfo = new DelayedProgressDialog();
        apiReceiverMethods = new ApiReceiverMethods(getApplicationContext());
        serveurNodeController = new ServeurNodeController();
        serveurNode = serveurNodeController.getServeurNodeInfos();
        /**URL Récupération de la liste des systèmes*/
        apiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/devis/devisByStatu";
        /**Récupération de session utilisateur**/
        utilisateur = UserSessionManager.getInstance(getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();
        utilisateurCosoc = utilisateur.getUtilisateurCosoc();
        utilisateurCoage = utilisateur.getUtilisateurCoage();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerViewDevisSoldes.setLayoutManager(linearLayoutManager);

        /**Récupération des devis soldés**/
        listeDevisSoldes(apiUrl01);
        /**Onclick fab_filter**/
        fab_filter.setOnClickListener(v -> {
            openFiltresDevis();
        });
    }

    // Options Menu (ActionBar Menu)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_devis_soldes_activity, menu);
        /**Gestion de menu recherche**/
        mSearchItem = menu.findItem(R.id.action_search);
        sv = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        sv.setIconified(true);

        SearchManager searchManager = (SearchManager)  getSystemService(Context.SEARCH_SERVICE);
        sv.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                devisSoldesAdapter.getFilter().filter(query);
                return true;
            }
        });
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    /**Récupération de la liste de devis en cours**/
    public void listeDevisSoldes(String api_url){
        RequestQueue requestQueue = new Volley().newRequestQueue(getApplicationContext());
        progressDialogInfo.show(getSupportFragmentManager(), "Loading...");
        progressDialogInfo.setCancelable(false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {

            try{
                JSONArray array = new JSONArray(s);
                if (array.length() == 0){
                    progressDialogInfo.cancel();
                    frameLayout.setVisibility(View.VISIBLE);
                }
                for (int i=0; i<array.length(); i++){
                    try{
                        JSONObject jsonObject = array.getJSONObject(i);
                        Devis devis = new Devis();
                        devis.setCliRasoc(jsonObject.getString("CLIRASOC").trim());
                        devis.setDevDaliv(jsonObject.getString("DEVDALIV"));
                        devis.setDevVadev(jsonObject.getDouble("DEVVADEV"));
                        devis.setDevlimon(jsonObject.getString("LIBCOMON").trim());
                        devis.setDevNudev(jsonObject.getString("DEVNUDEV"));
                        devis.setDevDadev(jsonObject.getString("DEVDADEV"));
                        devis.setDevStatut(jsonObject.getString("DEVSTATU"));
                        devis.setDevComon(jsonObject.getString("DEVCOMON"));
                        devis.setDevLieuv(jsonObject.getString("LIBLIEUV").trim());
                        devis.setDevNucom(jsonObject.getString("DEVNUCOM").trim());

                        //Populariser la liste des produits
                        devisSoldesList.add(devis);
                        progressDialogInfo.cancel();
                    }catch(JSONException e){
                        e.printStackTrace();
                        progressDialogInfo.cancel();

                    }
                }
                devisSoldesAdapter = new DevisSoldesAdapter(getApplicationContext(),devisSoldesList);
                recyclerViewDevisSoldes.setAdapter(devisSoldesAdapter);
            }catch(JSONException e){
                e.printStackTrace();

            }
        }, volleyError -> {
            volleyError.printStackTrace();
            progressDialogInfo.cancel();
            /**Erreur connexion**/
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_activity_Snackbar01_NoConnexion), Toast.LENGTH_LONG).show();
        })
        {
            protected Map<String,String> getParams(){
                Map<String, String> param = new HashMap<String, String>();
                param.put("systeme",systemeAdresse);
                param.put("login",utilisateurLogin);
                param.put("password",utilisateurPassword);
                param.put("devstatu", devStatut);
                param.put("cosoc", utilisateurCosoc);
                param.put("coage", utilisateurCoage);
                return param;
            }
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }

    /**Ouvrir fenetre de filtres**/
    private void openFiltresDevis() {
        /***Filtre sur les commandes en cours**/
        FragmentManager fragmentManager = getSupportFragmentManager();
        FiltresDevisDialog filtresDevisDialog = FiltresDevisDialog.newInstance();
        filtresDevisDialog.show(fragmentManager, ServeurNode.TAG);
    }

    @Override
    public void onDataReceivedDevis(CommandeFilterElements commandeFilterElements) {
        /**Transformation de date des informations reçues**/
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date dateInf = simpleDateFormat.parse(commandeFilterElements.getDateInf());
            Date dateSup = simpleDateFormat.parse(commandeFilterElements.getDateSup());

            devisSoldesAdapter.filterDateRange(dateInf, dateSup);
        }catch (ParseException e){
            e.printStackTrace();
        }
    }
}

