package com.sominfor.somisal_app.handler.models;

import java.io.Serializable;

/**
 * Créé par vatsou le 26,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Modèle de classe Commande
 */
public class Commande implements Serializable {
    /**Données générales**/
    private String comcosoc;
    private String comcoage;
    private String comnucom;
    private String comdacom;
    private String comnucli;
    private String comnamar;
    private String comlieuv;
    private String comcomag;
    private Double comvacom;
    private String comcomon;
    private Double comtxchg;
    private String comnudev;
    private String comstatu;


    /**Données - Adresse facturation**/
    private String comrasoc;
    private String comadre1;
    private String comadre2;
    private String combopos;
    private String comcopos;
    private String comville;
    private String comcpays;

    /**Données - Livraison**/
    private String comdaliv;
    private String comcotrp;
    private String comcotrn;
    private String comcoliv;

    private String comrasol;
    private String comadr1l;
    private String comadr2l;
    private String combopol;
    private String comcopol;
    private String comvilll;
    private String comcpayl;

    /***Données commerciales**/
    private String comuscom;
    private Double comtxrem;
    private Double comtxesc;
    private Double comecova;

    /**Données paiement**/
    private String commoreg;
    private String comdereg;

    /**Extras**/
    private String comliliv;
    private String comlilieuv;
    private String comlitrn;
    private String comlimon;
    private String comlicpays;
    private String comlicpayr;
    private String comlimag;
    private String comlista;
    private String comtxhen;
    private String comtxhpd;
    private String coxtexte;
    private String comnacli;

    //Expandables
    private boolean expandable;

    public Commande(){
        this.expandable = false;
    }

    public Commande(String comnucom, String comdacom, String comlieuv, Double comvacom, String comrasoc, String comdaliv, String comcotrn, String comcoliv) {
        this.comnucom = comnucom;
        this.comdacom = comdacom;
        this.comlieuv = comlieuv;
        this.comvacom = comvacom;
        this.comrasoc = comrasoc;
        this.comdaliv = comdaliv;
        this.comcotrn = comcotrn;
        this.comcoliv = comcoliv;
    }

    public String getComnacli() {
        return comnacli;
    }

    public void setComnacli(String comnacli) {
        this.comnacli = comnacli;
    }

    public String getCoxtexte() {
        return coxtexte;
    }

    public void setCoxtexte(String coxtexte) {
        this.coxtexte = coxtexte;
    }

    public String getComtxhen() {
        return comtxhen;
    }

    public void setComtxhen(String comtxhen) {
        this.comtxhen = comtxhen;
    }

    public String getComtxhpd() {
        return comtxhpd;
    }

    public void setComtxhpd(String comtxhpd) {
        this.comtxhpd = comtxhpd;
    }

    public String getComlista() {
        return comlista;
    }

    public void setComlista(String comlista) {
        this.comlista = comlista;
    }

    public String getComlimag() {
        return comlimag;
    }

    public void setComlimag(String comlimag) {
        this.comlimag = comlimag;
    }

    public String getComlicpayr() {
        return comlicpayr;
    }

    public void setComlicpayr(String comlicpayr) {
        this.comlicpayr = comlicpayr;
    }

    public String getComlicpays() {
        return comlicpays;
    }

    public void setComlicpays(String comlicpays) {
        this.comlicpays = comlicpays;
    }

    public String getComlitrn() {
        return comlitrn;
    }

    public void setComlitrn(String comlitrn) {
        this.comlitrn = comlitrn;
    }

    public String getComlilieuv() {
        return comlilieuv;
    }

    public void setComlilieuv(String comlilieuv) {
        this.comlilieuv = comlilieuv;
    }

    public String getComliliv() {
        return comliliv;
    }

    public void setComliliv(String comliliv) {
        this.comliliv = comliliv;
    }

    public String getComlimon() {
        return comlimon;
    }

    public void setComlimon(String comlimon) {
        this.comlimon = comlimon;
    }

    public String getComcosoc() {
        return comcosoc;
    }

    public void setComcosoc(String comcosoc) {
        this.comcosoc = comcosoc;
    }

    public String getComcoage() {
        return comcoage;
    }

    public void setComcoage(String comcoage) {
        this.comcoage = comcoage;
    }

    public String getComnucom() {
        return comnucom;
    }

    public void setComnucom(String comnucom) {
        this.comnucom = comnucom;
    }

    public String getComdacom() {
        return comdacom;
    }

    public void setComdacom(String comdacom) {
        this.comdacom = comdacom;
    }

    public String getComnucli() {
        return comnucli;
    }

    public void setComnucli(String comnucli) {
        this.comnucli = comnucli;
    }

    public String getComnamar() {
        return comnamar;
    }

    public void setComnamar(String comnamar) {
        this.comnamar = comnamar;
    }

    public String getComlieuv() {
        return comlieuv;
    }

    public void setComlieuv(String comlieuv) {
        this.comlieuv = comlieuv;
    }

    public String getComcomag() {
        return comcomag;
    }

    public void setComcomag(String comcomag) {
        this.comcomag = comcomag;
    }

    public Double getComvacom() {
        return comvacom;
    }

    public void setComvacom(Double comvacom) {
        this.comvacom = comvacom;
    }

    public String getComcomon() {
        return comcomon;
    }

    public void setComcomon(String comcomon) {
        this.comcomon = comcomon;
    }

    public Double getComtxchg() {
        return comtxchg;
    }

    public void setComtxchg(Double comtxchg) {
        this.comtxchg = comtxchg;
    }

    public String getComnudev() {
        return comnudev;
    }

    public void setComnudev(String comnudev) {
        this.comnudev = comnudev;
    }

    public String getComstatu() {
        return comstatu;
    }

    public void setComstatu(String comstatu) {
        this.comstatu = comstatu;
    }

    public String getComrasoc() {
        return comrasoc;
    }

    public void setComrasoc(String comrasoc) {
        this.comrasoc = comrasoc;
    }

    public String getComadre1() {
        return comadre1;
    }

    public void setComadre1(String comadre1) {
        this.comadre1 = comadre1;
    }

    public String getComadre2() {
        return comadre2;
    }

    public void setComadre2(String comadre2) {
        this.comadre2 = comadre2;
    }

    public String getCombopos() {
        return combopos;
    }

    public void setCombopos(String combopos) {
        this.combopos = combopos;
    }

    public String getComcopos() {
        return comcopos;
    }

    public void setComcopos(String comcopos) {
        this.comcopos = comcopos;
    }

    public String getComville() {
        return comville;
    }

    public void setComville(String comville) {
        this.comville = comville;
    }

    public String getComcpays() {
        return comcpays;
    }

    public void setComcpays(String comcpays) {
        this.comcpays = comcpays;
    }

    public String getComdaliv() {
        return comdaliv;
    }

    public void setComdaliv(String comdaliv) {
        this.comdaliv = comdaliv;
    }

    public String getComcotrp() {
        return comcotrp;
    }

    public void setComcotrp(String comcotrp) {
        this.comcotrp = comcotrp;
    }

    public String getComcotrn() {
        return comcotrn;
    }

    public void setComcotrn(String comcotrn) {
        this.comcotrn = comcotrn;
    }

    public String getComcoliv() {
        return comcoliv;
    }

    public void setComcoliv(String comcoliv) {
        this.comcoliv = comcoliv;
    }

    public String getComrasol() {
        return comrasol;
    }

    public void setComrasol(String comrasol) {
        this.comrasol = comrasol;
    }

    public String getComadr1l() {
        return comadr1l;
    }

    public void setComadr1l(String comadr1l) {
        this.comadr1l = comadr1l;
    }

    public String getComadr2l() {
        return comadr2l;
    }

    public void setComadr2l(String comadr2l) {
        this.comadr2l = comadr2l;
    }

    public String getCombopol() {
        return combopol;
    }

    public void setCombopol(String combopol) {
        this.combopol = combopol;
    }

    public String getComcopol() {
        return comcopol;
    }

    public void setComcopol(String comcopol) {
        this.comcopol = comcopol;
    }

    public String getComvilll() {
        return comvilll;
    }

    public void setComvilll(String comvilll) {
        this.comvilll = comvilll;
    }

    public String getComcpayl() {
        return comcpayl;
    }

    public void setComcpayl(String comcpayl) {
        this.comcpayl = comcpayl;
    }

    public String getComuscom() {
        return comuscom;
    }

    public void setComuscom(String comuscom) {
        this.comuscom = comuscom;
    }

    public Double getComtxrem() {
        return comtxrem;
    }

    public void setComtxrem(Double comtxrem) {
        this.comtxrem = comtxrem;
    }

    public Double getComtxesc() {
        return comtxesc;
    }

    public void setComtxesc(Double comtxesc) {
        this.comtxesc = comtxesc;
    }

    public Double getComecova() {
        return comecova;
    }

    public void setComecova(Double comecova) {
        this.comecova = comecova;
    }

    public String getCommoreg() {
        return commoreg;
    }

    public void setCommoreg(String commoreg) {
        this.commoreg = commoreg;
    }

    public String getComdereg() {
        return comdereg;
    }

    public void setComdereg(String comdereg) {
        this.comdereg = comdereg;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }
}
