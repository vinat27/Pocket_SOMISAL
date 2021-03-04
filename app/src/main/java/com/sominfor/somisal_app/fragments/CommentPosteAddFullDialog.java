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
import com.sominfor.somisal_app.handler.models.DetailDevis;
import com.sominfor.somisal_app.interfaces.DevisProduitsListener;

/**
 * Créé par vatsou le 24,février,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class CommentPosteAddFullDialog extends DialogFragment {
    public static final String TAG = CommentPosteAddFullDialog.class.getSimpleName();
    private Toolbar toolbar;
    DevisProduitsListener devisProduitsListener;
    TextInputEditText EdtDdvTxnPo;
    MaterialButton BtnValider;
    DetailDevis detailDevis;


    public static CommentPosteAddFullDialog newInstance(){ return new CommentPosteAddFullDialog(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SomisalTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.comment_poste_add_full_dialog_fragment, container, false);
        /***Instanciation des widgets***/
        toolbar = view.findViewById(R.id.toolbar);
        EdtDdvTxnPo = view.findViewById(R.id.EdtDdvTxnPo);
        BtnValider = view.findViewById(R.id.BtnValider);

        detailDevis = (DetailDevis) getArguments().getSerializable("detailDevis");

        /*Set values to Edittexts**/
        EdtDdvTxnPo.setText(detailDevis.getDdvTxnPo());

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(getResources().getString(R.string.post_it_devis_full_dialog_setTitle));
        BtnValider.setOnClickListener(v -> {
            /**Récupération Post-it**/
            detailDevis.setDdvTxnPo(EdtDdvTxnPo.getText().toString());
            Toast.makeText(getActivity(), "Commentaire enregistré", Toast.LENGTH_LONG).show();
            devisProduitsListener = (DevisProduitsListener) getActivity();
            devisProduitsListener.onDataReceived(detailDevis);

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
