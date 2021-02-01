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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.activities.AddCommandeActivity;
import com.sominfor.somisal_app.activities.AddDevisActivity;
import com.sominfor.somisal_app.activities.LoginActivity;
import com.sominfor.somisal_app.adapters.CommandeAdapter;
import com.sominfor.somisal_app.adapters.DevisAdapter;
import com.sominfor.somisal_app.handler.models.Commande;
import com.sominfor.somisal_app.handler.models.Devis;
import com.sominfor.somisal_app.utils.UserSessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Créé par vatsou le 26,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Répertoire des commandes
 */
public class CommandeFragment extends Fragment {
    FrameLayout frameLayout, container;
    RecyclerView recyclerViewCde;
    List<Commande> commandeList;
    private MenuItem mSearchItem;
    private SearchView sv;
    CommandeAdapter commandeAdapter;
    FloatingActionButton fab_add_commande;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.commande_fragment, container, false);
        /***** Déclaration de barre de menu dans le fragment*******/
        setHasOptionsMenu(true);
        getActivity().setTitle("Commandes");
        /**Contrôle session***/
        if (!UserSessionManager.getInstance(getActivity()).isLoggedIn()) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        /***Instanciation des widgets***/
        frameLayout = view.findViewById(R.id.frameLayout);
        container = view.findViewById(R.id.container);
        recyclerViewCde = view.findViewById(R.id.RecyclerViewCde);
        fab_add_commande = view.findViewById(R.id.fab_add_commande);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerViewCde.setLayoutManager(linearLayoutManager);

        initData();
        if (commandeList.isEmpty()){
            frameLayout.setVisibility(View.VISIBLE);
        }
        setRecyclerview();
        /**Ajout de commande**/
        fab_add_commande.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), AddCommandeActivity.class);
            startActivity(i);
        });

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
                commandeAdapter.getFilter().filter(query);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void setRecyclerview(){
        commandeAdapter = new CommandeAdapter(getActivity().getApplicationContext(), commandeList);
        recyclerViewCde.setAdapter(commandeAdapter);
        recyclerViewCde.setHasFixedSize(true);
    }

    public void initData(){
        commandeList = new ArrayList<>();
        commandeList.add(new Commande("0000002", "2021-01-26", "Saint Pierre", 28.28, "BOULANGERIE PATISSERIE CARRON",  "2021-01-29", "TOURNEE SUD", "RLS COTRAM"));
        commandeList.add(new Commande("0000003", "2021-01-27", "Saint Pierre", 30.28, "BOULANGERIE PATISSERIE AFC",  "2021-01-31", "TOURNEE NORD", "RLS COTRAM"));

    }
}
