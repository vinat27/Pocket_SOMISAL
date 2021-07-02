package com.sominfor.somisal_app.fragments;

import android.app.DatePickerDialog;
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
import com.sominfor.somisal_app.adapters.ClientSpinnerAdapter;
import com.sominfor.somisal_app.adapters.StatutCommandeAdapter;
import com.sominfor.somisal_app.handler.models.CommandeFilterElements;
import com.sominfor.somisal_app.handler.models.StatutCommande;
import com.sominfor.somisal_app.interfaces.CommandeFilterListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Créé par vatsou le 30,juin,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class FiltresCommandesClientDialog extends DialogFragment {
    public static final String TAG = FiltresCommandesClientDialog.class.getSimpleName();
    private Toolbar toolbar;
    TextInputEditText EdtDaInf, EdtDaSup;
    MaterialButton btnValider;
    String commandeStatut;
    DatePickerDialog DpDaInf, DpDaSup;
    CommandeFilterListener commandeFilterListener;
    MaterialBetterSpinner MbSpnComSta;
    List<StatutCommande> statutCommandes;
    StatutCommandeAdapter statutCommandeAdapter;
    StatutCommande statutCommande;

    public static FiltresCommandesClientDialog newInstance(){ return new FiltresCommandesClientDialog(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SomisalTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.filtres_commandes_client_dialog, container, false);
        /***Instanciation des widgets***/
        toolbar = view.findViewById(R.id.toolbar);
        EdtDaInf = view.findViewById(R.id.EdtDateInf);
        EdtDaSup = view.findViewById(R.id.EdtDateSup);
        MbSpnComSta = view.findViewById(R.id.MbSpnComSta);
        btnValider = view.findViewById(R.id.BtnValider);

        /**Date commande**/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        EdtDaInf.setText(sdf.format(new Date()));
        EdtDaSup.setText(sdf.format(new Date()));

        populariseStatutCommande();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(getResources().getString(R.string.filtres_commandes_dialog));

        /**Champ de date, selection de date  DatePicker**/
        EdtDaInf.setOnClickListener(v -> DpDaInf.show());
        /**Marquer la date selectionnée dans le champ date devis**/
        setDateOnEdtDate();

        /**Champ de date, selection de date sup  DatePicker**/
        EdtDaSup.setOnClickListener(v -> DpDaSup.show());
        /**Marquer la date selectionnée dans le champ date devis**/
        setDateOnEdtDaSup();

        MbSpnComSta.setOnItemClickListener((parent, view1, position, id) -> {
            statutCommande = statutCommandeAdapter.getItem(position);
        });

        /***Au clic de validation**/
        btnValider.setOnClickListener(v -> {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date dateInf = simpleDateFormat.parse(EdtDaInf.getText().toString());
                Date dateSup = simpleDateFormat.parse(EdtDaSup.getText().toString());
                assert dateInf != null;
                /**Comparaison des dates**/
                if (dateSup.compareTo(dateInf) >= 0 && MbSpnComSta.length() != 0){
                    CommandeFilterElements commandeFilterElements = new CommandeFilterElements();
                    commandeFilterElements.setDateInf(EdtDaInf.getText().toString());
                    commandeFilterElements.setDateSup(EdtDaSup.getText().toString());
                    commandeFilterElements.setCommandeStatut(statutCommande.getCodSta());

                    commandeFilterListener = (CommandeFilterListener) getActivity();
                    commandeFilterListener.onDataReceived(commandeFilterElements);
                    dismiss();
                }else{
                    Toast.makeText(getActivity(), "Intervalle de date incorrecte ou statut non selectionné", Toast.LENGTH_LONG).show();
                }
            }catch (ParseException e){
                e.printStackTrace();
            }

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

    /**
     * Attribuer la date selectionnée au champ de date inf
     **/
    public void setDateOnEdtDate() {
        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DpDaInf = new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            EdtDaInf.setText(dateFormat.format(newDate.getTime()));
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Attribuer la date selectionnée au champ de date sup
     **/
    public void setDateOnEdtDaSup() {
        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DpDaSup = new DatePickerDialog(getActivity(), (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            EdtDaSup.setText(dateFormat.format(newDate.getTime()));
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    /**popularise Liste statut commande*/
    public void populariseStatutCommande(){
        statutCommandes = new ArrayList<>();
        statutCommandes.add(new StatutCommande("E", "En cours"));
        statutCommandes.add(new StatutCommande("S", "Soldé"));
        statutCommandes.add(new StatutCommande("P", "Préparation"));
        statutCommandes.add(new StatutCommande("R", "Reliquat"));

        statutCommandeAdapter = new StatutCommandeAdapter(getActivity(), android.R.layout.simple_spinner_item, statutCommandes);
        MbSpnComSta.setAdapter(statutCommandeAdapter);
    }
}
