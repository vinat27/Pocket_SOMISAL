package com.sominfor.somisal_app.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.activities.AddProduitDevisActivity;
import com.sominfor.somisal_app.activities.UpdateProduitDevisActivity;
import com.sominfor.somisal_app.handler.models.DetailDevis;

/**
 * Créé par vatsou le 28,février,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class DeleteAlertDialogFragmentFromUpdate extends DialogFragment {
    public static DeleteAlertDialogFragmentFromUpdate newInstance(){ return new DeleteAlertDialogFragmentFromUpdate(); }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DetailDevis detailDevis = (DetailDevis) getArguments().getSerializable("detailDevis");

        return new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.DeleteAlertDialogFragment_Title))
                .setMessage(getResources().getString(R.string.alert_dialog_message))
                .setPositiveButton(getResources().getString(R.string.alert_dialog_ok),
                        (dialog, whichButton) -> ((UpdateProduitDevisActivity)getActivity()).doPositiveClick(detailDevis)
                )

                .setNegativeButton(getResources().getString(R.string.alert_dialog_cancel),
                        (dialog, whichButton) -> ((UpdateProduitDevisActivity)getActivity()).doNegativeClick(detailDevis)
                )
                .create();
    }
}
