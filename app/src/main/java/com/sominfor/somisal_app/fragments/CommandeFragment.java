package com.sominfor.somisal_app.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.drawable.DrawableCompat;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.activities.AddCommandeActivity;
import com.sominfor.somisal_app.activities.AddDevisActivity;
import com.sominfor.somisal_app.activities.CommandesCoursActivity;
import com.sominfor.somisal_app.activities.CommandesPreparees;
import com.sominfor.somisal_app.activities.CommandesSoldees;
import com.sominfor.somisal_app.activities.DashboardActivity;
import com.sominfor.somisal_app.activities.DelayedProgressDialog;
import com.sominfor.somisal_app.activities.DevisSoldesActivity;
import com.sominfor.somisal_app.activities.LoginActivity;
import com.sominfor.somisal_app.adapters.CommandeAdapter;
import com.sominfor.somisal_app.adapters.CommandeAttenteAdapter;
import com.sominfor.somisal_app.adapters.DevisAdapter;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Client;
import com.sominfor.somisal_app.handler.models.Commande;
import com.sominfor.somisal_app.handler.models.CommandeFilterElements;
import com.sominfor.somisal_app.handler.models.DelaiLivraison;
import com.sominfor.somisal_app.handler.models.DetailCommande;
import com.sominfor.somisal_app.handler.models.Devis;
import com.sominfor.somisal_app.handler.models.Livreur;
import com.sominfor.somisal_app.handler.models.Magasin;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Tournee;
import com.sominfor.somisal_app.handler.models.Transport;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.interfaces.CommandeFilterListener;
import com.sominfor.somisal_app.utils.ApiReceiverMethods;
import com.sominfor.somisal_app.utils.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.sominfor.somisal_app.activities.LoginActivity.protocole;

/**
 * Créé par vatsou le 26,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Répertoire des commandes
 */
public class CommandeFragment extends Fragment {
    /***Variables globales**/
    public static String FRAGMENT_COMMANDE = "3";
    public static final String TAG = CommandeFragment.class.getSimpleName();
    private static final int SPLASH_TIME = 4000;
    public static List<Client> clientListCde;
    static CommandeFragment instanceCommandeFragment = null;
    public static List<Tournee> tourneeListCdeFragment;
    public static List<Livreur> livreurListCdeFragment;
    public static List<Transport> transportListCdeFragment;
    public static List<Magasin> magasinsCdeFragment;
    public static List<DelaiLivraison> delaiLivraisons;
    /***Widgets**/
    RecyclerView recyclerViewCde;
    FrameLayout frameLayout;
    private MenuItem mSearchItem;
    private SearchView sv;
    RequestQueue rq;
    FloatingActionButton fab_add_commande;
    /**Classe d'objets*/
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    String apiUrl01, systemeAdresse, utilisateurLogin, utilisateurPassword, comStatu, apiUrl02, coactDel, apiUrl03, apiUrl04, apiUrl05, apiUrl06, apiUrl07, apiUrl08, utilisateurCosoc, utilisateurCoage;
    Utilisateur utilisateur;
    DelayedProgressDialog progressDialogInfo;
    List<Commande> commandeList;
    CommandeAttenteAdapter commandeAttenteAdapter;
    ApiReceiverMethods apiReceiverMethods;
    JSONArray coactJson;

    public static CommandeFragment newInstance(){ return new CommandeFragment(); }

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
        /**Initialisation instance**/
        instanceCommandeFragment = this;
        /***Instanciation des widgets***/
        frameLayout = view.findViewById(R.id.frameLayout);
        recyclerViewCde = view.findViewById(R.id.RecyclerViewCde);
        fab_add_commande = view.findViewById(R.id.fab_add_commande);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerViewCde.setLayoutManager(linearLayoutManager);
        /**Initialisation code opération et statut**/
        coactDel = "DEL";
        comStatu = "I";
        /***Initialisation liste et instanciation de classes**/
        progressDialogInfo = new DelayedProgressDialog();
        serveurNodeController = new ServeurNodeController();
        commandeList = new ArrayList<>();
        apiReceiverMethods = new ApiReceiverMethods(getActivity().getApplicationContext());
        clientListCde = new ArrayList<>();
        tourneeListCdeFragment = new ArrayList<>();
        livreurListCdeFragment = new ArrayList<>();
        transportListCdeFragment = new ArrayList<>();
        delaiLivraisons = new ArrayList<>();
        /**Récupération des informations serveur**/
        serveurNode = serveurNodeController.getServeurNodeInfos();
        /*URL Récupération de la liste des systèmes*/
        apiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/commande/commandeByStatu";
        apiUrl02 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/client/allClient";
        apiUrl03 = protocole+"://"+serveurNode.getServeurNodeIp()+"/create/commande/OneCommande";
        apiUrl04 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allCotrn";
        apiUrl05 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allColiv";
        apiUrl06 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allCotrp";
        apiUrl07 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allComag";
        apiUrl08 = protocole + "://" + serveurNode.getServeurNodeIp() + "/read/parametre/allDlv";
        utilisateur = UserSessionManager.getInstance(getActivity().getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();
        utilisateurCosoc = utilisateur.getUtilisateurCosoc();
        utilisateurCoage = utilisateur.getUtilisateurCoage();
        coactJson = new JSONArray();
        /**Récupération de la liste des clients, tournées, transport et livreur**/
        clientListCde = apiReceiverMethods.recupererListeClients(apiUrl02,systemeAdresse,utilisateurLogin,utilisateurPassword,utilisateurCosoc, utilisateurCoage);
        tourneeListCdeFragment = apiReceiverMethods.recupererListeTournees(apiUrl04, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        livreurListCdeFragment = apiReceiverMethods.recupererListeLivreurs(apiUrl05, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        transportListCdeFragment = apiReceiverMethods.recupererListeTransport(apiUrl06, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        magasinsCdeFragment = apiReceiverMethods.recupererListeMagasins(apiUrl07, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
        delaiLivraisons = apiReceiverMethods.recupererDlv(apiUrl08, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage);
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
        switch (id){
            case R.id.action_cours:
                Intent intt = new Intent(getActivity(), CommandesCoursActivity.class);
                intt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intt);
                return true;
            case R.id.action_soldes:
                Intent intent = new Intent(getActivity(), CommandesSoldees.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            case R.id.action_preparation:
                Intent i = new Intent(getActivity(), CommandesPreparees.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                return true;
            case R.id.filter_:
                openFiltresCommandes();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onDataReceived(CommandeFilterElements commandeFilterElements) {
        /**Transformation de date des informations reçues**/
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date dateInf = simpleDateFormat.parse(commandeFilterElements.getDateInf());
            Date dateSup = simpleDateFormat.parse(commandeFilterElements.getDateSup());

            Log.v("Date", commandeFilterElements.getDateInf());
            commandeAttenteAdapter.filterDateRange(dateInf, dateSup);
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.commande_fragment_menu, menu);
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
                commandeAttenteAdapter.getFilter().filter(query);
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
    public static CommandeFragment getInstance(){
        return instanceCommandeFragment;
    }

    /***Suppression approuvée**/
    public void doDeletePositiveClick(Commande commandeDelete) {
                for(int i=0;i<1;i++){
                    coactJson.put(coactDel);
                }
                /**Suppresion**/
                deleteCommande(apiUrl03, commandeDelete);
                DelayedProgressDialog pgDialog = new DelayedProgressDialog();
                pgDialog.show(getActivity().getSupportFragmentManager(), "Load");
                pgDialog.setCancelable(false);
                new Thread(() -> {
                    try {
                        Thread.sleep(SPLASH_TIME);
                        Intent i = new Intent(getActivity(), DashboardActivity.class);
                        i.putExtra("frgToLoad", FRAGMENT_COMMANDE);
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
    public void doDeleteNegativeClick(Commande commandeDelete) {
    }

    /**Récupération de la liste de commandes en cours**/
    public void listeCommandesEnCours(String api_url){
        RequestQueue requestQueue = new Volley().newRequestQueue(getActivity().getApplicationContext());
        progressDialogInfo.show(getActivity().getSupportFragmentManager(), "Loading...");
        progressDialogInfo.setCancelable(false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {
            Log.v("Commandes",s);
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
                        commande.setComvacom(jsonObject.getDouble("COMVACOM"));
                        commande.setComnucom(jsonObject.getString("COMNUCOM"));
                        commande.setComnucli(jsonObject.getString("COMNUCLI"));
                        commande.setComdacom(jsonObject.getString("COMDACOM"));
                        commande.setComlieuv(jsonObject.getString("COMLIEUV"));
                        commande.setComcomag(jsonObject.getString("COMCOMAG"));
                        commande.setComuscom(jsonObject.getString("COMUSCOM").trim());
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
                        commande.setComcotrp(jsonObject.getString("COMCOTRP"));
                        commande.setComcotrn(jsonObject.getString("COMCOTRN").trim());
                        commande.setComcoliv(jsonObject.getString("COMCOLIV").trim());
                        commande.setComcpays(jsonObject.getString("COMCPAYS"));
                        commande.setComcpayl(jsonObject.getString("COMCPAYL"));
                        commande.setComdereg(jsonObject.getString("COMDEREG"));
                        commande.setCommoreg(jsonObject.getString("COMMOREG"));
                        commande.setComcomon(jsonObject.getString("COMCOMON"));
                        commande.setComnacli(jsonObject.getString("CLINACLI"));

                        //Populariser la liste des commandes
                        commandeList.add(commande);
                        progressDialogInfo.cancel();
                    }catch(JSONException e){
                        e.printStackTrace();
                        progressDialogInfo.cancel();
                    }
                }
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                commandeAttenteAdapter = new CommandeAttenteAdapter(getActivity(),commandeList,fragmentManager);
                recyclerViewCde.setAdapter(commandeAttenteAdapter);
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
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }

    /**Suppression commandes**/
    public void deleteCommande(String api_url, Commande com){
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
                param.put("nucom",com.getComnucom());
                param.put("dacom",com.getComdacom());
                param.put("nucli",com.getComnucli());
                param.put("namar", "");
                param.put("lieuv", com.getComlieuv());
                param.put("comag", com.getComcomag());
                param.put("notes", "");
                param.put("uscom", com.getComuscom());
                param.put("txesc", "0");
                param.put("ecova", "0");
                param.put("daliv", com.getComdaliv());
                param.put("cotrp", com.getComcotrp());
                param.put("cotrn", com.getComcotrn());
                param.put("coliv", com.getComcoliv());
                param.put("moreg", "");
                param.put("dereg", "");
                param.put("cosoc", utilisateurCosoc);
                param.put("coage", utilisateurCoage);

                Log.v("param", param.toString());

                return param;
            }
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }

    /**Set Color to menu icon*/
    public static void tintMenuIcon(Context context, MenuItem item, @ColorRes int color) {
        Drawable normalDrawable = item.getIcon();
        Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
        DrawableCompat.setTint(wrapDrawable, context.getResources().getColor(color));

        item.setIcon(wrapDrawable);
    }

    /**Ouvrir fenetre de filtres**/
    private void openFiltresCommandes() {
        /***Filtre sur les commandes en cours**/
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FiltresCommandesDialog filtresCommandesDialog = FiltresCommandesDialog.newInstance();

        //devisPostItFullDialog.setTargetFragment(devisPostItFullDialog, 100);
        filtresCommandesDialog.show(fragmentManager, ServeurNode.TAG);
    }

    private CommandeFilterListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (CommandeFilterListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement MyListener");
        }
    }

}
