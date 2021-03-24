package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.DetailCommandeAdapter;
import com.sominfor.somisal_app.adapters.DetailDevisAdapter;
import com.sominfor.somisal_app.fragments.CommentairesCommandesFullDialog;
import com.sominfor.somisal_app.fragments.CommentairesDevisFullDialog;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Commande;
import com.sominfor.somisal_app.handler.models.DetailCommande;
import com.sominfor.somisal_app.handler.models.DetailDevis;
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

public class CommandeDetailsActivity extends AppCompatActivity {
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, apiUrl01, utilisateurCosoc, utilisateurCoage;
    Commande commande;
    RecyclerView RecyclerViewDetailsCommandes;
    List<DetailCommande> detailCommandeList;
    DetailCommandeAdapter detailCommandeAdapter;
    DelayedProgressDialog progressDialogInfo;
    TextView TxtClirasoc,TxtComStatu, TxtComLimag, TxtComLiliv, TxtComVacom, TxtComadre1, TxtComadre2, TxtComcopos, TxtComville, TxtCombopos, TxtComcpays, TxtComrasol, TxtComrasoc, TxtComadr1l, TxtComadr2l, TxtComcopol, TxtComvilll, TxtCombopol, TxtComcpayl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande_details);

        /**Contrôle de sessions utilisateur**/
        if (!UserSessionManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        /**Gestion du menu d'action**/
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Détails commande");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /**Effacer l'ombre sous l'actionBar**/
        getSupportActionBar().setElevation(0);

        /**Initialisation des widgets**/
        TxtClirasoc = findViewById(R.id.TxtClirasoc);
        TxtComStatu = findViewById(R.id.TxtComStatu);
        TxtComLimag = findViewById(R.id.TxtComLimag);
        TxtComLiliv = findViewById(R.id.TxtComLiliv);
        TxtComVacom = findViewById(R.id.TxtComVacom);
        TxtComadre1 = findViewById(R.id.TxtComadre1);
        TxtComadre2 = findViewById(R.id.TxtComadre2);
        TxtComcopos = findViewById(R.id.TxtComcopos);
        TxtComville = findViewById(R.id.TxtComville);
        TxtCombopos = findViewById(R.id.TxtCombopos);
        TxtComcpays = findViewById(R.id.TxtComcpays);
        TxtComrasol = findViewById(R.id.TxtComrasol);
        TxtComrasoc = findViewById(R.id.TxtComrasoc);
        TxtComadr1l = findViewById(R.id.TxtComadr1l);
        TxtComadr2l = findViewById(R.id.TxtComadr2l);
        TxtComcopol = findViewById(R.id.TxtComcopol);
        TxtComvilll = findViewById(R.id.TxtComvilll);
        TxtCombopol = findViewById(R.id.TxtCombopol);
        TxtComcpayl = findViewById(R.id.TxtComcpayl);

        serveurNodeController = new ServeurNodeController();
        progressDialogInfo = new DelayedProgressDialog();
        serveurNode = serveurNodeController.getServeurNodeInfos();
        detailCommandeList = new ArrayList<>();

        /**Récupération de session utilisateur**/
        utilisateur = UserSessionManager.getInstance(getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();
        utilisateurCosoc = utilisateur.getUtilisateurCosoc();
        utilisateurCoage = utilisateur.getUtilisateurCoage();

        /*URL Récupération de la liste des systèmes*/
        apiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/commande/detailCommande";

        /**Récupération de l'objet devis**/
        Bundle bundle = getIntent().getExtras();
        commande = (Commande) bundle.getSerializable("commande");
        String wvacom = String.format("%.2f", commande.getComvacom())+" "+commande.getComlimon();

        /**Set values to text**/
        TxtClirasoc.setText(commande.getComrasoc());
        TxtComStatu.setText(commande.getComlista());
        TxtComLimag.setText(commande.getComlimag());
        TxtComLiliv.setText(commande.getComliliv());
        TxtComVacom.setText(wvacom);
        TxtComadre1.setText(commande.getComadre1());
        TxtComadre2.setText(commande.getComadre2());
        TxtComcopos.setText(commande.getComcopos());
        TxtComville.setText(commande.getComville());
        TxtCombopos.setText(commande.getCombopos());
        TxtComcpays.setText(commande.getComlicpays());
        TxtComrasol.setText(commande.getComrasol());
        TxtComrasoc.setText(commande.getComrasoc());
        TxtComadr1l.setText(commande.getComadr1l());
        TxtComadr2l.setText(commande.getComadr2l());
        TxtComcopol.setText(commande.getComcopol());
        TxtComvilll.setText(commande.getComvilll());
        TxtCombopol.setText(commande.getCombopol());
        TxtComcpayl.setText(commande.getComlicpayr());

        RecyclerViewDetailsCommandes = findViewById(R.id.RecyclerViewDetailsCommandes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        RecyclerViewDetailsCommandes.setLayoutManager(linearLayoutManager);

        recupererDetailsCommande(apiUrl01, commande.getComnucom(), commande.getComcomon());

    }

    // Options Menu (ActionBar Menu)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fiche_commande_activity, menu);
        return true;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.comment_commande:
                /***Filtre sur familles et sous familles**/
                FragmentManager fragmentManager = getSupportFragmentManager();
                CommentairesCommandesFullDialog commentairesCommandesFullDialog = CommentairesCommandesFullDialog.newInstance();
                Bundle args = new Bundle();
                args.putString("comtxnpi", commande.getComtxhpd());
                args.putString("comtxnen", commande.getComtxhen());
                args.putString("coxtxt", commande.getCoxtexte());
                commentairesCommandesFullDialog.setArguments(args);
                commentairesCommandesFullDialog.show(fragmentManager, ServeurNode.TAG);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /***Récupérer les détails devis**/
    public void recupererDetailsCommande(String api_url, final String comNucom, final String comComon) {
        RequestQueue requestQueue = new Volley().newRequestQueue(getApplicationContext());
        progressDialogInfo.show(getSupportFragmentManager(), "Loading...");
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {

            try {
                JSONObject jsonObject = new JSONObject(s);
                /**Commentaires et Post-it***/
                commande.setComtxhen(jsonObject.getString("COMTXHEN"));
                commande.setComtxhpd(jsonObject.getString("COMTXHPI"));
                commande.setCoxtexte(jsonObject.getString("DCOTEXTE"));
                /**Formatage de l'array produit**/
                JSONArray array= jsonObject.getJSONArray("Produits");
                for(int i=0;i<array.length();i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    DetailCommande detailCommande = new DetailCommande();
                    detailCommande.setDcopocom(object1.getInt("DCOPOCOM"));
                    detailCommande.setDcoqtcom(object1.getDouble("DCOQTCOM"));
                    detailCommande.setDcoputar(object1.getDouble("DCOPUTAR"));
                    detailCommande.setDcotxrem(object1.getDouble("DCOTXREM"));
                    detailCommande.setDcovarem(object1.getDouble("DCOVAREM"));
                    detailCommande.setDcocopro(object1.getInt("DCOCOPRO"));
                    detailCommande.setDcounvte(object1.getString("DCOUNVTE"));
                    detailCommande.setDcolipro(object1.getString("PROLIPRO"));
                    detailCommande.setDcotxn(object1.getString("TXNTEXTE"));
                    detailCommande.setDconuprm(object1.getInt("DCONUPRM"));
                    detailCommande.setDcoqtliv(object1.getDouble("DCOQTLIV"));

                    detailCommandeList.add(detailCommande);

                    progressDialogInfo.cancel();
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                detailCommandeAdapter = new DetailCommandeAdapter(getApplicationContext(), detailCommandeList, fragmentManager);
                RecyclerViewDetailsCommandes.setAdapter(detailCommandeAdapter);
                RecyclerViewDetailsCommandes.setHasFixedSize(true);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }, volleyError -> {
            volleyError.printStackTrace();
            progressDialogInfo.cancel();
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("systeme",systemeAdresse);
                param.put("login",utilisateurLogin);
                param.put("password",utilisateurPassword);
                param.put("nucom", comNucom);
                param.put("comon", comComon);
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