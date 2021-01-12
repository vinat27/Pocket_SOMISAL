package com.sominfor.somisal_app.adapters;

import android.content.Context;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sominfor.somisal_app.Helpers;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.handler.models.Produit;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.text.TextUtils.substring;

/**
 * Créé par vatsou le 10,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class ProduitAdapter extends RecyclerView.Adapter<ProduitAdapter.ProduitVh> implements SectionIndexer {

    private List<Produit> produits;
    private List<Produit> produitsSearchs;
    private Context context;
    private String mSections = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#", prolipro;
    private HashMap<Integer, Integer> sectionsTranslator = new HashMap<>();
    private ArrayList<Integer> mSectionPositions;

    /**Constructeur**/
    public ProduitAdapter(Context context, List<Produit> produits){
        this.context = context;
        this.produits = produits;
        produitsSearchs = new ArrayList<>(produits);
    }

    /**ViewHolder - Appel du fichier ligne de produit**/
    @Override
    public ProduitVh onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_produit, parent, false);
        return new ProduitAdapter.ProduitVh(view);
    }

    /**Set texts to TextView**/
    @Override
    public void onBindViewHolder(ProduitAdapter.ProduitVh holder, int position) {
        Produit produit = produits.get(position);


        holder.TxtProLiPro.setText(produit.getProlipro().trim().toUpperCase());
        holder.TxtProCoPro.setText(String.valueOf(produit.getProcopro()));
    }
    /**Taille de la liste**/
    @Override
    public int getItemCount() {
        return produits == null ? 0 : produits.size();
    }

    /**Récupérer l'objet sélectionné**/
    public Produit getItem(int position){
        return produits.get(position);
    }

    /**Récupération de la position de l"élément selectionné**/
    @Override
    public long getItemId(int position){
        return position;
    }

    /**Filtre par lettre alphabétique**/
    @Override
    public Object[] getSections() {

        List<String> sections = new ArrayList<>(27);
        ArrayList<String> alphabetFull = new ArrayList<>();

        mSectionPositions = new ArrayList<>();
        for (int i = 0, size = produits.size(); i < size; i++) {
            String section = String.valueOf(produits.get(i).getProlipro().charAt(0)).toUpperCase();
            if (!sections.contains(section)) {
                sections.add(section);
                mSectionPositions.add(i);
            }
        }
        for (int i = 0; i < mSections.length(); i++) {
            alphabetFull.add(String.valueOf(mSections.charAt(i)));
        }

        sectionsTranslator = Helpers.Companion.sectionsHelper(sections, alphabetFull);
        return alphabetFull.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return mSectionPositions.get(sectionsTranslator.get(sectionIndex));
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    /**Paramétrage des vues**/
    public static class ProduitVh extends RecyclerView.ViewHolder {
        View mView;
        private TextView TxtProLiPro;
        private TextView TxtProCoPro;

        public ProduitVh(View itemView) {
            super(itemView);

            TxtProLiPro =  itemView.findViewById(R.id.TxtProLiPro);
            TxtProCoPro =  itemView.findViewById(R.id.TxtProCoPro);
            mView = itemView;

        }
    }
}
