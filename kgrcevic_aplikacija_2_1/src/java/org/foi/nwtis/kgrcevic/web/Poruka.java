/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kgrcevic.web;

import java.util.Date;
import java.util.List;
import javax.mail.Flags;

/**
 * klasa poruka sadrži elemente koje bi trebala imati svaka mail Poruka.
 *
 * @author kgrcevic
 */
public class Poruka {

    private String id;
    private Date datum;
    private String posiljatelj;
    private String naslov;
    private String vrsta;
    private String sadrzaj;
    private Flags flag;
    private boolean brisanje;
    private boolean citanje;

    public Poruka(String id, Date datum, String posiljatelj, String naslov, String vrsta, String sadrzaj, Flags flag, boolean brisanje, boolean citanje) {
        this.id = id;
        this.datum = datum;
        this.posiljatelj = posiljatelj;
        this.naslov = naslov;
        this.vrsta = vrsta;
        this.sadrzaj = sadrzaj;
        this.flag = flag;
        this.brisanje = brisanje;
        this.citanje = citanje;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public String getPosiljatelj() {
        return posiljatelj;
    }

    public void setPosiljatelj(String posiljatelj) {
        this.posiljatelj = posiljatelj;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public String getVrsta() {
        return vrsta;
    }

    public void setVrsta(String vrsta) {
        this.vrsta = vrsta;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }

    public Flags getFlag() {
        return flag;
    }

    public void setFlag(Flags flag) {
        this.flag = flag;
    }

    public boolean isBrisanje() {
        return brisanje;
    }

    public void setBrisanje(boolean brisanje) {
        this.brisanje = brisanje;
    }

    public boolean isCitanje() {
        return citanje;
    }

    public void setCitanje(boolean citanje) {
        this.citanje = citanje;
    }   
}
