package com.sominfor.somisal_app.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sominfor.somisal_app.handler.models.Systeme;

import java.util.List;

/**
 * Créé par vatsou le 06,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Adapter - Gestion de liste de systèmes
 */
public class SystemeAdapter extends ArrayAdapter<Systeme> {
    private Context context;
    private List<Systeme> values;
    /**Constructeur**/
    public SystemeAdapter(Context context, int textViewResourceId, List<Systeme> values){
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }
    /**Longueur de la liste**/
    @Override
    public int getCount(){
        return values.size();
    }
    /**Objet Systeme**/
    @Override
    public Systeme getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        /**Récupération des informations de la classe courante**/
        label.setText(values.toArray(new Object[values.size()])[position].toString());
        label.setPadding(18,18,18,18);
        label.setTextSize(18);

        return label;
    }
    /***Organisation de la boite de select**/
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setText(values.toArray(new Object[values.size()])[position].toString());
        label.setPadding(18,18,18,18);
        label.setTextSize(18);
        return label;
    }

}
