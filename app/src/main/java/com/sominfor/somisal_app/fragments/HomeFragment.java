package com.sominfor.somisal_app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.activities.AddDevisActivity;
import com.sominfor.somisal_app.activities.AddProduitDevisActivity;
import com.sominfor.somisal_app.activities.DelayedProgressDialog;
import com.sominfor.somisal_app.activities.LoginActivity;
import com.sominfor.somisal_app.activities.ShowCommandeUserActivity;
import com.sominfor.somisal_app.activities.ShowDevisUserActivity;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.utils.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import static com.sominfor.somisal_app.activities.LoginActivity.protocole;

/**
 * Créé par vatsou le 20,mars,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class HomeFragment extends Fragment {
    TextView TxtCdeNumber, TxtDevNumber, TxthelloScreenUser;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    String apiUrl01, systemeAdresse, utilisateurLogin, utilisateurPassword, devStatu, apiUrl02, apiUrl03, apiUrl04, apiUrl05, apiUrl06, utilisateurCosoc, utilisateurCoage, coactVal, coactDel, coactArc;
    Utilisateur utilisateur;
    CardView CommandeCardView, DevisCardView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        getActivity().setTitle("Tableau de bord");
        /**Contrôle session***/
        if (!UserSessionManager.getInstance(getActivity()).isLoggedIn()) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        serveurNodeController = new ServeurNodeController();
        /**Récupération des informations serveur**/
        serveurNode = serveurNodeController.getServeurNodeInfos();
        /*URL Récupération de la liste des systèmes*/
        apiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/devis/devisByClientAndStatut";
        apiUrl02 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/commande/commandeByClientAndSatut";
        utilisateur = UserSessionManager.getInstance(getActivity().getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();
        utilisateurCosoc = utilisateur.getUtilisateurCosoc();
        utilisateurCoage = utilisateur.getUtilisateurCoage();
        /**Instanciation des widgets**/
        TxtCdeNumber = view.findViewById(R.id.CdeNumber);
        TxtDevNumber = view.findViewById(R.id.DevNumber);
        TxthelloScreenUser = view.findViewById(R.id.helloScreenUser);
        CommandeCardView = view.findViewById(R.id.CommandeCardView);
        DevisCardView = view.findViewById(R.id.DevisCardView);

        TxthelloScreenUser.setText(utilisateurLogin);
        /**Compter le nombre de commandes et de devis**/
        countDev(apiUrl01);
        countCom(apiUrl02);

        CommandeCardView.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), ShowCommandeUserActivity.class);
            startActivity(i);
        });

        DevisCardView.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), ShowDevisUserActivity.class);
            startActivity(i);
        });
        return view;
    }

    public void countDev(String api_url){
        RequestQueue requestQueue = new Volley().newRequestQueue(getActivity().getApplicationContext());
        DelayedProgressDialog progressDialogInfo = new DelayedProgressDialog();
        progressDialogInfo.show(getActivity().getSupportFragmentManager(), "Loading...");
        progressDialogInfo.setCancelable(false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {

            try{
                JSONArray array = new JSONArray(s);
                TxtDevNumber.setText(String.valueOf(array.length()));
                progressDialogInfo.cancel();
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

    public void countCom(String api_url){
        RequestQueue requestQueue = new Volley().newRequestQueue(getActivity().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {

            try{
                JSONArray array = new JSONArray(s);
                TxtCdeNumber.setText(String.valueOf(array.length()));
            }catch(JSONException e){
                e.printStackTrace();
            }
        }, volleyError -> {
            volleyError.printStackTrace();
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
