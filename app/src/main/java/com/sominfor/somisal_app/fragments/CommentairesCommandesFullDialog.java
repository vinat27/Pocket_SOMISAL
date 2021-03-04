package com.sominfor.somisal_app.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.sominfor.somisal_app.R;

/**
 * Créé par vatsou le 18,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Affichage des commentaires de devis
 */
public class CommentairesCommandesFullDialog extends DialogFragment {
    private Toolbar toolbar;
    TextView TxtTxnComTxnPi, TxtTxnComTxnEn, TxtCoxTxt;
    String DevTxnPi, DevTxnEn, DevDexTxt;

    public static CommentairesCommandesFullDialog newInstance(){ return new CommentairesCommandesFullDialog(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SomisalTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.commentaires_commandes_full_dialog_fragment, container, false);
        /***Instanciation des widgets***/
        toolbar = view.findViewById(R.id.toolbar);
        TxtTxnComTxnPi = view.findViewById(R.id.TxtTxnComTxnPi);
        TxtTxnComTxnEn = view.findViewById(R.id.TxtTxnComTxnEn);
        TxtCoxTxt = view.findViewById(R.id.TxtCoxTxt);
        /**Récupération des commentaires de poste**/
        assert getArguments() != null;
        DevTxnPi = getArguments().getString("comtxnpi");
        DevTxnEn = getArguments().getString("comtxnen");
        DevDexTxt = getArguments().getString("coxtxt");
        TxtTxnComTxnPi.setText(DevTxnPi);
        TxtTxnComTxnEn.setText(DevTxnEn);
        TxtCoxTxt.setText(DevDexTxt);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(getResources().getString(R.string.commentaires_devis_full_dialog_setTitle));
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

}
