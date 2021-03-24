package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
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
import com.sominfor.somisal_app.adapters.CommandeAdapter;
import com.sominfor.somisal_app.adapters.CommandeClientAdapter;
import com.sominfor.somisal_app.adapters.DevisAdapter;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Commande;
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

public class ShowCommandeUserActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    RecyclerView recyclerViewCommande;
    List<Commande> commandeList;
    CommandeClientAdapter commandeClientAdapter;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, apiUrl02, utilisateurCosoc, utilisateurCoage;
    Utilisateur utilisateur;
    public RequestQueue rq;
    DelayedProgressDialog progressDialogInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_commande_user);

        /**Controle orientation***/
        if(!getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        /**Contrôle de sessions utilisateur**/
        if (!UserSessionManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        /**Gestion du menu d'action**/
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Mes Commandes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        serveurNodeController = new ServeurNodeController();
        serveurNode = serveurNodeController.getServeurNodeInfos();
        commandeList = new ArrayList<>();
        progressDialogInfo = new DelayedProgressDialog();
        apiUrl02 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/commande/commandeByClientAndSatut";
        /**Récupération de session utilisateur**/
        utilisateur = UserSessionManager.getInstance(getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();
        utilisateurCosoc = utilisateur.getUtilisateurCosoc();
        utilisateurCoage = utilisateur.getUtilisateurCoage();

        /***Instanciation des widgets***/
        frameLayout = findViewById(R.id.frameLayout);
        recyclerViewCommande = findViewById(R.id.RecyclerViewCommande);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerViewCommande.setLayoutManager(linearLayoutManager);

        listeCommandesEnCours(apiUrl02);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**Controle de l'orientation écran**/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    /**Récupération de la liste de commandes en cours**/
    public void listeCommandesEnCours(String api_url){
        RequestQueue requestQueue = new Volley().newRequestQueue(getApplicationContext());
        progressDialogInfo.show(getSupportFragmentManager(), "Loading...");
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
                        commande.setComcotrn(jsonObject.getString("COMCOTRN"));
                        commande.setComcoliv(jsonObject.getString("COMCOLIV").trim());
                        commande.setComcpays(jsonObject.getString("COMCPAYS"));
                        commande.setComcpayl(jsonObject.getString("COMCPAYL"));
                        commande.setComdereg(jsonObject.getString("COMDEREG"));
                        commande.setCommoreg(jsonObject.getString("COMMOREG"));
                        commande.setComcomon(jsonObject.getString("COMCOMON"));

                        //Populariser la liste des commandes
                        commandeList.add(commande);
                        progressDialogInfo.cancel();
                    }catch(JSONException e){
                        e.printStackTrace();
                        progressDialogInfo.cancel();
                    }
                }
                commandeClientAdapter = new CommandeClientAdapter(this,commandeList);
                recyclerViewCommande.setAdapter(commandeClientAdapter);
            }catch(JSONException e){
                e.printStackTrace();

            }
        }, volleyError -> {
            volleyError.printStackTrace();
            progressDialogInfo.cancel();
            /**Erreur connexion**/
            Toast.makeText(this, getResources().getString(R.string.login_activity_Snackbar01_NoConnexion), Toast.LENGTH_LONG).show();
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