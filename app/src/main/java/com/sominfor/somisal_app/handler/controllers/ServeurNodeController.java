package com.sominfor.somisal_app.handler.controllers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sominfor.somisal_app.handler.models.ServeurNode;

/**
 * Fichier : ServeurNodeController.java
 * Auteur : ATSOU Koffi Vincent
 * Description: Gestion de serveur (Création - Modification - Suppression )
 * Date Création: 06 Janvier 2021
 * **/

public class ServeurNodeController {
    ServeurNode serveurNode =  new ServeurNode();
    public static final String TAG = ServeurNodeController.class.getSimpleName();

    public ServeurNodeController(){}
    /**
     * Script SQL - Création de la table
     * @return string
     */
    public static String createTable(){
        String scriptSQL = "CREATE TABLE "+ ServeurNode.TABLE + "("
                + ServeurNode.KEY_serveurNodeId + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + ServeurNode.KEY_serveurNodeIp + " TEXT )";
        return scriptSQL;
    }

    /**
     * Insertion dans la table
     * @return boolean
     */
    public boolean insert(ServeurNode serveurNode){
        boolean statut;
        SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(ServeurNode.KEY_serveurNodeIp, serveurNode.getServeurNodeIp());

        if (sqLiteDatabase.insert(ServeurNode.TABLE, null, values) != -1){
            statut = true;
        }else{
            statut = false;
        }
        DatabaseManager.getInstance().closeDatabase();
        return statut;
    }

    /**
     * Vérification d'existence du serveur
     * @return boolean
     */
    public boolean checkIfIsExist(){
        boolean statut;
        SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "SELECT * FROM "+ServeurNode.TABLE;
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0){
            statut = true;
        }else{
            statut = false;
        }
        return statut;
    }

    /**
     *  Modification de serveur
     *  @return boolean
     */
    public boolean update(ServeurNode serveurNode){
        boolean statut;
        SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(ServeurNode.KEY_serveurNodeIp, serveurNode.getServeurNodeIp());

        if (sqLiteDatabase.update(ServeurNode.TABLE, values, "", null) > 0){
            statut = true;
        }else{
            statut = false;
        }
        DatabaseManager.getInstance().closeDatabase();
        return statut;
    }

    /**
     * Récupérer la valeur pour un serveur
     * @return Serveur
     */
    public ServeurNode getServeurNodeInfos(){
        SQLiteDatabase sqLiteDatabase = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "SELECT * FROM "+ServeurNode.TABLE; // select * from
        Cursor c = sqLiteDatabase.rawQuery(selectQuery, null);
        ServeurNode serveurNode = new ServeurNode();
        if (c.moveToFirst()){
            serveurNode.setServeurNodeIp(c.getString(c.getColumnIndex(ServeurNode.KEY_serveurNodeIp)));
        }
        return serveurNode;
    }

}
