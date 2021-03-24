package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
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
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.DevisArchivesAdapter;
import com.sominfor.somisal_app.adapters.DevisSoldesAdapter;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Devis;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.utils.ApiReceiverMethods;
import com.sominfor.somisal_app.utils.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sominfor.somisal_app.activities.LoginActivity.protocole;

public class DevisArchivesActivity extends AppCompatActivity {
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, apiUrl01, utilisateurCosoc, utilisateurCoage, devStatut;
    RecyclerView recyclerViewDevisArchives;
    public RequestQueue rq;
    DelayedProgressDialog progressDialogInfo;
    ApiReceiverMethods apiReceiverMethods;
    FrameLayout frameLayout;
    List<Devis> devisArchivesList;
    DevisArchivesAdapter devisArchivesAdapter;
    private MenuItem mSearchItem;
    private SearchView sv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**Controle orientation***/
        if(!getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devis_archives);


        /**Contrôle de sessions utilisateur**/
        if (!UserSessionManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        rq = Volley.newRequestQueue(this);
        devisArchivesList = new ArrayList<>();
        recyclerViewDevisArchives = findViewById(R.id.RecyclerViewDevisArchives);
        frameLayout = findViewById(R.id.frameLayout);
        devStatut = "H";

        /**Gestion du menu d'action**/
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Devis Archivés");
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
        recyclerViewDevisArchives.setLayoutManager(linearLayoutManager);

        /**Récupération des devis soldés**/
        listeDevisArchives(apiUrl01);
    }
    /**Récupération de la liste de devis archivés**/
    public void listeDevisArchives(String api_url){
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
                        devis.setCliRasoc(jsonObject.getString("CLIRASOC"));
                        devis.setDevDaliv(jsonObject.getString("DEVDALIV"));
                        devis.setDevVadev(jsonObject.getDouble("DEVVADEV"));
                        devis.setDevComon(jsonObject.getString("DEVCOMON"));
                        devis.setDevNudev(jsonObject.getString("DEVNUDEV"));
                        devis.setDevNucom(jsonObject.getString("DEVNUCOM"));
                        devis.setDevDadev(jsonObject.getString("DEVDADEV"));
                        devis.setDevLieuv(jsonObject.getString("DEVLIEUVLIB"));
                        devis.setDevColieuv(jsonObject.getString("DEVLIEUV"));
                        devis.setDevRfdev(jsonObject.getString("DEVRFDEV"));
                        devis.setDevStatut(jsonObject.getString("DEVSTATULIB"));
                        devis.setDevComag(jsonObject.getString("DEVCOMAG"));
                        devis.setDevLimag(jsonObject.getString("DEVCOMAGLIB"));
                        devis.setDevColiv(jsonObject.getString("DEVCOLIV").trim());
                        devis.setDevliliv(jsonObject.getString("DEVCOLIVLIB"));
                        devis.setDevNacli(jsonObject.getString("CLINACLI"));
                        devis.setDevNucli(jsonObject.getString("DEVNUCLI"));
                        devis.setDevlimon(jsonObject.getString("DEVCOMONLIB"));

                        //Populariser la liste des produits
                        devisArchivesList.add(devis);
                        progressDialogInfo.cancel();
                    }catch(JSONException e){
                        e.printStackTrace();
                        progressDialogInfo.cancel();

                    }
                }
                devisArchivesAdapter = new DevisArchivesAdapter(getApplicationContext(),devisArchivesList);
                recyclerViewDevisArchives.setAdapter(devisArchivesAdapter);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
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
                devisArchivesAdapter.getFilter().filter(query);
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

}