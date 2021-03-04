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
import com.sominfor.somisal_app.interfaces.CallBackPostIt;
import com.sominfor.somisal_app.interfaces.DevisProduitsListener;

/**
 * Créé par vatsou le 21,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class DevisPostItFullDialog extends DialogFragment {
    public static final String TAG = DevisPostItFullDialog.class.getSimpleName();
    private Toolbar toolbar;
    DevisProduitsListener devisProduitsListener;
    TextInputEditText EdtDexTexte, EdtDevTxnEn, EdtDevTxnPd;
    MaterialButton BtnValider;
    String dexTexte, devTxnEn, devTxnPd;

    public static DevisPostItFullDialog newInstance(){ return new DevisPostItFullDialog(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SomisalTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.post_it_devis_full_dialog_fragment, container, false);
        /***Instanciation des widgets***/
        toolbar = view.findViewById(R.id.toolbar);
        EdtDexTexte = view.findViewById(R.id.EdtDexTexte);
        EdtDevTxnEn = view.findViewById(R.id.EdtDevTxnEn);
        EdtDevTxnPd = view.findViewById(R.id.EdtDevTxnPd);
        BtnValider = view.findViewById(R.id.BtnValider);

        /**Initialisation des Strings**/
        assert getArguments() != null;
        dexTexte = getArguments().getString("dextexte");
        devTxnEn = getArguments().getString("devtxten");
        devTxnPd = getArguments().getString("devtxtpd");

        /*Set values to Edittexts**/
        EdtDexTexte.setText(dexTexte);
        EdtDevTxnEn.setText(devTxnEn);
        EdtDevTxnPd.setText(devTxnPd);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(getResources().getString(R.string.post_it_devis_full_dialog_setTitle));
        BtnValider.setOnClickListener(v -> {
            /**Récupération Post-it**/
            if (EdtDexTexte.getText().length() != 0)
            dexTexte = EdtDexTexte.getText().toString();
            /**Récupération de commentaire En-tête**/
            if (EdtDevTxnEn.getText().length() != 0)
            devTxnEn = EdtDevTxnEn.getText().toString();

            /**Récupération de commentaire Pied**/
            if (EdtDevTxnPd.getText().length() != 0)
                devTxnPd = EdtDevTxnPd.getText().toString();

            devisProduitsListener = (DevisProduitsListener) getActivity();
            devisProduitsListener.onDataReceivedPostIt(dexTexte, devTxnEn, devTxnPd);
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
