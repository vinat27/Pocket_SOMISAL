package com.sominfor.somisal_app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.activities.LoginActivity;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.interfaces.CallbackListener;
import com.sominfor.somisal_app.utils.UserSessionManager;

/**
 * Créé par vatsou le 08,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class ProduitFragment extends Fragment implements SearchView.OnQueryTextListener, CallbackListener {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.produit_fragment, container, false);
        /***** Déclaration de barre de menu dans le fragment*******/
        setHasOptionsMenu(true);
        if (!UserSessionManager.getInstance(getActivity()).isLoggedIn()) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));

        }


        return view;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        //interventionAdapter.getFilter().filter(newText);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Gestion de menu
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            /***Filtre, ouverture du formulaire**/
            openDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.produit_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void openDialog() {
        FilterProduitFullDialog.display(getFragmentManager());
    }

    @Override
    public void onDataReceived(String data) {

    }
}
