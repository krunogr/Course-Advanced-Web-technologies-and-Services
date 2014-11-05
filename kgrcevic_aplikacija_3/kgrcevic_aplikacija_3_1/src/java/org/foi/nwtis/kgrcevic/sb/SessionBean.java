/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kgrcevic.sb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import org.foi.nwtis.jms.PorukaZaJMS;

/**
 * Pomoćna klasa za primanje JMS poruka.
 * @author Kruno
 */
@Stateless
public class SessionBean {

    private static List<PorukaZaJMS> listPoruka = new ArrayList<PorukaZaJMS>();
    private static PorukaZaJMS poruka;

    public static List<PorukaZaJMS> getListPoruka() {
        return listPoruka;
    }

    public static void setListPoruka(List<PorukaZaJMS> listPoruka) {
        SessionBean.listPoruka = listPoruka;
    }

    public static PorukaZaJMS getPoruka() {
        return poruka;
    }

    public static void setPoruka(PorukaZaJMS poruka) {
        listPoruka.add(poruka);
        SessionBean.poruka = poruka;
    }
}
