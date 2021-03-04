package com.sominfor.somisal_app.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.activities.FicheDevisActivity;
import com.sominfor.somisal_app.activities.FicheProduitActivity;
import com.sominfor.somisal_app.fragments.CommentPosteFullDialog;
import com.sominfor.somisal_app.fragments.FilterProduitFullDialog;
import com.sominfor.somisal_app.handler.models.DetailDevis;
import com.sominfor.somisal_app.handler.models.Devis;
import com.sominfor.somisal_app.handler.models.Produit;
import com.sominfor.somisal_app.handler.models.ServeurNode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Créé par vatsou le 16,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class DetailDevisAdapter extends RecyclerView.Adapter<DetailDevisAdapter.DetailDevisVh> {
    private static List<DetailDevis> detailDevisList;
    private List<DetailDevis> detailDevisSearch;
    private Context context;
    AppCompatActivity activity;
    FragmentManager fragmentManager;

    /**Constructeur**/
    public DetailDevisAdapter(Context context, List<DetailDevis> detailDevisList, FragmentManager fragmentManager){
        this.context = context;
        this.detailDevisList = detailDevisList;
        detailDevisSearch = new ArrayList<>(detailDevisList);
        this.fragmentManager = fragmentManager;
    }
    @NonNull
    @Override
    public DetailDevisAdapter.DetailDevisVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_details_devis, parent, false);
        return new DetailDevisAdapter.DetailDevisVh(view);
    }
    @Override
    public void onBindViewHolder(@NonNull DetailDevisAdapter.DetailDevisVh holder, int position) {
        DetailDevis detailDevis = detailDevisList.get(position);

        String Qtdev = detailDevis.getDdvQtdev()+detailDevis.getDdvUnvte();
        Double wvarem = 0.00;
        Double wvapos = 0.00;

        /**Calcul de la valeur de poste**/
        if (detailDevis.getDdvTxrem() > 0){
            wvarem = (detailDevis.getDdvQtdev() * detailDevis.getDdvPutar()) * (detailDevis.getDdvTxrem()/100);
        }
        if (detailDevis.getDdvVarem() > 0){
            wvarem   = wvarem + (detailDevis.getDdvQtdev() * detailDevis.getDdvVarem());
        }
        /***Calcul de la valeur de poste**/
        wvapos = detailDevis.getDdvPutar() * detailDevis.getDdvQtdev();
        wvapos = wvapos - wvarem;

        /**Initialisation des informations devis**/
        holder.TxtProLipro.setText(detailDevis.getDdvLipro());
        holder.TxtDdvPodev.setText(String.valueOf(detailDevis.getDdvPodev()));
        holder.TxtDdvPutar.setText(String.valueOf(detailDevis.getDdvPutar()));
        holder.TxtDdvQtdev.setText(Qtdev);
        holder.TxtDdvVadev.setText(String.format("%.2f", wvapos));
        holder.TxtDdvTxRem.setText(String.valueOf(detailDevis.getDdvTxrem()));
        holder.TxtDdvVarem.setText(String.valueOf(detailDevis.getDdvVarem()));
        /**Au clic du bouton détail**/
        holder.FabDetPro.setOnClickListener(v -> {
            Produit produit = new Produit();
            produit.setProcopro(detailDevis.getDdvCopro());
            produit.setPronuprm(detailDevis.getDdvNuprm());
            produit.setProlipro(detailDevis.getDdvLipro());

            if (produit.getPronuprm() != 0) {
                //Produits
                Intent i = new Intent(context, FicheProduitActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("produit", produit);
                i.putExtras(bundle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }else{
                Toast.makeText(context, context.getResources().getString(R.string.produit_fragment_noStock), Toast.LENGTH_LONG).show();
            }

        });

        /**Commentaires de poste***/
        holder.FabTxnPro.setOnClickListener(v -> {
            /***Ajout / Mise à jour de serveur**/

            CommentPosteFullDialog commentPosteFullDialog = CommentPosteFullDialog.newInstance();
            Bundle args = new Bundle();
            args.putString("txnpodev", detailDevis.getDdvTxnPo());
            commentPosteFullDialog.setArguments(args);
            commentPosteFullDialog.show(fragmentManager, ServeurNode.TAG);
        });

        boolean isExpandable = detailDevisList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return detailDevisList == null ? 0 : detailDevisList.size();
    }


    public DetailDevis getItem(int position){
        return detailDevisList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public class DetailDevisVh extends RecyclerView.ViewHolder {

        TextView TxtProLipro,TxtDdvPodev,TxtDdvQtdev, TxtDdvPutar, TxtDdvVadev, TxtDdvTxRem, TxtDdvVarem;
        MaterialButton FabDetPro, FabTxnPro;
        LinearLayout Lnr01, expandableLayout;


        public DetailDevisVh(View itemView) {
            super(itemView);

            /**Instanciation des widgets**/
            TxtProLipro = itemView.findViewById(R.id.TxtProLipro);
            TxtDdvPodev = itemView.findViewById(R.id.TxtDdvPodev);
            TxtDdvQtdev = itemView.findViewById(R.id.TxtDdvQtdev);
            TxtDdvPutar = itemView.findViewById(R.id.TxtDdvPutar);
            TxtDdvVadev = itemView.findViewById(R.id.TxtDdvVadev);
            TxtDdvTxRem = itemView.findViewById(R.id.TxtDdvTxRem);
            TxtDdvVarem = itemView.findViewById(R.id.TxtDdvVarem);
            FabDetPro = itemView.findViewById(R.id.FabDetPro);
            FabTxnPro = itemView.findViewById(R.id.FabTxnPro);
            Lnr01 = itemView.findViewById(R.id.Lnr01);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            Lnr01.setOnClickListener(v -> {
                DetailDevis detailDevis = detailDevisList.get(getAdapterPosition());
                detailDevis.setExpandable(!detailDevis.isExpandable());
                notifyItemChanged(getAdapterPosition());
            });

        }
    }

}
