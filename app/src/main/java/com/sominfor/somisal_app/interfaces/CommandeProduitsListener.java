package com.sominfor.somisal_app.interfaces;

import com.sominfor.somisal_app.handler.models.DetailCommande;
import com.sominfor.somisal_app.handler.models.DetailDevis;

/**
 * Créé par vatsou le 01,février,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public interface CommandeProduitsListener {
    void onDataReceived(DetailCommande detailCommande);
    void onDataReceivedPostIt(String CoxTexte, String ComTxnEn, String ComTxnPd);
}
