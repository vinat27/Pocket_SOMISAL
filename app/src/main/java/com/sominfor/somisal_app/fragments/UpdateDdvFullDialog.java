package com.sominfor.somisal_app.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.DetailDevis;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.handler.models.Valrem;
import com.sominfor.somisal_app.interfaces.DevisProduitsListener;
import com.sominfor.somisal_app.utils.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.sominfor.somisal_app.activities.LoginActivity.protocole;

/**
 * Créé par vatsou le 01,mars,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class UpdateDdvFullDialog extends DialogFragment {
    public static final String TAG = CommentPosteAddFullDialog.class.getSimpleName();
    private Toolbar toolbar;
    DevisProduitsListener devisProduitsListener;
    TextInputEditText EdtDdvQtpro;
    MaterialButton BtnValider;
    DetailDevis detailDevis;
    TextView TxtProLiPro, TxtProUnvte, TxtDdvCofvt;
    Double wvarem, wvapos;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;
    Utilisateur utilisateur;
    String systemeAdresse, utilisateurLogin, utilisateurPassword, apiUrl01, utilisateurCosoc, utilisateurCoage;


    public static UpdateDdvFullDialog newInstance(){ return new UpdateDdvFullDialog(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SomisalTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.update_ddv_full_dialog_fragment, container, false);
        /***Instanciation des widgets***/
        toolbar = view.findViewById(R.id.toolbar);
        EdtDdvQtpro = view.findViewById(R.id.EdtDdvQtpro);
        TxtProLiPro = view.findViewById(R.id.TxtProLiPro);
        TxtProUnvte = view.findViewById(R.id.TxtProUnvte);
        TxtDdvCofvt = view.findViewById(R.id.TxtDdvCofvt);
        BtnValider = view.findViewById(R.id.BtnValider);
        serveurNodeController = new ServeurNodeController();
        detailDevis = (DetailDevis) getArguments().getSerializable("detailDevis");
        /**Initialisation des valeurs de poste et de remise***/
        wvapos = 0.00;
        wvarem = 0.00;
        /**Récupération du serveur node**/
        serveurNode = serveurNodeController.getServeurNodeInfos();

        /**Récupération de session utilisateur**/
        utilisateur = UserSessionManager.getInstance(getActivity().getApplicationContext()).getUtilisateurDetail();
        systemeAdresse = utilisateur.getUtilisateurSysteme();
        utilisateurLogin = utilisateur.getUtilisateurLogin();
        utilisateurPassword = utilisateur.getUtilisateurPassword();
        utilisateurCosoc = utilisateur.getUtilisateurCosoc();
        utilisateurCoage = utilisateur.getUtilisateurCoage();

        apiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/produit/TarifProduitById";
        /*Set values to Edittexts**/
        TxtProLiPro.setText(detailDevis.getDdvLipro());
        TxtProUnvte.setText(detailDevis.getDdvUnvte());
        TxtDdvCofvt.setText(String.valueOf(detailDevis.getDdvCofvt()));

        EdtDdvQtpro.setText(String.format("%.2f", detailDevis.getDdvQtdev()));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(getResources().getString(R.string.update_ddv_full_dialog_setTitle));
        BtnValider.setOnClickListener(v -> {

            if (EdtDdvQtpro.getText().length() != 0){
                if (Double.parseDouble(EdtDdvQtpro.getText().toString()) != 0){
                    /**Calcul du tarif**/
                    calculTarifRemise(apiUrl01, detailDevis.getDdvCopro(), detailDevis.getDdvUnvte(), detailDevis.getDdvLieuv(), detailDevis.getDdvNacli(), detailDevis.getDdvDadev(), detailDevis.getDdvNucli(), Double.parseDouble(EdtDdvQtpro.getText().toString()));
                }else{
                    Toast.makeText(getActivity(), "Quantité invalide - Minimum 1", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getActivity(), "Aucune quantité saisie", Toast.LENGTH_LONG).show();
            }

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
                        wvarem = (Double.parseDouble(EdtDdvQtpro.getText().toString()) * valrem.getTarPriun()) * (valrem.getRemTxrem()/100);
                    }

                    if (valrem.getRemVarem() > 0){
                        wvarem   = wvarem + (Double.parseDouble(EdtDdvQtpro.getText().toString()) * valrem.getRemVarem());
                    }
                    /***Calcul de la valeur de poste**/
                    wvapos = valrem.getTarPriun() * Double.parseDouble(EdtDdvQtpro.getText().toString());
                    wvapos = wvapos - wvarem;

                    detailDevis.setDdvPutar(valrem.getTarPriun());
                    detailDevis.setDdvQtdev(Double.parseDouble(EdtDdvQtpro.getText().toString()));
                    detailDevis.setDdvVadev(wvapos);
                    detailDevis.setDdvTxrem(valrem.getRemTxrem());
                    detailDevis.setDdvVarem(wvarem);
                    detailDevis.setDdvDadev(dadev);


                    devisProduitsListener = (DevisProduitsListener) getActivity();
                    devisProduitsListener.onDataReceived(detailDevis);
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
                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }

}
