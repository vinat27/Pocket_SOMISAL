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
import android.widget.TextView;

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
import com.sominfor.somisal_app.handler.models.Client;
import com.sominfor.somisal_app.handler.models.Produit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        adapter.addFrag(new CommercialClientFragment(), getString(R.string.activity_client_details_TabMenu2_Title));
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
        TextView TxtCliRasoc, TxtCliNucli, TxtCliNacli, TxtCliLivth, TxtComadre1, TxtCliAdre2,TxtCliCopos, TxtCliville, TxtCliBopos, TxtCliCpays;

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

            /**Set values to Textviews**/
            TxtCliRasoc.setText(client.getCliRasoc());
            TxtCliNucli.setText(client.getCliNucli());
            TxtCliNacli.setText(client.getCliNacli());

            return view;
        }


    }

    /**
     * Fragment CommercialClientFragment
     */
    public static class CommercialClientFragment extends Fragment {
        int color;

        public CommercialClientFragment() {
        }
        @SuppressLint("ValidFragment")
        public CommercialClientFragment(int color) {
            this.color = color;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.commercial_client_fragment, container, false);
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