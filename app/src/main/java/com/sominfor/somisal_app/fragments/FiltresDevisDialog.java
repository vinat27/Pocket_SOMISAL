package com.sominfor.somisal_app.fragments;

import android.app.DatePickerDialog;
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
import com.sominfor.somisal_app.handler.models.CommandeFilterElements;
import com.sominfor.somisal_app.interfaces.DevisFilterListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Créé par vatsou le 30,juin,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class FiltresDevisDialog extends DialogFragment {
    public static final String TAG = FiltresDevisDialog.class.getSimpleName();
    private Toolbar toolbar;
    TextInputEditText EdtDaInf, EdtDaSup;
    MaterialButton btnValider;
    DatePickerDialog DpDaInf, DpDaSup;
    DevisFilterListener devisFilterListener;

    public static FiltresDevisDialog newInstance(){ return new FiltresDevisDialog(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SomisalTheme_FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.filtres_devis_dialog, container, false);
        /***Instanciation des widgets***/
        toolbar = view.findViewById(R.id.toolbar);
        EdtDaInf = view.findViewById(R.id.EdtDateInf);
        EdtDaSup = view.findViewById(R.id.EdtDateSup);
        btnValider = view.findViewById(R.id.BtnValider);

        /**Date commande**/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        EdtDaInf.setText(sdf.format(new Date()));
        EdtDaSup.setText(sdf.format(new Date()));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(getResources().getString(R.string.filtres_devis_dialog));

        /**Champ de date, selection de date  DatePicker**/
        EdtDaInf.setOnClickListener(v -> DpDaInf.show());
        /**Marquer la date selectionnée dans le champ date devis**/
        setDateOnEdtDate();

        /**Champ de date, selection de date sup  DatePicker**/
        EdtDaSup.setOnClickListener(v -> DpDaSup.show());
        /**Marquer la date selectionnée dans le champ date devis**/
        setDateOnEdtDaSup();

        /***Au clic de validation**/
        btnValider.setOnClickListener(v -> {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date dateInf = simpleDateFormat.parse(EdtDaInf.getText().toString());
                Date dateSup = simpleDateFormat.parse(EdtDaSup.getText().toString());
                assert dateInf != null;
                /**Comparaison des dates**/
                if (dateSup.compareTo(dateInf) >= 0){
                    CommandeFilterElements commandeFilterElements = new CommandeFilterElements();
                    commandeFilterElements.setDateInf(EdtDaInf.getText().toString());
                    commandeFilterElements.setDateSup(EdtDaSup.getText().toString());

                    devisFilterListener = (DevisFilterListener) getActivity();
                    devisFilterListener.onDataReceivedDevis(commandeFilterElements);
                    dismiss();
                }else{
                    Toast.makeText(getActivity(), "Intervalle de date incorrecte", Toast.LENGTH_LONG).show();
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

}
