package com.sominfor.somisal_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sominfor.somisal_app.R;
import com.sominfor.somisal_app.fragments.ServeurNodeFragment;
import com.sominfor.somisal_app.handler.controllers.ServeurNodeController;
import com.sominfor.somisal_app.handler.models.ServeurNode;

import org.w3c.dom.Text;

import java.net.ServerSocket;

public class SettingsActivity extends AppCompatActivity {

    TextView TxtParametreValue;
    ServeurNode serveurNode;
    ServeurNodeController serveurNodeController;
    Boolean statut;
    ImageView updateIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**Controle orientation***/
        if(!getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /**Gestion du menu d'action**/
        if (getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /**Instanciation des widgets**/
        TxtParametreValue = findViewById(R.id.TxtParametreValue);
        updateIcon = findViewById(R.id.updateIcon);
        serveurNodeController = new ServeurNodeController();
        /**Affichage de serveur si existant**/
        statut = serveurNodeController.checkIfIsExist();
        if (statut){
            serveurNode = serveurNodeController.getServeurNodeInfos();
            TxtParametreValue.setText(serveurNode.getServeurNodeIp());
        }else{
            TxtParametreValue.setText(getResources().getString(R.string.settings_activity_NoServer));
        }

        /**Clic d'ajout ou de mise à jour de l'adresse du serveur**/
        updateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /***Ajout / Mise à jour de serveur**/
                FragmentManager fragmentManager = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                ServeurNodeFragment serveurNodeFragment = ServeurNodeFragment.newInstance();
                serveurNodeFragment.setArguments(bundle);
                serveurNodeFragment.show(fragmentManager, ServeurNode.TAG);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!getResources().getBoolean(R.bool.isTablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    // Options Menu (ActionBar Menu)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home || id == R.id.validateSettings) {
                Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Affichage nom de serveur
     * @void
     */
    public void setServeurNodeToTextView(String label){
        TxtParametreValue.setText(label);
    }
}