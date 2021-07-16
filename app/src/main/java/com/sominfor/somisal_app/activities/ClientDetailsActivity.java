package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.CommandeAdapter;
import com.sominfor.somisal_app.adapters.CommandeClientAdapter;
import com.sominfor.somisal_app.adapters.DevisAdapter;
import com.sominfor.somisal_app.adapters.DevisClientAdapter;
import com.sominfor.somisal_app.fragments.CommandeFragment;
import com.sominfor.somisal_app.fragments.FiltresCommandesClientDialog;
import com.sominfor.somisal_app.fragments.FiltresCommandesDialog;
import com.sominfor.somisal_app.fragments.FiltresDevisClientDialog;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Client;
import com.sominfor.somisal_app.handler.models.Commande;
import com.sominfor.somisal_app.handler.models.CommandeFilterElements;
import com.sominfor.somisal_app.handler.models.Devis;
import com.sominfor.somisal_app.handler.models.Produit;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.interfaces.CommandeFilterListener;
import com.sominfor.somisal_app.interfaces.DevisFilterListener;
import com.sominfor.somisal_app.utils.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.sominfor.somisal_app.activities.LoginActivity.protocole;

public class ClientDetailsActivity extends AppCompatActivity implements CommandeFilterListener, DevisFilterListener {
    public Toolbar toolbar;
    TabLayout tabLayout;
    public static Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);

        toolbar =  findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);

        if(!getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            client = (Client) bundle.getSerializable("client");
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Détails Client");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        final ViewPager viewPager =  findViewById(R.id.htab_viewpager);
        setupViewPager(viewPager);
        tabLayout =  findViewById(R.id.htab_tabs);
        tabLayout.setupWithViewPager(viewPager);
        //setupTabIcons();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_result_scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Tabs - Menus
     *
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new GeneralitesClientFragment(), getString(R.string.activity_client_details_TabMenu1_Title));
        adapter.addFrag(new CommandesClientFragment(), getString(R.string.activity_client_details_TabMenu2_Title));
        adapter.addFrag(new DevisClientFragment(), getString(R.string.activity_client_details_TabMenu3_Title));

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onDataReceived(CommandeFilterElements commandeFilterElements) {
        CommandesClientFragment commandesClientFragment = CommandesClientFragment.getInstance();
        commandesClientFragment.onDataReceived(commandeFilterElements);
    }

    @Override
    public void onDataReceivedDevis(CommandeFilterElements commandeFilterElements) {
        DevisClientFragment devisClientFragment = DevisClientFragment.getInstance();
        devisClientFragment.onDataDevisReceived(commandeFilterElements);
    }

    /**
     * ViewPageAdapter - Afficheur de fragments
     */
    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    /**
     * Fragment GeneralitesClientFragment
     */
    public static class GeneralitesClientFragment extends Fragment {
        int color;
        TextView TxtCliRasoc, TxtCliNucli, TxtCliNacli, TxtCliadre1, TxtCliAdre2,TxtCliCopos, TxtCliville, TxtCliBopos, TxtCliCpays, TxtSoldeCpteGene, TxtSoldeLimon, TxtSoldePlafond;
        ServeurNodeController serveurNodeController;
        ServeurNode serveurNode;
        String apiUrl01, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage;
        Utilisateur utilisateur;
        public GeneralitesClientFragment() {
        }
        @SuppressLint("ValidFragment")
        public GeneralitesClientFragment(int color) {
            this.color = color;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.generalites_client_fragment, container, false);

            /**Instanciation des widgets**/
            TxtCliRasoc = view.findViewById(R.id.TxtCliRasoc);
            TxtCliNucli = view.findViewById(R.id.TxtCliNucli);
            TxtCliNacli = view.findViewById(R.id.TxtCliNacli);
            TxtCliadre1 = view.findViewById(R.id.TxtCliadre1);
            TxtCliAdre2 = view.findViewById(R.id.TxtCliAdre2);
            TxtCliCopos = view.findViewById(R.id.TxtCliCopos);
            TxtCliville = view.findViewById(R.id.TxtCliville);
            TxtCliBopos = view.findViewById(R.id.TxtCliBopos);
            TxtCliCpays = view.findViewById(R.id.TxtCliCpays);
            TxtSoldeCpteGene = view.findViewById(R.id.TxtSoldeCpteGene);
            TxtSoldeLimon = view.findViewById(R.id.TxtSoldeLimon);
            TxtSoldePlafond = view.findViewById(R.id.TxtSoldePlafond);

            /**Set values to Textviews**/
            TxtCliRasoc.setText(client.getCliRasoc());
            TxtCliNucli.setText(client.getCliNucli());
            TxtCliNacli.setText(client.getCliLiNacli());
            TxtCliadre1.setText(client.getCliAdre1());
            TxtCliAdre2.setText(client.getCliAdre2());
            TxtCliCopos.setText(client.getCliCopos());
            TxtCliville.setText(client.getCliVille());
            TxtCliBopos.setText(client.getCliBopos());
            TxtCliCpays.setText(client.getCliLiCpays());
            BigDecimal bd = new BigDecimal(client.getCliMtplf());
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
            symbols.setGroupingSeparator(' ');

            DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);
            formatter.setRoundingMode(RoundingMode.DOWN);
            TxtSoldePlafond.setText(formatter.format(bd.floatValue()));

            serveurNodeController = new ServeurNodeController();
            /**Récupération des informations serveur**/
            serveurNode = serveurNodeController.getServeurNodeInfos();
            /*URL Récupération de la liste des systèmes*/
            apiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/client/soldeByIdClient";
            utilisateur = UserSessionManager.getInstance(getActivity().getApplicationContext()).getUtilisateurDetail();
            systemeAdresse = utilisateur.getUtilisateurSysteme();
            utilisateurLogin = utilisateur.getUtilisateurLogin();
            utilisateurPassword = utilisateur.getUtilisateurPassword();
            utilisateurCosoc = utilisateur.getUtilisateurCosoc();
            utilisateurCoage = utilisateur.getUtilisateurCoage();

            /**Récupération du solde client**/
            getClientSolde(apiUrl01);

            return view;
        }
        /**
         *
         * @param api_url l'url de récupération des information du fichier JSON
         *
         */
        public void getClientSolde(String api_url){
            RequestQueue requestQueue = new Volley().newRequestQueue(getActivity().getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, api_url, s -> {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("SOLDE") == "null"){
                        TxtSoldeCpteGene.setText("0");
                        TxtSoldeLimon.setText(client.getCliLiComon().trim());
                    }else{
                        String solde = jsonObject.getString("SOLDE");
                        TxtSoldeCpteGene.setText(solde);
                        TxtSoldeLimon.setText(client.getCliLiComon().trim());
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }, Throwable::printStackTrace)
            {
                /**Paramètres envoyés**/
                protected Map<String,String> getParams(){
                    Map<String, String> param = new HashMap<>();
                    param.put("systeme",systemeAdresse);
                    param.put("login",utilisateurLogin);
                    param.put("password",utilisateurPassword);
                    param.put("cosoc", utilisateurCosoc);
                    param.put("coage", utilisateurCoage);
                    param.put("comon", client.getCliComon());
                    param.put("nacpx", client.getCliNacpx());
                    param.put("cpaux", client.getCliCpaux());
                    param.put("cpgen", client.getCliCpgen());

                    return param;
                }
            };
            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            requestQueue.add(stringRequest);
        }

    }

    /**
     * Fragment CommercialClientFragment
     */
    public static class CommandesClientFragment extends Fragment {
        int color;
        RecyclerView recyclerViewCommandeClient;
        FrameLayout frameLayoutCdeClient;
        ServeurNodeController serveurNodeController;
        ServeurNode serveurNode;
        String apiUrl01, systemeAdresse, utilisateurLogin, utilisateurPassword, utilisateurCosoc, utilisateurCoage;
        Utilisateur utilisateur;
        public RequestQueue rq;
        DelayedProgressDialog progressDialogInfo;
        List<Commande> commandeList;
        CommandeClientAdapter commandeClientAdapter;
        FloatingActionButton fab_filter;
        static CommandesClientFragment instanceClientCommandeFragment = null;

        public CommandesClientFragment() {
        }
        @SuppressLint("ValidFragment")
        public CommandesClientFragment(int color) {
            this.color = color;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.commandes_client_fragment, container, false);

            rq = Volley.newRequestQueue(getActivity());
            /***Instanciation des widgets***/
            frameLayoutCdeClient = view.findViewById(R.id.frameLayout);
            fab_filter = view.findViewById(R.id.Fab_Filter);
            recyclerViewCommandeClient = view.findViewById(R.id.RecyclerViewCommandeClient);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerViewCommandeClient.setLayoutManager(linearLayoutManager);

            /**Initialisation instance**/
            instanceClientCommandeFragment = this;

            progressDialogInfo = new DelayedProgressDialog();
            serveurNodeController = new ServeurNodeController();
            commandeList = new ArrayList<>();
            /**Récupération des informations serveur**/
            serveurNode = serveurNodeController.getServeurNodeInfos();
            /*URL Récupération de la liste des systèmes*/
            apiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/commande/commandeByIdClient";
            utilisateur = UserSessionManager.getInstance(getActivity().getApplicationContext()).getUtilisateurDetail();
            systemeAdresse = utilisateur.getUtilisateurSysteme();
            utilisateurLogin = utilisateur.getUtilisateurLogin();
            utilisateurPassword = utilisateur.getUtilisateurPassword();
            utilisateurCosoc = utilisateur.getUtilisateurCosoc();
            utilisateurCoage = utilisateur.getUtilisateurCoage();

            listeCommandesClients(apiUrl01);

            /**fab_filter onClick**/
            fab_filter.setOnClickListener(v -> {
                openFiltresCommandes();
            });

            return view;
        }

        /**Instance de fragment**/
        public static CommandesClientFragment getInstance(){
            return instanceClientCommandeFragment;
        }

        private CommandeFilterListener mListener;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            try {
                mListener = (CommandeFilterListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString()
                        + " must implement MyListener");
            }
        }

        public void onDataReceived(CommandeFilterElements commandeFilterElements) {
            /**Transformation de date des informations reçues**/
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date dateInf = simpleDateFormat.parse(commandeFilterElements.getDateInf());
                Date dateSup = simpleDateFormat.parse(commandeFilterElements.getDateSup());
                String statut = commandeFilterElements.getCommandeStatut();

                commandeClientAdapter.filterDateRange(dateInf, dateSup, statut);
            }catch (ParseException e){
                e.printStackTrace();
            }
        }

        /**Récupération de la liste de devis en cours**/
        public void listeCommandesClients(String api_url){
            RequestQueue requestQueue = new Volley().newRequestQueue(getActivity().getApplicationContext());
            progressDialogInfo.show(getActivity().getSupportFragmentManager(), "Loading...");
            progressDialogInfo.setCancelable(false);
            StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {

                try{
                    JSONArray array = new JSONArray(s);
                    if (array.length() == 0){
                        progressDialogInfo.cancel();
                        frameLayoutCdeClient.setVisibility(View.VISIBLE);
                    }
                    for (int i=0; i<array.length(); i++){
                        try{
                            JSONObject jsonObject = array.getJSONObject(i);
                            Commande commande = new Commande();
                            commande.setComrasoc(jsonObject.getString("COMRASOC"));
                            commande.setComdaliv(jsonObject.getString("COMDALIV"));
                            commande.setComliliv(jsonObject.getString("LIBCOLIV").trim());
                            commande.setComvacom(jsonObject.getDouble("COMVACOM"));
                            commande.setComnucom(jsonObject.getString("COMNUCOM"));
                            commande.setComdacom(jsonObject.getString("COMDACOM"));
                            commande.setComlilieuv(jsonObject.getString("LIBLIEUV").trim());
                            commande.setComlitrn(jsonObject.getString("LIBCOTRN").trim());
                            commande.setComstatu(jsonObject.getString("COMSTATU"));
                            commande.setComlista(jsonObject.getString("LIBSTATU").trim());
                            commande.setComlimag(jsonObject.getString("LIBCOMAG").trim());
                            commande.setComlimon(jsonObject.getString("LIBCOMON").trim());
                            commande.setComadre1(jsonObject.getString("COMADRE1").trim());
                            commande.setComadre2(jsonObject.getString("COMADRE2").trim());
                            commande.setComcopos(jsonObject.getString("COMCOPOS").trim());
                            commande.setComville(jsonObject.getString("COMVILLE").trim());
                            commande.setCombopos(jsonObject.getString("COMBOPOS").trim());
                            commande.setComlicpays(jsonObject.getString("LIBCPAYS").trim());
                            commande.setComrasol(jsonObject.getString("COMRASOL").trim());
                            commande.setComadr1l(jsonObject.getString("COMADR1L").trim());
                            commande.setComadr2l(jsonObject.getString("COMADR2L").trim());
                            commande.setComcopol(jsonObject.getString("COMCOPOL").trim());
                            commande.setComvilll(jsonObject.getString("COMVILLL").trim());
                            commande.setComlicpayr(jsonObject.getString("LIBCPAYL").trim());
                            commande.setCombopol(jsonObject.getString("COMBOPOL").trim());
                            commande.setComcomon(jsonObject.getString("COMCOMON"));

                            //Populariser la liste des commandes
                            commandeList.add(commande);
                            progressDialogInfo.cancel();
                        }catch(JSONException e){
                            e.printStackTrace();
                            progressDialogInfo.cancel();

                        }
                    }
                    commandeClientAdapter = new CommandeClientAdapter(getActivity(),commandeList);
                    recyclerViewCommandeClient.setAdapter(commandeClientAdapter);
                }catch(JSONException e){
                    e.printStackTrace();

                }
            }, volleyError -> {
                volleyError.printStackTrace();
                progressDialogInfo.cancel();
                /**Erreur connexion**/
                Toast.makeText(getActivity(), getResources().getString(R.string.login_activity_Snackbar01_NoConnexion), Toast.LENGTH_LONG).show();
            })
            {
                protected Map<String,String> getParams(){
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("systeme",systemeAdresse);
                    param.put("login",utilisateurLogin);
                    param.put("password",utilisateurPassword);
                    param.put("cosoc", utilisateurCosoc);
                    param.put("coage", utilisateurCoage);
                    param.put("nucli", client.getCliNucli());
                    return param;
                }
            };
            int socketTimeout = 10000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            postRequest.setRetryPolicy(policy);
            requestQueue.add(postRequest);
        }

        /**Ouvrir fenetre de filtres**/
        private void openFiltresCommandes() {
            /***Filtre sur les commandes en cours**/
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FiltresCommandesClientDialog filtresCommandesClientDialog = FiltresCommandesClientDialog.newInstance();
            filtresCommandesClientDialog.show(fragmentManager, ServeurNode.TAG);
        }
    }

    /**
     * Fragment ComptabiliteClientFragment
     */
    public static class DevisClientFragment extends Fragment {
        int color;
        RequestQueue req;
        RecyclerView recyclerViewDevisClient;
        FrameLayout frameLayoutDevClient;
        ServeurNodeController serveurNodeController;
        ServeurNode serveurNode;
        String apiUrl01, systemeAdresse, utilisateurLogin, utilisateurPassword, comStatu, apiUrl02, apiUrl03, apiUrl04, apiUrl05, utilisateurCosoc, utilisateurCoage;
        Utilisateur utilisateur;
        public RequestQueue rq;
        DelayedProgressDialog progressDialogInfo;
        List<Devis> devisList;
        DevisClientAdapter devisClientAdapter;
        FloatingActionButton fab_filter;
        static DevisClientFragment instanceClientDevisFragment = null;
        public DevisClientFragment() {
        }
        @SuppressLint("ValidFragment")
        public DevisClientFragment(int color) {
            this.color = color;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.devis_client_fragment, container, false);
            req = Volley.newRequestQueue(getActivity());

            /***Instanciation des widgets***/
            frameLayoutDevClient = view.findViewById(R.id.frameLayout);
            recyclerViewDevisClient = view.findViewById(R.id.RecyclerViewDevisClient);
            fab_filter = view.findViewById(R.id.Fab_Filter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerViewDevisClient.setLayoutManager(linearLayoutManager);
            instanceClientDevisFragment = this;
            progressDialogInfo = new DelayedProgressDialog();
            serveurNodeController = new ServeurNodeController();
            devisList = new ArrayList<>();
            /**Récupération des informations serveur**/
            serveurNode = serveurNodeController.getServeurNodeInfos();
            /*URL Récupération de la liste des systèmes*/
            apiUrl01 = protocole+"://"+serveurNode.getServeurNodeIp()+"/read/devis/devisByIdClient";
            utilisateur = UserSessionManager.getInstance(getActivity().getApplicationContext()).getUtilisateurDetail();
            systemeAdresse = utilisateur.getUtilisateurSysteme();
            utilisateurLogin = utilisateur.getUtilisateurLogin();
            utilisateurPassword = utilisateur.getUtilisateurPassword();
            utilisateurCosoc = utilisateur.getUtilisateurCosoc();
            utilisateurCoage = utilisateur.getUtilisateurCoage();

            listeDevisClient(apiUrl01);

            fab_filter.setOnClickListener(v -> {
                openFiltresDevis();
            });

            return view;
        }
        /**Instance de fragment**/
        public static DevisClientFragment getInstance(){
            return instanceClientDevisFragment;
        }

        private DevisFilterListener mListener;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            try {
                mListener = (DevisFilterListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString()
                        + " must implement MyListener");
            }
        }

        public void onDataDevisReceived(CommandeFilterElements commandeFilterElements) {
            /**Transformation de date des informations reçues**/
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date dateInf = simpleDateFormat.parse(commandeFilterElements.getDateInf());
                Date dateSup = simpleDateFormat.parse(commandeFilterElements.getDateSup());
                String statut = commandeFilterElements.getCommandeStatut();
                Log.v("StatutCde", statut);

                devisClientAdapter.filterDateRange(dateInf, dateSup, statut);
            }catch (ParseException e){
                e.printStackTrace();
            }
        }
        /**Récupération de la liste de devis en cours**/
        public void listeDevisClient(String api_url){
            RequestQueue requestQueue = new Volley().newRequestQueue(getActivity().getApplicationContext());
            progressDialogInfo.show(getActivity().getSupportFragmentManager(), "Loading...");
            progressDialogInfo.setCancelable(false);
            StringRequest postRequest = new StringRequest(Request.Method.POST, api_url, s -> {
                try{
                    JSONArray array = new JSONArray(s);
                    if (array.length() == 0){
                        progressDialogInfo.cancel();
                        frameLayoutDevClient.setVisibility(View.VISIBLE);
                    }
                    for (int i=0; i<array.length(); i++){
                        try{
                            JSONObject jsonObject = array.getJSONObject(i);
                            Devis devis = new Devis();
                            devis.setCliRasoc(client.getCliRasoc());
                            devis.setDevDaliv(jsonObject.getString("DEVDALIV"));
                            devis.setDevVadev(jsonObject.getDouble("DEVVADEV"));
                            devis.setDevlimon(jsonObject.getString("LIBCOMON"));
                            devis.setDevNudev(jsonObject.getString("DEVNUDEV"));
                            devis.setDevDadev(jsonObject.getString("DEVDADEV"));
                            devis.setDevLieuv(jsonObject.getString("LIBLIEUV").trim());
                            devis.setDevColieuv(jsonObject.getString("DEVLIEUV"));
                            devis.setDevRfdev(jsonObject.getString("DEVRFDEV"));
                            devis.setDevStatut(jsonObject.getString("DEVSTATU"));
                            devis.setDevlista(jsonObject.getString("LIBSTATU"));
                            devis.setDevComag(jsonObject.getString("DEVCOMAG"));
                            devis.setDevLimag(jsonObject.getString("LIBCOMAG").trim());
                            devis.setDevColiv(jsonObject.getString("DEVCOLIV").trim());
                            devis.setDevliliv(jsonObject.getString("LIBCOLIV").trim());
                            devis.setDevNacli(client.getCliNacli());
                            devis.setDevNucli(jsonObject.getString("DEVNUCLI"));
                            devis.setDevMoreg(jsonObject.getString("DEVMOREG"));
                            devis.setDevDereg(jsonObject.getString("DEVDEREG"));
                            devis.setDevComon(jsonObject.getString("DEVCOMON"));

                            //Populariser la liste des produits
                            devisList.add(devis);
                            progressDialogInfo.cancel();
                        }catch(JSONException e){
                            e.printStackTrace();
                            progressDialogInfo.cancel();

                        }
                    }
                    devisClientAdapter = new DevisClientAdapter(getActivity(),devisList);
                    recyclerViewDevisClient.setAdapter(devisClientAdapter);
                }catch(JSONException e){
                    e.printStackTrace();

                }
            }, volleyError -> {
                volleyError.printStackTrace();
                progressDialogInfo.cancel();
                /**Erreur connexion**/
                Toast.makeText(getActivity(), getResources().getString(R.string.login_activity_Snackbar01_NoConnexion), Toast.LENGTH_LONG).show();
            })
            {
                protected Map<String,String> getParams(){
                    Map<String, String> param = new HashMap<String, String>();
                    param.put("systeme",systemeAdresse);
                    param.put("login",utilisateurLogin);
                    param.put("password",utilisateurPassword);
                    param.put("nucli", client.getCliNucli());
                    param.put("cosoc", utilisateurCosoc);
                    param.put("coage", utilisateurCoage);
                    return param;
                }
            };
            int socketTimeout = 10000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            postRequest.setRetryPolicy(policy);
            requestQueue.add(postRequest);
        }

        /**Ouvrir fenetre de filtres**/
        private void openFiltresDevis() {
            /***Filtre sur les devis en cours**/
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FiltresDevisClientDialog filtresDevisClientDialog = FiltresDevisClientDialog.newInstance();
            filtresDevisClientDialog.show(fragmentManager, ServeurNode.TAG);
        }
    }

}