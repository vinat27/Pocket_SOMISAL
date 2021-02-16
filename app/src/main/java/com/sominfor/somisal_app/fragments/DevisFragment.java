package com.sominfor.somisal_app.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.activities.AddDevisActivity;
import com.sominfor.somisal_app.activities.DelayedProgressDialog;
import com.sominfor.somisal_app.activities.FicheProduitActivity;
import com.sominfor.somisal_app.activities.LoginActivity;
import com.sominfor.somisal_app.adapters.DevisAdapter;
import com.sominfor.somisal_app.adapters.ProduitAdapter;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Devis;
import com.sominfor.somisal_app.handler.models.Produit;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.utils.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sominfor.somisal_app.activities.LoginActivity.protocole;

/**
 * Créé par vatsou le 13,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Liste des devis
 */
public class DevisFragment extends Fragment {
    FrameLayout frameLayout;
    RecyclerView recyclerViewDevis;
    List<Devis> devisList;
    private MenuItem mSearchItem;
    private SearchView sv;
    DevisAdapter devisAdapter;
    FloatingActionButton fab_add_devis;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    String ApiUrl01, systemeAdresse, utilisateurLogin, utilisateurPassword, devStatu;
    Utilisateur utilisateur;
    public RequestQueue rq;
    DelayedProgressDialog progressDialogInfo;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.devis_fragment, container, false);
        /***** Déclaration de barre de menu dans le fragment*******/
        setHasOptionsMenu(true);
        getActivity().setTitle("Devis");
        /**Contrôle session***/
        if (!UserSessionManager.getInstance(getActivity()).isLoggedIn()) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        rq = Volley.newRequestQueue(getActivity());
        devisList = new ArrayList<>();
        progressDialogInfo = new DelayedProgressDialog();
        serveurNodeController = new ServeurNodeController();


        /***Instanciation des widgets***/
        frameLayout = view.findViewById(R.id.frameLayout);
        recyclerViewDevis = view.findViewById(R.id.RecyclerViewDevis);
        fab_add_devis = view.findViewById(R.id.fab_add_devis);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerViewDevis.setLayoutManager(linearLayoutManager);

        /**Récupération des informations serveur**/
        serveurNode = serveurNodeController.getServeurNodeInfos();
        /*URL Récupération de la liste des systèmes*/
        ApiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/DevisByStatu";
        utilisateur = UserSessionManager.getInstance(getActivity().getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();

        /**Statut devis**/
        devStatu = "E";

        listeDevisEnCours(ApiUrl01);



        /***Ajout de devis**/
        fab_add_devis.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), AddDevisActivity.class);
            startActivity(i);
        });

        return view;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Gestion de menu
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.devis_fragment_menu, menu);
        /**Gestion de menu recherche**/
        mSearchItem = menu.findItem(R.id.action_search);
        sv = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        sv.setIconified(true);

        SearchManager searchManager = (SearchManager)  getActivity().getSystemService(Context.SEARCH_SERVICE);
        sv.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                devisAdapter.getFilter().filter(query);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    /**Récupération de la liste de devis en cours**/
    public void listeDevisEnCours(String api_url){
        RequestQueue requestQueue = new Volley().newRequestQueue(getActivity().getApplicationContext());
        progressDialogInfo.show(getActivity().getSupportFragmentManager(), "Loading...");
        progressDialogInfo.setCancelable(false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {

            try{
                JSONArray array = new JSONArray(s);
                if (array.length() == 0)
                    frameLayout.setVisibility(View.VISIBLE);
                for (int i=0; i<array.length(); i++){
                    try{
                        JSONObject jsonObject = array.getJSONObject(i);
                        Devis devis = new Devis();
                        devis.setCliRasoc(jsonObject.getString("CLIRASOC"));
                        devis.setDevDaliv(jsonObject.getString("DEVDALIV"));
                        devis.setDevVadev(jsonObject.getDouble("DEVVADEV"));
                        devis.setDevComon(jsonObject.getString("DEVCOMONLIB"));
                        devis.setDevNudev(jsonObject.getString("DEVNUDEV"));
                        devis.setDevDadev(jsonObject.getString("DEVDADEV"));
                        devis.setDevLieuv(jsonObject.getString("DEVLIEUVLIB"));
                        devis.setDevRfdev(jsonObject.getString("DEVRFDEV"));
                        devis.setDevStatut(jsonObject.getString("DEVSTATULIB"));
                        devis.setDevComag(jsonObject.getString("DEVCOMAGLIB"));
                        devis.setDevColiv(jsonObject.getString("DEVCOLIVLIB"));

                        //Populariser la liste des produits
                        devisList.add(devis);

                        progressDialogInfo.cancel();
                    }catch(JSONException e){
                        e.printStackTrace();

                    }
                }
                devisAdapter = new DevisAdapter(getActivity().getApplicationContext(),devisList);
                recyclerViewDevis.setAdapter(devisAdapter);
            }catch(JSONException e){
                e.printStackTrace();

            }
        }, volleyError -> {
            volleyError.printStackTrace();
            progressDialogInfo.cancel();
            /**Erreur connexion**/
            Toast.makeText(getActivity(), getResources().getString(R.string.login_activity_Snackbar01_NoConnexion), Toast.LENGTH_LONG).show();
        })
        {
            protected Map<String,String> getParams(){
                Map<String, String> param = new HashMap<String, String>();
                param.put("systeme",systemeAdresse);
                param.put("login",utilisateurLogin);
                param.put("password",utilisateurPassword);
                param.put("devstatu", devStatu);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }

}
