package com.sominfor.somisal_app.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.activities.AddProduitCommandeActivity;
import com.sominfor.somisal_app.activities.UpdateProduitCommandeActivity;
import com.sominfor.somisal_app.activities.UpdateProduitDevisActivity;
import com.sominfor.somisal_app.handler.models.DetailCommande;
import com.sominfor.somisal_app.handler.models.DetailDevis;

/**
 * Créé par vatsou le 19,mars,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class DeleteAlertDialogCdeFromUpdate  extends DialogFragment {
    public static DeleteAlertDialogCdeFromUpdate newInstance(){ return new DeleteAlertDialogCdeFromUpdate(); }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DetailCommande detailCommande = (DetailCommande) getArguments().getSerializable("detailCommande");

        return new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.DeleteAlertDialogFragment_Title))
                .setMessage(getResources().getString(R.string.alert_dialog_message))
                .setPositiveButton(getResources().getString(R.string.alert_dialog_ok),
                        (dialog, whichButton) -> ((UpdateProduitCommandeActivity)getActivity()).doPositiveClick(detailCommande)
                )

                .setNegativeButton(getResources().getString(R.string.alert_dialog_cancel),
                        (dialog, whichButton) -> ((UpdateProduitCommandeActivity)getActivity()).doNegativeClick(detailCommande)
                )
                .create();
    }
}

