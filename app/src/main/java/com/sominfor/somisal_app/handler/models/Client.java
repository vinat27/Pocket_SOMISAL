package com.sominfor.somisal_app.handler.models;

import java.io.Serializable;

/**
 * Créé par vatsou le 25,janvier,2021
 * SOMINFOR
 * Paris, FRANCE
 * Modèle de classe Client
 */
public class Client implements Serializable {
    /***Données générales**/
    private String CliCosoc;
    private String CliCoage;
    private String CliNucli;
    private String CliNacli;
    private String CliOprgp;
    private String CliNurgp;
    private String CliLivth;
    private String CliRasoc;
    private String CliAdre1;
    private String CliAdre2;
    private String CliBopos;
    private String CliCopos;
    private String CliVille;
    private String CliCpays;
    private String CliTelep;
    private String CliTefax;
    private String CliEmail;
    private String CliMnadr;
    private String CliStatu;

    /**Données légales**/
    private String CliNucee;
    private String CliNuape;
    private String CliSiren;
    private String CliStati;

    /**Données de Livraison**/
    private String CliRasol;
    private String CliAdr1l;
    private String CliAdr2l;
    private String CliBopol;
    private String CliCopol;
    private String CliVilll;
    private String CliCpayl;
    private String CliMaill;
    private String CliZogeo;

    /**Données bancaires**/
    private String CliBnqu1;
    private String CliGich1;
    private String CliNubq1;
    private int CliNuri1;
    private String CliRayo1;
    private String CliEtpa1;
    private String CliLipa1;
    private String CliBnqu2;
    private String CliGich2;
    private String CliNubq2;
    private int CliNuri2;
    private String CliRayo2;
    private String CliEtpa2;
    private String CliLipa2;

    /**Données - Règlement (Adresse)**/
    private String CliRasor;
    private String CliAdr1r;
    private String CliAdr2r;
    private String CliBopor;
    private String CliCopor;
    private String CliVillr;
    private String CliCpayr;
    private String CliMailr;

    /**Données - Règlement (Conditions)**/
    private String CliMoreg;
    private String CliDereg;
    private String CliJoreg;
    private int CliIdrib;
    private String CliBqreg;
    private String CliGireg;
    private String CliComon;

    /***Données - Contact (Relance)**/
    private String CliRlnom;
    private String CliRlfct;
    private String CliRltel;
    private String CliRlmai;

    /***Données - Facturation**/
    private String CliFatyp;
    private String CliFargp;
    private String CliFatax;
    private String CliCota1;
    private String CliCota2;
    private String CliCota3;
    private String CliCota4;
    private String CliCota5;

    /**Données - Exploitation**/
    private String CliCotrp;
    private String CliCotrn;
    private String CliColiv;

    /**Données - Comptable**/
    private String CliCogrp;
    private String CliSoitg;
    private String CliAgitg;
    private String CliCpgen;
    private String CliNacpx;
    private String CliCpaux;
    private String CliNiaxe;
    private String CliCpana;
    private String CliCpcli;
    private Double CliMtplf;

    /**Données - Commerciales**/
    private String CliUscom;
    private String CliAutcd;
    private int CliCdnbr;
    private int CliPfnbr;
    private int CliFanbr;
    private int CliBlnbr;
    private String CliBledv;
    private String CliCmnom;
    private String CliCmfct;
    private String CliCmtel;
    private String CliCmmai;
    private String CliCoass;
    private Double CliMtass;
    private String CliIdniu;
    private String CliCoens;
    private Double CliMtcau;
    private String CliDatlc;

    /**Libellés**/
    private String CliLiNacli;
    private String CliLiCpays;
    private String CliLiComon;
    private String CliLiliv;
    private String CliLitrn;
    private String CliLitrp;
    private String Clililivth;

    public Client(){}

    /*public Client(String cliNucli, String cliNacli, String cliRasoc) {
        CliNucli = cliNucli;
        CliNacli = cliNacli;
        CliRasoc = cliRasoc;
    }*/

    public String getCliZogeo() {
        return CliZogeo;
    }

    public void setCliZogeo(String cliZogeo) {
        CliZogeo = cliZogeo;
    }

    public String getClililivth() {
        return Clililivth;
    }

    public void setClililivth(String clililivth) {
        Clililivth = clililivth;
    }

    public String getCliLitrp() {
        return CliLitrp;
    }

    public void setCliLitrp(String cliLitrp) {
        CliLitrp = cliLitrp;
    }

    public String getCliLitrn() {
        return CliLitrn;
    }

    public void setCliLitrn(String cliLitrn) {
        CliLitrn = cliLitrn;
    }

    public String getCliLiCpays() {
        return CliLiCpays;
    }

    public void setCliLiCpays(String cliLiCpays) {
        CliLiCpays = cliLiCpays;
    }

    public String getCliCosoc() {
        return CliCosoc;
    }

    public void setCliCosoc(String cliCosoc) {
        CliCosoc = cliCosoc;
    }

    public String getCliCoage() {
        return CliCoage;
    }

    public void setCliCoage(String cliCoage) {
        CliCoage = cliCoage;
    }

    public String getCliNucli() {
        return CliNucli;
    }

    public void setCliNucli(String cliNucli) {
        CliNucli = cliNucli;
    }

    public String getCliNacli() {
        return CliNacli;
    }

    public void setCliNacli(String cliNacli) {
        CliNacli = cliNacli;
    }

    public String getCliOprgp() {
        return CliOprgp;
    }

    public void setCliOprgp(String cliOprgp) {
        CliOprgp = cliOprgp;
    }

    public String getCliNurgp() {
        return CliNurgp;
    }

    public void setCliNurgp(String cliNurgp) {
        CliNurgp = cliNurgp;
    }

    public String getCliLivth() {
        return CliLivth;
    }

    public void setCliLivth(String cliLivth) {
        CliLivth = cliLivth;
    }

    public String getCliRasoc() {
        return CliRasoc;
    }

    public void setCliRasoc(String cliRasoc) {
        CliRasoc = cliRasoc;
    }

    public String getCliAdre1() {
        return CliAdre1;
    }

    public void setCliAdre1(String cliAdre1) {
        CliAdre1 = cliAdre1;
    }

    public String getCliAdre2() {
        return CliAdre2;
    }

    public void setCliAdre2(String cliAdre2) {
        CliAdre2 = cliAdre2;
    }

    public String getCliBopos() {
        return CliBopos;
    }

    public void setCliBopos(String cliBopos) {
        CliBopos = cliBopos;
    }

    public String getCliCopos() {
        return CliCopos;
    }

    public void setCliCopos(String cliCopos) {
        CliCopos = cliCopos;
    }

    public String getCliVille() {
        return CliVille;
    }

    public void setCliVille(String cliVille) {
        CliVille = cliVille;
    }

    public String getCliCpays() {
        return CliCpays;
    }

    public void setCliCpays(String cliCpays) {
        CliCpays = cliCpays;
    }

    public String getCliTelep() {
        return CliTelep;
    }

    public void setCliTelep(String cliTelep) {
        CliTelep = cliTelep;
    }

    public String getCliTefax() {
        return CliTefax;
    }

    public void setCliTefax(String cliTefax) {
        CliTefax = cliTefax;
    }

    public String getCliEmail() {
        return CliEmail;
    }

    public void setCliEmail(String cliEmail) {
        CliEmail = cliEmail;
    }

    public String getCliMnadr() {
        return CliMnadr;
    }

    public void setCliMnadr(String cliMnadr) {
        CliMnadr = cliMnadr;
    }

    public String getCliStatu() {
        return CliStatu;
    }

    public void setCliStatu(String cliStatu) {
        CliStatu = cliStatu;
    }

    public String getCliNucee() {
        return CliNucee;
    }

    public void setCliNucee(String cliNucee) {
        CliNucee = cliNucee;
    }

    public String getCliNuape() {
        return CliNuape;
    }

    public void setCliNuape(String cliNuape) {
        CliNuape = cliNuape;
    }

    public String getCliSiren() {
        return CliSiren;
    }

    public void setCliSiren(String cliSiren) {
        CliSiren = cliSiren;
    }

    public String getCliStati() {
        return CliStati;
    }

    public void setCliStati(String cliStati) {
        CliStati = cliStati;
    }

    public String getCliRasol() {
        return CliRasol;
    }

    public void setCliRasol(String cliRasol) {
        CliRasol = cliRasol;
    }

    public String getCliAdr1l() {
        return CliAdr1l;
    }

    public void setCliAdr1l(String cliAdr1l) {
        CliAdr1l = cliAdr1l;
    }

    public String getCliAdr2l() {
        return CliAdr2l;
    }

    public void setCliAdr2l(String cliAdr2l) {
        CliAdr2l = cliAdr2l;
    }

    public String getCliBopol() {
        return CliBopol;
    }

    public void setCliBopol(String cliBopol) {
        CliBopol = cliBopol;
    }

    public String getCliCopol() {
        return CliCopol;
    }

    public void setCliCopol(String cliCopol) {
        CliCopol = cliCopol;
    }

    public String getCliVilll() {
        return CliVilll;
    }

    public void setCliVilll(String cliVilll) {
        CliVilll = cliVilll;
    }

    public String getCliCpayl() {
        return CliCpayl;
    }

    public void setCliCpayl(String cliCpayl) {
        CliCpayl = cliCpayl;
    }

    public String getCliMaill() {
        return CliMaill;
    }

    public void setCliMaill(String cliMaill) {
        CliMaill = cliMaill;
    }

    public String getCliBnqu1() {
        return CliBnqu1;
    }

    public void setCliBnqu1(String cliBnqu1) {
        CliBnqu1 = cliBnqu1;
    }

    public String getCliGich1() {
        return CliGich1;
    }

    public void setCliGich1(String cliGich1) {
        CliGich1 = cliGich1;
    }

    public String getCliNubq1() {
        return CliNubq1;
    }

    public void setCliNubq1(String cliNubq1) {
        CliNubq1 = cliNubq1;
    }

    public int getCliNuri1() {
        return CliNuri1;
    }

    public void setCliNuri1(int cliNuri1) {
        CliNuri1 = cliNuri1;
    }

    public String getCliRayo1() {
        return CliRayo1;
    }

    public void setCliRayo1(String cliRayo1) {
        CliRayo1 = cliRayo1;
    }

    public String getCliEtpa1() {
        return CliEtpa1;
    }

    public void setCliEtpa1(String cliEtpa1) {
        CliEtpa1 = cliEtpa1;
    }

    public String getCliLipa1() {
        return CliLipa1;
    }

    public void setCliLipa1(String cliLipa1) {
        CliLipa1 = cliLipa1;
    }

    public String getCliBnqu2() {
        return CliBnqu2;
    }

    public void setCliBnqu2(String cliBnqu2) {
        CliBnqu2 = cliBnqu2;
    }

    public String getCliGich2() {
        return CliGich2;
    }

    public void setCliGich2(String cliGich2) {
        CliGich2 = cliGich2;
    }

    public String getCliNubq2() {
        return CliNubq2;
    }

    public void setCliNubq2(String cliNubq2) {
        CliNubq2 = cliNubq2;
    }

    public int getCliNuri2() {
        return CliNuri2;
    }

    public void setCliNuri2(int cliNuri2) {
        CliNuri2 = cliNuri2;
    }

    public String getCliRayo2() {
        return CliRayo2;
    }

    public void setCliRayo2(String cliRayo2) {
        CliRayo2 = cliRayo2;
    }

    public String getCliEtpa2() {
        return CliEtpa2;
    }

    public void setCliEtpa2(String cliEtpa2) {
        CliEtpa2 = cliEtpa2;
    }

    public String getCliLipa2() {
        return CliLipa2;
    }

    public void setCliLipa2(String cliLipa2) {
        CliLipa2 = cliLipa2;
    }

    public String getCliRasor() {
        return CliRasor;
    }

    public void setCliRasor(String cliRasor) {
        CliRasor = cliRasor;
    }

    public String getCliAdr1r() {
        return CliAdr1r;
    }

    public void setCliAdr1r(String cliAdr1r) {
        CliAdr1r = cliAdr1r;
    }

    public String getCliAdr2r() {
        return CliAdr2r;
    }

    public void setCliAdr2r(String cliAdr2r) {
        CliAdr2r = cliAdr2r;
    }

    public String getCliBopor() {
        return CliBopor;
    }

    public void setCliBopor(String cliBopor) {
        CliBopor = cliBopor;
    }

    public String getCliCopor() {
        return CliCopor;
    }

    public void setCliCopor(String cliCopor) {
        CliCopor = cliCopor;
    }

    public String getCliVillr() {
        return CliVillr;
    }

    public void setCliVillr(String cliVillr) {
        CliVillr = cliVillr;
    }

    public String getCliCpayr() {
        return CliCpayr;
    }

    public void setCliCpayr(String cliCpayr) {
        CliCpayr = cliCpayr;
    }

    public String getCliMailr() {
        return CliMailr;
    }

    public void setCliMailr(String cliMailr) {
        CliMailr = cliMailr;
    }

    public String getCliMoreg() {
        return CliMoreg;
    }

    public void setCliMoreg(String cliMoreg) {
        CliMoreg = cliMoreg;
    }

    public String getCliDereg() {
        return CliDereg;
    }

    public void setCliDereg(String cliDereg) {
        CliDereg = cliDereg;
    }

    public String getCliJoreg() {
        return CliJoreg;
    }

    public void setCliJoreg(String cliJoreg) {
        CliJoreg = cliJoreg;
    }

    public int getCliIdrib() {
        return CliIdrib;
    }

    public void setCliIdrib(int cliIdrib) {
        CliIdrib = cliIdrib;
    }

    public String getCliBqreg() {
        return CliBqreg;
    }

    public void setCliBqreg(String cliBqreg) {
        CliBqreg = cliBqreg;
    }

    public String getCliGireg() {
        return CliGireg;
    }

    public void setCliGireg(String cliGireg) {
        CliGireg = cliGireg;
    }

    public String getCliComon() {
        return CliComon;
    }

    public void setCliComon(String cliComon) {
        CliComon = cliComon;
    }

    public String getCliRlnom() {
        return CliRlnom;
    }

    public void setCliRlnom(String cliRlnom) {
        CliRlnom = cliRlnom;
    }

    public String getCliRlfct() {
        return CliRlfct;
    }

    public void setCliRlfct(String cliRlfct) {
        CliRlfct = cliRlfct;
    }

    public String getCliRltel() {
        return CliRltel;
    }

    public void setCliRltel(String cliRltel) {
        CliRltel = cliRltel;
    }

    public String getCliRlmai() {
        return CliRlmai;
    }

    public void setCliRlmai(String cliRlmai) {
        CliRlmai = cliRlmai;
    }

    public String getCliFatyp() {
        return CliFatyp;
    }

    public void setCliFatyp(String cliFatyp) {
        CliFatyp = cliFatyp;
    }

    public String getCliFargp() {
        return CliFargp;
    }

    public void setCliFargp(String cliFargp) {
        CliFargp = cliFargp;
    }

    public String getCliFatax() {
        return CliFatax;
    }

    public void setCliFatax(String cliFatax) {
        CliFatax = cliFatax;
    }

    public String getCliCota1() {
        return CliCota1;
    }

    public void setCliCota1(String cliCota1) {
        CliCota1 = cliCota1;
    }

    public String getCliCota2() {
        return CliCota2;
    }

    public void setCliCota2(String cliCota2) {
        CliCota2 = cliCota2;
    }

    public String getCliCota3() {
        return CliCota3;
    }

    public void setCliCota3(String cliCota3) {
        CliCota3 = cliCota3;
    }

    public String getCliCota4() {
        return CliCota4;
    }

    public void setCliCota4(String cliCota4) {
        CliCota4 = cliCota4;
    }

    public String getCliCota5() {
        return CliCota5;
    }

    public void setCliCota5(String cliCota5) {
        CliCota5 = cliCota5;
    }

    public String getCliCotrp() {
        return CliCotrp;
    }

    public void setCliCotrp(String cliCotrp) {
        CliCotrp = cliCotrp;
    }

    public String getCliCotrn() {
        return CliCotrn;
    }

    public void setCliCotrn(String cliCotrn) {
        CliCotrn = cliCotrn;
    }

    public String getCliColiv() {
        return CliColiv;
    }

    public void setCliColiv(String cliColiv) {
        CliColiv = cliColiv;
    }

    public String getCliCogrp() {
        return CliCogrp;
    }

    public void setCliCogrp(String cliCogrp) {
        CliCogrp = cliCogrp;
    }

    public String getCliSoitg() {
        return CliSoitg;
    }

    public void setCliSoitg(String cliSoitg) {
        CliSoitg = cliSoitg;
    }

    public String getCliAgitg() {
        return CliAgitg;
    }

    public void setCliAgitg(String cliAgitg) {
        CliAgitg = cliAgitg;
    }

    public String getCliCpgen() {
        return CliCpgen;
    }

    public void setCliCpgen(String cliCpgen) {
        CliCpgen = cliCpgen;
    }

    public String getCliNacpx() {
        return CliNacpx;
    }

    public void setCliNacpx(String cliNacpx) {
        CliNacpx = cliNacpx;
    }

    public String getCliCpaux() {
        return CliCpaux;
    }

    public void setCliCpaux(String cliCpaux) {
        CliCpaux = cliCpaux;
    }

    public String getCliNiaxe() {
        return CliNiaxe;
    }

    public void setCliNiaxe(String cliNiaxe) {
        CliNiaxe = cliNiaxe;
    }

    public String getCliCpana() {
        return CliCpana;
    }

    public void setCliCpana(String cliCpana) {
        CliCpana = cliCpana;
    }

    public String getCliCpcli() {
        return CliCpcli;
    }

    public void setCliCpcli(String cliCpcli) {
        CliCpcli = cliCpcli;
    }

    public Double getCliMtplf() {
        return CliMtplf;
    }

    public void setCliMtplf(Double cliMtplf) {
        CliMtplf = cliMtplf;
    }

    public String getCliUscom() {
        return CliUscom;
    }

    public void setCliUscom(String cliUscom) {
        CliUscom = cliUscom;
    }

    public String getCliAutcd() {
        return CliAutcd;
    }

    public void setCliAutcd(String cliAutcd) {
        CliAutcd = cliAutcd;
    }

    public int getCliCdnbr() {
        return CliCdnbr;
    }

    public void setCliCdnbr(int cliCdnbr) {
        CliCdnbr = cliCdnbr;
    }

    public int getCliPfnbr() {
        return CliPfnbr;
    }

    public void setCliPfnbr(int cliPfnbr) {
        CliPfnbr = cliPfnbr;
    }

    public int getCliFanbr() {
        return CliFanbr;
    }

    public void setCliFanbr(int cliFanbr) {
        CliFanbr = cliFanbr;
    }

    public int getCliBlnbr() {
        return CliBlnbr;
    }

    public void setCliBlnbr(int cliBlnbr) {
        CliBlnbr = cliBlnbr;
    }

    public String getCliBledv() {
        return CliBledv;
    }

    public void setCliBledv(String cliBledv) {
        CliBledv = cliBledv;
    }

    public String getCliCmnom() {
        return CliCmnom;
    }

    public void setCliCmnom(String cliCmnom) {
        CliCmnom = cliCmnom;
    }

    public String getCliCmfct() {
        return CliCmfct;
    }

    public void setCliCmfct(String cliCmfct) {
        CliCmfct = cliCmfct;
    }

    public String getCliCmtel() {
        return CliCmtel;
    }

    public void setCliCmtel(String cliCmtel) {
        CliCmtel = cliCmtel;
    }

    public String getCliCmmai() {
        return CliCmmai;
    }

    public void setCliCmmai(String cliCmmai) {
        CliCmmai = cliCmmai;
    }

    public String getCliCoass() {
        return CliCoass;
    }

    public void setCliCoass(String cliCoass) {
        CliCoass = cliCoass;
    }

    public Double getCliMtass() {
        return CliMtass;
    }

    public void setCliMtass(Double cliMtass) {
        CliMtass = cliMtass;
    }

    public String getCliIdniu() {
        return CliIdniu;
    }

    public void setCliIdniu(String cliIdniu) {
        CliIdniu = cliIdniu;
    }

    public String getCliCoens() {
        return CliCoens;
    }

    public void setCliCoens(String cliCoens) {
        CliCoens = cliCoens;
    }

    public Double getCliMtcau() {
        return CliMtcau;
    }

    public void setCliMtcau(Double cliMtcau) {
        CliMtcau = cliMtcau;
    }

    public String getCliDatlc() {
        return CliDatlc;
    }

    public void setCliDatlc(String cliDatlc) {
        CliDatlc = cliDatlc;
    }

    public String getCliLiNacli() {
        return CliLiNacli;
    }

    public void setCliLiNacli(String cliLiNacli) {
        CliLiNacli = cliLiNacli;
    }

    public String getCliLiComon() {
        return CliLiComon;
    }

    public void setCliLiComon(String cliLiComon) {
        CliLiComon = cliLiComon;
    }

    public String getCliLiliv() {
        return CliLiliv;
    }

    public void setCliLiliv(String cliLiliv) {
        CliLiliv = cliLiliv;
    }

    @Override
    public String toString() {
        return getCliRasoc();
    }
}
