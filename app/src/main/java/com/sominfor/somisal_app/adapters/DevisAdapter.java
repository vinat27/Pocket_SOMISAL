package com.sominfor.somisal_app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.activities.FicheDevisActivity;
import com.sominfor.somisal_app.activities.UpdateDevisActivity;
import com.sominfor.somisal_app.handler.models.Devis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Créé par vatsou le 13,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 */

//TODO: 27-02-2021 Gestion des boutons archiver - supprimer - valider


public class DevisAdapter extends RecyclerView.Adapter<DevisAdapter.DevisVh> {
    private static List<Devis> devisList;
    private List<Devis> devisSearchs;
    private Context context;
    /**Constructeur**/
    public DevisAdapter(Context context, List<Devis> devisList){
        this.context = context;
        this.devisList = devisList;
        devisSearchs = new ArrayList<>(devisList);
    }
    @NonNull
    @Override
    public DevisAdapter.DevisVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_devis, parent, false);
        return new DevisAdapter.DevisVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DevisAdapter.DevisVh holder, int position) {
        Devis devis = devisList.get(position);
        /** Formattage des dates*****/
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fromUser = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String DevDadevFormat = "";
        String DevDalivFormat = "";
        String vadev = String.format("%.2f", devis.getDevVadev())+" "+devis.getDevComon().trim();

        try {
            DevDadevFormat = fromUser.format(Objects.requireNonNull(myFormat.parse(devis.getDevDadev())));
            DevDalivFormat = fromUser.format(Objects.requireNonNull(myFormat.parse(devis.getDevDaliv())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /**Initialisation des informations devis**/
        holder.TxtClirasoc.setText(devis.getCliRasoc().trim());
        holder.TxtDevDaliv.setText(DevDalivFormat);
        holder.TxtDevVadev.setText(vadev);
        holder.TxtDevNudev.setText(devis.getDevNudev());
        holder.TxtDevDadev.setText(DevDadevFormat);
        holder.TxtDevLieuv.setText(devis.getDevLieuv().trim());
        holder.TxtDevRefdev.setText(devis.getDevRfdev().trim());
        /**Au clic du bouton détail**/
        holder.FabDevisDetails.setOnClickListener(v -> {
            Intent i = new Intent(context, FicheDevisActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("devis", devis);
            i.putExtras(bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        });

        /**Button de modification**/
        holder.FabUpdateDevis.setOnClickListener(v -> {
            Intent i = new Intent(context, UpdateDevisActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("devis", devis);
            i.putExtras(bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        });

        boolean isExpandable = devisList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return devisList == null ? 0 : devisList.size();
    }


    public Devis getItem(int position){
        return devisList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public class DevisVh extends RecyclerView.ViewHolder {

        TextView TxtClirasoc,TxtDevDaliv,TxtDevVadev, TxtDevNudev, TxtDevDadev, TxtDevLieuv, TxtDevRefdev;
        MaterialButton FabDevisDetails, FabUpdateDevis, FabValiderDevis, FabArchiverDevis, FabDeleteDevis;
        LinearLayout Lnr01, expandableLayout;

        public DevisVh(View itemView) {
            super(itemView);

            /**Instanciation des widgets**/
            TxtClirasoc = itemView.findViewById(R.id.TxtClirasoc);
            TxtDevDaliv = itemView.findViewById(R.id.TxtDevDaliv);
            TxtDevVadev = itemView.findViewById(R.id.TxtDevVadev);
            TxtDevNudev = itemView.findViewById(R.id.TxtDevNudev);
            TxtDevDadev = itemView.findViewById(R.id.TxtDevDadev);
            TxtDevLieuv = itemView.findViewById(R.id.TxtDevLieuv);
            TxtDevRefdev = itemView.findViewById(R.id.TxtDevRefdev);
            FabDevisDetails = itemView.findViewById(R.id.FabDevisDetails);
            FabUpdateDevis = itemView.findViewById(R.id.FabUpdateDevis);
            FabValiderDevis = itemView.findViewById(R.id.FabValiderDevis);
            FabDeleteDevis = itemView.findViewById(R.id.FabDeleteDevis);
            FabArchiverDevis = itemView.findViewById(R.id.FabArchiverDevis);
            Lnr01 = itemView.findViewById(R.id.Lnr01);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            Lnr01.setOnClickListener(v -> {
                Devis devis = devisList.get(getAdapterPosition());
                devis.setExpandable(!devis.isExpandable());
                notifyItemChanged(getAdapterPosition());
            });

        }
    }

    /**Filtre des informations avec formulaire de recherche**/
    public Filter getFilter() {
        return devisFilter;
    }

    private Filter devisFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Devis> filteredList = new ArrayList<>();
            /**Contrôle valeur de recherche**/
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(devisSearchs);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                /**Parcours de la liste des interventions**/
                for (Devis item : devisSearchs) {
                    /**Comparaison des résultats et ajout dans la liste de résultats**/
                    if (item.getCliRasoc().toLowerCase().contains(filterPattern) ) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }
        /**Actualisation de la RecyclerView**/
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            devisList.clear();
            devisList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
