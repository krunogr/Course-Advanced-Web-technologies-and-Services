/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kgrcevic.web.ws.klijenti;

import net.wxbug.api.LiveWeatherData;

/**
 *
 * @author Kruno
 */
public class MeteoKlijent {

    public static java.util.List<org.foi.nwtis.kgrcevic.web.ws.serveri.MeteoPodatak> nNajcescihZipova(java.lang.String broj) {
        org.foi.nwtis.kgrcevic.web.ws.serveri.MeteoServis_Service service = new org.foi.nwtis.kgrcevic.web.ws.serveri.MeteoServis_Service();
        org.foi.nwtis.kgrcevic.web.ws.serveri.MeteoServis port = service.getMeteoServisPort();
        return port.nNajcescihZipova(broj);
    }

    public static java.util.List<org.foi.nwtis.kgrcevic.web.ws.serveri.MeteoPodatak> nZadjnihMeteoPodataka(java.lang.String zip, java.lang.String broj) {
        org.foi.nwtis.kgrcevic.web.ws.serveri.MeteoServis_Service service = new org.foi.nwtis.kgrcevic.web.ws.serveri.MeteoServis_Service();
        org.foi.nwtis.kgrcevic.web.ws.serveri.MeteoServis port = service.getMeteoServisPort();
        return port.nZadjnihMeteoPodataka(zip, broj);
    }

    public static java.util.List<org.foi.nwtis.kgrcevic.web.ws.serveri.MeteoPodatak> podaciZaZipUIntervalu(java.lang.String dateod, java.lang.String datedo, java.lang.String zip) {
        org.foi.nwtis.kgrcevic.web.ws.serveri.MeteoServis_Service service = new org.foi.nwtis.kgrcevic.web.ws.serveri.MeteoServis_Service();
        org.foi.nwtis.kgrcevic.web.ws.serveri.MeteoServis port = service.getMeteoServisPort();
        return port.podaciZaZipUIntervalu(dateod, datedo, zip);
    }

    public static LiveWeatherData zaJedanZip(java.lang.String zip) {
        org.foi.nwtis.kgrcevic.web.ws.serveri.MeteoServis_Service service = new org.foi.nwtis.kgrcevic.web.ws.serveri.MeteoServis_Service();
        org.foi.nwtis.kgrcevic.web.ws.serveri.MeteoServis port = service.getMeteoServisPort();
        return port.zaJedanZip(zip);
    }

    public static java.util.List<org.foi.nwtis.kgrcevic.web.ws.serveri.MeteoPodatak> zaSveZipove() {
        org.foi.nwtis.kgrcevic.web.ws.serveri.MeteoServis_Service service = new org.foi.nwtis.kgrcevic.web.ws.serveri.MeteoServis_Service();
        org.foi.nwtis.kgrcevic.web.ws.serveri.MeteoServis port = service.getMeteoServisPort();
        return port.zaSveZipove();
    }
}
