package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.fragments.ClientFragment;
import com.sominfor.somisal_app.fragments.CommandeFragment;
import com.sominfor.somisal_app.utils.UserSessionManager;

public class AddProduitCommandeActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    AppCompatActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produit_commande);


        /**Contrôle de sessions utilisateur**/
        if (!UserSessionManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        /**Gestion du menu d'action**/
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Nouvelle Commande");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Options Menu (ActionBar Menu)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_add_commande_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            startActivity(new Intent(this, DashboardActivity.class));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}