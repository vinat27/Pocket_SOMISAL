package com.sominfor.somisal_app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.activities.SettingsActivity;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.ServeurNode;

/**
 * Créé par vatsou le 06,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Ajout, Modification de serveur
 */
public class ServeurNodeFragment extends DialogFragment {
    TextInputEditText EdtServeurNodeIp;
    Button BtnValider;
    Boolean statut;
    ServeurNodeController serveurNodeController;
    ServeurNode serveurNode;

    public ServeurNodeFragment(){}
    public static ServeurNodeFragment newInstance(){ return new ServeurNodeFragment(); }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        getDialog().setTitle(getResources().getString(R.string.serveur_node_fragment_Title));
        View view = inflater.inflate(R.layout.serveur_node_fragment, container);

        /**Instanciation des widgets**/
        EdtServeurNodeIp = view.findViewById(R.id.EdtServeurNodeIp);
        BtnValider = view.findViewById(R.id.BtnValider);
        serveurNodeController = new ServeurNodeController();
        serveurNode = serveurNodeController.getServeurNodeInfos();
        EdtServeurNodeIp.setText(serveurNode.getServeurNodeIp());
        /**Validation formulaire**/
        BtnValider.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (EdtServeurNodeIp.getText().length() != 0){
                    statut = serveurNodeController.checkIfIsExist();
                    /***Mise à jour si existe sinon création***/
                    if (statut){
                        /**Création**/
                        ServeurNode serveurNode = new ServeurNode();
                        serveurNode.setServeurNodeIp(EdtServeurNodeIp.getText().toString());
                        if (serveurNodeController.update(serveurNode)){
                            Toast.makeText(getActivity(),getResources().getString(R.string.serveur_node_fragment_toast03), Toast.LENGTH_SHORT).show();
                            getDialog().cancel();
                            ((SettingsActivity) getActivity()).setServeurNodeToTextView(EdtServeurNodeIp.getText().toString());
                        }else{
                            Toast.makeText(getActivity(),getResources().getString(R.string.serveur_node_fragment_toast02), Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        /**Mise à jour**/
                        ServeurNode serveurNode = new ServeurNode();
                        serveurNode.setServeurNodeIp(EdtServeurNodeIp.getText().toString());
                        if (serveurNodeController.insert(serveurNode)){
                            Toast.makeText(getActivity(),getResources().getString(R.string.serveur_node_fragment_toast01), Toast.LENGTH_SHORT).show();
                            getDialog().cancel();
                            ((SettingsActivity) getActivity()).setServeurNodeToTextView(EdtServeurNodeIp.getText().toString());
                        }else{
                            Toast.makeText(getActivity(),getResources().getString(R.string.serveur_node_fragment_toast02), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        return view;
    }


}
