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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.activities.LoginActivity;
import com.sominfor.somisal_app.adapters.ClientAdapter;
import com.sominfor.somisal_app.handler.models.Client;
import com.sominfor.somisal_app.utils.UserSessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

/**
 * Créé par vatsou le 25,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Répertoire de clients
 */
public class ClientFragment extends Fragment {
    IndexFastScrollRecyclerView mRecyclerView;
    List<Client> clients;
    ClientAdapter clientAdapter;
    private MenuItem mSearchItem;
    private SearchView sv;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.client_fragment, container, false);
        /***** Déclaration de barre de menu dans le fragment*******/
        setHasOptionsMenu(true);
        getActivity().setTitle("Clients");
        if (!UserSessionManager.getInstance(getActivity()).isLoggedIn()) {
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));

        }

        /**initialisation des Widgets**/
        mRecyclerView =  view.findViewById(R.id.fast_scroller_recycler_client);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        initData();
        setRecyclerview();
        initialiseUI();


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
        inflater.inflate(R.menu.client_fragment_menu, menu);
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
                clientAdapter.getFilter().filter(query);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**Barre de choix de recherche par ordre alphabétique**/
    protected void initialiseUI() {

        mRecyclerView.setIndexTextSize(12);
        mRecyclerView.setIndexBarColor("#33334c");
        mRecyclerView.setIndexBarCornerRadius(3);
        mRecyclerView.setIndexBarTransparentValue((float) 0.6);
        mRecyclerView.setIndexbarMargin(0);
        mRecyclerView.setIndexbarWidth(70);
        mRecyclerView.setPreviewPadding(0);
        mRecyclerView.setIndexBarTextColor("#FFFFFF");

        mRecyclerView.setPreviewTextSize(60);
        mRecyclerView.setPreviewColor("#33334c");
        mRecyclerView.setPreviewTextColor("#FFFFFF");
        mRecyclerView.setPreviewTransparentValue(0.6f);

        mRecyclerView.setIndexBarVisibility(true);

        mRecyclerView.setIndexBarStrokeVisibility(true);
        mRecyclerView.setIndexBarStrokeWidth(1);
        mRecyclerView.setIndexBarStrokeColor("#000000");

        mRecyclerView.setIndexbarHighLightTextColor("#33334c");
        mRecyclerView.setIndexBarHighLightTextVisibility(true);

        Objects.requireNonNull(mRecyclerView.getLayoutManager()).scrollToPosition(0);
    }

    public void setRecyclerview(){
        clientAdapter = new ClientAdapter(getActivity().getApplicationContext(), clients);
        mRecyclerView.setAdapter(clientAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    public void initData(){
        clients = new ArrayList<>();

        clients.add(new Client("001186", "Boulangerie Patisserie Artisanale", "BOULANGERIE PATISSERIE CARRON"));
        clients.add(new Client("001071", "Grande et Moyenne Surface", "ANDAMAYE CATHERINE - LOR EMY"));
    }
}
