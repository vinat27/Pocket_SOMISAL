package com.sominfor.somisal_app.handler.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * Créé par vatsou le 16,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class DetailDevis implements Serializable {
    private String DdvCosoc;
    private String DdvCoage;
    private int DdvCopro;
    private int DdvNuprm;
    private String DdvUnvte;
    private int DdvCofvt;
    private String DdvNudev;
    private int DdvPodev;
    private Double DdvQtdev;
    private Double DdvPutar;
    private Double DdvTxrem;
    private Double DdvVarem;
    private String DdvLipro;
    private Double DdvVadev;
    private String DdvTxnPo;
    private String DdvComon;

    //Expandables
    private boolean expandable;

    public DetailDevis(){
        this.expandable = false;
    }

    public DetailDevis(int ddvCopro, int ddvNuprm, String ddvUnvte, int ddvPodev, Double ddvQtdev, Double ddvPutar, Double ddvTxrem, Double ddvVarem, Double ddvVadev, String ddvLipro, String ddvTxnPo, String ddvcomon) {
        DdvCopro = ddvCopro;
        DdvNuprm = ddvNuprm;
        DdvUnvte = ddvUnvte;
        DdvPodev = ddvPodev;
        DdvQtdev = ddvQtdev;
        DdvPutar = ddvPutar;
        DdvTxrem = ddvTxrem;
        DdvVarem = ddvVarem;
        DdvVadev = ddvVadev;
        DdvLipro = ddvLipro;
        DdvTxnPo = ddvTxnPo;
        DdvComon = ddvcomon;
        this.expandable = false;
    }

    public String getDdvCosoc() {
        return DdvCosoc;
    }

    public void setDdvCosoc(String ddvCosoc) {
        DdvCosoc = ddvCosoc;
    }

    public String getDdvCoage() {
        return DdvCoage;
    }

    public void setDdvCoage(String ddvCoage) {
        DdvCoage = ddvCoage;
    }

    public int getDdvCopro() {
        return DdvCopro;
    }

    public void setDdvCopro(int ddvCopro) {
        DdvCopro = ddvCopro;
    }

    public int getDdvNuprm() {
        return DdvNuprm;
    }

    public void setDdvNuprm(int ddvNuprm) {
        DdvNuprm = ddvNuprm;
    }

    public String getDdvUnvte() {
        return DdvUnvte;
    }

    public void setDdvUnvte(String ddvUnvte) {
        DdvUnvte = ddvUnvte;
    }

    public int getDdvCofvt() {
        return DdvCofvt;
    }

    public void setDdvCofvt(int ddvCofvt) {
        DdvCofvt = ddvCofvt;
    }

    public String getDdvNudev() {
        return DdvNudev;
    }

    public void setDdvNudev(String ddvNudev) {
        DdvNudev = ddvNudev;
    }

    public int getDdvPodev() {
        return DdvPodev;
    }

    public void setDdvPodev(int ddvPodev) {
        DdvPodev = ddvPodev;
    }

    public Double getDdvQtdev() {
        return DdvQtdev;
    }

    public void setDdvQtdev(Double ddvQtdev) {
        DdvQtdev = ddvQtdev;
    }

    public Double getDdvPutar() {
        return DdvPutar;
    }

    public void setDdvPutar(Double ddvPutar) {
        DdvPutar = ddvPutar;
    }

    public Double getDdvTxrem() {
        return DdvTxrem;
    }

    public void setDdvTxrem(Double ddvTxrem) {
        DdvTxrem = ddvTxrem;
    }

    public Double getDdvVarem() {
        return DdvVarem;
    }

    public void setDdvVarem(Double ddvVarem) {
        DdvVarem = ddvVarem;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public String getDdvLipro() {
        return DdvLipro;
    }

    public void setDdvLipro(String ddvLipro) {
        DdvLipro = ddvLipro;
    }

    public Double getDdvVadev() {
        return DdvVadev;
    }

    public void setDdvVadev(Double ddvVadev) {
        DdvVadev = ddvVadev;
    }

    public String getDdvTxnPo() {
        return DdvTxnPo;
    }

    public void setDdvTxnPo(String ddvTxnPo) {
        DdvTxnPo = ddvTxnPo;
    }

    public String getDdvComon() {
        return DdvComon;
    }

    public void setDdvComon(String ddvComon) {
        DdvComon = ddvComon;
    }

    @Override
    public boolean equals(Object t){
        if(!(t instanceof DetailDevis)){
            return false;
        }
        DetailDevis c = (DetailDevis) t;
        //Compare however you want, ie
        return  (String.valueOf(c.getDdvPodev()).equals(String.valueOf(this.getDdvPodev())));
    }


}
