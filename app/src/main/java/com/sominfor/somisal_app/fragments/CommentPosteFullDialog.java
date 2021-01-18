package com.sominfor.somisal_app.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.sominfor.somisal_app.R;

/**
 * Créé par vatsou le 18,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Affichage de commentaires de poste
 */
public class CommentPosteFullDialog extends DialogFragment {

    private Toolbar toolbar;
    TextView TxtDdvTxn;
    FrameLayout frameLayout;
    String TxnPodev;

    public static CommentPosteFullDialog newInstance(){ return new CommentPosteFullDialog(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SomisalTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.comment_poste_full_dialog_fragment, container, false);
        /***Instanciation des widgets***/
        toolbar = view.findViewById(R.id.toolbar);
        frameLayout = view.findViewById(R.id.frameLayout);
        TxtDdvTxn = view.findViewById(R.id.TxtTxnPodev);
        /**Récupération des commentaires de poste**/
        assert getArguments() != null;
        TxnPodev = getArguments().getString("txnpodev");
        /**Aucun commentaire**/
        if (TxnPodev.equals("")){
            frameLayout.setVisibility(View.VISIBLE);
        }

        TxtDdvTxn.setText(TxnPodev);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(getResources().getString(R.string.comment_poste_full_dialog_setTitle));
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
