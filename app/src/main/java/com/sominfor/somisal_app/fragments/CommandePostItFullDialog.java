package com.sominfor.somisal_app.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.handler.models.Commande;
import com.sominfor.somisal_app.interfaces.CommandeProduitsListener;
import com.sominfor.somisal_app.interfaces.DevisProduitsListener;

/**
 * Créé par vatsou le 08,mars,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class CommandePostItFullDialog extends DialogFragment {
    public static final String TAG = CommandePostItFullDialog.class.getSimpleName();
    private Toolbar toolbar;
    CommandeProduitsListener commandeProduitsListener;
    TextInputEditText EdtCoxTexte, EdtComTxnEn, EdtComTxnPd;
    MaterialButton BtnValider;
    String coxTexte, comTxnEn, comTxnPd;

    public static CommandePostItFullDialog newInstance(){ return new CommandePostItFullDialog(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SomisalTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.post_it_commande_full_dialog_fragment, container, false);
        /***Instanciation des widgets***/
        toolbar = view.findViewById(R.id.toolbar);
        EdtCoxTexte = view.findViewById(R.id.EdtCoxTexte);
        EdtComTxnEn = view.findViewById(R.id.EdtComTxnEn);
        EdtComTxnPd = view.findViewById(R.id.EdtComTxnPd);
        BtnValider = view.findViewById(R.id.BtnValider);

        /**Initialisation des Strings**/
        assert getArguments() != null;
        coxTexte = getArguments().getString("coxtexte");
        comTxnEn = getArguments().getString("comtxten");
        comTxnPd = getArguments().getString("comtxtpd");
        /*Set values to Edittexts**/
        EdtCoxTexte.setText(coxTexte);
        EdtComTxnEn.setText(comTxnEn);
        EdtComTxnPd.setText(comTxnPd);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(getResources().getString(R.string.post_it_devis_full_dialog_setTitle));
        BtnValider.setOnClickListener(v -> {
            /**Récupération Post-it**/
            if (EdtCoxTexte.getText().length() != 0)
                coxTexte = EdtCoxTexte.getText().toString();
            /**Récupération de commentaire En-tête**/
            if (EdtComTxnEn.getText().length() != 0)
                comTxnEn = EdtComTxnEn.getText().toString();

            /**Récupération de commentaire Pied**/
            if (EdtComTxnPd.getText().length() != 0)
                comTxnPd = EdtComTxnPd.getText().toString();

            commandeProduitsListener = (CommandeProduitsListener) getActivity();
            commandeProduitsListener.onDataReceivedPostIt(coxTexte, comTxnEn, comTxnPd);
            Toast.makeText(getActivity(), "Informations enregistrées", Toast.LENGTH_LONG).show();
            dismiss();
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


}
