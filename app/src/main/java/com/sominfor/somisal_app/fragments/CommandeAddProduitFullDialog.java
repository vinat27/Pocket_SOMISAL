package com.sominfor.somisal_app.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.ProduitsSearchableAdapter;
import com.sominfor.somisal_app.adapters.UniteSpinnerAdapter;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.DetailCommande;
import com.sominfor.somisal_app.handler.models.DetailDevis;
import com.sominfor.somisal_app.handler.models.Produit;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Unite;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.handler.models.Valrem;
import com.sominfor.somisal_app.interfaces.CommandeProduitsListener;
import com.sominfor.somisal_app.interfaces.DevisProduitsListener;
import com.sominfor.somisal_app.utils.ApiReceiverMethods;
import com.sominfor.somisal_app.utils.UserSessionManager;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sominfor.somisal_app.activities.AddProduitCommandeActivity.produitsCommandes;
import static com.sominfor.somisal_app.activities.AddProduitDevisActivity.produitsDevisList;
import static com.sominfor.somisal_app.activities.LoginActivity.protocole;

/**
 * Créé par vatsou le 02,février,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class CommandeAddProduitFullDialog extends DialogFragment {
    public static final String TAG = CommandeAddProduitFullDialog.class.getSimpleName();
    Toolbar toolbar;
    MaterialButton BtnValider;
    SearchableSpinner SsnDcoCopro;
    TextInputEditText EdtDcoCofvt, EdtDcoQtpro;
    TextView MbSpnDcoUnvte, TxtDcoCofvt;
    List<Produit> produitList;
    List<Unite> uniteList;
    ProduitsSearchableAdapter produitsSearchableAdapter;
    UniteSpinnerAdapter uniteSpinnerAdapter;
    Produit produit;
    Unite unite;
    CommandeProduitsListener commandeProduitsListener;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, apiUrl01, apiUrl02, apiUrl03, dacom, messageErreur, utilisateurCosoc, utilisateurCoage, cliNucli, cliNacli, clilieuv, uniteVente, coeff;
    public RequestQueue rq;
    Double wvarem, wvapos;
    ApiReceiverMethods apiReceiverMethods;
    public static CommandeAddProduitFullDialog newInstance(){ return new CommandeAddProduitFullDialog(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SomisalTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.commande_add_produit_full_dialog_fragment, container, false);

        /***Instanciation des widgets***/
        toolbar = view.findViewById(R.id.toolbar);
        BtnValider = view.findViewById(R.id.BtnValider);
        SsnDcoCopro = view.findViewById(R.id.MbSpnCopro);
        MbSpnDcoUnvte = view.findViewById(R.id.MbSpnDcoUnvte);
        TxtDcoCofvt = view.findViewById(R.id.TxtDcoCofvt);
        EdtDcoQtpro = view.findViewById(R.id.EdtDcoQtpro);
        serveurNodeController = new ServeurNodeController();
        /**Initialisation des valeurs de poste et de remise***/
        wvapos = 0.00;
        wvarem = 0.00;
        /**Récupération du serveur node**/
        serveurNode = serveurNodeController.getServeurNodeInfos();
        apiReceiverMethods = new ApiReceiverMethods(getActivity().getApplicationContext());
        /**Récupération de session utilisateur**/
        utilisateur = UserSessionManager.getInstance(getActivity().getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();
        utilisateurCosoc = utilisateur.getUtilisateurCosoc();
        utilisateurCoage = utilisateur.getUtilisateurCoage();
        /**Initialisation de listes et de la requestQueue**/
        rq = Volley.newRequestQueue(getActivity());
        produitList = new ArrayList<>();
        uniteList = new ArrayList<>();
        messageErreur = "";
        /**URL Récupération de la liste des produits**/
        apiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/produit/allProduit";
        apiUrl02 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/produit/TarifProduitById";
        apiUrl03 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/parametre/allUnvte";

        /**Récupération des commentaires de poste**/
        assert getArguments() != null;
        cliNucli = getArguments().getString("clinucli");
        cliNacli = getArguments().getString("clinacli");
        clilieuv = getArguments().getString("clilieuv");
        /**Format de date de commande**/
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dacom = sdf.format(new Date());

        /*** Récupération de liste de produits***/
        /**Récupération de la liste des livreurs**/
        if (produitsCommandes  == null){
            produitList = apiReceiverMethods.recupererProduits(apiUrl01, systemeAdresse, utilisateurLogin, utilisateurPassword,utilisateurCosoc, utilisateurCoage);
        }else{
            produitList = produitsCommandes;
        }
        produitsSearchableAdapter = new ProduitsSearchableAdapter(getContext(), android.R.layout.simple_spinner_item, produitList);
        SsnDcoCopro.setAdapter(produitsSearchableAdapter);

        /**Récupération de la liste des unités**/
        //recupererListeUnites(apiUrl03);

        SsnDcoCopro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                produit = produitsSearchableAdapter.getItem(position);
                /**Selection automatique d'unités**/
                String unvte = produit.getProunvte();
                String liunvte = produit.getProliunvte();
                uniteVente = "Unité de vente: "+liunvte;
                MbSpnDcoUnvte.setText(uniteVente);
                coeff = "Coefficient: "+produit.getProcofvt();
                TxtDcoCofvt.setText(coeff);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /**Validation**/
        BtnValider.setOnClickListener(v -> {
            if (SsnDcoCopro.getSelectedItem()!=null && EdtDcoQtpro.getText().length() != 0){
                if (Double.parseDouble(EdtDcoQtpro.getText().toString()) != 0){
                    /**Calcul du tarif**/
                    calculTarifRemise(apiUrl02, produit.getProcopro(), produit.getProunvte(), clilieuv, cliNacli, dacom, cliNucli, Double.parseDouble(EdtDcoQtpro.getText().toString()));
                }else{
                    Toast.makeText(getActivity(), "Quantité invalide - Minimum 1", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getActivity(), getResources().getString(R.string.devis_add_produit_full_dialog_fields_error), Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(getResources().getString(R.string.devis_add_produit_full_dialog_setTitle));
        toolbar.setOnMenuItemClickListener(item -> {
            dismiss();
            return true;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.SomisalTheme_Slide);
        }
    }

    /**Récupération des tarifs et remises**/
    public void calculTarifRemise(String api_url, final int proCopro, final String proUnvte, final String cliLieuv, final String cliNacli, final String dadev, final String cliNucli, final Double qtcom) {
        RequestQueue requestQueue = new Volley().newRequestQueue(getActivity().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {
            Valrem valrem = new Valrem();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("succes") == "true") {
                    JSONObject jsonObjectInfo = jsonObject.getJSONObject("Tarif");
                    valrem.setTarUnvte(jsonObjectInfo.getString("TarUnvte"));
                    valrem.setTarCofvt(jsonObjectInfo.getInt("TarCofvt"));
                    valrem.setTarPriun(jsonObjectInfo.getDouble("TarPriun"));
                    valrem.setRemTxrem(jsonObjectInfo.getDouble("RemTxrem"));
                    valrem.setRemVarem(jsonObjectInfo.getDouble("RemVarem"));

                    /**Calcul de la valeur de remise**/
                    if (valrem.getRemTxrem() > 0){
                        wvarem = (Double.parseDouble(EdtDcoQtpro.getText().toString()) * valrem.getTarPriun()) * (valrem.getRemTxrem()/100);
                    }

                    if (valrem.getRemVarem() > 0){
                        wvarem   = wvarem + (Double.parseDouble(EdtDcoQtpro.getText().toString()) * valrem.getRemVarem());
                    }
                    /***Calcul de la valeur de poste**/
                    wvapos = valrem.getTarPriun() * Double.parseDouble(EdtDcoQtpro.getText().toString());
                    wvapos = wvapos - wvarem;
                    DetailCommande detailCommande = new DetailCommande();
                    detailCommande.setDcocopro(produit.getProcopro());
                    detailCommande.setDcolipro(produit.getProlipro());
                    detailCommande.setDconuprm(produit.getPronuprm());
                    detailCommande.setDcounvte(produit.getProunvte());
                    detailCommande.setDcoputar(valrem.getTarPriun());
                    detailCommande.setDcoqtcom(Double.parseDouble(EdtDcoQtpro.getText().toString()));
                    detailCommande.setDcovacom(wvapos);
                    detailCommande.setDcotxrem(valrem.getRemTxrem());
                    detailCommande.setDcovarem(wvarem);
                    detailCommande.setDcotxn("");
                    detailCommande.setDcoDacom(dadev);
                    detailCommande.setDcocofvt(produit.getProcofvt());
                    detailCommande.setDcoNucli(cliNucli);
                    detailCommande.setDcoNacli(cliNacli);
                    detailCommande.setDcoLieuv(clilieuv);

                    commandeProduitsListener = (CommandeProduitsListener) getActivity();
                    commandeProduitsListener.onDataReceived(detailCommande);
                    dismiss();

                }else{
                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace) {
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("systeme",systemeAdresse);
                param.put("login",utilisateurLogin);
                param.put("password",utilisateurPassword);
                param.put("copro", String.valueOf(proCopro));
                param.put("unvte", proUnvte);
                param.put("lieuv", cliLieuv);
                param.put("nacli", cliNacli);
                param.put("nucli", cliNucli);
                param.put("dacom", dadev);
                param.put("qtcom", String.valueOf(qtcom));
                param.put("cosoc", utilisateurCosoc);
                param.put("coage", utilisateurCoage);

                Log.v("Tarifs", param.toString());
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }

    /**Récupération de la liste des unités**/
    /*public void recupererListeUnites(String api_url){
        RequestQueue requestQueue = new Volley().newRequestQueue(getActivity().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {
            try{
                JSONArray array = new JSONArray(s);
                for (int i=0; i<array.length(); i++){
                    try{
                        JSONObject jsonObject = array.getJSONObject(i);
                        Unite unite = new Unite();

                        unite.setUniteLibelle(jsonObject.getString("DATA1").trim());
                        unite.setUniteCode(jsonObject.getString("ARGUM").trim());
                        //Populariser la liste des produits
                        uniteList.add(unite);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
                uniteSpinnerAdapter = new UniteSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, uniteList);
                MbSpnDcoUnvte.setAdapter(uniteSpinnerAdapter);
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
    }*/

}
