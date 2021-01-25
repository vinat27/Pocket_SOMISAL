package com.sominfor.somisal_app.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.interfaces.CallBackPostIt;

/**
 * Créé par vatsou le 21,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class DevisPostItFullDialog extends DialogFragment {
    public static final String TAG = DevisPostItFullDialog.class.getSimpleName();
    private Toolbar toolbar;
    CallBackPostIt callBackPostIt;
    TextInputEditText EdtDexTexte;
    MaterialButton BtnValider;
    String dexTexte;

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
        BtnValider = view.findViewById(R.id.BtnValider);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(getResources().getString(R.string.post_it_devis_full_dialog_setTitle));
        //toolbar.inflateMenu(R.menu.menu_filter_produit_full_dialog);
        BtnValider.setOnClickListener(v -> {
            dexTexte = EdtDexTexte.getText().toString();
            callBackPostIt = (CallBackPostIt) getActivity();
            callBackPostIt.onDataReceived(dexTexte);
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
