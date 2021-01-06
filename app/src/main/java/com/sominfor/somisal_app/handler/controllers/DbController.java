package com.sominfor.somisal_app.handler.controllers;

/**
 * Fichier : DbController.java
 * Auteur : ATSOU Koffi Vincent
 * Description: Gestion de base de données SQLite - Création des tables
 * Date Création: 06 Janvier 2021
 * **/

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sominfor.somisal_app.app.SomisalApp;
import com.sominfor.somisal_app.handler.models.ServeurNode;

public class DbController extends SQLiteOpenHelper {
    private static final String LOGCAT = null;
    private static final String DATABASENAME = "somisal.db";
    private static final int DATABASEVERSION = 1;
    private static final String TAG = DbController.class.getSimpleName().toString();


    public DbController() {
        super(SomisalApp.getContext(), DATABASENAME, null, DATABASEVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ServeurNodeController.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ServeurNode.TABLE);
    }
}
