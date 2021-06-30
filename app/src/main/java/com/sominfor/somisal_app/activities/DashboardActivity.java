package com.sominfor.somisal_app.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.adapters.DevisAdapter;
import com.sominfor.somisal_app.fragments.ClientFragment;
import com.sominfor.somisal_app.fragments.CommandeFragment;
import com.sominfor.somisal_app.fragments.DevisFragment;
import com.sominfor.somisal_app.fragments.HomeFragment;
import com.sominfor.somisal_app.fragments.ProduitFragment;
import com.sominfor.somisal_app.handler.models.Commande;
import com.sominfor.somisal_app.handler.models.CommandeFilterElements;
import com.sominfor.somisal_app.handler.models.Devis;
import com.sominfor.somisal_app.handler.models.Utilisateur;
import com.sominfor.somisal_app.interfaces.CommandeFilterListener;
import com.sominfor.somisal_app.utils.CustomTypefaceSpan;
import com.sominfor.somisal_app.utils.UserSessionManager;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CommandeFilterListener {

    TextView TxtLogin, TxtFiliale;
    Utilisateur utilisateur;
    FragmentManager fragmentManager;
    String intentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        /**Contrôle de sessions utilisateur**/
        if (!UserSessionManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        /**Controle orientation***/
        if(!getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        /**Récupération de session utilisateur**/
        utilisateur = UserSessionManager.getInstance(getApplicationContext()).getUtilisateurDetail();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        /**Informations utilisateur connecté**/
        final View headerLayout = navigationView.getHeaderView(0);
        TxtLogin =  headerLayout.findViewById(R.id.TxtLogin);
        TxtFiliale =  headerLayout.findViewById(R.id.TxtFiliale);
        TxtLogin.setText(utilisateur.getUtilisateurLogin());
        TxtFiliale.setText(utilisateur.getUtilisateurFiliale());

        if(getIntent()!=null && getIntent().getExtras() != null && getIntent().getExtras().getString("frgToLoad") != null) {
            intentFragment = getIntent().getExtras().getString("frgToLoad");

            switch (intentFragment) {
                case "2":
                    Fragment f = new DevisFragment();
                    fragmentManager = (DashboardActivity.this).getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, f).commit();
                    break;
                case "3":
                    Fragment frag = new CommandeFragment();
                    fragmentManager = (DashboardActivity.this).getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, frag).commit();
                    break;
            }
        }else{
            if (savedInstanceState == null) {
                Fragment f = new HomeFragment();
                fragmentManager = (DashboardActivity.this).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, f).commit();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.deconnexion) {
            finish();
            UserSessionManager.getInstance(getApplicationContext()).logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Gestion de la navigation
        int id = item.getItemId();

        Fragment f = new Fragment();
        /**Gestion des fragments***/
        if (id == R.id.nav_client){
            f = new ClientFragment();
        }else if (id == R.id.nav_produit) {
            /**Ecran Produit (saisie d'intervention)**/
            f = new ProduitFragment();
        }else if (id == R.id.nav_devis){
            f = new DevisFragment();
        }else if (id == R.id.nav_commande){
            f = new CommandeFragment();
        }else if (id == R.id.nav_home){
            f = new HomeFragment();
        }
        fragmentManager = (DashboardActivity.this).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, f).commit();
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Override this method in the activity that hosts the Fragment and call super
        // in order to receive the result inside onActivityResult from the fragment.
        super.onActivityResult(requestCode, resultCode, data);
    }

    /***Modification de fonts sur le navigation**/
    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = ResourcesCompat.getFont(getApplicationContext(), R.font.montserratalternates_semibold);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("montserratalternates_semibold" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    public void onDataReceived(CommandeFilterElements commandeFilterElements) {
        CommandeFragment commandeFragment = CommandeFragment.getInstance();
        commandeFragment.onDataReceived(commandeFilterElements);
    }
}