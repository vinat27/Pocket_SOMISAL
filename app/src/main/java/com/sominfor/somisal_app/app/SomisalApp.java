package com.sominfor.somisal_app.app;
/**
 * Fichier : SomisalApp.java
 * Auteur : ATSOU Koffi Vincent
 * Description: Initialisation de la base de données
 * Date Création: 06 Janvier 2021
 * **/
import android.app.Application;
import android.content.Context;

import com.sominfor.somisal_app.handler.controllers.DatabaseManager;
import com.sominfor.somisal_app.handler.controllers.DbController;

public class SomisalApp extends Application {
    private static SomisalApp mInstance;
    private static DbController dbController;
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        context = this.getApplicationContext();
        dbController = new DbController();
        DatabaseManager.initializeInstance(dbController);
    }

    public static synchronized SomisalApp getInstance() {
        return mInstance;
    }

    public static Context getContext(){
        return context;
    }
}
