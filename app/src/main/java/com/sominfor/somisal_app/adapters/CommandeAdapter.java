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
import com.sominfor.somisal_app.activities.CommandeDetailsActivity;
import com.sominfor.somisal_app.handler.models.Commande;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Créé par vatsou le 26,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Adapter des commandes
 */
public class CommandeAdapter extends RecyclerView.Adapter<CommandeAdapter.CommandeVh>{
private static List<Commande> commandeList;
private List<Commande> commandeSearchs;
private Context context;
/**Constructeur**/
public CommandeAdapter(Context context, List<Commande> commandeList){
        this.context = context;
        this.commandeList = commandeList;
        commandeSearchs = new ArrayList<>(commandeList);
        }
@NonNull
@Override
public CommandeAdapter.CommandeVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_commande, parent, false);
        return new CommandeAdapter.CommandeVh(view);
        }

@Override
public void onBindViewHolder(@NonNull CommandeAdapter.CommandeVh holder, int position) {
        Commande commande = commandeList.get(position);
/** Formattage des dates*****/
@SuppressLint("SimpleDateFormat") SimpleDateFormat fromUser = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String ComDacomFormat = "";
        String ComDalivFormat = "";
    String vacom = String.format("%.2f", commande.getComvacom()) + " " + commande.getComlimon();

    try {
        ComDacomFormat = fromUser.format(Objects.requireNonNull(myFormat.parse(commande.getComdacom())));
        ComDalivFormat = fromUser.format(Objects.requireNonNull(myFormat.parse(commande.getComdaliv())));
    } catch (ParseException e) {
        e.printStackTrace();
    }
    /**Initialisation des informations devis**/
    holder.TxtComrasoc.setText(commande.getComrasoc());
    holder.TxtComdaliv.setText(ComDalivFormat);
    holder.TxtComcoliv.setText(commande.getComliliv());
    holder.TxtComVacom.setText(vacom);
    holder.TxtComNucom.setText(commande.getComnucom());
    holder.TxtComDacom.setText(ComDacomFormat);
    holder.TxtComLieuv.setText(commande.getComlilieuv());
    holder.TxtComCotrn.setText(commande.getComlitrn());
    /**Au clic du bouton détail**/
    holder.FabComDetails.setOnClickListener(v -> {
        Intent i = new Intent(context, CommandeDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("commande", commande);
        i.putExtras(bundle);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    });

    boolean isExpandable = commandeList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
        }

@Override
public int getItemCount() {
        return commandeList == null ? 0 : commandeList.size();
        }


public Commande getItem(int position){
        return commandeList.get(position);
        }

@Override
public long getItemId(int position){
        return position;
        }

public class CommandeVh extends RecyclerView.ViewHolder {

    TextView TxtComrasoc,TxtComdaliv,TxtComVacom, TxtComNucom, TxtComDacom, TxtComLieuv, TxtComCotrn, TxtComcoliv;
    MaterialButton FabComDetails;
    LinearLayout Lnr01, expandableLayout;

    public CommandeVh(View itemView) {
        super(itemView);

        /**Instanciation des widgets**/
        TxtComrasoc = itemView.findViewById(R.id.TxtComrasoc);
        TxtComdaliv = itemView.findViewById(R.id.TxtComdaliv);
        TxtComVacom = itemView.findViewById(R.id.TxtComVacom);
        TxtComNucom = itemView.findViewById(R.id.TxtComNucom);
        TxtComDacom = itemView.findViewById(R.id.TxtComDacom);
        TxtComLieuv = itemView.findViewById(R.id.TxtComLieuv);
        TxtComCotrn = itemView.findViewById(R.id.TxtComCotrn);
        TxtComcoliv = itemView.findViewById(R.id.TxtComcoliv);
        FabComDetails = itemView.findViewById(R.id.FabComDetails);
        Lnr01 = itemView.findViewById(R.id.Lnr01);
        expandableLayout = itemView.findViewById(R.id.expandable_layout);

        Lnr01.setOnClickListener(v -> {
            Commande commande = commandeList.get(getAdapterPosition());
            commande.setExpandable(!commande.isExpandable());
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
            List<Commande> filteredList = new ArrayList<>();
            /**Contrôle valeur de recherche**/
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(commandeSearchs);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                /**Parcours de la liste des interventions**/
                for (Commande item : commandeSearchs) {
                    /**Comparaison des résultats et ajout dans la liste de résultats**/
                    if (item.getComrasoc().toLowerCase().contains(filterPattern) ) {
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
            commandeList.clear();
            commandeList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}
