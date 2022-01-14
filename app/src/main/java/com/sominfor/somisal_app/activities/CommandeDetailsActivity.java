package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;
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
import com.sominfor.somisal_app.utils.ApiReceiverMethods;
import com.sominfor.somisal_app.utils.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.sominfor.somisal_app.activities.LoginActivity.protocole;

public class CommandeDetailsActivity extends AppCompatActivity {
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, apiUrl01, utilisateurCosoc, utilisateurCoage, ComDalivFormat, apiurl02;
    Commande commande, commandeInfos;
    RecyclerView RecyclerViewDetailsCommandes;
    List<DetailCommande> detailCommandeList;
    DetailCommandeAdapter detailCommandeAdapter;
    DelayedProgressDialog progressDialogInfo;
    ApiReceiverMethods apiReceiverMethods;
    Chip chip;
    TextView TxtClirasoc,TxtComStatu, TxtComLimag, TxtComLiliv, TxtComVacom, TxtComadre1, TxtComadre2, TxtComcopos, TxtComville, TxtCombopos, TxtComcpays,TxtComDaliv, TxtComrasol, TxtComrasoc, TxtComadr1l, TxtComadr2l, TxtComcopol, TxtComvilll, TxtCombopol, TxtComcpayl;
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
        /**Récupération de l'objet devis**/
        Bundle bundle = getIntent().getExtras();
        commande = (Commande) bundle.getSerializable("commande");

        /**Gestion du menu d'action**/
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(Html.fromHtml("<small>Commande N°"+commande.getComnucom()+"</small>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /**Effacer l'ombre sous l'actionBar**/
        getSupportActionBar().setElevation(0);

        apiReceiverMethods = new ApiReceiverMethods(getApplicationContext());

        /**Initialisation des widgets**/
        TxtClirasoc = findViewById(R.id.TxtClirasoc);
        TxtComStatu = findViewById(R.id.TxtComStatu);
        TxtComLimag = findViewById(R.id.TxtComLimag);
        TxtComDaliv = findViewById(R.id.TxtComDaliv);
        TxtComVacom = findViewById(R.id.TxtComVacom);
        TxtComrasol = findViewById(R.id.TxtComrasol);
        TxtComadr1l = findViewById(R.id.TxtComadr1l);
        TxtComadr2l = findViewById(R.id.TxtComadr2l);
        TxtComcopol = findViewById(R.id.TxtComcopol);
        TxtComvilll = findViewById(R.id.TxtComvilll);
        TxtCombopol = findViewById(R.id.TxtCombopol);
        TxtComcpayl = findViewById(R.id.TxtComcpayl);
        chip = findViewById(R.id.chip);

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
        apiurl02 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/commande/commandeByNucom";


        BigDecimal bd = new BigDecimal(commande.getComvacom());
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');

        DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);
        formatter.setRoundingMode(RoundingMode.DOWN);
        String wvacom = formatter.format(bd.floatValue()) + " " + commande.getComlimon();

        /**Set values to text**/
        TxtClirasoc.setText(commande.getComrasoc());
        TxtComVacom.setText(wvacom);

        /**Récupération des information en-tête commande**/
        recupererEnteteCommande(apiurl02, commande.getComnucom());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat fromUser = new SimpleDateFormat("dd MMM yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            ComDalivFormat = fromUser.format(Objects.requireNonNull(myFormat.parse(commande.getComdaliv())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TxtComDaliv.setText(ComDalivFormat);

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

    /**Récupération des informations En-tête commande**/
    public void recupererEnteteCommande(String api_url, final String comNucom){
        RequestQueue requestQueue = new Volley().newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {
            commandeInfos = new Commande();
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonObjectInfo = jsonObject.getJSONObject("FicheCommande");

                commandeInfos.setComstatu(jsonObjectInfo.getString("COMSTATU"));
                commandeInfos.setComlimag(jsonObjectInfo.getString("LIBCOMAG").trim());
                commandeInfos.setComrasol(jsonObjectInfo.getString("COMRASOL").trim());
                commandeInfos.setComadr1l(jsonObjectInfo.getString("COMADR1L").trim());
                commandeInfos.setComcopol(jsonObjectInfo.getString("COMCOPOL").trim());
                commandeInfos.setComvilll(jsonObjectInfo.getString("COMVILLL").trim());
                commandeInfos.setCombopol(jsonObjectInfo.getString("COMBOPOL").trim());
                commandeInfos.setComlicpayr(jsonObjectInfo.getString("LIBCPAYS").trim());
                /**Récupération de libellé statut**/
                String statut = apiReceiverMethods.recupererStatutCommande(commandeInfos.getComstatu());

                /**Set values to Textviews**/
                TxtComLimag.setText(commandeInfos.getComlimag());
                TxtComrasol.setText(commandeInfos.getComrasol());
                TxtComStatu.setText(statut);
                TxtComadr1l.setText(commandeInfos.getComadr1l());
                TxtComadr2l.setText(commandeInfos.getComadr2l());
                TxtComcopol.setText(commandeInfos.getComcopol());
                TxtComvilll.setText(commandeInfos.getComvilll());
                TxtCombopol.setText(commandeInfos.getCombopol());
                TxtComcpayl.setText(commandeInfos.getComlicpayr());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, volleyError -> {
            volleyError.printStackTrace();
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("systeme",systemeAdresse);
                param.put("login",utilisateurLogin);
                param.put("password",utilisateurPassword);
                param.put("cosoc", utilisateurCosoc);
                param.put("coage", utilisateurCoage);
                param.put("nucom", comNucom);
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
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
                String chipValue = array.length()+" produit(s)";
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

                    chip.setVisibility(View.VISIBLE);
                    chip.setText(chipValue);
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