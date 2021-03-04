package com.sominfor.somisal_app.handler.models;

import java.io.Serializable;

/**
 * Créé par vatsou le 13,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Modèle de classe Devis
 */
public class Devis implements Serializable {
    private String DevCosoc;
    private String DevCoage;
    private String DevNudev;
    private String DevDadev;
    private String DevNucli;
    private String DevRfdev;
    private String DevDaval;
    private String DevLieuv;
    private String DevComag;
    private Double DevVadev;
    private String DevComon;
    private Float DevTxchg;
    private String DevNucom;
    private String DevStatut;
    private String DevUscom;
    private Float DevTxrem;
    private Float DevTxesc;
    private Double DevEcova;
    private String DevMoexp;
    private String DevDaliv;
    private String DevColiv;
    private String DevMoreg;
    private String DevDereg;
    private String DevNacli;
    private String DevColieuv;

    private String DevTxhEnt;
    private String DevTxhPie;
    private String DexTexte;
    private String Devliliv;
    private String DevLimag;

    //Expandables
    private boolean expandable;

    /****Libelles***/
    private String CliRasoc;

    //Constructeur par défaut
    public Devis(){
        this.expandable = false;
    }

    public Devis(String devNudev, String devDadev, String devRfdev, String devLieuv, Double devVadev, String devDaliv, String cliRasoc, String devComon) {
        DevNudev = devNudev;
        DevDadev = devDadev;
        DevRfdev = devRfdev;
        DevLieuv = devLieuv;
        DevVadev = devVadev;
        DevDaliv = devDaliv;
        CliRasoc = cliRasoc;
        DevComon = devComon;
        this.expandable = false;
    }

    public String getDevColieuv() {
        return DevColieuv;
    }

    public void setDevColieuv(String devColieuv) {
        DevColieuv = devColieuv;
    }

    public String getDevNacli() {
        return DevNacli;
    }

    public void setDevNacli(String devNacli) {
        DevNacli = devNacli;
    }

    public String getDevLimag() {
        return DevLimag;
    }

    public void setDevLimag(String devLimag) {
        DevLimag = devLimag;
    }

    public String getDevCosoc() {
        return DevCosoc;
    }

    public void setDevCosoc(String devCosoc) {
        DevCosoc = devCosoc;
    }

    public String getDevCoage() {
        return DevCoage;
    }

    public void setDevCoage(String devCoage) {
        DevCoage = devCoage;
    }

    public String getDevNudev() {
        return DevNudev;
    }

    public void setDevNudev(String devNudev) {
        DevNudev = devNudev;
    }

    public String getDevDadev() {
        return DevDadev;
    }

    public void setDevDadev(String devDadev) {
        DevDadev = devDadev;
    }

    public String getDevNucli() {
        return DevNucli;
    }

    public void setDevNucli(String devNucli) {
        DevNucli = devNucli;
    }

    public String getDevRfdev() {
        return DevRfdev;
    }

    public void setDevRfdev(String devRfdev) {
        DevRfdev = devRfdev;
    }

    public String getDevDaval() {
        return DevDaval;
    }

    public void setDevDaval(String devDaval) {
        DevDaval = devDaval;
    }

    public String getDevLieuv() {
        return DevLieuv;
    }

    public void setDevLieuv(String devLieuv) {
        DevLieuv = devLieuv;
    }

    public String getDevComag() {
        return DevComag;
    }

    public void setDevComag(String devComag) {
        DevComag = devComag;
    }

    public Double getDevVadev() {
        return DevVadev;
    }

    public void setDevVadev(Double devVadev) {
        DevVadev = devVadev;
    }

    public String getDevComon() {
        return DevComon;
    }

    public void setDevComon(String devComon) {
        DevComon = devComon;
    }

    public Float getDevTxchg() {
        return DevTxchg;
    }

    public void setDevTxchg(Float devTxchg) {
        DevTxchg = devTxchg;
    }

    public String getDevNucom() {
        return DevNucom;
    }

    public void setDevNucom(String devNucom) {
        DevNucom = devNucom;
    }

    public String getDevStatut() {
        return DevStatut;
    }

    public void setDevStatut(String devStatut) {
        DevStatut = devStatut;
    }

    public String getCliRasoc() {
        return CliRasoc;
    }

    public void setCliRasoc(String cliRasoc) {
        CliRasoc = cliRasoc;
    }

    public String getDevUscom() {
        return DevUscom;
    }

    public void setDevUscom(String devUscom) {
        DevUscom = devUscom;
    }

    public Float getDevTxrem() {
        return DevTxrem;
    }

    public void setDevTxrem(Float devTxrem) {
        DevTxrem = devTxrem;
    }

    public Float getDevTxesc() {
        return DevTxesc;
    }

    public void setDevTxesc(Float devTxesc) {
        DevTxesc = devTxesc;
    }

    public Double getDevEcova() {
        return DevEcova;
    }

    public void setDevEcova(Double devEcova) {
        DevEcova = devEcova;
    }

    public String getDevMoexp() {
        return DevMoexp;
    }

    public void setDevMoexp(String devMoexp) {
        DevMoexp = devMoexp;
    }

    public String getDevDaliv() {
        return DevDaliv;
    }

    public void setDevDaliv(String devDaliv) {
        DevDaliv = devDaliv;
    }

    public String getDevColiv() {
        return DevColiv;
    }

    public void setDevColiv(String devColiv) {
        DevColiv = devColiv;
    }

    public String getDevMoreg() {
        return DevMoreg;
    }

    public void setDevMoreg(String devMoreg) {
        DevMoreg = devMoreg;
    }

    public String getDevDereg() {
        return DevDereg;
    }

    public void setDevDereg(String devDereg) {
        DevDereg = devDereg;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public String getDevTxhEnt() {
        return DevTxhEnt;
    }

    public void setDevTxhEnt(String devTxhEnt) {
        DevTxhEnt = devTxhEnt;
    }

    public String getDevTxhPie() {
        return DevTxhPie;
    }

    public void setDevTxhPie(String devTxhPie) {
        DevTxhPie = devTxhPie;
    }


    public String getDexTexte() {
        return DexTexte;
    }

    public void setDexTexte(String dexTexte) {
        DexTexte = dexTexte;
    }

    public String getDevliliv() {
        return Devliliv;
    }

    public void setDevliliv(String devliliv) {
        Devliliv = devliliv;
    }

    @Override
    public String toString() {
        return "Devis{" +
                "DevNudev='" + DevNudev + '\'' +
                ", DevNucli='" + DevNucli + '\'' +
                '}';
    }
}
