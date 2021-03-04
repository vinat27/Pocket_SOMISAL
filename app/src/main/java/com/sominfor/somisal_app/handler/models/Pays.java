package com.sominfor.somisal_app.handler.models;

import java.io.Serializable;

/**
 * Créé par vatsou le 22,février,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class Pays implements Serializable {
    public static final String TAG = Pays.class.getSimpleName();
    private String PaysCopay;
    private String PaysLipay;

    public String getPaysCopay() {
        return PaysCopay;
    }

    public void setPaysCopay(String paysCopay) {
        PaysCopay = paysCopay;
    }

    public String getPaysLipay() {
        return PaysLipay;
    }

    public void setPaysLipay(String paysLipay) {
        PaysLipay = paysLipay;
    }

    @Override
    public String toString() {
        return getPaysLipay();
    }

    @Override
    public boolean equals(Object t){
        if(!(t instanceof Pays)){
            return false;
        }
        Pays c = (Pays) t;
        //Compare however you want, ie
        return (c.getPaysCopay().equals(this.getPaysCopay()));
    }
}
