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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.activities.AddCommandeActivity;
import com.sominfor.somisal_app.activities.AddDevisActivity;
import com.sominfor.somisal_app.activities.DelayedProgressDialog;
import com.sominfor.somisal_app.activities.LoginActivity;
import com.sominfor.somisal_app.adapters.CommandeAdapter;
import com.sominfor.somisal_app.adapters.DevisAdapter;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Commande;
import com.sominfor.somisal_app.handler.models.Devis;
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
 * Créé par vatsou le 26,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Répertoire des commandes
 */
public class CommandeFragment extends Fragment {
    FrameLayout frameLayout, container;
    RecyclerView recyclerViewCde;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    String apiUrl01, systemeAdresse, utilisateurLogin, utilisateurPassword, comStatu, apiUrl02, apiUrl03, apiUrl04, apiUrl05, utilisateurCosoc, utilisateurCoage;
    Utilisateur utilisateur;
    public RequestQueue rq;
    DelayedProgressDialog progressDialogInfo;
    List<Commande> commandeList;
    private MenuItem mSearchItem;
    private SearchView sv;
    CommandeAdapter commandeAdapter;
    FloatingActionButton fab_add_commande;
    //TODO Libellé Statut - Libellé Monnaie - Commentaire commande - Post-it - CadZon sur les montants
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.commande_fragment, container, false);
        /***** Déclaration de barre de menu dans le fragment*******/
        setHasOptionsMenu(true);
        getActivity().setTitle("Commandes");
        /**Contrôle session***/
        if (!UserSessionManager.getInstance(getActivity()).isLoggedIn()) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        /***Instanciation des widgets***/
        frameLayout = view.findViewById(R.id.frameLayout);
        container = view.findViewById(R.id.container);
        recyclerViewCde = view.findViewById(R.id.RecyclerViewCde);
        fab_add_commande = view.findViewById(R.id.fab_add_commande);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerViewCde.setLayoutManager(linearLayoutManager);

        progressDialogInfo = new DelayedProgressDialog();
        serveurNodeController = new ServeurNodeController();
        commandeList = new ArrayList<>();
        /**Récupération des informations serveur**/
        serveurNode = serveurNodeController.getServeurNodeInfos();
        /*URL Récupération de la liste des systèmes*/
        apiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/parametre/commandeByStatu";
        utilisateur = UserSessionManager.getInstance(getActivity().getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();
        utilisateurCosoc = utilisateur.getUtilisateurCosoc();
        utilisateurCoage = utilisateur.getUtilisateurCoage();

        comStatu = "E";
        /**Liste de commandes**/
        listeCommandesEnCours(apiUrl01);
        /**Ajout de commande**/
        fab_add_commande.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), AddCommandeActivity.class);
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
        inflater.inflate(R.menu.commande_fragment_menu, menu);
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
                commandeAdapter.getFilter().filter(query);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }



    /**Récupération de la liste de devis en cours**/
    public void listeCommandesEnCours(String api_url){
        RequestQueue requestQueue = new Volley().newRequestQueue(getActivity().getApplicationContext());
        progressDialogInfo.show(getActivity().getSupportFragmentManager(), "Loading...");
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
                        Commande commande = new Commande();
                        commande.setComrasoc(jsonObject.getString("COMRASOC"));
                        commande.setComdaliv(jsonObject.getString("COMDALIV"));
                        commande.setComliliv(jsonObject.getString("LIBCOLIV").trim());
                        commande.setComvacom(jsonObject.getDouble("DIVVACOM"));
                        commande.setComnucom(jsonObject.getString("COMNUCOM"));
                        commande.setComdacom(jsonObject.getString("COMDACOM"));
                        commande.setComlilieuv(jsonObject.getString("LIBLIEUV").trim());
                        commande.setComlitrn(jsonObject.getString("LIBCOTRN").trim());
                        commande.setComstatu(jsonObject.getString("COMSTATU"));
                        commande.setComlista(jsonObject.getString("LIBSTATU"));
                        commande.setComlimag(jsonObject.getString("LIBCOMAG"));
                        commande.setComlimon(jsonObject.getString("LIBCOMON").trim());
                        commande.setComadre1(jsonObject.getString("COMADRE1").trim());
                        commande.setComadre2(jsonObject.getString("COMADRE2").trim());
                        commande.setComcopos(jsonObject.getString("COMCOPOS").trim());
                        commande.setComville(jsonObject.getString("COMVILLE").trim());
                        commande.setCombopos(jsonObject.getString("COMBOPOS").trim());
                        commande.setComlicpays(jsonObject.getString("LIBCPAYS").trim());
                        commande.setComrasol(jsonObject.getString("COMRASOL").trim());
                        commande.setComadr1l(jsonObject.getString("COMADR1L").trim());
                        commande.setComadr2l(jsonObject.getString("COMADR2L").trim());
                        commande.setComcopol(jsonObject.getString("COMCOPOL").trim());
                        commande.setComvilll(jsonObject.getString("COMVILLL").trim());
                        commande.setComlicpayr(jsonObject.getString("LIBCPAYL").trim());
                        commande.setCombopol(jsonObject.getString("COMBOPOL").trim());

                        //Populariser la liste des commandes
                        commandeList.add(commande);
                        progressDialogInfo.cancel();
                    }catch(JSONException e){
                        e.printStackTrace();
                        progressDialogInfo.cancel();

                    }
                }
                commandeAdapter = new CommandeAdapter(getActivity(),commandeList);
                recyclerViewCde.setAdapter(commandeAdapter);
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
                param.put("cosoc", utilisateurCosoc);
                param.put("coage", utilisateurCoage);
                param.put("statu", comStatu);
                return param;
            }
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }
}
