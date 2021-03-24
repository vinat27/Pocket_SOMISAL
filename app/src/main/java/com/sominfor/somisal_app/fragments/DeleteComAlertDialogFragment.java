package com.sominfor.somisal_app.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.handler.models.Commande;
import com.sominfor.somisal_app.handler.models.Devis;

/**
 * Créé par vatsou le 12,mars,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class DeleteComAlertDialogFragment extends DialogFragment {
    public static DeleteComAlertDialogFragment newInstance(){ return new DeleteComAlertDialogFragment(); }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Commande commande = (Commande) getArguments().getSerializable("commande");

        return new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.deleteCommandeAlertDialogFragment_Title))
                .setMessage(getResources().getString(R.string.delete_commande_alert_dialog_message))
                .setPositiveButton(getResources().getString(R.string.delete_commande_alert_dialog_ok),
                        (dialog, whichButton) -> CommandeFragment.getInstance().doDeletePositiveClick(commande)
                )

                .setNegativeButton(getResources().getString(R.string.delete_commande_alert_dialog_cancel),
                        (dialog, whichButton) ->  CommandeFragment.getInstance().doDeleteNegativeClick(commande)
                )
                .create();
    }
}
