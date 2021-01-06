package com.sominfor.somisal_app.activities;
/**
 * Fichier : LoginActivity.java
 * Auteur : ATSOU Koffi Vincent
 * Description: Gestion Authentification
 * Date Création: 06 Janvier 2021
 * **/

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.sominfor.somisal_app.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    // Options Menu (ActionBar Menu)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_login, menu);
        return true;
    }

    // When Options Menu is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Gestion de menus
        int id = item.getItemId();
        if (id == R.id.handleSystem){
            /**Ecran des paramètres***/
            Intent intent = new Intent(LoginActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}