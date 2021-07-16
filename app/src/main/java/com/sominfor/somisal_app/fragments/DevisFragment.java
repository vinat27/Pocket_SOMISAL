package com.sominfor.somisal_app.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.core.view.MenuCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.sominfor.somisal_app.activities.AddProduitDevisActivity;
import com.sominfor.somisal_app.activities.DashboardActivity;
import com.sominfor.somisal_app.activities.DelayedProgressDialog;
import com.sominfor.somisal_app.activities.DevisArchivesActivity;
import com.sominfor.somisal_app.activities.DevisSoldesActivity;
import com.sominfor.somisal_app.activities.FicheDevisActivity;
import com.sominfor.somisal_app.activities.FicheProduitActivity;
import com.sominfor.somisal_app.activities.LoginActivity;
import com.sominfor.somisal_app.adapters.ClientSpinnerAdapter;
import com.sominfor.somisal_app.adapters.DevisAdapter;
import com.sominfor.somisal_app.adapters.LivreurSpinnerAdapter;
import com.sominfor.somisal_app.adapters.ProduitAdapter;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Client;
import com.sominfor.somisal_app.handler.models.CommandeFilterElements;
import com.sominfor.somisal_app.handler.models.DelaiLivraison;
import com.sominfor.somisal_app.handler.models.DelaiReglement;
import com.sominfor.somisal_app.handler.models.DetailDevis;
import com.sominfor.somisal_app.handler.models.Devis;
import com.sominfor.somisal_app.handler.models.Livreur;
import com.sominfor.somisal_app.handler.models.ModeReglement;
import com.sominfor.somisal_app.handler.models.Produit;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.interfaces.CommandeFilterListener;
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

import static com.sominfor.somisal_app.activities.AddProduitDevisActivity.FRAGMENT_DEVIS;
import static com.sominfor.somisal_app.activities.LoginActivity.protocole;
import static com.sominfor.somisal_app.fragments.CommandeFragment.tintMenuIcon;

/**
 * Créé par vatsou le 13,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Liste des devis
 */
public class DevisFragment extends Fragment {
    private static final int SPLASH_TIME = 4000;
    public static final String TAG = DevisFragment.class.getSimpleName();
    FrameLayout frameLayout;
    RecyclerView recyclerViewDevis;
    List<Devis> devisList;
    public static List<DelaiLivraison> delaiDevisLivraisons;
    public static List<Client> clientListDevis;
    public static List<Livreur> livreurListDevis;
    public static List<DelaiReglement> delaiReglementListDevis;
    public static List<ModeReglement> modeReglementListDevis;
    private static DevisFragment instance = null;
    JSONArray coactJson, coactJsonDel, podevJson, coproArray, nuprmArray, unvteArray, cofvtArray, qtdevArray, putarArray, txremArray, varemArray, texteArray;
    private MenuItem mSearchItem;
    private SearchView sv;
    DevisAdapter devisAdapter;
    FloatingActionButton fab_add_devis;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    String ApiUrl01, systemeAdresse, utilisateurLogin, utilisateurPassword, devStatu, apiUrl02, apiUrl03, apiUrl04, apiUrl05, apiUrl06, apiUrl07, utilisateurCosoc, utilisateurCoage, coactVal, coactDel;
    Utilisateur utilisateur;
    public RequestQueue rq;
    DelayedProgressDialog progressDialogInfo;
    ApiReceiverMethods apiReceiverMethods;

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
        clientListDevis = new ArrayList<>();
        livreurListDevis = new ArrayList<>();
        delaiReglementListDevis = new ArrayList<>();
        modeReglementListDevis = new ArrayList<>();
        delaiDevisLivraisons = new ArrayList<>();
        /**Initialisation instance**/
        instance = this;
        progressDialogInfo = new DelayedProgressDialog();
        serveurNodeController = new ServeurNodeController();
        apiReceiverMethods = new ApiReceiverMethods(getActivity().getApplicationContext());
        coactVal = "VAL";
        coactDel = "DEL";

        /***Instanciation des widgets***/
        frameLayout = view.findViewById(R.id.frameLayout);
        recyclerViewDevis = view.findViewById(R.id.RecyclerViewDevis);
        fab_add_devis = view.findViewById(R.id.fab_add_devis);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerViewDevis.setLayoutManager(linearLayoutManager);

        /**Récupération des informations serveur**/
        serveurNode = serveurNodeController.getServeurNodeInfos();
        /*URL Récupération de la liste des systèmes*/
        ApiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/devis/devisByStatu";
        apiUrl02 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/client/allClient";
        apiUrl03 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/parametre/allColiv";
        apiUrl04 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/parametre/allDereg";
        apiUrl05 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/parametre/allMoreg";
        apiUrl06 = protocole+"://"+serveurNode.getServeurNodeIp()+"/create/devis/Onedevis";
        apiUrl07 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allDlv";
        utilisateur = UserSessionManager.getInstance(getActivity().getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();
        utilisateurCosoc = utilisateur.getUtilisateurCosoc();
        utilisateurCoage = utilisateur.getUtilisateurCoage();

        /**Statut devis**/
        devStatu = "E";
        /**Récupération des devis en cours**/
        listeDevisEnCours(ApiUrl01);

        /**Initialisation - ParseJson**/
        /**Parse Json - Récupération des arrays**/
        podevJson = new JSONArray();
        coproArray = new JSONArray();
        nuprmArray = new JSONArray();
        unvteArray = new JSONArray();
        cofvtArray = new JSONArray();
        qtdevArray = new JSONArray();
        putarArray = new JSONArray();
        txremArray = new JSONArray();
        varemArray = new JSONArray();
        texteArray = new JSONArray();
        coactJson = new JSONArray();
        coactJsonDel = new JSONArray();

        clientListDevis = apiReceiverMethods.recupererListeClients(apiUrl02,systemeAdresse,utilisateurLogin,utilisateurPassword,utilisateurCosoc, utilisateurCoage);
        /**Récupération de la liste des livreurs**/
        livreurListDevis = apiReceiverMethods.recupererListeLivreurs(apiUrl03, systemeAdresse, utilisateurLogin, utilisateurPassword,utilisateurCosoc, utilisateurCoage);
        /**Récupération de la liste de délais de règlement**/
        delaiReglementListDevis = apiReceiverMethods.recupererDelaiReglements(apiUrl04, systemeAdresse, utilisateurLogin, utilisateurPassword,utilisateurCosoc, utilisateurCoage);
        /**Récupération de la liste des modes de règlement**/
        modeReglementListDevis = apiReceiverMethods.recupererModeReglements(apiUrl05, systemeAdresse, utilisateurLogin, utilisateurPassword,utilisateurCosoc, utilisateurCoage);
        delaiDevisLivraisons = apiReceiverMethods.recupererDlv(apiUrl07, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);

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
        switch (id) {
            case R.id.action_soldes:
                Intent intent = new Intent(getActivity(), DevisSoldesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            case R.id.action_archives:
                Intent it = new Intent(getActivity(), DevisArchivesActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);
                return true;
            case R.id.filter_:
                openFiltresDevis();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.devis_fragment_menu, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
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
        MenuItem menuItem = menu.findItem(R.id.filter_);
        if (menuItem != null) {
            tintMenuIcon(getActivity(), menuItem, android.R.color.black);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**Instance de fragment**/
    public static DevisFragment getInstance(){
        return instance;
    }

    /***Validation approuvée**/
    public void doValidatePositiveClick(Devis devisValidate) {
        try {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date currentDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        Date dateDevisLivraison = simpleDateFormat.parse(devisValidate.getDevDaliv());

            if (dateDevisLivraison.compareTo(currentDate) >= 0){
                for(int i=0;i<1;i++){
                    coactJson.put(coactVal);
                }

                /**Validation*/
                validerDevisToAs400(apiUrl06, devisValidate);

                DelayedProgressDialog pgDialog = new DelayedProgressDialog();
                pgDialog.show(getActivity().getSupportFragmentManager(), "Load");
                pgDialog.setCancelable(false);
                new Thread(() -> {

                    try {
                        Thread.sleep(SPLASH_TIME);
                        Intent i = new Intent(getActivity(), DashboardActivity.class);
                        i.putExtra("frgToLoad", FRAGMENT_DEVIS);
                        // Now start your activity
                        pgDialog.cancel();
                        startActivity(i);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        pgDialog.cancel();
                    }
                }).start();
            }else {
                Toast.makeText(getActivity(), getResources().getString(R.string.error_SAL09), Toast.LENGTH_LONG).show();
            }
        }catch (ParseException e){
            e.printStackTrace();
        }

    }

    /**Validation non approuvée**/
    public void doValidateNegativeClick(Devis dd) {
    }

    /***Suppression approuvée**/
    public void doDeleteDevisPositiveClick(Devis devisValidate) {
                for(int i=0;i<1;i++){
                    coactJsonDel.put(coactDel);
                }
                deleteDevis400(apiUrl06, devisValidate);
                /***Attente de 4s**/
                DelayedProgressDialog pgDialog = new DelayedProgressDialog();
                pgDialog.show(getActivity().getSupportFragmentManager(), "Load");
                pgDialog.setCancelable(false);
                new Thread(() -> {

                    try {
                        Thread.sleep(SPLASH_TIME);
                        Intent i = new Intent(getActivity(), DashboardActivity.class);
                        i.putExtra("frgToLoad", FRAGMENT_DEVIS);
                        // Now start your activity
                        pgDialog.cancel();
                        startActivity(i);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        pgDialog.cancel();
                    }
                }).start();
    }

    /**Validation non approuvée**/
    public void doDeleteDevisNegativeClick(Devis dd) {
    }

    /**Récupération de la liste de devis en cours**/
    public void listeDevisEnCours(String api_url){
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
                        Devis devis = new Devis();
                        devis.setCliRasoc(jsonObject.getString("CLIRASOC"));
                        devis.setDevDaliv(jsonObject.getString("DEVDALIV"));
                        devis.setDevVadev(jsonObject.getDouble("DEVVADEV"));
                        devis.setDevlimon(jsonObject.getString("DEVCOMONLIB").trim());
                        devis.setDevNudev(jsonObject.getString("DEVNUDEV"));
                        devis.setDevDadev(jsonObject.getString("DEVDADEV"));
                        devis.setDevLieuv(jsonObject.getString("DEVLIEUVLIB"));
                        devis.setDevColieuv(jsonObject.getString("DEVLIEUV"));
                        devis.setDevRfdev(jsonObject.getString("DEVRFDEV").trim());
                        devis.setDevStatut(jsonObject.getString("DEVSTATULIB"));
                        devis.setDevComag(jsonObject.getString("DEVCOMAG"));
                        devis.setDevLimag(jsonObject.getString("DEVCOMAGLIB"));
                        devis.setDevColiv(jsonObject.getString("DEVCOLIV").trim());
                        devis.setDevliliv(jsonObject.getString("DEVCOLIVLIB"));
                        devis.setDevNacli(jsonObject.getString("CLINACLI"));
                        devis.setDevNucli(jsonObject.getString("DEVNUCLI"));
                        devis.setDevMoreg(jsonObject.getString("DEVMOREG"));
                        devis.setDevDereg(jsonObject.getString("DEVDEREG"));
                        devis.setDevEcova(jsonObject.getDouble("DEVECOVA"));
                        devis.setDevTxesc(jsonObject.getDouble("DEVTXESC"));
                        devis.setDevUscom(jsonObject.getString("DEVUSCOM").trim());
                        devis.setDevComon(jsonObject.getString("DEVCOMON"));
                        //Populariser la liste des produits
                        devisList.add(devis);
                        progressDialogInfo.cancel();
                    }catch(JSONException e){
                        e.printStackTrace();
                        progressDialogInfo.cancel();

                    }
                }
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                devisAdapter = new DevisAdapter(getActivity(),devisList,fragmentManager);
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

    /**Validate devis**/
    public void validerDevisToAs400(String api_url, Devis dev){
        DelayedProgressDialog progressDialog = new DelayedProgressDialog();
        RequestQueue requestQueue = new Volley().newRequestQueue(getActivity().getApplicationContext());
        progressDialog.show(getActivity().getSupportFragmentManager(), "Loading...");
        progressDialog.setCancelable(false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {

            try{
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("succes") == "true"){
                    progressDialog.cancel();
                }else{
                    progressDialog.cancel();
                    Toast.makeText(getActivity().getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }
            }catch(JSONException e){
                e.printStackTrace();
            }

        }, volleyError -> {
            volleyError.printStackTrace();
            progressDialog.cancel();
        })
        {
            protected Map<String,String> getParams(){
                Map<String, String> param = new HashMap<String, String>();
                param.put("coact", coactJson.toString());
                param.put("login",utilisateurLogin);
                param.put("password",utilisateurPassword);
                param.put("systeme",systemeAdresse);
                param.put("cosoc", utilisateurCosoc);
                param.put("coage", utilisateurCoage);
                param.put("nudev",dev.getDevNudev());
                param.put("dadev",dev.getDevDadev());
                param.put("nucli",dev.getDevNucli());
                param.put("rfdev", dev.getDevRfdev());
                param.put("daval", dev.getDevDadev());
                param.put("lieuv", "");
                param.put("comag",dev.getDevComag());
                param.put("notes", "");
                param.put("uscom", dev.getDevUscom());
                param.put("txesc", String.valueOf(dev.getDevTxesc()));
                param.put("ecova", String.valueOf(dev.getDevEcova()));
                param.put("moexp", "");
                param.put("daliv", dev.getDevDaliv());
                param.put("coliv", dev.getDevColiv());
                param.put("moreg", dev.getDevMoreg());
                param.put("dereg", dev.getDevDereg());

                Log.v("Coact", coactJson.toString());

                return param;
            }
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }

    /**Delete devis**/
    public void deleteDevis400(String api_url, Devis dev){
        DelayedProgressDialog progressDialog = new DelayedProgressDialog();
        RequestQueue requestQueue = new Volley().newRequestQueue(getActivity().getApplicationContext());
        progressDialog.show(getActivity().getSupportFragmentManager(), "Loading...");
        progressDialog.setCancelable(false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {

            try{
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("succes") == "true"){
                    progressDialog.cancel();
                }else{
                    progressDialog.cancel();
                    Toast.makeText(getActivity().getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }
            }catch(JSONException e){
                e.printStackTrace();
            }

        }, volleyError -> {
            volleyError.printStackTrace();
            progressDialog.cancel();
        })
        {
            protected Map<String,String> getParams(){
                Map<String, String> param = new HashMap<>();
                param.put("coact", coactJsonDel.toString());
                param.put("login",utilisateurLogin);
                param.put("password",utilisateurPassword);
                param.put("systeme",systemeAdresse);
                param.put("cosoc", utilisateurCosoc);
                param.put("coage", utilisateurCoage);
                param.put("nudev",dev.getDevNudev());
                param.put("dadev",dev.getDevDadev());
                param.put("nucli",dev.getDevNucli());
                param.put("rfdev", dev.getDevRfdev());
                param.put("daval", dev.getDevDadev());
                param.put("lieuv", "");
                param.put("comag",dev.getDevComag());
                param.put("notes", "");
                param.put("uscom", dev.getDevUscom());
                param.put("txesc", String.valueOf(dev.getDevTxesc()));
                param.put("ecova", String.valueOf(dev.getDevEcova()));
                param.put("moexp", "");
                param.put("daliv", dev.getDevDaliv());
                param.put("coliv", dev.getDevColiv());
                param.put("moreg", dev.getDevMoreg());
                param.put("dereg", dev.getDevDereg());

                return param;
            }
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }

    public void onDataReceivedDevis(CommandeFilterElements commandeFilterElements) {
        /**Transformation de date des informations reçues**/
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date dateInf = simpleDateFormat.parse(commandeFilterElements.getDateInf());
            Date dateSup = simpleDateFormat.parse(commandeFilterElements.getDateSup());

            devisAdapter.filterDateRange(dateInf, dateSup);
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    /**Ouvrir fenetre de filtres**/
    private void openFiltresDevis() {
        /***Filtre sur les commandes en cours**/
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FiltresDevisDialog filtresDevisDialog = FiltresDevisDialog.newInstance();
        filtresDevisDialog.show(fragmentManager, ServeurNode.TAG);
    }

    private DevisFilterListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (DevisFilterListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement MyListener");
        }
    }
}
