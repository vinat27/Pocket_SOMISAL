package com.sominfor.somisal_app.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.activities.AddProduitCommandeActivity;
import com.sominfor.somisal_app.activities.AddProduitDevisActivity;
import com.sominfor.somisal_app.handler.models.DetailCommande;
import com.sominfor.somisal_app.handler.models.DetailDevis;

/**
 * Créé par vatsou le 02,février,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class DeleteCommandeProduitAlertDialogFragment extends DialogFragment {
    public static DeleteCommandeProduitAlertDialogFragment newInstance(){ return new DeleteCommandeProduitAlertDialogFragment(); }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DetailCommande detailCommande = (DetailCommande) getArguments().getSerializable("detailCommande");

        return new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.DeleteAlertDialogFragment_Title))
                .setMessage(getResources().getString(R.string.alert_dialog_message))
                .setPositiveButton(getResources().getString(R.string.alert_dialog_ok),
                        (dialog, whichButton) -> ((AddProduitCommandeActivity)getActivity()).doPositiveClick(detailCommande)
                )

                .setNegativeButton(getResources().getString(R.string.alert_dialog_cancel),
                        (dialog, whichButton) -> ((AddProduitCommandeActivity)getActivity()).doNegativeClick(detailCommande)
                )
                .create();
    }
}
