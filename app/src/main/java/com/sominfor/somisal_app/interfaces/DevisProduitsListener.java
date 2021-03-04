package com.sominfor.somisal_app.interfaces;

import com.sominfor.somisal_app.handler.models.DetailDevis;

/**
 * Créé par vatsou le 25,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public interface DevisProduitsListener {
    void onDataReceived(DetailDevis detailDevis);
    void onDataReceivedPostIt(String DexTexte, String DevTxnEn, String DevTxnPd);
}
