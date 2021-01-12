package com.sominfor.somisal_app.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.handler.models.Famille;
import com.sominfor.somisal_app.interfaces.CallbackListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

/**
 * Créé par vatsou le 10,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Filtrer les produits par famille et sous famille
 */
public class FilterProduitFullDialog extends DialogFragment {
    public static final String TAG = FilterProduitFullDialog.class.getSimpleName();

    private Toolbar toolbar;
    CallbackListener callbackListener;
    Famille famille;
    SearchableSpinner MbSpnSoFamille, MbSpnFamille;

    public static FilterProduitFullDialog newInstance(){ return new FilterProduitFullDialog(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SomisalTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.filter_produit_full_dialog_fragment, container, false);
        /***Instanciation des widgets***/
        toolbar = view.findViewById(R.id.toolbar);
        MbSpnFamille = view.findViewById(R.id.MbSpnFamille);
        MbSpnSoFamille = view.findViewById(R.id.MbSpnSoFamille);

        famille = new Famille();

        famille.setFapdesam("Salut vous");



        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(getResources().getString(R.string.filter_produit_full_dialog_setTitle));
        toolbar.inflateMenu(R.menu.menu_filter_produit_full_dialog);
        toolbar.setOnMenuItemClickListener(item -> {
            callbackListener = (CallbackListener) getTargetFragment();
            callbackListener.onDataReceived(famille);
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
