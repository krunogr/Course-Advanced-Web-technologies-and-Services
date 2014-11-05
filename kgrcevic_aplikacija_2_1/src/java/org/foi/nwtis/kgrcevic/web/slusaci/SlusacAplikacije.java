/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kgrcevic.web.slusaci;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.kgrcevic.konfiguracije.Konfiguracija;
import org.foi.nwtis.kgrcevic.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.kgrcevic.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.kgrcevic.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.kgrcevic.web.Dretva;

/**
 * Slusac aplikacije ko postavlja parametar BP_konfiguracija kako bi se kasnije
 * iz konfiguracijske datoteke mogli dohvaćati podaci
 *
 * @author kruno
 *
 *
 */
@WebListener()
public class SlusacAplikacije implements ServletContextListener {

    public static ServletContext context = null;
    public static Konfiguracija konfig = null;
    public static BP_Konfiguracija bpKonfig = null;
    private Dretva dretva;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        String path = sc.getRealPath("WEB-INF") + java.io.File.separator;
        String datoteka = path + sc.getInitParameter("konfiguracija");


        bpKonfig = new BP_Konfiguracija(datoteka);



        sc.setAttribute("BP_Konfiguracija", bpKonfig);


        context = sce.getServletContext();
        try {
            konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);

        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        System.out.println("Aplikacija završena" + sc.getContextPath());
        if (dretva != null && dretva.isAlive()) {
            dretva.interrupt();
        }
    }

    public static BP_Konfiguracija getBpKonfig() {
        return bpKonfig;
    }

    public static void setBpKonfig(BP_Konfiguracija bpKonfig) {
        SlusacAplikacije.bpKonfig = bpKonfig;
    }
    
    
    
}