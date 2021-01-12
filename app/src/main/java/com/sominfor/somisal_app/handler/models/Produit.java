package com.sominfor.somisal_app.handler.models;

import java.io.Serializable;

/**
 * Créé par kguillard le 08,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Gestion de systèmes AS400
 */

public class Produit implements Serializable {
    private int procopro;
    private String protypro;
    private String prolipro;
    private String prolxpro;
    private String procofam;
    private String prosofam;
    private String procoedi;
    private String prostatu;
    private int pronuprm;
    private String prouncdt;
    private float procofcd;
    private String prounvte;
    private float procofvt;
    private String procota1;
    private String procota2;
    private String procota3;
    private String procota4;
    private String procota5;
    private String protaxca;
    private String procpgen;
    private String proaxevt;
    private String procpavt;
    private String proaxest;
    private String procpast;
    private String procpitg;

    public Produit() {
    }

    public int getProcopro() {
        return procopro;
    }

    public void setProcopro(int procopro) {
        this.procopro = procopro;
    }

    public int getPronuprm() {
        return pronuprm;
    }

    public void setPronuprm(int pronuprm) {
        this.pronuprm = pronuprm;
    }

    public String getProtypro() {
        return protypro;
    }

    public void setProtypro(String protypro) {
        this.protypro = protypro;
    }

    public String getProlipro() {
        return prolipro;
    }

    public void setProlipro(String prolipro) {
        this.prolipro = prolipro;
    }

    public String getProlxpro() {
        return prolxpro;
    }

    public void setProlxpro(String prolxpro) {
        this.prolxpro = prolxpro;
    }

    public String getProcofam() {
        return procofam;
    }

    public void setProcofam(String procofam) {
        this.procofam = procofam;
    }

    public String getProsofam() {
        return prosofam;
    }

    public void setProsofam(String prosofam) {
        this.prosofam = prosofam;
    }

    public String getProcoedi() {
        return procoedi;
    }

    public void setProcoedi(String procoedi) {
        this.procoedi = procoedi;
    }

    public String getProstatu() {
        return prostatu;
    }

    public void setProstatu(String prostatu) {
        this.prostatu = prostatu;
    }

    public String getProuncdt() {
        return prouncdt;
    }

    public void setProuncdt(String prouncdt) {
        this.prouncdt = prouncdt;
    }

    public float getProcofcd() {
        return procofcd;
    }

    public void setProcofcd(float procofcd) {
        this.procofcd = procofcd;
    }

    public String getProunvte() {
        return prounvte;
    }

    public void setProunvte(String prounvte) {
        this.prounvte = prounvte;
    }

    public float getProcofvt() {
        return procofvt;
    }

    public void setProcofvt(float procofvt) {
        this.procofvt = procofvt;
    }

    public String getProcota1() {
        return procota1;
    }

    public void setProcota1(String procota1) {
        this.procota1 = procota1;
    }

    public String getProcota2() {
        return procota2;
    }

    public void setProcota2(String procota2) {
        this.procota2 = procota2;
    }

    public String getProcota3() {
        return procota3;
    }

    public void setProcota3(String procota3) {
        this.procota3 = procota3;
    }

    public String getProcota4() {
        return procota4;
    }

    public void setProcota4(String procota4) {
        this.procota4 = procota4;
    }

    public String getProcota5() {
        return procota5;
    }

    public void setProcota5(String procota5) {
        this.procota5 = procota5;
    }

    public String getProtaxca() {
        return protaxca;
    }

    public void setProtaxca(String protaxca) {
        this.protaxca = protaxca;
    }

    public String getProcpgen() {
        return procpgen;
    }

    public void setProcpgen(String procpgen) {
        this.procpgen = procpgen;
    }

    public String getProaxevt() {
        return proaxevt;
    }

    public void setProaxevt(String proaxevt) {
        this.proaxevt = proaxevt;
    }

    public String getProcpavt() {
        return procpavt;
    }

    public void setProcpavt(String procpavt) {
        this.procpavt = procpavt;
    }

    public String getProaxest() {
        return proaxest;
    }

    public void setProaxest(String proaxest) {
        this.proaxest = proaxest;
    }

    public String getProcpast() {
        return procpast;
    }

    public void setProcpast(String procpast) {
        this.procpast = procpast;
    }

    public String getProcpitg() {
        return procpitg;
    }

    public void setProcpitg(String procpitg) {
        this.procpitg = procpitg;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "procopro=" + procopro +
                ", protypro='" + protypro + '\'' +
                ", prolipro='" + prolipro + '\'' +
                ", prolxpro='" + prolxpro + '\'' +
                ", procofam='" + procofam + '\'' +
                ", prosofam='" + prosofam + '\'' +
                ", procoedi='" + procoedi + '\'' +
                ", prostatu='" + prostatu + '\'' +
                ", pronuprm=" + pronuprm +
                ", prouncdt='" + prouncdt + '\'' +
                ", procofcd=" + procofcd +
                ", prounvte='" + prounvte + '\'' +
                ", procofvt=" + procofvt +
                ", procota1='" + procota1 + '\'' +
                ", procota2='" + procota2 + '\'' +
                ", procota3='" + procota3 + '\'' +
                ", procota4='" + procota4 + '\'' +
                ", procota5='" + procota5 + '\'' +
                ", protaxca='" + protaxca + '\'' +
                ", procpgen='" + procpgen + '\'' +
                ", proaxevt='" + proaxevt + '\'' +
                ", procpavt='" + procpavt + '\'' +
                ", proaxest='" + proaxest + '\'' +
                ", procpast='" + procpast + '\'' +
                ", procpitg='" + procpitg + '\'' +
                '}';
    }
}
