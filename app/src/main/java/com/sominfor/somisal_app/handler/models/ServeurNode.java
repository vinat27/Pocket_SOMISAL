package com.sominfor.somisal_app.handler.models;

/**
 * Fichier : ServeurNode.java
 * Auteur : ATSOU Koffi Vincent
 * Description: Modele de Classe ServeurNode
 * Date Création: 05 Janvier 2021
 * **/

public class ServeurNode {
    public static final String TAG = ServeurNode.class.getSimpleName();
    public static final String TABLE = "SERVEUR_NODE";
    /***Champs de la table***/
    public static final String KEY_serveurNodeId = "serveurNodeId";
    public static final String KEY_serveurNodeIp = "serveurNodeIp";

    /*****Variables*****/
    private int serveurNodeId;
    private String serveurNodeIp;
    /*********Getters et Setters**********/
    public int getServeurNodeId() {
        return serveurNodeId;
    }

    public void setServeurNodeId(int serveurNodeId) {
        this.serveurNodeId = serveurNodeId;
    }

    public String getServeurNodeIp() {
        return serveurNodeIp;
    }

    public void setServeurNodeIp(String serveurNodeIp) {
        this.serveurNodeIp = serveurNodeIp;
    }
}
