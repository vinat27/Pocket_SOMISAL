package com.sominfor.somisal_app.interfaces;

import com.sominfor.somisal_app.handler.models.Famille;

/**
 * Créé par vatsou le 10,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public interface CallbackListener {
    void onDataReceived(Famille famille);
}
