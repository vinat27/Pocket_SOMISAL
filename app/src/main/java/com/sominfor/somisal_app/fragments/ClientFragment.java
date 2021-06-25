package com.sominfor.somisal_app.fragments;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.activities.ClientDetailsActivity;
import com.sominfor.somisal_app.activities.DelayedProgressDialog;
import com.sominfor.somisal_app.activities.LoginActivity;
import com.sominfor.somisal_app.adapters.ClientAdapter;
import com.sominfor.somisal_app.adapters.ClientSpinnerAdapter;
import com.sominfor.somisal_app.adapters.ProduitAdapter;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Client;
import com.sominfor.somisal_app.handler.models.Pays;
import com.sominfor.somisal_app.handler.models.Produit;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.interfaces.RecyclerViewClickListener;
import com.sominfor.somisal_app.utils.ApiReceiverMethods;
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
import static com.sominfor.somisal_app.fragments.FilterProduitFullDialog.TAG;

/**
 * Créé par vatsou le 25,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Répertoire de clients
 */
public class ClientFragment extends Fragment {
    IndexFastScrollRecyclerView mRecyclerView;
    public static List<Client> clients;
    ClientAdapter clientAdapter;
    String apiUrl01, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    public RequestQueue rq;
    private MenuItem mSearchItem;
    private SearchView sv;
    DelayedProgressDialog progressDialogInfo;
    ApiReceiverMethods apiReceiverMethods;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.client_fragment, container, false);
        /***** Déclaration de barre de menu dans le fragment*******/
        setHasOptionsMenu(true);
        getActivity().setTitle("Clients");

        if (!UserSessionManager.getInstance(getActivity()).isLoggedIn()) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));

        }

        /**Initialisation des objets**/
        serveurNodeController = new ServeurNodeController();
        /**Récupération des informations serveur**/
        serveurNode = serveurNodeController.getServeurNodeInfos();
        rq = Volley.newRequestQueue(getActivity());
        progressDialogInfo = new DelayedProgressDialog();
        clients = new ArrayList<>();
        apiReceiverMethods = new ApiReceiverMethods(getActivity().getApplicationContext());

        apiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/client/allClient";
        utilisateur = UserSessionManager.getInstance(getActivity().getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();
        utilisateurCosoc = utilisateur.getUtilisateurCosoc();
        utilisateurCoage = utilisateur.getUtilisateurCoage();

        /**initialisation des Widgets**/
        mRecyclerView =  view.findViewById(R.id.fast_scroller_recycler_client);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        recupererListeClients(apiUrl01);


        initialiseUI();

        /**Détails Client**/
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), mRecyclerView, new RecyclerViewClickListener(){
            @Override
            public void onClick(View view, int position) {
               Client client = clients.get(position);
                    //Produits
                    Intent i = new Intent(getActivity(), ClientDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("client", client);
                    i.putExtras(bundle);
                    startActivity(i);
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.client_fragment_menu, menu);
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
                clientAdapter.getFilter().filter(query);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
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


    /**Récupération de la liste de clients**/
    public void recupererListeClients(String api_url){
        RequestQueue requestQueue = new Volley().newRequestQueue(getActivity().getApplicationContext());
        progressDialogInfo.show(getActivity().getSupportFragmentManager(), "Loading...");
        progressDialogInfo.setCancelable(false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {
            try{
                JSONArray array = new JSONArray(s);
                for (int i=0; i<array.length(); i++){
                    try{
                        JSONObject jsonObject = array.getJSONObject(i);
                        Client client = new Client();

                        client.setCliNucli(jsonObject.getString("CLINUCLI"));
                        client.setCliNacli(jsonObject.getString("CLINACLI"));
                        client.setCliRasoc(jsonObject.getString("CLIRASOC").trim());
                        client.setCliAdre1(jsonObject.getString("CLIADRE1").trim());
                        client.setCliAdre2(jsonObject.getString("CLIADRE2").trim());
                        client.setCliBopos(jsonObject.getString("CLIBOPOS").trim());
                        client.setCliCopos(jsonObject.getString("CLICOPOS").trim());
                        client.setCliVille(jsonObject.getString("CLIVILLE").trim());
                        client.setCliCpays(jsonObject.getString("CLICPAYS").trim());
                        client.setCliLiNacli(jsonObject.getString("LIBNACLI").trim());
                        client.setCliLiComon(jsonObject.getString("LIBCOMON").trim());
                        client.setCliColiv(jsonObject.getString("CLICOLIV").trim());
                        client.setCliLiliv(jsonObject.getString("LIBCOLIV").trim());
                        client.setCliDereg(jsonObject.getString("CLIDEREG").trim());
                        client.setCliMoreg(jsonObject.getString("CLIMOREG").trim());
                        client.setCliCotrn(jsonObject.getString("CLICOTRN").trim());
                        client.setCliLitrn(jsonObject.getString("LIBCOTRN").trim());
                        client.setCliCotrp(jsonObject.getString("CLICOTRP").trim());
                        client.setCliLitrp(jsonObject.getString("LIBCOTRP").trim());
                        client.setClililivth(jsonObject.getString("LIBLIVTH").trim());
                        client.setCliRasol(jsonObject.getString("CLIRASOL").trim());
                        client.setCliAdr1l(jsonObject.getString("CLIADR1L").trim());
                        client.setCliAdr2l(jsonObject.getString("CLIADR2L").trim());
                        client.setCliCopol(jsonObject.getString("CLICOPOL").trim());
                        client.setCliVilll(jsonObject.getString("CLIVILLL").trim());
                        client.setCliBopol(jsonObject.getString("CLIBOPOL").trim());
                        client.setCliCpayl(jsonObject.getString("CLICPAYL").trim());
                        client.setCliNacpx(jsonObject.getString("CLINACPX"));
                        client.setCliCpgen(jsonObject.getString("CLICPGEN").trim());
                        client.setCliCpaux(jsonObject.getString("CLICPAUX").trim());
                        client.setCliComon(jsonObject.getString("CLICOMON").trim());
                        client.setCliZogeo(jsonObject.getString("CLIZOGEO"));

                        //Populariser la liste des clients
                        clients.add(client);
                        progressDialogInfo.cancel();
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
                clientAdapter = new ClientAdapter(getActivity().getApplicationContext(), clients);
                mRecyclerView.setAdapter(clientAdapter);
                mRecyclerView.setHasFixedSize(true);
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
                return param;
            }
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }
}
