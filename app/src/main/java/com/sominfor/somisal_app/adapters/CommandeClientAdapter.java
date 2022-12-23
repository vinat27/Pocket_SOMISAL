package com.sominfor.somisal_app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Créé par vatsou le 02,mars,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class CommandeClientAdapter extends RecyclerView.Adapter<CommandeClientAdapter.CommandeVh>{
    private static List<Commande> commandeList;
    private Context context;
    private List<Commande> commandeSearchs;
    /**Constructeur**/
    public CommandeClientAdapter(Context context, List<Commande> commandeList){
        this.context = context;
        this.commandeList = commandeList;
        commandeSearchs = new ArrayList<>(commandeList);
    }
    @NonNull
    @Override
    public CommandeClientAdapter.CommandeVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_commande_client, parent, false);
        return new CommandeClientAdapter.CommandeVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommandeClientAdapter.CommandeVh holder, int position) {
        Commande commande = commandeList.get(position);
/** Formattage des dates*****/
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fromUser = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String ComDacomFormat = "";
        String ComDalivFormat = "";
        BigDecimal bd = new BigDecimal(commande.getComvacom());
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');

        DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);
        formatter.setRoundingMode(RoundingMode.DOWN);
        String vacom = formatter.format(bd.floatValue()) + " " + commande.getComlimon();

        try {
            ComDacomFormat = fromUser.format(Objects.requireNonNull(myFormat.parse(commande.getComdacom())));
            ComDalivFormat = fromUser.format(Objects.requireNonNull(myFormat.parse(commande.getComdaliv())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        switch (commande.getComstatu()){
            case "I":
            case "E":
                ((GradientDrawable) holder.TxtIcon.getBackground()).setColor(context.getResources().getColor(R.color.colorPrimary));
                break;
            case "P":
                ((GradientDrawable) holder.TxtIcon.getBackground()).setColor(context.getResources().getColor(R.color.orange));
                break;
            case "R":
                ((GradientDrawable) holder.TxtIcon.getBackground()).setColor(context.getResources().getColor(R.color.orange_darken_3));
                break;
            case "S":
                ((GradientDrawable) holder.TxtIcon.getBackground()).setColor(context.getResources().getColor(R.color.green_darken_3));
                break;
            default:
                holder.TxtIcon.setVisibility(View.GONE);
        }

        /**Initialisation des informations devis**/
        holder.TxtComrasoc.setText(commande.getComrasoc());
        holder.TxtComdaliv.setText(ComDalivFormat);
        holder.TxtComcoliv.setText(commande.getComlista());
        holder.TxtComVacom.setText(vacom);
        holder.TxtComNucom.setText(commande.getComnucom());
        holder.TxtComDacom.setText(ComDacomFormat);
        holder.TxtComLieuv.setText(commande.getComlilieuv());
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

        TextView TxtComrasoc,TxtComdaliv,TxtComVacom, TxtComNucom, TxtComDacom, TxtComLieuv, TxtComcoliv, TxtIcon;
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
            TxtComcoliv = itemView.findViewById(R.id.TxtComStatu);
            TxtIcon = itemView.findViewById(R.id.TxtIcon);
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

    public void filterDateRange(Date charText, Date charText1, String statut) {

        List<Commande> filteredList = new ArrayList<>();
        if (charText.equals("")||charText.equals(null)) {
            filteredList.addAll(commandeSearchs);
        } else {
            for (Commande wp : commandeSearchs) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date strDate = sdf.parse(wp.getComdacom());
                    if (wp.getComstatu().equals(statut) && charText1.compareTo(strDate) >= 0 && charText.compareTo(strDate) <= 0) {
                        filteredList.add(wp);
                    }else{

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
        commandeList.clear();
        commandeList.addAll(filteredList);
        notifyDataSetChanged();
    }

}
