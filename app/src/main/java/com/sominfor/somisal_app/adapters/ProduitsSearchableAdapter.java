package com.sominfor.somisal_app.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sominfor.somisal_app.handler.models.Produit;

import java.util.List;

/**
 * Créé par vatsou le 23,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Adapter pour les combobox
 */
public class ProduitsSearchableAdapter extends ArrayAdapter<Produit> {
    private final Context context;
    private final List<Produit> values;

    public ProduitsSearchableAdapter(Context context, int textViewResourceId, List<Produit> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Produit getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setText(values.toArray(new Object[values.size()])[position].toString().toUpperCase());
        label.setPadding(18, 18, 18, 18);
        label.setTextSize(18);

        return label;
    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setText(values.toArray(new Object[values.size()])[position].toString().toUpperCase());
        label.setPadding(18, 18, 18, 18);
        label.setTextSize(18);
        return label;
    }
}
