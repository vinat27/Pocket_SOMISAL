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
import com.sominfor.somisal_app.activities.FicheDevisActivity;
import com.sominfor.somisal_app.activities.UpdateDevisActivity;
import com.sominfor.somisal_app.handler.models.Commande;
import com.sominfor.somisal_app.handler.models.Devis;

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
 * Créé par vatsou le 07,mars,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class DevisClientAdapter extends RecyclerView.Adapter<DevisClientAdapter.DevisVh> {
    private static List<Devis> devisList;
    private Context context;
    private List<Devis> devisSearchs;
    /**Constructeur**/
    public DevisClientAdapter(Context context, List<Devis> devisList){
        this.context = context;
        this.devisList = devisList;
        devisSearchs = new ArrayList<>(devisList);
    }
    @NonNull
    @Override
    public DevisClientAdapter.DevisVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_devis_client, parent, false);
        return new DevisClientAdapter.DevisVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DevisClientAdapter.DevisVh holder, int position) {
        Devis devis = devisList.get(position);
        /** Formattage des dates*****/
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fromUser = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String DevDadevFormat = "";
        String DevDalivFormat = "";
        BigDecimal bd = new BigDecimal(devis.getDevVadev());
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');

        DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);
        formatter.setRoundingMode(RoundingMode.DOWN);
        String vadev = formatter.format(bd.floatValue())+" "+devis.getDevlimon().trim();


        try {
            DevDadevFormat = fromUser.format(Objects.requireNonNull(myFormat.parse(devis.getDevDadev())));
            DevDalivFormat = fromUser.format(Objects.requireNonNull(myFormat.parse(devis.getDevDaliv())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch (devis.getDevStatut()){
            case "E":
                ((GradientDrawable) holder.TxtIcon.getBackground()).setColor(context.getResources().getColor(R.color.colorPrimary));
                break;
            case "H":
                ((GradientDrawable) holder.TxtIcon.getBackground()).setColor(context.getResources().getColor(R.color.orange));
                break;
            case "S":
                ((GradientDrawable) holder.TxtIcon.getBackground()).setColor(context.getResources().getColor(R.color.green_darken_3));
                break;
            default:
                holder.TxtIcon.setVisibility(View.GONE);
        }
        /**Initialisation des informations devis**/
        holder.TxtClirasoc.setText(devis.getCliRasoc().trim());
        holder.TxtDevDaliv.setText(DevDadevFormat);
        holder.TxtDevVadev.setText(vadev);
        holder.TxtDevNudev.setText(devis.getDevNudev());
        holder.TxtDevDadev.setText(DevDadevFormat);
        holder.TxtDevLieuv.setText(devis.getDevLieuv().trim());
        /**Au clic du bouton détail**/
        holder.FabDevisDetails.setOnClickListener(v -> {
            Intent i = new Intent(context, FicheDevisActivity.class);
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

        TextView TxtClirasoc,TxtDevDaliv,TxtDevVadev, TxtDevNudev, TxtDevDadev, TxtDevLieuv, TxtIcon;
        MaterialButton FabDevisDetails;
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
            TxtIcon = itemView.findViewById(R.id.TxtIcon);
            FabDevisDetails = itemView.findViewById(R.id.FabDevisDetails);
            Lnr01 = itemView.findViewById(R.id.Lnr01);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            Lnr01.setOnClickListener(v -> {
                Devis devis = devisList.get(getAdapterPosition());
                devis.setExpandable(!devis.isExpandable());
                notifyItemChanged(getAdapterPosition());
            });

        }
    }

    public void filterDateRange(Date charText, Date charText1, String statut) {
        List<Devis> filteredList = new ArrayList<>();
        if (charText.equals("")||charText.equals(null)) {
            filteredList.addAll(devisSearchs);
        } else {
            for (Devis wp : devisSearchs) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date strDate = sdf.parse(wp.getDevDadev());
                    if (wp.getDevStatut().equals(statut) && charText1.compareTo(strDate) >= 0 && charText.compareTo(strDate) <= 0) {
                        filteredList.add(wp);
                    }else{

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
        devisList.clear();
        devisList.addAll(filteredList);
        notifyDataSetChanged();
    }
}
