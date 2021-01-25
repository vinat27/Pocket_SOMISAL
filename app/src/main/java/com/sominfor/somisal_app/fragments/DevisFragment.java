package com.sominfor.somisal_app.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.activities.AddDevisActivity;
import com.sominfor.somisal_app.activities.FicheProduitActivity;
import com.sominfor.somisal_app.activities.LoginActivity;
import com.sominfor.somisal_app.adapters.DevisAdapter;
import com.sominfor.somisal_app.handler.models.Devis;
import com.sominfor.somisal_app.utils.UserSessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Créé par vatsou le 13,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Liste des devis
 */
public class DevisFragment extends Fragment {
    FrameLayout frameLayout;
    RecyclerView recyclerViewDevis;
    List<Devis> devisList;
    private MenuItem mSearchItem;
    private SearchView sv;
    DevisAdapter devisAdapter;
    FloatingActionButton fab_add_devis;
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

        /***Instanciation des widgets***/
        frameLayout = view.findViewById(R.id.frameLayout);
        recyclerViewDevis = view.findViewById(R.id.RecyclerViewDevis);
        fab_add_devis = view.findViewById(R.id.fab_add_devis);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerViewDevis.setLayoutManager(linearLayoutManager);

        initData();
        if (devisList.isEmpty()){
            frameLayout.setVisibility(View.VISIBLE);
        }
        setRecyclerview();

        /***Ajout de devis**/
        fab_add_devis.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), AddDevisActivity.class);
            startActivity(i);
        });

        return view;
    }

    public void setRecyclerview(){
        devisAdapter = new DevisAdapter(getActivity().getApplicationContext(), devisList);
        recyclerViewDevis.setAdapter(devisAdapter);
        recyclerViewDevis.setHasFixedSize(true);
    }

    public void initData(){
        devisList = new ArrayList<>();

        devisList.add(new Devis("0000002", "2021-01-13", "*", "Saint Pierre", 28.28, "2021-01-13", "BOULANGERIE PATISSERIE CARRON"));
        devisList.add(new Devis("0000003", "2021-01-13", "*", "Saint Pierre", 30.28, "2021-01-13", "ALFACASH"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Gestion de menu
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.devis_fragment_menu, menu);
        /**Gestion de menu recherche**/
        mSearchItem = menu.findItem(R.id.action_search);
        sv = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        sv.setIconified(true);

        SearchManager searchManager = (SearchManager)  getActivity().getSystemService(Context.SEARCH_SERVICE);
        sv.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                devisAdapter.getFilter().filter(query);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

}
