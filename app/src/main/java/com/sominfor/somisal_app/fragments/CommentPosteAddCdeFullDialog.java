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
import com.sominfor.somisal_app.handler.models.DetailCommande;
import com.sominfor.somisal_app.interfaces.CommandeProduitsListener;

/**
 * Créé par vatsou le 08,mars,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class CommentPosteAddCdeFullDialog extends DialogFragment {
    public static final String TAG = CommentPosteAddCdeFullDialog.class.getSimpleName();
    private Toolbar toolbar;
    CommandeProduitsListener commandeProduitsListener;
    TextInputEditText EdtDcoTxnPo;
    MaterialButton BtnValider;
    DetailCommande detailCommande;


    public static CommentPosteAddCdeFullDialog newInstance(){ return new CommentPosteAddCdeFullDialog(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SomisalTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.comment_poste_add_cde_full_dialog_fragment, container, false);
        /***Instanciation des widgets***/
        toolbar = view.findViewById(R.id.toolbar);
        EdtDcoTxnPo = view.findViewById(R.id.EdtDcoTxnPo);
        BtnValider = view.findViewById(R.id.BtnValider);

        detailCommande = (DetailCommande) getArguments().getSerializable("detailCommande");

        /*Set values to Edittexts**/
        EdtDcoTxnPo.setText(detailCommande.getDcotxn());

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(getResources().getString(R.string.post_it_devis_full_dialog_setTitle));
        BtnValider.setOnClickListener(v -> {
            /**Récupération Post-it**/
            detailCommande.setDcotxn(EdtDcoTxnPo.getText().toString());
            Toast.makeText(getActivity(), "Commentaire enregistré", Toast.LENGTH_LONG).show();
            commandeProduitsListener = (CommandeProduitsListener) getActivity();
            commandeProduitsListener.onDataReceived(detailCommande);

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
