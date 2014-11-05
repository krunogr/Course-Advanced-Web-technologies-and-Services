/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kgrcevic.web.zrna;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.foi.nwtis.jms.PorukaZaJMS;
import org.foi.nwtis.kgrcevic.sb.SessionBean;

/**
 * Klasa za pregled JMS poruka.
 *
 * @author Kruno
 */
@ManagedBean
@SessionScoped
public class PregledJMS {

    @EJB
    private SessionBean sessionBean;
    private List<PorukaZaJMS> list = new ArrayList<PorukaZaJMS>();
    private String start;

    /**
     * Creates a new instance of ViewMailMessages
     */
    public PregledJMS() {
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public List<PorukaZaJMS> getList() {
        list = SessionBean.getListPoruka();
        return list;
    }

    public void setList(List<PorukaZaJMS> list) {
        this.list = list;
    }
}