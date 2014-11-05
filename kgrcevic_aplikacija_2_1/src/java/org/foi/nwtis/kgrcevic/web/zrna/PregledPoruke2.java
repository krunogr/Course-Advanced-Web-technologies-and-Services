/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kgrcevic.web.zrna;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.foi.nwtis.kgrcevic.web.Poruka;

/**
 * Klasa PregledPoruke2 služi za prikazivanje sadržaja pojedine poruke.
 *
 * @author kgrcevic
 */
@ManagedBean
@RequestScoped
public class PregledPoruke2 {

    private Poruka poruka;

    /**
     * Creates a new instance of PregledPoruke
     */
    public PregledPoruke2() {
    }

    public Poruka getPoruka() {
        
//        PregledPoruka pp = (PregledPoruka) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("PregledPoruka");
//        poruka = pp.getOdabranaPoruka();
        return PregledPoruka.odabranaPoruka;
    }

    public void setPoruka(Poruka poruka) {
        this.poruka = poruka;
    }

    public String vratiSvePoruke() {
        return "OK";

    }
}
