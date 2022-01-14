package com.sominfor.somisal_app.interfaces;

import com.sominfor.somisal_app.handler.models.Commande;

/**
 * Créé par vatsou le 15,décembre,2021
 * SOMINFOR
 * Paris, FRANCE
 * Interface récupération de reponse volley Détail En-tête commande
 */
public interface VolleyCallBackCommande {
    void onSuccess(Commande commande);
}
