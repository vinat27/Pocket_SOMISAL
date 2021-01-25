package com.sominfor.somisal_app.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.ProduitsSearchableAdapter;
import com.sominfor.somisal_app.adapters.UniteSpinnerAdapter;
import com.sominfor.somisal_app.handler.models.DetailDevis;
import com.sominfor.somisal_app.handler.models.Produit;
import com.sominfor.somisal_app.handler.models.Unite;
import com.sominfor.somisal_app.interfaces.CallBackPostIt;
import com.sominfor.somisal_app.interfaces.DevisProduitsListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Créé par vatsou le 22,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Ajout de produit - Details Devis
 */
public class DevisAddProduitFullDialog extends DialogFragment {
    public static final String TAG = DevisAddProduitFullDialog.class.getSimpleName();
    Toolbar toolbar;
    MaterialButton BtnValider;
    SearchableSpinner SsnDdvCopro;
    MaterialBetterSpinner MbSpnDdvUnvte;
    TextInputEditText EdtDdvCofvt, EdtDdvQtpro;
    List<Produit> produitList;
    List<Unite> uniteList;
    ProduitsSearchableAdapter produitsSearchableAdapter;
    UniteSpinnerAdapter uniteSpinnerAdapter;
    Produit produit;
    Unite unite;
    DevisProduitsListener devisProduitsListener;
    public static DevisAddProduitFullDialog newInstance(){ return new DevisAddProduitFullDialog(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SomisalTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.devis_add_produit_full_dialog_fragment, container, false);
        /***Instanciation des widgets***/
        toolbar = view.findViewById(R.id.toolbar);
        BtnValider = view.findViewById(R.id.BtnValider);
        SsnDdvCopro = view.findViewById(R.id.MbSpnCopro);
        MbSpnDdvUnvte = view.findViewById(R.id.MbSpnDdvUnvte);
        EdtDdvCofvt = view.findViewById(R.id.EdtDdvCofvt);
        EdtDdvQtpro = view.findViewById(R.id.EdtDdvQtpro);

        initDataProduits();

        produitsSearchableAdapter = new ProduitsSearchableAdapter(getContext(), android.R.layout.simple_spinner_item, produitList);
        SsnDdvCopro.setAdapter(produitsSearchableAdapter);

        uniteSpinnerAdapter = new UniteSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, uniteList);
        MbSpnDdvUnvte.setAdapter(uniteSpinnerAdapter);


        SsnDdvCopro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                produit = produitsSearchableAdapter.getItem(position);
                unite = new Unite(produit.getProunvte(), produit.getProliunvte());

                int spinnerPosition = uniteList.indexOf(unite);
                MbSpnDdvUnvte.setText(MbSpnDdvUnvte.getAdapter().getItem(spinnerPosition).toString());

                EdtDdvCofvt.setText(String.valueOf(produit.getProcofvt()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**Validation**/
        BtnValider.setOnClickListener(v -> {
            if (SsnDdvCopro.getSelectedItem()!=null && MbSpnDdvUnvte.length()!=0 && EdtDdvCofvt.getText().length() != 0 && EdtDdvQtpro.getText().length() != 0){
                DetailDevis detailDevis = new DetailDevis();
                detailDevis.setDdvPodev(1);
                detailDevis.setDdvCopro(1237);
                detailDevis.setDdvLipro(produit.getProlipro());
                detailDevis.setDdvNuprm(1004);
                detailDevis.setDdvPutar(18.47);
                detailDevis.setDdvQtdev(Double.parseDouble(EdtDdvQtpro.getText().toString()));
                detailDevis.setDdvVadev(18.47 * Double.parseDouble(EdtDdvQtpro.getText().toString()));
                detailDevis.setDdvTxrem(0.00);
                detailDevis.setDdvVarem(0.00);

                devisProduitsListener = (DevisProduitsListener) getActivity();
                devisProduitsListener.onDataReceived(detailDevis);
                dismiss();
            }else{
                Toast.makeText(getActivity(), getResources().getString(R.string.devis_add_produit_full_dialog_fields_error), Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }


    public void initDataProduits(){
        produitList = new ArrayList<>();
        produitList.add(new Produit(4334, "LEVURE BOULANGERE", "K", 1, "Kilos"));
        produitList.add(new Produit(4335, "AMELIORANT BOULANGERE", "S", 1, "Sac"));

        uniteList = new ArrayList<>();
        uniteList.add(new Unite("K","Kilos"));
        uniteList.add(new Unite("P", "Pièces"));
        uniteList.add(new Unite("S", "Sac"));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(getResources().getString(R.string.devis_add_produit_full_dialog_setTitle));
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
