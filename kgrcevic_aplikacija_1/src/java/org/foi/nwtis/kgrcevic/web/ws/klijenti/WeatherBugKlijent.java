/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kgrcevic.web.ws.klijenti;

import net.wxbug.api.LiveWeatherData;
import net.wxbug.api.UnitType;
import org.foi.nwtis.kgrcevic.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.kgrcevic.web.slusaci.SlusacAplikacije;

/**
 * Klasa pomoću koje se dohvaćaju meteo podaci za web servisa.
 *
 * @author Kruno
 */
public class WeatherBugKlijent {

    private static String weatherBugCode;
    BP_Konfiguracija bpKonfig = (BP_Konfiguracija) SlusacAplikacije.context.getAttribute("BP_Konfiguracija");

    public LiveWeatherData dajMeteoPodatke(String zip) {
        weatherBugCode = bpKonfig.getWeatherBugCode();
        return getLiveWeatherByUSZipCode(zip, UnitType.METRIC, weatherBugCode);
    }

    private static LiveWeatherData getLiveWeatherByUSZipCode(java.lang.String zipCode, net.wxbug.api.UnitType unittype, java.lang.String aCode) {
        net.wxbug.api.WeatherBugWebServices service = new net.wxbug.api.WeatherBugWebServices();
        net.wxbug.api.WeatherBugWebServicesSoap port = service.getWeatherBugWebServicesSoap();
        return port.getLiveWeatherByUSZipCode(zipCode, unittype, aCode);
    }
}
