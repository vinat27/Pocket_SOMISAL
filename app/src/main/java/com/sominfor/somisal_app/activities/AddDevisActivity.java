package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.fragments.DevisPostItFullDialog;
import com.sominfor.somisal_app.fragments.FilterProduitFullDialog;
import com.sominfor.somisal_app.handler.models.Famille;
import com.sominfor.somisal_app.handler.models.ServeurNode;
import com.sominfor.somisal_app.interfaces.CallBackPostIt;

public class AddDevisActivity extends AppCompatActivity implements CallBackPostIt {

    MaterialButton BtnValider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_devis);

        /**Gestion du menu d'action**/
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Nouveau devis");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /**Effacer l'ombre sous l'actionBar**/
        getSupportActionBar().setElevation(0);

        /**Instanciation des widgets**/
        BtnValider = findViewById(R.id.BtnValider);

        BtnValider.setOnClickListener(v -> {
            Intent i = new Intent(AddDevisActivity.this, AddProduitDevisActivity.class);
            startActivity(i);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_devis_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.post_it_devis:
                /**Devis **/
                openDialog();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openDialog() {
        /***Filtre sur familles et sous familles**/
        FragmentManager fragmentManager = getSupportFragmentManager();
        DevisPostItFullDialog devisPostItFullDialog = DevisPostItFullDialog.newInstance();
        //devisPostItFullDialog.setTargetFragment(devisPostItFullDialog, 100);
        devisPostItFullDialog.show(fragmentManager, ServeurNode.TAG);
    }

    @Override
    public void onDataReceived(String DexTexte) {
        Toast.makeText(this, DexTexte, Toast.LENGTH_LONG).show();
    }

}