package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.ClientAdapter;
import com.sominfor.somisal_app.adapters.ClientSpinnerAdapter;
import com.sominfor.somisal_app.adapters.ProduitsSearchableAdapter;
import com.sominfor.somisal_app.fragments.DevisPostItFullDialog;
import com.sominfor.somisal_app.fragments.FilterProduitFullDialog;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Client;
import com.sominfor.somisal_app.handler.models.Famille;
import com.sominfor.somisal_app.handler.models.Produit;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.interfaces.CallBackPostIt;
import com.sominfor.somisal_app.utils.UserSessionManager;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sominfor.somisal_app.activities.LoginActivity.protocole;

public class AddDevisActivity extends AppCompatActivity implements CallBackPostIt {
    public static final String TAG = AddDevisActivity.class.getSimpleName();
    MaterialButton BtnValider;
    List<Client> clients;
    ClientSpinnerAdapter clientSpinnerAdapter;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, apiUrl01;
    public RequestQueue rq;
    SearchableSpinner SsnDevCliRasoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_devis);

        /**Gestion du menu d'action**/
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Nouveau devis");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /**Effacer l'ombre sous l'actionBar**/
        getSupportActionBar().setElevation(0);
        /**Récupération de session utilisateur**/
        utilisateur = UserSessionManager.getInstance(getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();

        serveurNodeController = new ServeurNodeController();
        /**Récupération du serveur node**/
        serveurNode = serveurNodeController.getServeurNodeInfos();

        /**Initialisation de listes et de la requestQueue**/
        rq = Volley.newRequestQueue(getApplicationContext());
        clients = new ArrayList<>();

        /**URL Récupération de la liste des clients**/
        apiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/allClient";

        /**Instanciation des widgets**/
        SsnDevCliRasoc = findViewById(R.id.MbSpnCliRasoc);
        BtnValider = findViewById(R.id.BtnValider);

        /**Récupération de la liste de clients**/
        recupererListeClients(apiUrl01);

        BtnValider.setOnClickListener(v -> {
            Intent i = new Intent(AddDevisActivity.this, AddProduitDevisActivity.class);
            startActivity(i);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_devis_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.post_it_devis:
                /**Devis **/
                openDialog();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openDialog() {
        /***Filtre sur familles et sous familles**/
        FragmentManager fragmentManager = getSupportFragmentManager();
        DevisPostItFullDialog devisPostItFullDialog = DevisPostItFullDialog.newInstance();
        //devisPostItFullDialog.setTargetFragment(devisPostItFullDialog, 100);
        devisPostItFullDialog.show(fragmentManager, ServeurNode.TAG);
    }

    @Override
    public void onDataReceived(String DexTexte) {
        Toast.makeText(this, DexTexte, Toast.LENGTH_LONG).show();
    }

    /**Récupération de la liste de clients**/
    public void recupererListeClients(String api_url){
        RequestQueue requestQueue = new Volley().newRequestQueue(getApplicationContext());
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
                        client.setCliLiNacli(jsonObject.getString("LIBNACLI").trim());
                        //client.setCliLiCpays(jsonObject.getString(""));

                        //Populariser la liste des clients
                        clients.add(client);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
                clientSpinnerAdapter = new ClientSpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, clients);
                SsnDevCliRasoc.setAdapter(clientSpinnerAdapter);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }, volleyError -> {
            volleyError.printStackTrace();

        })
        {
            protected Map<String,String> getParams(){
                Map<String, String> param = new HashMap<String, String>();
                param.put("systeme",systemeAdresse);
                param.put("login",utilisateurLogin);
                param.put("password",utilisateurPassword);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }

}