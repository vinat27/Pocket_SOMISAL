package com.sominfor.somisal_app.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.activities.DelayedProgressDialog;
import com.sominfor.somisal_app.adapters.ClientSpinnerAdapter;
import com.sominfor.somisal_app.adapters.DevisAdapter;
import com.sominfor.somisal_app.adapters.LieuVenteSpinnerAdapter;
import com.sominfor.somisal_app.handler.models.Client;
import com.sominfor.somisal_app.handler.models.Commercial;
import com.sominfor.somisal_app.handler.models.DelaiLivraison;
import com.sominfor.somisal_app.handler.models.DelaiReglement;
import com.sominfor.somisal_app.handler.models.Devis;
import com.sominfor.somisal_app.handler.models.GestionParametre;
import com.sominfor.somisal_app.handler.models.LieuVente;
import com.sominfor.somisal_app.handler.models.Livreur;
import com.sominfor.somisal_app.handler.models.Magasin;
import com.sominfor.somisal_app.handler.models.ModeReglement;
import com.sominfor.somisal_app.handler.models.Pays;
import com.sominfor.somisal_app.handler.models.Produit;
import com.sominfor.somisal_app.handler.models.Tournee;
import com.sominfor.somisal_app.handler.models.Transport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sominfor.somisal_app.handler.models.Utilisateur.TAG;

/**
 * Créé par vatsou le 19,février,2021
 * SOMINFOR
 * Paris, FRANCE
 * Récupération des informations depuis le Serveur API
 */
public class ApiReceiverMethods {
    private final Context context;
    public ApiReceiverMethods(Context context) {
        this.context = context;
    }

    /**Récupération de la liste des lieux de vente**/
    public List<LieuVente> recupererListeLieuv(String api_url, String systemeAdresse, String utilisateurLogin, String utilisateurPassword, String utilisateurCosoc, String utilisateurCoage){
        RequestQueue requestQueue = new Volley().newRequestQueue(context);
        List<LieuVente> lieuVentes = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {
            try{
                JSONArray array = new JSONArray(s);
                for (int i=0; i<array.length(); i++){
                    try{
                        JSONObject jsonObject = array.getJSONObject(i);
                        LieuVente lieuVente = new LieuVente();
                        lieuVente.setColieuv(jsonObject.getString("ARGUM"));
                        lieuVente.setLilieuv(jsonObject.getString("DATA1").trim());
                        lieuVente.setComag(jsonObject.getString("MAG").trim());
                        //Populariser la liste des lieux de vente
                        lieuVentes.add(lieuVente);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }

            }catch(JSONException e){
                e.printStackTrace();
            }
        }, Throwable::printStackTrace)
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
        return lieuVentes;
    }

    /**
     * Récupération de la liste des clients
     * Spécificité: Avec chargement
     */
    public List<Client> recupererListeClients(String api_url, String systemeAdresse, String utilisateurLogin, String utilisateurPassword, String utilisateurCosoc, String utilisateurCoage){
        RequestQueue requestQueue = new Volley().newRequestQueue(context);
        List<Client> clients = new ArrayList<>();
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
                        client.setCliRasol(jsonObject.getString("CLIRASOL").trim());
                        client.setCliAdr1l(jsonObject.getString("CLIADR1L").trim());
                        client.setCliAdr2l(jsonObject.getString("CLIADR2L").trim());
                        client.setCliCopol(jsonObject.getString("CLICOPOL").trim());
                        client.setCliVilll(jsonObject.getString("CLIVILLL").trim());
                        client.setCliBopol(jsonObject.getString("CLIBOPOL").trim());
                        client.setCliCpayl(jsonObject.getString("CLICPAYL").trim());
                        client.setCliNacpx(jsonObject.getString("CLINACPX"));
                        client.setCliCpgen(jsonObject.getString("CLICPGEN"));
                        client.setCliCpaux(jsonObject.getString("CLICPAUX"));
                        client.setCliZogeo(jsonObject.getString("CLIZOGEO"));
                        client.setCliMtplf(jsonObject.getDouble("CLIMTPLF"));
                        client.setCliComon(jsonObject.getString("CLICOMON"));

                        //Populariser la liste des clients
                        clients.add(client);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
        }, Throwable::printStackTrace)
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
        return clients;
    }

    /**Récupération de la liste des magasins**/
    public List<Magasin> recupererListeMagasins(String api_url, String systemeAdresse, String utilisateurLogin, String utilisateurPassword, String utilisateurCosoc, String utilisateurCoage){
        RequestQueue requestQueue = new Volley().newRequestQueue(context);
        List<Magasin> magasins = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {
            try{
                JSONArray array = new JSONArray(s);
                for (int i=0; i<array.length(); i++){
                    try{
                        JSONObject jsonObject = array.getJSONObject(i);
                        Magasin magasin = new Magasin();
                        magasin.setMagcomag(jsonObject.getString("ARGUM").trim());
                        magasin.setMaglimag(jsonObject.getString("DATA1").trim());
                        //Populariser la liste des magasins
                        magasins.add(magasin);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }

            }catch(JSONException e){
                e.printStackTrace();
            }
        }, Throwable::printStackTrace)
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
        return magasins;
    }

    /**Récupération de la liste des lieux de vente**/
    public List<Tournee> recupererListeTournees(String api_url, String systemeAdresse, String utilisateurLogin, String utilisateurPassword, String utilisateurCosoc, String utilisateurCoage){
        RequestQueue requestQueue = new Volley().newRequestQueue(context);
        List<Tournee> tournees = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {
            try{
                JSONArray array = new JSONArray(s);
                for (int i=0; i<array.length(); i++){
                    try{
                        JSONObject jsonObject = array.getJSONObject(i);
                        Tournee tournee = new Tournee();
                        tournee.setTrnCotrn(jsonObject.getString("ARGUM").trim());
                        tournee.setTrnLitrn(jsonObject.getString("DATA1").trim());
                        //Populariser la liste des tournée
                        tournees.add(tournee);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }

            }catch(JSONException e){
                e.printStackTrace();
            }
        }, Throwable::printStackTrace)
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
        return tournees;
    }

    /**Récupération de la liste des livreurs**/
    public List<Livreur> recupererListeLivreurs(String api_url, String systemeAdresse, String utilisateurLogin, String utilisateurPassword, String utilisateurCosoc, String utilisateurCoage){
        RequestQueue requestQueue = new Volley().newRequestQueue(context);
        List<Livreur> livreurs = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {
            try{
                JSONArray array = new JSONArray(s);
                for (int i=0; i<array.length(); i++){
                    try{
                        JSONObject jsonObject = array.getJSONObject(i);
                        Livreur livreur = new Livreur();
                        livreur.setLivColiv(jsonObject.getString("ARGUM").trim());
                        livreur.setLivliliv(jsonObject.getString("DATA1").trim());
                        //Populariser la liste des livreurs
                        livreurs.add(livreur);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }

            }catch(JSONException e){
                e.printStackTrace();
            }
        }, Throwable::printStackTrace)
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
        return livreurs;
    }

    /**Récupération de la liste des transports**/
    public List<Transport> recupererListeTransport(String api_url, String systemeAdresse, String utilisateurLogin, String utilisateurPassword, String utilisateurCosoc, String utilisateurCoage ){
        RequestQueue requestQueue = new Volley().newRequestQueue(context);
        List<Transport> transports = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {
            try{
                JSONArray array = new JSONArray(s);
                for (int i=0; i<array.length(); i++){
                    try{
                        JSONObject jsonObject = array.getJSONObject(i);
                        Transport transport = new Transport();
                        transport.setTrpCotrp(jsonObject.getString("ARGUM").trim());
                        transport.setTrpLitrp(jsonObject.getString("DATA1").trim());
                        //Populariser la liste des transports
                        transports.add(transport);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }

            }catch(JSONException e){
                e.printStackTrace();
            }
        }, Throwable::printStackTrace)
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
        return transports;
    }

    /**Récupération de la liste des pays**/
    public List<Pays> recupererListePays(String api_url, String systemeAdresse, String utilisateurLogin, String utilisateurPassword, String utilisateurCosoc, String utilisateurCoage){
        RequestQueue requestQueue = new Volley().newRequestQueue(context);
        List<Pays> paysList = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {

            try{
                JSONArray array = new JSONArray(s);
                for (int i=0; i<array.length(); i++){
                    try{
                        JSONObject jsonObject = array.getJSONObject(i);
                        Pays pays = new Pays();
                        pays.setPaysCopay(jsonObject.getString("ARGUM").trim());
                        pays.setPaysLipay(jsonObject.getString("DATA1").trim());
                        //Populariser la liste des transports
                        paysList.add(pays);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }

            }catch(JSONException e){
                e.printStackTrace();
            }
        }, Throwable::printStackTrace)
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
        return paysList;
    }


    /**Récupération de la liste des délais de règlement**/
    public List<DelaiReglement> recupererDelaiReglements(String api_url, String systemeAdresse, String utilisateurLogin, String utilisateurPassword, String utilisateurCosoc, String utilisateurCoage){
        RequestQueue requestQueue = new Volley().newRequestQueue(context);
        List<DelaiReglement> delaiReglements = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {

            try{
                JSONArray array = new JSONArray(s);
                for (int i=0; i<array.length(); i++){
                    try{
                        JSONObject jsonObject = array.getJSONObject(i);
                        DelaiReglement delaiReglement = new DelaiReglement();
                        delaiReglement.setCoDereg(jsonObject.getString("ARGUM").trim());
                        delaiReglement.setLiDereg(jsonObject.getString("DATA1").trim());
                        //Populariser la liste des livreurs
                        delaiReglements.add(delaiReglement);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }

            }catch(JSONException e){
                e.printStackTrace();
            }
        }, Throwable::printStackTrace)
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
        return delaiReglements;
    }

    /**Récupération de la liste des modes de règlement**/
    public List<ModeReglement> recupererModeReglements(String api_url, String systemeAdresse, String utilisateurLogin, String utilisateurPassword,  String utilisateurCosoc, String utilisateurCoage){
        RequestQueue requestQueue = new Volley().newRequestQueue(context);
        List<ModeReglement> modeReglements = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {
            try{
                JSONArray array = new JSONArray(s);
                for (int i=0; i<array.length(); i++){
                    try{
                        JSONObject jsonObject = array.getJSONObject(i);
                        ModeReglement modeReglement = new ModeReglement();
                        modeReglement.setCoMoreg(jsonObject.getString("ARGUM").trim());
                        modeReglement.setLiMoreg(jsonObject.getString("DATA1").trim());
                        //Populariser la liste des livreurs
                        modeReglements.add(modeReglement);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }

            }catch(JSONException e){
                e.printStackTrace();
            }
        }, Throwable::printStackTrace)
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
        return modeReglements;
    }


    /***Récupération de la liste des commerciaux**/
    public List<Commercial> recupererCommerciaux(String api_url, String systemeAdresse, String utilisateurLogin, String utilisateurPassword, String utilisateurCosoc, String utilisateurCoage){
        RequestQueue requestQueue = new Volley().newRequestQueue(context);
        List<Commercial> commercialList = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {
            try{
                JSONArray array = new JSONArray(s);
                for (int i=0; i<array.length(); i++){
                    try{
                        JSONObject jsonObject = array.getJSONObject(i);
                        Commercial commercial = new Commercial();
                        commercial.setCoUscom(jsonObject.getString("ARGUM").trim());
                        commercial.setLiUscom(jsonObject.getString("DATA1").trim());
                        //Populariser la liste
                        commercialList.add(commercial);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }

            }catch(JSONException e){
                e.printStackTrace();
            }
        }, Throwable::printStackTrace)
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
        return commercialList;
    }

    /***Récupération de la liste des produits**/
    public List<Produit> recupererProduits(String api_url, String systemeAdresse, String utilisateurLogin, String utilisateurPassword, String utilisateurCosoc, String utilisateurCoage){
        RequestQueue requestQueue = new Volley().newRequestQueue(context);
        List<Produit> produitList = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {
            try{
                JSONArray array = new JSONArray(s);
                for (int i=0; i<array.length(); i++){
                    try{
                        JSONObject jsonObject = array.getJSONObject(i);
                        Produit produit = new Produit();

                        produit.setProcopro(jsonObject.getInt("PROCOPRO"));
                        produit.setProlipro(jsonObject.getString("PROLIPRO").trim());
                        produit.setProcofam(jsonObject.getString("PROCOFAM"));
                        produit.setProsofam(jsonObject.getString("PROSOFAM"));
                        produit.setPronuprm(jsonObject.getInt("PRONUPRM"));
                        produit.setProunvte(jsonObject.getString("PROUNVTE").trim());
                        produit.setProcofvt(jsonObject.getInt("PROCOFVT"));
                        produit.setProliunvte(jsonObject.getString("DATA1").trim());
                        //Populariser la liste des produits
                        produitList.add(produit);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }

            }catch(JSONException e){
                e.printStackTrace();
            }
        }, Throwable::printStackTrace)
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
        return produitList;
    }

    /**Récupération des paramètres de gestion**/
    public List<GestionParametre> recupererGestParam(String api_url, String systemeAdresse, String utilisateurLogin, String utilisateurPassword, String utilisateurCosoc, String utilisateurCoage){
        RequestQueue requestQueue = new Volley().newRequestQueue(context);
        List<GestionParametre> gestionParametres = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {
            try{
                JSONArray array = new JSONArray(s);
                for (int i=0; i<array.length(); i++){
                    try{
                        JSONObject jsonObject = array.getJSONObject(i);
                        GestionParametre gestionParametre = new GestionParametre();
                        gestionParametre.setCopar(jsonObject.getString("ARGUM").trim());
                        gestionParametre.setDatas(jsonObject.getString("CHOIX").trim());
                        //Populariser la liste des magasins
                        gestionParametres.add(gestionParametre);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }

            }catch(JSONException e){
                e.printStackTrace();
            }
        }, Throwable::printStackTrace)
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
        return gestionParametres;
    }

    /**Récupération de la liste des délai de livraison**/
    public List<DelaiLivraison> recupererDlv(String api_url, String systemeAdresse, String utilisateurLogin, String utilisateurPassword, String utilisateurCosoc, String utilisateurCoage){
        RequestQueue requestQueue = new Volley().newRequestQueue(context);
        List<DelaiLivraison> delaiLivraisons = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {
            Log.v("Dlv",s);
            try{
                JSONArray array = new JSONArray(s);
                for (int i=0; i<array.length(); i++){
                    try{
                        JSONObject jsonObject = array.getJSONObject(i);
                        DelaiLivraison delaiLivraison = new DelaiLivraison();
                        delaiLivraison.setNudlv(jsonObject.getString("ARGUM").trim());
                        delaiLivraison.setLidlv(jsonObject.getString("DESIGNATION").trim());
                        delaiLivraison.setDldlv(Integer.parseInt(jsonObject.getString("DELAI").trim()));
                        //Populariser la liste de délai de livraison
                        delaiLivraisons.add(delaiLivraison);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }

            }catch(JSONException e){
                e.printStackTrace();
            }
        }, Throwable::printStackTrace)
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
        return delaiLivraisons;
    }

}
