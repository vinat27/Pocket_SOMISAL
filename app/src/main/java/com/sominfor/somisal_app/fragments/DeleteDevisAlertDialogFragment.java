package com.sominfor.somisal_app.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.handler.models.Devis;

/**
 * Créé par vatsou le 12,mars,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class DeleteDevisAlertDialogFragment extends DialogFragment {
    public static DeleteDevisAlertDialogFragment newInstance(){ return new DeleteDevisAlertDialogFragment(); }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Devis devis = (Devis) getArguments().getSerializable("devis");

        return new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.DeleteAlertDialogFragment_Title))
                .setMessage(getResources().getString(R.string.DeleteDevis_alert_dialog_message))
                .setPositiveButton(getResources().getString(R.string.DeleteDevis_alert_dialog_ok),
                        (dialog, whichButton) -> DevisFragment.getInstance().doDeleteDevisPositiveClick(devis)
                )

                .setNegativeButton(getResources().getString(R.string.DeleteDevis_alert_dialog_cancel),
                        (dialog, whichButton) ->  DevisFragment.getInstance().doDeleteDevisNegativeClick(devis)
                )
                .create();
    }
}
