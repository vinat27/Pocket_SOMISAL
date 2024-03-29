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
import com.sominfor.somisal_app.fragments.CommentPosteAddCdeFullDialog;
import com.sominfor.somisal_app.fragments.CommentPosteFullDialog;
import com.sominfor.somisal_app.fragments.DeleteAlertDialogFragment;
import com.sominfor.somisal_app.fragments.UpdateDcoFullDialog;
import com.sominfor.somisal_app.handler.models.DetailCommande;
import com.sominfor.somisal_app.handler.models.DetailDevis;
import com.sominfor.somisal_app.handler.models.Produit;
import com.sominfor.somisal_app.handler.models.ServeurNode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

/**
 * Créé par vatsou le 01,février,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class CommandeProduitAdapter extends RecyclerView.Adapter<CommandeProduitAdapter.CommandeProduitVh> {
    private static List<DetailCommande> detailCommandeList;
    private List<DetailCommande> detailCommandeSearch;
    private Context context;
    AppCompatActivity activity;
    FragmentManager fragmentManager;

    /**Constructeur**/
    public CommandeProduitAdapter(Context context, List<DetailCommande> detailCommandeList, FragmentManager fragmentManager){
        this.context = context;
        this.detailCommandeList = detailCommandeList;
        detailCommandeSearch = new ArrayList<>(detailCommandeList);
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public CommandeProduitAdapter.CommandeProduitVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_commande_produit, parent, false);
        return new CommandeProduitAdapter.CommandeProduitVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommandeProduitAdapter.CommandeProduitVh holder, int position) {
        DetailCommande detailCommande = detailCommandeList.get(position);


        /**Initialisation des informations devis**/
        holder.TxtProLipro.setText(detailCommande.getDcolipro());
        BigDecimal bd = new BigDecimal(detailCommande.getDcovacom());
        BigDecimal pt = new BigDecimal(detailCommande.getDcoputar());
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');

        DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);
        formatter.setRoundingMode(RoundingMode.DOWN);
        holder.TxtDcoputar.setText(formatter.format(pt.floatValue()));
        holder.TxtDcoqtcom.setText(String.format("%.1f", detailCommande.getDcoqtcom()));
        holder.TxtDcovacom.setText(formatter.format(bd.floatValue()));
        holder.TxtDcoTxRem.setText(String.valueOf(detailCommande.getDcotxrem()));
        holder.TxtDcoVarem.setText(String.format("%.2f", detailCommande.getDcovarem()));
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
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }else{
                Toast.makeText(context, context.getResources().getString(R.string.produit_fragment_noStock), Toast.LENGTH_LONG).show();
            }

        });

        /**Modification de poste**/
        holder.FabUpdatePro.setOnClickListener(v -> {
            UpdateDcoFullDialog updateDcoFullDialog = UpdateDcoFullDialog.newInstance();
            Bundle args = new Bundle();
            args.putSerializable("detailCommande", detailCommande);
            updateDcoFullDialog.setArguments(args);
            updateDcoFullDialog.show(fragmentManager, ServeurNode.TAG);
        });

        /**Suppression de poste**/
        holder.FabDeletePro.setOnClickListener(v -> {
            DeleteAlertDialogFragment deleteAlertDialog = DeleteAlertDialogFragment.newInstance();
            Bundle args = new Bundle();
            args.putSerializable("detailDevis", detailCommande);
            deleteAlertDialog.setArguments(args);
            deleteAlertDialog.show(fragmentManager, ServeurNode.TAG);
        });

        /**Commentaires de poste***/
        holder.FabTxnPro.setOnClickListener(v -> {
            /***Ajout / Mise à jour de commentaires**/

            CommentPosteAddCdeFullDialog commentPosteAddCdeFullDialog = CommentPosteAddCdeFullDialog.newInstance();
            Bundle args = new Bundle();
            args.putSerializable("detailCommande", detailCommande);
            commentPosteAddCdeFullDialog.setArguments(args);
            commentPosteAddCdeFullDialog.show(fragmentManager, ServeurNode.TAG);
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

    public class CommandeProduitVh extends RecyclerView.ViewHolder {

        TextView TxtProLipro,TxtDcopocom,TxtDcoqtcom, TxtDcoputar, TxtDcovacom, TxtDcoTxRem, TxtDcoVarem;
        MaterialButton FabDetPro, FabTxnPro, FabUpdatePro, FabDeletePro;
        LinearLayout Lnr01, expandableLayout;


        public CommandeProduitVh(View itemView) {
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
            FabUpdatePro = itemView.findViewById(R.id.FabUpdatePro);
            FabDeletePro = itemView.findViewById(R.id.FabDeletePro);
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
