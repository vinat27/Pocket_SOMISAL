package com.sominfor.somisal_app.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.activities.AddDevisActivity;
import com.sominfor.somisal_app.adapters.ClientSpinnerAdapter;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Client;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.utils.UserSessionManager;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sominfor.somisal_app.activities.LoginActivity.protocole;

/**
 * Créé par vatsou le 31,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class EnteteFragment extends Fragment implements BlockingStep {
    public static final String TAG = EnteteFragment.class.getSimpleName();
    List<Client> clients;
    ClientSpinnerAdapter clientSpinnerAdapter;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, apiUrl01, utilisateurCosoc, utilisateurCoage;
    public RequestQueue rq;
    SearchableSpinner SsnComCliRasoc;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entete, container, false);

        /**Récupération de session utilisateur**/
        utilisateur = UserSessionManager.getInstance(getActivity().getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();
        utilisateurCosoc = utilisateur.getUtilisateurCosoc();
        utilisateurCoage = utilisateur.getUtilisateurCoage();

        serveurNodeController = new ServeurNodeController();
        /**Récupération du serveur node**/
        serveurNode = serveurNodeController.getServeurNodeInfos();

        /**Initialisation de listes et de la requestQueue**/
        rq = Volley.newRequestQueue(getActivity().getApplicationContext());
        clients = new ArrayList<>();

        /**URL Récupération de la liste des clients**/
        apiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/allClient";

        /**Instanciation des widgets**/
        SsnComCliRasoc = view.findViewById(R.id.MbSpnComRasoc);

        /**Récupération de la liste de clients**/
        recupererListeClients(apiUrl01);
        return view;
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.goToNextStep();
            }
        }, 500L);
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        //Toast.makeText(this.getContext(), "Complete", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        //Toast.makeText(this.getContext(), "Your custom back action. Here you should cancel currently running operations", Toast.LENGTH_SHORT).show();
        callback.goToPrevStep();
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    /**Récupération de la liste de clients**/
    public void recupererListeClients(String api_url){
        RequestQueue requestQueue = new Volley().newRequestQueue(getActivity().getApplicationContext());
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
                clientSpinnerAdapter = new ClientSpinnerAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, clients);
                SsnComCliRasoc.setAdapter(clientSpinnerAdapter);
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
