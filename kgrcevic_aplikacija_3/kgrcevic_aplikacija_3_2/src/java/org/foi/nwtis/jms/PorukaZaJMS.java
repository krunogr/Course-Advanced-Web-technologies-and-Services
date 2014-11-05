/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jms;

import java.io.Serializable;
import java.util.Date;

/**
 * Klasa koja sadrži elemente koje treba imati svaka JMS poruka.
 * 
 * @author Kruno
 */
public class PorukaZaJMS implements Serializable {

    private int nwtisPoruke;
    private int neNwtisPoruke;
    private int ukupno;
    private String vrijemePocetkaObrade;
    private String vrijemeKrajaObrade;

    public PorukaZaJMS(int nwtisPoruke, int neNwtisPoruke, int ukupno, String vrijemePocetkaObrade, String vrijemeKrajaObrade) {
        this.nwtisPoruke = nwtisPoruke;
        this.neNwtisPoruke = neNwtisPoruke;
        this.ukupno = ukupno;
        this.vrijemePocetkaObrade = vrijemePocetkaObrade;
        this.vrijemeKrajaObrade = vrijemeKrajaObrade;
    }

    public int getNwtisPoruke() {
        return nwtisPoruke;
    }

    public void setNwtisPoruke(int nwtisPoruke) {
        this.nwtisPoruke = nwtisPoruke;
    }

    public int getNeNwtisPoruke() {
        return neNwtisPoruke;
    }

    public void setNeNwtisPoruke(int neNwtisPoruke) {
        this.neNwtisPoruke = neNwtisPoruke;
    }

    public int getUkupno() {
        return ukupno;
    }

    public void setUkupno(int ukupno) {
        this.ukupno = ukupno;
    }

    public String getVrijemePocetkaObrade() {
        return vrijemePocetkaObrade;
    }

    public void setVrijemePocetkaObrade(String vrijemePocetkaObrade) {
        this.vrijemePocetkaObrade = vrijemePocetkaObrade;
    }

    public String getVrijemeKrajaObrade() {
        return vrijemeKrajaObrade;
    }

    public void setVrijemeKrajaObrade(String vrijemeKrajaObrade) {
        this.vrijemeKrajaObrade = vrijemeKrajaObrade;
    }


    
    
}
