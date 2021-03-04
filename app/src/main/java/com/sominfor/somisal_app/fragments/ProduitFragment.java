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
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.activities.FicheProduitActivity;
import com.sominfor.somisal_app.activities.LoginActivity;
import com.sominfor.somisal_app.adapters.ProduitAdapter;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Famille;
import com.sominfor.somisal_app.handler.models.Produit;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.interfaces.CallbackListener;
import com.sominfor.somisal_app.interfaces.RecyclerViewClickListener;
import com.sominfor.somisal_app.utils.RecyclerViewTouchListener;
import com.sominfor.somisal_app.utils.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

import static com.sominfor.somisal_app.activities.LoginActivity.protocole;

/**
 * Créé par vatsou le 08,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Répertoire de produits
 */
public class ProduitFragment extends Fragment implements CallbackListener {
    IndexFastScrollRecyclerView mRecyclerView;
    List<Produit> produits;
    ProduitAdapter produitAdapter;
    public RequestQueue rq;
    String ApiUrl01, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    private MenuItem mSearchItem;
    private SearchView sv;
    RelativeLayout relativeLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.produit_fragment, container, false);
        /***** Déclaration de barre de menu dans le fragment*******/
        setHasOptionsMenu(true);
        getActivity().setTitle("Produits");
        if (!UserSessionManager.getInstance(getActivity()).isLoggedIn()) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));

        }
        rq = Volley.newRequestQueue(getActivity());
        produits = new ArrayList<>();
        serveurNodeController = new ServeurNodeController();

        /**initialisation des Widgets**/
        mRecyclerView =  view.findViewById(R.id.fast_scroller_recycler_produits);
        relativeLayout = view.findViewById(R.id.Rlt01);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        /**Récupération des informations serveur**/
        serveurNode = serveurNodeController.getServeurNodeInfos();
        /*URL Récupération de la liste des systèmes*/
        ApiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/produit";
        utilisateur = UserSessionManager.getInstance(getActivity().getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();
        utilisateurCosoc = utilisateur.getUtilisateurCosoc();
        utilisateurCoage = utilisateur.getUtilisateurCoage();
        /**Récupération des produits**/
        recupererListeProduits(ApiUrl01);
        /**Configuration de la barre de scroll**/
        initialiseUI();
        /**Au clic d'un recyclerview**/
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), mRecyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Produit produit = produits.get(position);
                if (produit != null && produit.getPronuprm() != 0) {
                    //Produits
                    Intent i = new Intent(getActivity(), FicheProduitActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("produit", produit);
                    i.putExtras(bundle);
                    startActivity(i);
                }else{
                    Toast.makeText(getActivity(), getResources().getString(R.string.produit_fragment_noStock), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Gestion de menu
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            /***Filtre, ouverture du formulaire**/
            openDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.produit_fragment_menu, menu);
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
                produitAdapter.getFilter().filter(query);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void openDialog() {
        /***Filtre sur familles et sous familles**/
        FragmentManager fragmentManager = getFragmentManager();
        FilterProduitFullDialog filterProduitFullDialog = FilterProduitFullDialog.newInstance();
        filterProduitFullDialog.setTargetFragment(this, 100);
        filterProduitFullDialog.show(fragmentManager, ServeurNode.TAG);
    }

    @Override
    public void onDataReceived(Famille famille) {

    }

    /**Barre de choix de recherche par ordre alphabétique**/
    protected void initialiseUI() {

        mRecyclerView.setIndexTextSize(12);
        mRecyclerView.setIndexBarColor("#33334c");
        mRecyclerView.setIndexBarCornerRadius(3);
        mRecyclerView.setIndexBarTransparentValue((float) 0.6);
        mRecyclerView.setIndexbarMargin(0);
        mRecyclerView.setIndexbarWidth(70);
        mRecyclerView.setPreviewPadding(0);
        mRecyclerView.setIndexBarTextColor("#FFFFFF");

        mRecyclerView.setPreviewTextSize(60);
        mRecyclerView.setPreviewColor("#33334c");
        mRecyclerView.setPreviewTextColor("#FFFFFF");
        mRecyclerView.setPreviewTransparentValue(0.6f);

        mRecyclerView.setIndexBarVisibility(true);

        mRecyclerView.setIndexBarStrokeVisibility(true);
        mRecyclerView.setIndexBarStrokeWidth(1);
        mRecyclerView.setIndexBarStrokeColor("#000000");

        mRecyclerView.setIndexbarHighLightTextColor("#33334c");
        mRecyclerView.setIndexBarHighLightTextVisibility(true);

        Objects.requireNonNull(mRecyclerView.getLayoutManager()).scrollToPosition(0);
    }

    /**Récupération de la liste de produits**/
    public void recupererListeProduits(String api_url){
        RequestQueue requestQueue = new Volley().newRequestQueue(getActivity().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {

            try{
                JSONArray array = new JSONArray(s);
                for (int i=0; i<array.length(); i++){
                    try{
                        JSONObject jsonObject = array.getJSONObject(i);
                        Produit produit = new Produit();

                        produit.setProcopro(jsonObject.getInt("PROCOPRO"));
                        produit.setProlipro(jsonObject.getString("PROLIPRO").trim());
                        produit.setProcofam(jsonObject.getString("PROCOFAM"));
                        produit.setProsofam(jsonObject.getString("PROSOFAM"));
                        produit.setPronuprm(jsonObject.getInt("PRONUPRM"));
                        produit.setProunvte(jsonObject.getString("PROUNVTE").trim());
                        produit.setProcofvt(jsonObject.getInt("PROCOFVT"));
                        produit.setProliunvte(jsonObject.getString("DATA1").trim());
                        //Populariser la liste des produits
                        produits.add(produit);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
                produitAdapter = new ProduitAdapter(getActivity().getApplicationContext(),produits);
                mRecyclerView.setAdapter(produitAdapter);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }, volleyError -> {
            volleyError.printStackTrace();
            /**Erreur connexion**/
            Snackbar snackbar =  Snackbar.make(relativeLayout,getResources().getString(R.string.login_activity_Snackbar01_NoConnexion), Snackbar.LENGTH_LONG);
            snackbar.setBackgroundTint(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.red));
            snackbar.show();
        })
        {
            protected Map<String,String> getParams(){
                Map<String, String> param = new HashMap<String, String>();
                param.put("systeme",systemeAdresse);
                param.put("login",utilisateurLogin);
                param.put("password",utilisateurPassword);
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
}
