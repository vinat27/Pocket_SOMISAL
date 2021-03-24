package com.sominfor.somisal_app.adapters;

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
import com.sominfor.somisal_app.activities.FicheProduitActivity;
import com.sominfor.somisal_app.fragments.CommentPosteFullDialog;
import com.sominfor.somisal_app.handler.models.DetailCommande;
import com.sominfor.somisal_app.handler.models.DetailDevis;
import com.sominfor.somisal_app.handler.models.Produit;
import com.sominfor.somisal_app.handler.models.ServeurNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Créé par vatsou le 27,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class DetailCommandeAdapter extends  RecyclerView.Adapter<DetailCommandeAdapter.DetailCommandeVh>{
    private static List<DetailCommande> detailCommandeList;
    private List<DetailCommande> detailCommandeSearch;
    private Context context;
    AppCompatActivity activity;
    FragmentManager fragmentManager;

    /**Constructeur**/
    public DetailCommandeAdapter(Context context, List<DetailCommande> detailCommandeList, FragmentManager fragmentManager){
        this.context = context;
        this.detailCommandeList = detailCommandeList;
        detailCommandeSearch = new ArrayList<>(detailCommandeList);
        this.fragmentManager = fragmentManager;
    }
    @NonNull
    @Override
    public DetailCommandeAdapter.DetailCommandeVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_details_commande, parent, false);
        return new DetailCommandeAdapter.DetailCommandeVh(view);
    }
    @Override
    public void onBindViewHolder(@NonNull DetailCommandeAdapter.DetailCommandeVh holder, int position) {
        DetailCommande detailCommande = detailCommandeList.get(position);
        String qtliv = String.format("%.3f", detailCommande.getDcoqtcom()) +" "+detailCommande.getDcounvte();
        Double wvarem = 0.00;
        Double wvapos = 0.00;

        /**Calcul de la valeur de poste**/
        if (detailCommande.getDcotxrem() > 0){
            wvarem = (detailCommande.getDcoqtcom() * detailCommande.getDcoputar()) * (detailCommande.getDcotxrem()/100);
        }
        if (detailCommande.getDcovarem() > 0){
            wvarem   = wvarem + (detailCommande.getDcoqtcom() * detailCommande.getDcovarem());
        }
        /***Calcul de la valeur de poste**/
        wvapos = detailCommande.getDcoputar() * detailCommande.getDcoqtcom();
        wvapos = wvapos - wvarem;
        /**Initialisation des informations devis**/
        holder.TxtProLipro.setText(detailCommande.getDcolipro());
        holder.TxtDcopocom.setText(String.valueOf(detailCommande.getDcopocom()));
        holder.TxtDcoputar.setText(String.format("%.2f", detailCommande.getDcoputar()));
        holder.TxtDcoqtcom.setText(String.format("%.3f", detailCommande.getDcoqtcom()));
        holder.TxtDcovacom.setText(String.format("%.2f", wvapos));
        holder.TxtDcoTxRem.setText(String.valueOf(detailCommande.getDcotxrem()));
        holder.TxtDcoVarem.setText(String.valueOf(detailCommande.getDcovarem()));
        holder.TxtDcoqtliv.setText(qtliv);
        /**Au clic du bouton détail**/
        holder.FabDetPro.setOnClickListener(v -> {
            Produit produit = new Produit();
            produit.setProcopro(detailCommande.getDcocopro());
            produit.setPronuprm(detailCommande.getDconuprm());
            produit.setProlipro(detailCommande.getDcolipro());

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
            /***Ajout / Mise à jour de commentaires**/

            CommentPosteFullDialog commentPosteFullDialog = CommentPosteFullDialog.newInstance();
            Bundle args = new Bundle();
            args.putString("txnpodev", detailCommande.getDcotxn());
            commentPosteFullDialog.setArguments(args);
            commentPosteFullDialog.show(fragmentManager, ServeurNode.TAG);
        });

        boolean isExpandable = detailCommandeList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return detailCommandeList == null ? 0 : detailCommandeList.size();
    }


    public DetailCommande getItem(int position){
        return detailCommandeList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public class DetailCommandeVh extends RecyclerView.ViewHolder {

        TextView TxtProLipro,TxtDcopocom,TxtDcoqtcom, TxtDcoputar, TxtDcovacom, TxtDcoTxRem, TxtDcoVarem, TxtDcoqtliv;
        MaterialButton FabDetPro, FabTxnPro;
        LinearLayout Lnr01, expandableLayout;


        public DetailCommandeVh(View itemView) {
            super(itemView);

            /**Instanciation des widgets**/
            TxtProLipro = itemView.findViewById(R.id.TxtProLipro);
            TxtDcopocom = itemView.findViewById(R.id.TxtDcopocom);
            TxtDcoqtcom = itemView.findViewById(R.id.TxtDcoqtcom);
            TxtDcoputar = itemView.findViewById(R.id.TxtDcoputar);
            TxtDcovacom = itemView.findViewById(R.id.TxtDcovacom);
            TxtDcoTxRem = itemView.findViewById(R.id.TxtDcoTxRem);
            TxtDcoVarem = itemView.findViewById(R.id.TxtDcoVarem);
            FabDetPro = itemView.findViewById(R.id.FabDetPro);
            FabTxnPro = itemView.findViewById(R.id.FabTxnPro);
            TxtDcoqtliv = itemView.findViewById(R.id.TxtDcoqtliv);
            Lnr01 = itemView.findViewById(R.id.Lnr01);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            Lnr01.setOnClickListener(v -> {
                DetailCommande detailCommande = detailCommandeList.get(getAdapterPosition());
                detailCommande.setExpandable(!detailCommande.isExpandable());
                notifyItemChanged(getAdapterPosition());
            });

        }
    }

}
