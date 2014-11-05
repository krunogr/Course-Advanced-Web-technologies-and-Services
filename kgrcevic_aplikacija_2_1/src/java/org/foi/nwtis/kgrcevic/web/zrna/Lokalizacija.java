/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kgrcevic.web.zrna;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.print.attribute.HashAttributeSet;

/** 
 * Zrno za lokalizaciju. Moguće je podesiti na hrvatski i engleski jezik.
 * @author kgrcevic
 */
@ManagedBean
@SessionScoped
public class Lokalizacija implements Serializable {

    private Map<String, Object> jezici;
    private String odabraniJezik;
    private Locale odabraniLocale;

    /**
     * Creates a new instance of Lokalizacija
     */
    public Lokalizacija() {
        jezici = new HashMap<String, Object>();
        jezici.put("Engleski", Locale.ENGLISH);
        jezici.put("Hrvatski", new Locale("hr"));
    }

    public Object odaberiJezik() {
        for (Map.Entry<String, Object> entry : jezici.entrySet()) {
            if (entry.getValue().toString().equals(odabraniJezik)) {
                FacesContext.getCurrentInstance()
                        .getViewRoot().setLocale((Locale) entry.getValue());
            }
        }
        if (odabraniJezik == null) {
            odabraniJezik = "hr";


        }
        return "OK";
    }

    public Map<String, Object> getJezici() {
        return jezici;
    }

    public void setJezici(Map<String, Object> jezici) {
        this.jezici = jezici;
    }

    public String getOdabraniJezik() {
        return odabraniJezik;
    }

    public void setOdabraniJezik(String odabraniJezik) {
        this.odabraniJezik = odabraniJezik;
    }

    public Locale getOdabraniLocale() {
        return odabraniLocale;
    }

    public void setOdabraniLocale(Locale odabraniLocale) {
        this.odabraniLocale = odabraniLocale;
    }
}
