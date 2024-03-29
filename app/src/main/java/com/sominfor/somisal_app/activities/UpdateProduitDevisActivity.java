package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.DdvUpdateProduitsAdapter;
import com.sominfor.somisal_app.adapters.DetailDevisAdapter;
import com.sominfor.somisal_app.fragments.DevisAddProduitFullDialog;
import com.sominfor.somisal_app.fragments.DevisPostItFullDialog;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Client;
import com.sominfor.somisal_app.handler.models.DetailDevis;
import com.sominfor.somisal_app.handler.models.Devis;
import com.sominfor.somisal_app.handler.models.LieuVente;
import com.sominfor.somisal_app.handler.models.Livreur;
import com.sominfor.somisal_app.handler.models.Magasin;
import com.sominfor.somisal_app.handler.models.Produit;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.interfaces.DevisProduitsListener;
import com.sominfor.somisal_app.utils.ApiReceiverMethods;
import com.sominfor.somisal_app.utils.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.sominfor.somisal_app.activities.LoginActivity.protocole;

public class UpdateProduitDevisActivity extends AppCompatActivity implements DevisProduitsListener {
    public static String FRAGMENT_DEVIS = "2";
    private static final int SPLASH_TIME = 4000;

    TextView TxtClirasoc, TxtDevLieuv, TxtDevStatu, TxtDevLimag, TxtDevLiliv, TxtDevDaliv, TxtDevVadev, TxtDevDadev;
    MaterialButton BtnValider;
    FloatingActionButton fab_add_devis_details;
    LinearLayout Lnr01;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    String DevDalivFormat, DevDadevFormat, systemeAdresse, utilisateurLogin, utilisateurPassword, apiUrl01, utilisateurCosoc, utilisateurCoage, apiUrl02, apiUrl03;
    RecyclerView RecyclerViewDetailsDevis;
    DdvUpdateProduitsAdapter ddvUpdateProduitsAdapter;
    List<DetailDevis> detailDevisList = new ArrayList<>();
    List<DetailDevis> detailDelDevisList = new ArrayList<>();
    List<DetailDevis> detailDevisListSav = new ArrayList<>();
    Client client;
    LieuVente lieuVente;
    Magasin magasin;
    Devis devis;
    Livreur livreur;
    MaterialButton BtnTerminer;
    String DexTexteSend = "";
    String DevTxnEnSend = "";
    String DevTxnPdSend = "";
    double vadev;
    JSONArray coactJson, podevJson, coproArray, nuprmArray, unvteArray, cofvtArray, qtdevArray, putarArray, txremArray, varemArray, texteArray;
    List<Produit> produitsUpdateDevisList;
    ApiReceiverMethods apiReceiverMethods;
    FragmentManager fragmentManager;
    DelayedProgressDialog progressDialog, progressDialogInfo;

    /***Retour d'ajout de produits***/
    @Override
    public void onDataReceived(DetailDevis detailDevis) {
        Lnr01.setVisibility(View.GONE);

        int spinnerPosition = detailDevisList.indexOf(detailDevis);
        /**Actualisation si produit existe ou ajout si produit absent dans la liste**/
        if (spinnerPosition != -1){
            detailDevis.setDdvPodev(detailDevisList.get(spinnerPosition).getDdvPodev());
            Log.d("CommentPoste", detailDevis.getDdvTxnPo());
            detailDevisList.set(spinnerPosition, detailDevis);
            ddvUpdateProduitsAdapter.notifyItemChanged(spinnerPosition);
        }else{
            DetailDevis max = Collections.max(detailDevisList);
            detailDevis.setDdvPodev((max.getDdvPodev()+1000));
            detailDevis.setDdvCoact("UPD");
            detailDevisList.add(detailDevis);
            ddvUpdateProduitsAdapter.notifyItemInserted(detailDevisList.size());
        }
        /**Calcul de la valeur de devis**/
        calvadev(detailDevisList);
    }

    @Override
    public void onDataReceivedPostIt(String DexTexte, String DevTxnEn, String DevTxnPd) {
        DexTexteSend = DexTexte;
        DevTxnEnSend = DevTxnEn;
        DevTxnPdSend = DevTxnPd;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_produit_devis);

        /**Gestion du menu d'action**/
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Actualisation Devis");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /**Effacer l'ombre sous l'actionBar**/
        getSupportActionBar().setElevation(0);

        /**Récupération des informations reçus**/
        client = (Client) getIntent().getSerializableExtra("client");
        lieuVente = (LieuVente) getIntent().getSerializableExtra("Lieuvente");
        magasin = (Magasin) getIntent().getSerializableExtra("Magasin");
        devis = (Devis) getIntent().getSerializableExtra("devis");
        livreur = (Livreur) getIntent().getSerializableExtra("Livreur");


        progressDialog = new DelayedProgressDialog();
        progressDialogInfo = new DelayedProgressDialog();

        /**Instanciation des widgets**/
        RecyclerViewDetailsDevis = findViewById(R.id.RecyclerViewDetailsDevis);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        RecyclerViewDetailsDevis.setLayoutManager(linearLayoutManager);
        /***Initialisation de la liste des produits**/
        produitsUpdateDevisList = new ArrayList<>();
        apiReceiverMethods = new ApiReceiverMethods(getApplicationContext());

        TxtClirasoc = findViewById(R.id.TxtClirasoc);
        TxtDevDadev = findViewById(R.id.TxtDevDadev);
        TxtDevDaliv = findViewById(R.id.TxtDevDaliv);
        TxtDevVadev = findViewById(R.id.TxtDevVadev);
        BtnValider = findViewById(R.id.BtnValider);
        BtnTerminer = findViewById(R.id.BtnTerminer);
        Lnr01 = findViewById(R.id.Lnr01);
        fab_add_devis_details = findViewById(R.id.fab_add_devis_details);

        serveurNodeController = new ServeurNodeController();
        serveurNode = serveurNodeController.getServeurNodeInfos();

        /**Récupération de session utilisateur**/
        utilisateur = UserSessionManager.getInstance(getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();
        utilisateurCosoc = utilisateur.getUtilisateurCosoc();
        utilisateurCoage = utilisateur.getUtilisateurCoage();

        /**URL Insertion**/
        apiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/create/devis/Onedevis";
        apiUrl02 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/produit/allProduit";
        apiUrl03 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/devis/detailDevis";

        recupererDetailsDevis(apiUrl03, devis.getDevNudev());

        FragmentManager fragmentManager = getSupportFragmentManager();
        ddvUpdateProduitsAdapter = new DdvUpdateProduitsAdapter(getApplicationContext(), detailDevisList, fragmentManager);
        RecyclerViewDetailsDevis.setAdapter(ddvUpdateProduitsAdapter);

        /**Set values to Textviews**/
        TxtClirasoc.setText(devis.getCliRasoc());
        TxtDevVadev.setText("");

        /**Récupération de la liste des produits**/
        produitsUpdateDevisList = apiReceiverMethods.recupererProduits(apiUrl02, systemeAdresse, utilisateurLogin, utilisateurPassword,utilisateurCosoc, utilisateurCoage);

        /**Date de livraison**/
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fromUser = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            DevDalivFormat = fromUser.format(Objects.requireNonNull(myFormat.parse(devis.getDevDaliv())));
            DevDadevFormat = fromUser.format(Objects.requireNonNull(myFormat.parse(devis.getDevDadev())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TxtDevDaliv.setText(DevDalivFormat);
        TxtDevDadev.setText(DevDadevFormat);

        /**Terminer Devis**/
        BtnTerminer.setOnClickListener(v -> {
            if (!detailDevisList.isEmpty()){
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
                /****/
                if (!detailDelDevisList.isEmpty()){
                    detailDevisList.addAll(detailDelDevisList);
                }

                for(int i=0;i<detailDevisList.size();i++){
                    coactJson.put(detailDevisList.get(i).getDdvCoact());
                    podevJson.put(detailDevisList.get(i).getDdvPodev());
                    coproArray.put(detailDevisList.get(i).getDdvCopro());
                    nuprmArray.put(detailDevisList.get(i).getDdvNuprm());
                    unvteArray.put(detailDevisList.get(i).getDdvUnvte());
                    cofvtArray.put(detailDevisList.get(i).getDdvCofvt());
                    qtdevArray.put(detailDevisList.get(i).getDdvQtdev());
                    putarArray.put(detailDevisList.get(i).getDdvPutar());
                    txremArray.put(detailDevisList.get(i).getDdvTxrem());
                    varemArray.put(0);
                    texteArray.put(detailDevisList.get(i).getDdvTxnPo());
                }

                updateDevisToAs400(apiUrl01);

                /**Attendre 4 secondes, le temps que l'insertion se fasse**/
                DelayedProgressDialog pgDialog = new DelayedProgressDialog();
                pgDialog.show(getSupportFragmentManager(), "Load");
                pgDialog.setCancelable(false);
                new Thread(() -> {

                    try {
                        Thread.sleep(SPLASH_TIME);
                        UpdateDevisActivity.getInstance().finish();
                        Intent intent = new Intent(UpdateProduitDevisActivity.this, DashboardActivity.class);
                        intent.putExtra("frgToLoad", FRAGMENT_DEVIS);
                        // Now start your activity
                        pgDialog.cancel();
                        startActivity(intent);
                        finish();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        pgDialog.cancel();
                    }
                }).start();
            }else{
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_SAL10), Toast.LENGTH_LONG).show();
            }
        });

        /**Ajout de produits**/
        fab_add_devis_details.setOnClickListener(v -> {
            openAddProduitDialog();
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
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

    /**Mettre à jour le post-it**/
    private void openDialog() {
        /***Filtre sur familles et sous familles**/
        FragmentManager fragmentManager = getSupportFragmentManager();
        DevisPostItFullDialog devisPostItFullDialog = DevisPostItFullDialog.newInstance();

        Bundle args = new Bundle();
        args.putString("dextexte", DexTexteSend);
        args.putString("devtxten", DevTxnEnSend);
        args.putString("devtxtpd", DevTxnPdSend);
        devisPostItFullDialog.setArguments(args);
        devisPostItFullDialog.show(fragmentManager, ServeurNode.TAG);
    }

    /**Ajout de produit**/

    private void openAddProduitDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DevisAddProduitFullDialog devisAddProduitFullDialog = DevisAddProduitFullDialog.newInstance();
        Bundle args = new Bundle();
        args.putString("clinucli", client.getCliNucli());
        args.putString("clinacli", client.getCliNacli());
        args.putString("clilieuv", lieuVente.getColieuv());
        devisAddProduitFullDialog.setArguments(args);
        devisAddProduitFullDialog.show(fragmentManager, ServeurNode.TAG);
    }

    public void doPositiveClick(DetailDevis dd) {
        int removeIndex = detailDevisList.indexOf(dd);

        int spinnerPos = detailDevisListSav.indexOf(dd);
        /**Actualisation si produit existe ou ajout si produit absent dans la liste**/
        if (spinnerPos != -1){
            dd.setDdvCoact("UPD");
            dd.setDdvQtdev(0.0);
            /**Sauvegarder avant de supprimer**/
            detailDelDevisList.add(dd);
        }
        detailDevisList.remove(removeIndex);
        ddvUpdateProduitsAdapter.notifyItemRemoved(removeIndex);
        /**Recalcul de la valeur de devis**/
        calvadev(detailDevisList);
    }

    public void doNegativeClick(DetailDevis dd) {
    }

    public void calvadev(List<DetailDevis> listDetailDevis){
        vadev = 0.0;
        for (DetailDevis dd:listDetailDevis) {
            vadev += dd.getDdvVadev();
        }
        @SuppressLint("DefaultLocale")
        String wvadev = String.format("%.2f", vadev)+" "+devis.getDevlimon();
        TxtDevVadev.setText(wvadev);
    }

    /***Récupérer les détails devis**/
    public void recupererDetailsDevis(String api_url, final String devNudev) {
        RequestQueue requestQueue = new Volley().newRequestQueue(getApplicationContext());
        progressDialogInfo.show(getSupportFragmentManager(), "Loading...");
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {
            Double wvarem;
            Double wvapos;
            try {
                Log.v("Response", s);
                JSONObject jsonObject = new JSONObject(s);
                /**Commentaires et Post-it***/
                /**Set values to txh**/
                DexTexteSend = jsonObject.getString("DEXTEXTE");
                DevTxnEnSend = jsonObject.getString("DEVTXHEN");
                DevTxnPdSend = jsonObject.getString("DEVTXHPI");

                /**Formatage de l'array produit**/
                JSONArray array= jsonObject.getJSONArray("Produits");
                for(int i=0;i<array.length();i++) {
                   wvarem = 0.00;
                    JSONObject object1 = array.getJSONObject(i);
                    DetailDevis detailDevis = new DetailDevis();
                    detailDevis.setDdvPodev(object1.getInt("DDVPODEV"));
                    detailDevis.setDdvQtdev(object1.getDouble("DDVQTDEV"));
                    detailDevis.setDdvPutar(object1.getDouble("DDVPUTAR"));
                    detailDevis.setDdvTxrem(object1.getDouble("DDVTXREM"));
                    detailDevis.setDdvVarem(object1.getDouble("DDVVAREM"));
                    detailDevis.setDdvCopro(object1.getInt("DDVCOPRO"));
                    detailDevis.setDdvUnvte(object1.getString("DDVUNVTE"));
                    detailDevis.setDdvLipro(object1.getString("PROLIPRO"));
                    detailDevis.setDdvTxnPo(object1.getString("TXNTEXTE"));
                    detailDevis.setDdvNuprm(object1.getInt("DDVNUPRM"));
                    detailDevis.setDdvCoact("UPD");
                    detailDevis.setDdvNucli(client.getCliNucli());
                    detailDevis.setDdvNacli(client.getCliNacli());
                    detailDevis.setDdvLieuv(lieuVente.getColieuv());
                    detailDevis.setDdvDadev(devis.getDevDadev());

                    /**Calcul de la valeur de remise**/
                    if (object1.getDouble("DDVTXREM") > 0){
                        wvarem = (object1.getDouble("DDVQTDEV") * object1.getDouble("DDVPUTAR")) * (object1.getDouble("DDVTXREM")/100);
                    }

                    if (object1.getDouble("DDVVAREM") > 0){
                        wvarem   = wvarem + (object1.getDouble("DDVQTDEV") * object1.getDouble("DDVVAREM"));
                    }
                    /***Calcul de la valeur de poste**/
                    wvapos =  object1.getDouble("DDVPUTAR") * object1.getDouble("DDVQTDEV");
                    wvapos = wvapos - wvarem;

                    detailDevis.setDdvVadev(wvapos);

                    detailDevisList.add(detailDevis);
                    detailDevisListSav.add(detailDevis);
                    progressDialogInfo.cancel();
                }
                FragmentManager fragmentManager = getSupportFragmentManager();
                ddvUpdateProduitsAdapter = new DdvUpdateProduitsAdapter(getApplicationContext(), detailDevisList, fragmentManager);
                RecyclerViewDetailsDevis.setAdapter(ddvUpdateProduitsAdapter);

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
                param.put("nudev", devNudev);
                param.put("comon", devis.getDevComon());
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

    public void updateDevisToAs400(String api_url){
        RequestQueue requestQueue = new Volley().newRequestQueue(getApplicationContext());
        progressDialog.show(getSupportFragmentManager(), "Loading...");
        progressDialog.setCancelable(false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {

            try{
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("succes") == "true"){
                    progressDialog.cancel();
                }else{
                    progressDialog.cancel();
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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
                param.put("nudev", devis.getDevNudev());
                param.put("dadev",devis.getDevDadev());
                param.put("nucli",client.getCliNucli());
                param.put("rfdev", devis.getDevRfdev().trim().replaceAll("'","\\u0027"));
                param.put("daval", devis.getDevDadev());
                param.put("lieuv", lieuVente.getColieuv());
                param.put("comag", magasin.getMagcomag());
                param.put("comon", client.getCliComon());
                if (DexTexteSend!=null){
                    param.put("notes", DexTexteSend);
                }else{
                    param.put("notes", "");
                }
                /**Commentaires en-tête**/
                if (DevTxnEnSend != null){
                    param.put("txtop", DevTxnEnSend);
                }else{
                    param.put("txtop", "");
                }
                /**Commentaires Pied**/
                if (DevTxnPdSend != null){
                    param.put("txbot", DevTxnPdSend.trim());
                }else{
                    param.put("txbot","");
                }
                if (devis.getDevUscom()!=null){
                    param.put("uscom", devis.getDevUscom());
                }else{
                    param.put("uscom", "");
                }
                param.put("txesc", String.valueOf(devis.getDevTxesc()));
                param.put("ecova", String.valueOf(devis.getDevEcova()));
                param.put("moexp", "");
                param.put("daliv", devis.getDevDaliv());
                param.put("coliv", livreur.getLivColiv());
                param.put("moreg", devis.getDevMoreg());
                param.put("dereg", devis.getDevDereg());
                param.put("podev", podevJson.toString());
                param.put("copro", coproArray.toString());
                param.put("nuprm", nuprmArray.toString());
                param.put("unvte", unvteArray.toString());
                param.put("cofvt", cofvtArray.toString());
                param.put("qtdev", qtdevArray.toString());
                param.put("putar", putarArray.toString());
                param.put("txrem", txremArray.toString());
                param.put("varem", varemArray.toString());
                param.put("texte", texteArray.toString());

                Log.v("Infos", param.toString());

                return param;
            }
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }


}