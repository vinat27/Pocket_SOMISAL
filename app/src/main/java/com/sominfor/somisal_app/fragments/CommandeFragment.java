package com.sominfor.somisal_app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.activities.LoginActivity;
import com.sominfor.somisal_app.utils.UserSessionManager;

/**
 * Créé par vatsou le 26,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Répertoire des commandes
 */
public class CommandeFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.devis_fragment, container, false);
        /***** Déclaration de barre de menu dans le fragment*******/
        setHasOptionsMenu(true);
        getActivity().setTitle("Devis");
        /**Contrôle session***/
        if (!UserSessionManager.getInstance(getActivity()).isLoggedIn()) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Gestion de menu
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.commande_fragment_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }
}
