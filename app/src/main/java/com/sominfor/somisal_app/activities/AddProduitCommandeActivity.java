package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.sominfor.somisal_app.adapters.CommandeProduitAdapter;
import com.sominfor.somisal_app.fragments.CommandeAddProduitFullDialog;
import com.sominfor.somisal_app.fragments.CommandePostItFullDialog;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Client;
import com.sominfor.somisal_app.handler.models.Commande;
import com.sominfor.somisal_app.handler.models.Commercial;
import com.sominfor.somisal_app.handler.models.DelaiReglement;
import com.sominfor.somisal_app.handler.models.DetailCommande;
import com.sominfor.somisal_app.handler.models.LieuVente;
import com.sominfor.somisal_app.handler.models.Livreur;
import com.sominfor.somisal_app.handler.models.Magasin;
import com.sominfor.somisal_app.handler.models.ModeReglement;
import com.sominfor.somisal_app.handler.models.Pays;
import com.sominfor.somisal_app.handler.models.Produit;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Tournee;
import com.sominfor.somisal_app.handler.models.Transport;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.interfaces.CommandeProduitsListener;
import com.sominfor.somisal_app.utils.ApiReceiverMethods;
import com.sominfor.somisal_app.utils.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.sominfor.somisal_app.activities.LoginActivity.protocole;

public class AddProduitCommandeActivity extends AppCompatActivity implements CommandeProduitsListener {
    public static String FRAGMENT_COMMANDE = "3";
    private static final int SPLASH_TIME = 4000;
    Double vacom;
    FloatingActionButton fab_add_commande_details;
    LinearLayout Lnr01;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    MaterialButton BtnTerminer;
    String systemeAdresse, utilisateurLogin, apiUrl02, utilisateurPassword, apiUrl01, utilisateurCosoc, utilisateurCoage, ComDalivFormat, coactOperation;
    TextView TxtComNucom, TxtComRasoc, TxtComLieuv, TxtComStatu, TxtComComag, TxtComColiv, TxtComDaliv, TxtComVacom;
    RecyclerView recyclerViewDetailsCommande;
    CommandeProduitAdapter commandeProduitAdapter;
    List<DetailCommande> detailCommandeList = new ArrayList<>();
    public static List<Produit> produitsCommandes;
    Client client;
    LieuVente lieuVente;
    Magasin magasin;
    Tournee tournee;
    Transport transport;
    Commande commande;
    Livreur livreur;
    Pays paysFacturation, paysLivraison;
    ModeReglement modeReglement;
    DelaiReglement delaiReglement;
    Commercial commercial;
    ApiReceiverMethods apiReceiverMethods;
    String CoxTexteSend = "";
    String ComTxnEnSend = "";
    String ComTxnPdSend = "";
    DelayedProgressDialog progressDialog;
    JSONArray coactJson, pocomJson, coproArray, nuprmArray, unvteArray, cofvtArray, qtcomArray, putarArray, txremArray, varemArray, texteArray;
    @Override
    public void onDataReceived(DetailCommande detailCommande) {
        Lnr01.setVisibility(View.GONE);
        int spinnerPosition = detailCommandeList.indexOf(detailCommande);
        if (spinnerPosition != -1){
            detailCommande.setDcopocom(detailCommandeList.get(spinnerPosition).getDcopocom());
            detailCommandeList.set(spinnerPosition, detailCommande);
            commandeProduitAdapter.notifyItemChanged(spinnerPosition);
        }else{
            detailCommande.setDcopocom((detailCommandeList.size()+1) * 1000);
            detailCommandeList.add(detailCommande);
            commandeProduitAdapter.notifyItemInserted(detailCommandeList.size());
        }
        /**Calcul de la valeur de commande**/
        calvacom(detailCommandeList);
    }

    @Override
    public void onDataReceivedPostIt(String CoxTexte, String ComTxnEn, String ComTxnPd) {
        CoxTexteSend = CoxTexte;
        ComTxnEnSend = ComTxnEn;
        ComTxnPdSend = ComTxnPd;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produit_commande);

        /**Contrôle de sessions utilisateur**/
        if (!UserSessionManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        /**Gestion du menu d'action**/
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Détails Commande");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /**Effacer l'ombre sous l'actionBar**/
        getSupportActionBar().setElevation(0);

        /**Instanciation des widgets**/
        recyclerViewDetailsCommande = findViewById(R.id.RecyclerViewDetailsCommande);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerViewDetailsCommande.setLayoutManager(linearLayoutManager);
        TxtComNucom = findViewById(R.id.TxtComNucom);
        TxtComRasoc = findViewById(R.id.TxtComRasoc);
        TxtComLieuv = findViewById(R.id.TxtComLieuv);
        TxtComStatu = findViewById(R.id.TxtComStatu);
        TxtComComag = findViewById(R.id.TxtComComag);
        TxtComColiv = findViewById(R.id.TxtComColiv);
        TxtComDaliv = findViewById(R.id.TxtComDaliv);
        TxtComVacom = findViewById(R.id.TxtComVacom);
        BtnTerminer = findViewById(R.id.BtnTerminer);
        Lnr01 = findViewById(R.id.Lnr01);
        fab_add_commande_details = findViewById(R.id.fab_add_commande_details);

        serveurNodeController = new ServeurNodeController();
        serveurNode = serveurNodeController.getServeurNodeInfos();
        apiReceiverMethods = new ApiReceiverMethods(getApplicationContext());
        progressDialog = new DelayedProgressDialog();

        /**Récupération de session utilisateur**/
        utilisateur = UserSessionManager.getInstance(getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();
        utilisateurCosoc = utilisateur.getUtilisateurCosoc();
        utilisateurCoage = utilisateur.getUtilisateurCoage();
        coactOperation = "UPD";
        apiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/produit/allProduit";
        apiUrl02 = protocole+"://"+serveurNode.getServeurNodeIp()+"/create/commande/OneCommande";

        /**Récupération des informations envoyées***/
        client = (Client) getIntent().getSerializableExtra("client");
        lieuVente = (LieuVente) getIntent().getSerializableExtra("lieuvente");
        magasin = (Magasin) getIntent().getSerializableExtra("magasin");
        tournee = (Tournee) getIntent().getSerializableExtra("tournee");
        transport = (Transport) getIntent().getSerializableExtra("transport");
        commande = (Commande) getIntent().getSerializableExtra("commande");
        livreur = (Livreur) getIntent().getSerializableExtra("livreur");
        paysFacturation = (Pays) getIntent().getSerializableExtra("pays");
        paysLivraison = (Pays) getIntent().getSerializableExtra("paysLivraison");
        modeReglement = (ModeReglement) getIntent().getSerializableExtra("modeReglement");
        delaiReglement = (DelaiReglement) getIntent().getSerializableExtra("delaiReglement");
        commercial = (Commercial) getIntent().getSerializableExtra("commercial");

        /**Set values to TextViews**/
        TxtComNucom.setText("");
        TxtComRasoc.setText(client.getCliRasoc());
        TxtComLieuv.setText(lieuVente.getLilieuv());
        TxtComStatu.setText(getResources().getString(R.string.AddProduitDevisStatu));
        TxtComComag.setText(magasin.getMaglimag());
        TxtComColiv.setText(livreur.getLivliliv());
        /**Date de livraison**/
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fromUser = new SimpleDateFormat("dd MMM yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            ComDalivFormat = fromUser.format(Objects.requireNonNull(myFormat.parse(commande.getComdaliv())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TxtComDaliv.setText(ComDalivFormat);
        TxtComVacom.setText("");

        produitsCommandes = apiReceiverMethods.recupererProduits(apiUrl01, systemeAdresse, utilisateurLogin, utilisateurPassword,utilisateurCosoc, utilisateurCoage);

        FragmentManager fragmentManager = getSupportFragmentManager();
        commandeProduitAdapter = new CommandeProduitAdapter(getApplicationContext(), detailCommandeList, fragmentManager);
        recyclerViewDetailsCommande.setAdapter(commandeProduitAdapter);

        if (detailCommandeList.isEmpty()){
            Lnr01.setVisibility(View.VISIBLE);
        }else{
            Lnr01.setVisibility(View.GONE);
        }

        /**Envoie de données à AS400**/
        BtnTerminer.setOnClickListener(v -> {
            /**Vérifier si la liste contient des données**/
            if (!detailCommandeList.isEmpty()){
                /**Parse Json - Récupération des arrays**/
                pocomJson = new JSONArray();
                coproArray = new JSONArray();
                nuprmArray = new JSONArray();
                unvteArray = new JSONArray();
                cofvtArray = new JSONArray();
                qtcomArray = new JSONArray();
                putarArray = new JSONArray();
                txremArray = new JSONArray();
                varemArray = new JSONArray();
                texteArray = new JSONArray();
                coactJson = new JSONArray();

                for(int i=0;i<detailCommandeList.size();i++){
                    coactJson.put(coactOperation);
                    pocomJson.put(detailCommandeList.get(i).getDcopocom());
                    coproArray.put(detailCommandeList.get(i).getDcocopro());
                    nuprmArray.put(detailCommandeList.get(i).getDconuprm());
                    unvteArray.put(detailCommandeList.get(i).getDcounvte());
                    cofvtArray.put(detailCommandeList.get(i).getDcocofvt());
                    qtcomArray.put(detailCommandeList.get(i).getDcoqtcom());
                    putarArray.put(detailCommandeList.get(i).getDcoputar());
                    txremArray.put(detailCommandeList.get(i).getDcotxrem());
                    varemArray.put(0);
                    texteArray.put(detailCommandeList.get(i).getDcotxn());
                }

                /**Insertion*/
                insertCommandeToAs400(apiUrl02);
                /**Attendre 6 secondes, le temps que l'insertion se fasse**/
                DelayedProgressDialog pgDialog = new DelayedProgressDialog();
                pgDialog.show(getSupportFragmentManager(), "Load");
                pgDialog.setCancelable(false);
                new Thread(() -> {

                    try {
                        Thread.sleep(SPLASH_TIME);
                        ReglementActivity.getInstance().finish();
                        Intent i = new Intent(AddProduitCommandeActivity.this, DashboardActivity.class);
                        i.putExtra("frgToLoad", FRAGMENT_COMMANDE);
                        // Now start your activity
                        pgDialog.cancel();
                        startActivity(i);
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
        fab_add_commande_details.setOnClickListener(v -> {
            openAddProduitDialog();
        });
    }

    // Options Menu (ActionBar Menu)
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

    /**Mettre à jour le post-it**/
    private void openDialog() {
        /***Filtre sur familles et sous familles**/
        FragmentManager fragmentManager = getSupportFragmentManager();
        CommandePostItFullDialog commandePostItFullDialog = CommandePostItFullDialog.newInstance();

        Bundle args = new Bundle();
        args.putString("coxtexte", CoxTexteSend);
        args.putString("comtxten", ComTxnEnSend);
        args.putString("comtxtpd", ComTxnPdSend);
        commandePostItFullDialog.setArguments(args);
        //devisPostItFullDialog.setTargetFragment(devisPostItFullDialog, 100);
        commandePostItFullDialog.show(fragmentManager, ServeurNode.TAG);
    }

    /**Ajout de produit**/

    private void openAddProduitDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        CommandeAddProduitFullDialog commandeAddProduitFullDialog = CommandeAddProduitFullDialog.newInstance();
        Bundle args = new Bundle();
        args.putString("clinucli", client.getCliNucli());
        args.putString("clinacli", client.getCliNacli());
        args.putString("clilieuv", lieuVente.getColieuv());
        commandeAddProduitFullDialog.setArguments(args);
        //devisPostItFullDialog.setTargetFragment(devisPostItFullDialog, 100);
        commandeAddProduitFullDialog.show(fragmentManager, ServeurNode.TAG);
    }

    public void doPositiveClick(DetailCommande dd) {
        int removeIndex = detailCommandeList.indexOf(dd);
        detailCommandeList.remove(removeIndex);
        commandeProduitAdapter.notifyItemRemoved(removeIndex);
    }

    public void doNegativeClick(DetailCommande dd) {
    }

    /**Calcul de valeur de commande**/
    public void calvacom(List<DetailCommande> listDetailCommandes){
        vacom = 0.0;
        for (DetailCommande dd:listDetailCommandes) {
            vacom += dd.getDcovacom();
        }
        @SuppressLint("DefaultLocale")
        String wvacom = String.format("%.2f", vacom)+" "+client.getCliLiComon();
        TxtComVacom.setText(wvacom);
    }

    public void insertCommandeToAs400(String api_url){
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
                param.put("nucom","");
                param.put("dacom",commande.getComdacom());
                param.put("nucli",client.getCliNucli());
                param.put("namar", commande.getComnamar());
                param.put("lieuv", lieuVente.getColieuv());
                param.put("comag", magasin.getMagcomag());
                param.put("comon", client.getCliComon());
                if (CoxTexteSend!=null){
                    param.put("notes", CoxTexteSend);
                }else{
                    param.put("notes", "");
                }
                /**Commentaires en-tête**/
                if (ComTxnEnSend != null){
                    param.put("txtop", ComTxnEnSend);
                }else{
                    param.put("txtop","");
                }

                /**Commentaires pied**/
                if (ComTxnPdSend != null){
                    param.put("txbot", ComTxnPdSend);
                }else{
                    param.put("txbot", "");
                }
                if (commercial.getCoUscom()!=null){
                    param.put("uscom", commercial.getCoUscom());
                }else{
                    param.put("uscom", "");
                }
                param.put("rasoc", commande.getComrasoc().replaceAll("'","''"));
                param.put("adre1", commande.getComadre1().replaceAll("'","''"));
                param.put("adre2", commande.getComadre2().replaceAll("'","''"));
                param.put("bopos", commande.getCombopos());
                param.put("copos", commande.getComcopos());
                param.put("ville", commande.getComville().replaceAll("'","''"));
                param.put("cpays", paysFacturation.getPaysCopay());
                param.put("txesc", String.valueOf(commande.getComtxesc()));
                param.put("ecova", String.valueOf(commande.getComecova()));
                param.put("moexp", "");
                param.put("daliv", commande.getComdaliv());
                param.put("cotrp", transport.getTrpCotrp());
                param.put("cotrn", tournee.getTrnCotrn());
                param.put("coliv", livreur.getLivColiv());
                param.put("zogeo", commande.getComzogeo());
                param.put("rasol", commande.getComrasol().replaceAll("'","''"));
                param.put("adr1l", commande.getComadr1l().replaceAll("'","''"));
                param.put("adr2l", commande.getComadr2l().replaceAll("'","''"));
                param.put("bopol", commande.getCombopol());
                param.put("copol", commande.getComcopol());
                param.put("villl", commande.getComvilll().replaceAll("'","''"));
                param.put("cpayl", paysLivraison.getPaysCopay());
                param.put("moreg", modeReglement.getCoMoreg());
                param.put("dereg", delaiReglement.getCoDereg());
                param.put("pocom", pocomJson.toString());
                param.put("copro", coproArray.toString());
                param.put("nuprm", nuprmArray.toString());
                param.put("unvte", unvteArray.toString());
                param.put("cofvt", cofvtArray.toString());
                param.put("qtcom", qtcomArray.toString());
                param.put("putar", putarArray.toString());
                param.put("txrem", txremArray.toString());
                param.put("varem", varemArray.toString());
                param.put("texte", texteArray.toString());

                Log.v("Envoi", param.toString());

                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }
}