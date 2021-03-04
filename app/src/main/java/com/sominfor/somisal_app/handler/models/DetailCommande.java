package com.sominfor.somisal_app.handler.models;

import java.io.Serializable;

/**
 * Créé par vatsou le 27,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 */
public class DetailCommande implements Serializable {
    /**Données générales**/
    private String dcocosoc;
    private String dcocoage;
    private int dcocopro;
    private int dconuprm;
    private String dcounvte;
    private long dcocofvt;

    private String dcoTexte;

    /**Données commande**/
    private String dconucom;
    private int dcopocom;
    private Double dcoqtcom;
    private Double dcoputar;
    private Double dcotxrem;
    private Double dcovarem;
    private Double dcoqtliv;
    private Double Dcovacom;

    /**Libelles**/
    private boolean expandable;
    private String dcolipro;

    /***Commentaire  de poste**/
    private String dcotxn;

    public DetailCommande(){this.expandable = false;}

    public DetailCommande(int dcocopro, int dconuprm, String dcounvte, String dconucom, int dcopocom, Double dcoqtcom, Double dcoputar, Double dcotxrem, Double dcovarem, Double dcoqtliv, Double dcovacom, String dcolipro, String dcotxn) {
        this.dcocopro = dcocopro;
        this.dconuprm = dconuprm;
        this.dcounvte = dcounvte;
        this.dconucom = dconucom;
        this.dcopocom = dcopocom;
        this.dcoqtcom = dcoqtcom;
        this.dcoputar = dcoputar;
        this.dcotxrem = dcotxrem;
        this.dcovarem = dcovarem;
        this.dcoqtliv = dcoqtliv;
        Dcovacom = dcovacom;
        this.dcolipro = dcolipro;
        this.dcotxn = dcotxn;
    }

    public String getDcoTexte() {
        return dcoTexte;
    }

    public void setDcoTexte(String dcoTexte) {
        this.dcoTexte = dcoTexte;
    }

    public String getDcocosoc() {
        return dcocosoc;
    }

    public void setDcocosoc(String dcocosoc) {
        this.dcocosoc = dcocosoc;
    }

    public String getDcocoage() {
        return dcocoage;
    }

    public void setDcocoage(String dcocoage) {
        this.dcocoage = dcocoage;
    }

    public int getDcocopro() {
        return dcocopro;
    }

    public void setDcocopro(int dcocopro) {
        this.dcocopro = dcocopro;
    }

    public int getDconuprm() {
        return dconuprm;
    }

    public void setDconuprm(int dconuprm) {
        this.dconuprm = dconuprm;
    }

    public String getDcounvte() {
        return dcounvte;
    }

    public void setDcounvte(String dcounvte) {
        this.dcounvte = dcounvte;
    }

    public long getDcocofvt() {
        return dcocofvt;
    }

    public void setDcocofvt(long dcocofvt) {
        this.dcocofvt = dcocofvt;
    }

    public String getDconucom() {
        return dconucom;
    }

    public void setDconucom(String dconucom) {
        this.dconucom = dconucom;
    }

    public int getDcopocom() {
        return dcopocom;
    }

    public void setDcopocom(int dcopocom) {
        this.dcopocom = dcopocom;
    }

    public Double getDcoqtcom() {
        return dcoqtcom;
    }

    public void setDcoqtcom(Double dcoqtcom) {
        this.dcoqtcom = dcoqtcom;
    }

    public Double getDcoputar() {
        return dcoputar;
    }

    public void setDcoputar(Double dcoputar) {
        this.dcoputar = dcoputar;
    }

    public Double getDcotxrem() {
        return dcotxrem;
    }

    public void setDcotxrem(Double dcotxrem) {
        this.dcotxrem = dcotxrem;
    }

    public Double getDcovarem() {
        return dcovarem;
    }

    public void setDcovarem(Double dcovarem) {
        this.dcovarem = dcovarem;
    }

    public Double getDcoqtliv() {
        return dcoqtliv;
    }

    public void setDcoqtliv(Double dcoqtliv) {
        this.dcoqtliv = dcoqtliv;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public String getDcolipro() {
        return dcolipro;
    }

    public void setDcolipro(String dcolipro) {
        this.dcolipro = dcolipro;
    }

    public Double getDcovacom() {
        return Dcovacom;
    }

    public void setDcovacom(Double dcovacom) {
        Dcovacom = dcovacom;
    }

    public String getDcotxn() {
        return dcotxn;
    }

    public void setDcotxn(String dcotxn) {
        this.dcotxn = dcotxn;
    }

    @Override
    public boolean equals(Object t){
        if(!(t instanceof DetailCommande)){
            return false;
        }
        DetailCommande c = (DetailCommande) t;
        //Compare however you want, ie
        return  (String.valueOf(c.getDcocopro()).equals(String.valueOf(this.getDcocopro())));
    }
}
