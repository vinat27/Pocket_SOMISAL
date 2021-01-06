package com.sominfor.somisal_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.DialogFragment;

import com.sominfor.somisal_app.R;

/**
 * Créé par vatsou le 06,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Ajout, Modification de serveur
 */
public class ServeurNodeFragment extends DialogFragment {
    public ServeurNodeFragment(){}
    public static ServeurNodeFragment newInstance(){ return new ServeurNodeFragment(); }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        getDialog().setTitle(getResources().getString(R.string.serveur_node_fragment_Title));
        View view = inflater.inflate(R.layout.serveur_node_fragment, container);


        return view;
    }


}
