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
import com.google.android.material.tabs.TabLayout;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.CommandeAdapter;
import com.sominfor.somisal_app.adapters.CommandeClientAdapter;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.Client;
import com.sominfor.somisal_app.handler.models.Commande;
import com.sominfor.somisal_app.handler.models.Produit;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.utils.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sominfor.somisal_app.activities.LoginActivity.protocole;

public class ClientDetailsActivity extends AppCompatActivity {
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
        adapter.addFrag(new ComptabiliteClientFragment(), getString(R.string.activity_client_details_TabMenu3_Title));

        viewPager.setAdapter(adapter);
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
        TextView TxtCliRasoc, TxtCliNucli, TxtCliNacli, TxtCliadre1, TxtCliAdre2,TxtCliCopos, TxtCliville, TxtCliBopos, TxtCliCpays;

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

            return view;
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
        String apiUrl01, systemeAdresse, utilisateurLogin, utilisateurPassword, comStatu, apiUrl02, apiUrl03, apiUrl04, apiUrl05, utilisateurCosoc, utilisateurCoage;
        Utilisateur utilisateur;
        public RequestQueue rq;
        DelayedProgressDialog progressDialogInfo;
        List<Commande> commandeList;
        CommandeClientAdapter commandeClientAdapter;

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
            recyclerViewCommandeClient = view.findViewById(R.id.RecyclerViewCommandeClient);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerViewCommandeClient.setLayoutManager(linearLayoutManager);

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

            return view;
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
                            commande.setComvacom(jsonObject.getDouble("DIVVACOM"));
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

    }

    /**
     * Fragment ComptabiliteClientFragment
     */
    public static class ComptabiliteClientFragment extends Fragment {
        int color;

        public ComptabiliteClientFragment() {
        }
        @SuppressLint("ValidFragment")
        public ComptabiliteClientFragment(int color) {
            this.color = color;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.generalites_client_fragment, container, false);
            /*rq = Volley.newRequestQueue(getActivity());
            rubriques = new ArrayList<>();

            recyclerView =  view.findViewById(R.id.RecyclerViewRubriques);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);

            error_info =  view.findViewById(R.id.error_info);*/

            //recupererListeRubrique(url_machine_rubrique,eqpcodep,eqpnueqp,eqpidrep,eqpcosct,eqpcosoc, eqpcoage);
            return view;
        }


    }

}