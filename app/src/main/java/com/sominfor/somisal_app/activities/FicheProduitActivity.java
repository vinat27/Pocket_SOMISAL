package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.fragments.DevisPostItFullDialog;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Produit;
import com.sominfor.somisal_app.handler.models.ProduitFini;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.utils.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.sominfor.somisal_app.activities.LoginActivity.protocole;

public class FicheProduitActivity extends AppCompatActivity {
    Utilisateur utilisateur;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage, ApiUrl01;
    Produit produit;
    TextView TxtProLiPro, TxtProCoPro, TxtPrmRfprm, TxtPrmNuprm, TxtPrmLimrq, TxtPrmDecat, TxtPrmDefam;
    TextView TxtPrmLisfa, TxtPrmNacpt, TxtPrmQtMag, TxtPrmQtRes, TxtPrmQtcom, TxtPrmQtsto, TxtPrmQtsal;
    TextView TxtPrmDaden, TxtPrmDadso, TxtPrmDadin, TxtPrmPoids;
    TextView TxtPrmUnstk, TxtPrmLimag;
    public RequestQueue rq;
    DelayedProgressDialog progressDialogInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_produit);

        /**Contrôle de sessions utilisateur**/
        if (!UserSessionManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        /**Gestion du menu d'action**/
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Fiche Produit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /**Effacer l'ombre sous l'actionBar**/
        getSupportActionBar().setElevation(0);

        serveurNodeController = new ServeurNodeController();
        serveurNode = serveurNodeController.getServeurNodeInfos();
        progressDialogInfo = new DelayedProgressDialog();
        /*URL Récupération de la liste des systèmes*/
        ApiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/produit/ficheProduit";
        /**Instanciation des widgets**/
        TxtProLiPro = findViewById(R.id.TxtProLiPro);
        TxtProCoPro = findViewById(R.id.TxtProCoPro);
        TxtPrmRfprm = findViewById(R.id.TxtPrmRfprm);
        TxtPrmNuprm = findViewById(R.id.TxtPrmNuprm);
        TxtPrmLimrq = findViewById(R.id.TxtPrmLimrq);
        TxtPrmDecat = findViewById(R.id.TxtPrmDecat);
        TxtPrmDefam = findViewById(R.id.TxtPrmDefam);
        TxtPrmLisfa = findViewById(R.id.TxtPrmLisfa);
        TxtPrmNacpt = findViewById(R.id.TxtPrmNacpt);
        TxtPrmQtMag = findViewById(R.id.TxtPrmQtMag);
        TxtPrmQtRes = findViewById(R.id.TxtPrmQtRes);
        TxtPrmQtcom = findViewById(R.id.TxtPrmQtcom);
        TxtPrmQtsto = findViewById(R.id.TxtPrmQtsto);
        TxtPrmQtsal = findViewById(R.id.TxtPrmQtsal);
        TxtPrmDaden = findViewById(R.id.TxtPrmDaden);
        TxtPrmDadso = findViewById(R.id.TxtPrmDadso);
        TxtPrmDadin = findViewById(R.id.TxtPrmDadin);
        TxtPrmPoids = findViewById(R.id.TxtPrmPoids);
        TxtPrmUnstk = findViewById(R.id.TxtPrmUnstk);
        TxtPrmLimag = findViewById(R.id.TxtPrmLimag);

        /**Récupération de session utilisateur**/
        utilisateur = UserSessionManager.getInstance(getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();
        utilisateurCosoc = utilisateur.getUtilisateurCosoc();
        utilisateurCoage = utilisateur.getUtilisateurCoage();

        /**Récupération de l'objet produit**/
        Bundle bundle = getIntent().getExtras();
        produit = (Produit) bundle.getSerializable("produit");

        TxtProLiPro.setText(produit.getProlipro().trim().toUpperCase());
        TxtProCoPro.setText(String.valueOf(produit.getProcopro()));
        TxtPrmNuprm.setText(String.valueOf(produit.getPronuprm()));

        /**Récupération de la fiche produit**/
        interrogerStock(ApiUrl01, produit.getPronuprm());

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
        getMenuInflater().inflate(R.menu.menu_fiche_produit_activity, menu);
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

    /***Récupérer les informations de stocks**/
    public void interrogerStock(String api_url, final int proNuprm) {
        RequestQueue requestQueue = new Volley().newRequestQueue(getApplicationContext());
        progressDialogInfo.show(getSupportFragmentManager(), "Loading...");
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {
            final ProduitFini produitFini = new ProduitFini();
            try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonObjectInfo = jsonObject.getJSONObject("FicheProduit");
                    produitFini.setPrmrfprm(jsonObjectInfo.getString("PRMREFER"));
                    produitFini.setPrmlimrq(jsonObjectInfo.getString("PRMCOMRQLIB"));
                    produitFini.setPrmdecat(jsonObjectInfo.getString("PRMCATPMLIB"));
                    produitFini.setPrmdefam(jsonObjectInfo.getString("PRMCOFAMLIB"));
                    produitFini.setPrmlisfa(jsonObjectInfo.getString("PRMSOFAMLIB"));
                    produitFini.setPrmnacpt(jsonObjectInfo.getString("FAMNATCO"));
                    produitFini.setPrmqtsto(jsonObjectInfo.getDouble("WQSTO"));
                    /*Quantité reservée*/
                    //produitFini.setPrmqtres(jsonObjectInfo.getDouble());
                    produitFini.setPrmqtcom(jsonObjectInfo.getDouble("WQTCOM"));
                    produitFini.setPrmqtsto(jsonObjectInfo.getDouble("WQSTO"));
                    //produitFini.setPrmqtsal(jsonObjectInfo.getDouble(""));
                    produitFini.setPrmpriun(jsonObjectInfo.getDouble("PRUMP"));
                    produitFini.setPrmdepru(jsonObjectInfo.getDouble("MAGPUDEN"));
                    produitFini.setPrmvasto(jsonObjectInfo.getDouble("WVASTO"));
                    produitFini.setPrmdaden(jsonObjectInfo.getString("MAGDADEN"));
                    produitFini.setPrmdador(jsonObjectInfo.getString("MAGDADSO"));
                    produitFini.setPrmdadin(jsonObjectInfo.getString("MAGDADIN"));
                    produitFini.setPrmpoids(jsonObjectInfo.getDouble("PRMPOIDS"));
                    produitFini.setPrmunstk(jsonObjectInfo.getString("PRMUNSTKLIB"));
                    produitFini.setPrmlimag(jsonObjectInfo.getString("MAGCOMAG"));

                /**Conversion date**/
                @SuppressLint("SimpleDateFormat") SimpleDateFormat fromUser = new SimpleDateFormat("dd MMM yyyy");
                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
                String PrmDadenFormat = "";
                String PrmDadsoFormat = "";
                String PrmDadinFormat = "";
                /**Contrôle Date de dernière entrée**/
                if (!produitFini.getPrmdaden().equals("0001-01-01")){
                    try {
                        PrmDadenFormat = fromUser.format(Objects.requireNonNull(myFormat.parse(produitFini.getPrmdaden())));
                    }catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                /**Contrôle Date de dernière sortie**/
                if (!produitFini.getPrmdador().equals("0001-01-01")){
                    try {
                        PrmDadsoFormat = fromUser.format(Objects.requireNonNull(myFormat.parse(produitFini.getPrmdador())));
                    }catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                /**Contrôle Date inventaire**/
                if (!produitFini.getPrmdadin().equals("0001-01-01")){
                    try {
                        PrmDadinFormat = fromUser.format(Objects.requireNonNull(myFormat.parse(produitFini.getPrmdadin())));
                    }catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                /**Initialisation des information s de fiche de stock**/
                TxtPrmRfprm.setText(produitFini.getPrmrfprm().trim());
                TxtPrmLimrq.setText(produitFini.getPrmlimrq().trim());
                TxtPrmDecat.setText(produitFini.getPrmdecat().trim());
                TxtPrmDefam.setText(produitFini.getPrmdefam().trim());
                TxtPrmLisfa.setText(produitFini.getPrmlisfa().trim());
                TxtPrmNacpt.setText(produitFini.getPrmnacpt().trim());
                TxtPrmQtMag.setText(String.format("%.2f", produitFini.getPrmqtsto()));
                TxtPrmQtRes.setText("");
                TxtPrmQtcom.setText(String.format("%.2f", produitFini.getPrmqtcom()));
                TxtPrmQtsto.setText(String.format("%.2f", produitFini.getPrmqtsto()));
                TxtPrmQtsal.setText("");
                TxtPrmDaden.setText(PrmDadenFormat);
                TxtPrmDadso.setText(PrmDadsoFormat);
                TxtPrmDadin.setText(PrmDadinFormat);
                TxtPrmPoids.setText(String.valueOf(produitFini.getPrmpoids()));
                TxtPrmUnstk.setText(produitFini.getPrmunstk().trim());
                TxtPrmLimag.setText(produitFini.getPrmlimag().trim());

                progressDialogInfo.cancel();
            } catch (JSONException e) {
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
                param.put("nuprm", String.valueOf(proNuprm));
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