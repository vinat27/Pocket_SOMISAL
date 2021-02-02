package com.sominfor.somisal_app.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.ProduitsSearchableAdapter;
import com.sominfor.somisal_app.adapters.UniteSpinnerAdapter;
import com.sominfor.somisal_app.handler.models.DetailCommande;
import com.sominfor.somisal_app.handler.models.DetailDevis;
import com.sominfor.somisal_app.handler.models.Produit;
import com.sominfor.somisal_app.handler.models.Unite;
import com.sominfor.somisal_app.interfaces.CommandeProduitsListener;
import com.sominfor.somisal_app.interfaces.DevisProduitsListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Créé par vatsou le 02,février,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class CommandeAddProduitFullDialog extends DialogFragment {
    public static final String TAG = CommandeAddProduitFullDialog.class.getSimpleName();
    Toolbar toolbar;
    MaterialButton BtnValider;
    SearchableSpinner SsnDcoCopro;
    MaterialBetterSpinner MbSpnDcoUnvte;
    TextInputEditText EdtDcoCofvt, EdtDcoQtpro;
    List<Produit> produitList;
    List<Unite> uniteList;
    ProduitsSearchableAdapter produitsSearchableAdapter;
    UniteSpinnerAdapter uniteSpinnerAdapter;
    Produit produit;
    Unite unite;
    CommandeProduitsListener commandeProduitsListener;
    public static CommandeAddProduitFullDialog newInstance(){ return new CommandeAddProduitFullDialog(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SomisalTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.commande_add_produit_full_dialog_fragment, container, false);

        /***Instanciation des widgets***/
        toolbar = view.findViewById(R.id.toolbar);
        BtnValider = view.findViewById(R.id.BtnValider);
        SsnDcoCopro = view.findViewById(R.id.MbSpnCopro);
        MbSpnDcoUnvte = view.findViewById(R.id.MbSpnDcoUnvte);
        EdtDcoCofvt = view.findViewById(R.id.EdtDcoCofvt);
        EdtDcoQtpro = view.findViewById(R.id.EdtDcoQtpro);

        initDataProduits();

        produitsSearchableAdapter = new ProduitsSearchableAdapter(getContext(), android.R.layout.simple_spinner_item, produitList);
        SsnDcoCopro.setAdapter(produitsSearchableAdapter);

        uniteSpinnerAdapter = new UniteSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, uniteList);
        MbSpnDcoUnvte.setAdapter(uniteSpinnerAdapter);


        SsnDcoCopro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                produit = produitsSearchableAdapter.getItem(position);
                unite = new Unite(produit.getProunvte(), produit.getProliunvte());

                int spinnerPosition = uniteList.indexOf(unite);
                MbSpnDcoUnvte.setText(MbSpnDcoUnvte.getAdapter().getItem(spinnerPosition).toString());

                EdtDcoCofvt.setText(String.valueOf(produit.getProcofvt()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**Validation**/
        BtnValider.setOnClickListener(v -> {
            if (SsnDcoCopro.getSelectedItem()!=null && MbSpnDcoUnvte.length()!=0 && EdtDcoCofvt.getText().length() != 0 && EdtDcoQtpro.getText().length() != 0){
                DetailCommande detailCommande = new DetailCommande();
                detailCommande.setDcopocom(1);
                detailCommande.setDcocopro(1237);
                detailCommande.setDcolipro(produit.getProlipro());
                detailCommande.setDconuprm(1004);
                detailCommande.setDcoputar(18.47);
                detailCommande.setDcoqtcom(Double.parseDouble(EdtDcoQtpro.getText().toString()));
                detailCommande.setDcovacom(18.47 * Double.parseDouble(EdtDcoQtpro.getText().toString()));
                detailCommande.setDcotxrem(0.00);
                detailCommande.setDcovarem(0.00);

                commandeProduitsListener = (CommandeProduitsListener) getActivity();
                commandeProduitsListener.onDataReceived(detailCommande);
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
