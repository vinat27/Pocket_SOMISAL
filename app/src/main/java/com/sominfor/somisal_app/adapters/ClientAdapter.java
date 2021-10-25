package com.sominfor.somisal_app.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sominfor.somisal_app.Helpers;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.handler.models.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Créé par vatsou le 25,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientVh> implements SectionIndexer {
    private List<Client> clientList;
    private List<Client> clientSearch;
    private Context context;
    private String mSections = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#", prolipro;
    private HashMap<Integer, Integer> sectionsTranslator = new HashMap<>();
    private ArrayList<Integer> mSectionPositions;
    String initials;

    /**Constructeur**/
    public ClientAdapter(Context context, List<Client> clientList){
        this.context = context;
        this.clientList = clientList;
        clientSearch = new ArrayList<>(clientList);
    }
    @NonNull
    @Override
    public ClientAdapter.ClientVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_client, parent, false);
        return new ClientAdapter.ClientVh(view);
    }
    /**Set texts to TextView**/
    @Override
    public void onBindViewHolder(ClientAdapter.ClientVh holder, int position) {
        Client client = clientList.get(position);
        if (client.getCliRasoc().length() >= 2){
            initials = client.getCliRasoc().trim().substring(0,1).toUpperCase() +""+ client.getCliRasoc().trim().substring(1,2).toLowerCase();
        }else{
            initials = client.getCliRasoc().trim().substring(0,1).toUpperCase();
        }


        holder.TxtIcon.setText(initials);
        holder.TxtCliRasoc.setText(client.getCliRasoc().trim().toUpperCase());
        holder.TxtCliNucli.setText("Numéro: "+client.getCliNucli());
        holder.TxtCliNacli.setText("Nature: "+client.getCliLiNacli());

        /***Changement de couleurs**/
        Random mRandom = new Random();
        final int color = Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
        ((GradientDrawable) holder.TxtIcon.getBackground()).setColor(color);
    }
    /**Taille de la liste**/
    @Override
    public int getItemCount() {
        return clientList == null ? 0 : clientList.size();
    }

    /**Récupérer l'objet sélectionné**/
    public Client getItem(int position){
        return clientList.get(position);
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
        for (int i = 0, size = clientList.size(); i < size; i++) {
            String section = String.valueOf(clientList.get(i).getCliRasoc().charAt(0)).toUpperCase();
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
    public static class ClientVh extends RecyclerView.ViewHolder {
        View mView;
        private  TextView TxtCliRasoc;
        private  TextView TxtCliNucli;
        private  TextView TxtCliNacli;
        private  TextView TxtIcon;

        public ClientVh(View itemView) {
            super(itemView);

            TxtCliRasoc =  itemView.findViewById(R.id.TxtCliRasoc);
            TxtCliNucli =  itemView.findViewById(R.id.TxtCliNucli);
            TxtCliNacli = itemView.findViewById(R.id.TxtCliNacli);
            TxtIcon = itemView.findViewById(R.id.TxtIcon);
            mView = itemView;
        }
    }
    /**Filtre des informations avec formulaire de recherche**/
    public Filter getFilter() {
        return clientFilter;
    }

    private Filter clientFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Client> filteredList = new ArrayList<>();
            /**Contrôle valeur de recherche**/
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(clientSearch);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                /**Parcours de la liste des interventions**/
                for (Client item : clientSearch) {
                    /**Comparaison des résultats et ajout dans la liste de résultats**/

                    if (item.getCliRasoc().toLowerCase().contains(filterPattern) || String.valueOf(item.getCliLiNacli()).toLowerCase().contains(filterPattern) || String.valueOf(item.getCliNucli()).toLowerCase().contains(filterPattern)) {
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
            clientList.clear();
            clientList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
