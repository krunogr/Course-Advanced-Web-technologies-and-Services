/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kgrcevic.web.zrna;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import net.wxbug.api.LiveWeatherData;
import org.foi.nwtis.kgrcevic.web.ws.klijenti.MeteoKlijent;
import org.foi.nwtis.kgrcevic.web.ws.serveri.MeteoPodatak;

/**
 *
 * @author Kruno
 */
@ManagedBean
@RequestScoped
public class PregledMeteoPodataka {

    private String zipKod;
    private String zadnji;
    private String najcesci;
    private String intervalOd;
    private String intervalDo;
    private List<MeteoPodatak> listaMP = new ArrayList();
    private LiveWeatherData meteoP;
    private List<String> listaString;
    private List<String> listaStringSve;
    private static int brojPodataka = 5;
    private int pocetniBrojPodataka = 5;
    private static int kreniOd = 0;
    private boolean first = false;

    /**
     * Creates a new instance of Javno
     */
    public PregledMeteoPodataka() {
    }

    public String getZipKod() {
        return zipKod;
    }

    public void setZipKod(String zipKod) {
        this.zipKod = zipKod;
    }

    public String getZadnji() {
        return zadnji;
    }

    public void setZadnji(String zadnji) {
        this.zadnji = zadnji;
    }

    public String getNajcesci() {
        return najcesci;
    }

    public void setNajcesci(String najcesci) {
        this.najcesci = najcesci;
    }

    public String getIntervalOd() {
        return intervalOd;
    }

    public void setIntervalOd(String intervalOd) {
        this.intervalOd = intervalOd;
    }

    public String getIntervalDo() {
        return intervalDo;
    }

    public void setIntervalDo(String intervalDo) {
        this.intervalDo = intervalDo;
    }

    public List<MeteoPodatak> getListaMP() {
        return listaMP;
    }

    public void setListaMP(List<MeteoPodatak> listaMP) {
        this.listaMP = listaMP;
    }

    public LiveWeatherData getMeteoP() {
        return meteoP;
    }

    public void setMeteoP(LiveWeatherData meteoP) {
        this.meteoP = meteoP;
    }

    public List<String> getListaStringSve() {
        return listaStringSve;
    }

    public void setListaStringSve(List<String> listaStringSve) {
        this.listaStringSve = listaStringSve;
    }

    public List<String> getListaString() {
        return listaString;
    }

    public void setListaString(List<String> listaString) {
        this.listaString = listaString;
    }

    public String zaJedanZip() {
        meteoP = MeteoKlijent.zaJedanZip(zipKod);
        return "";

    }

    public String zaSveZipove() {
        listaMP = MeteoKlijent.zaSveZipove();
        return "";

    }

    public String nNajcescihZipova() {
        listaMP = MeteoKlijent.nNajcescihZipova(najcesci);
        return "";

    }

    public String nZadjnihMeteoPodataka() {
        listaMP = MeteoKlijent.nZadjnihMeteoPodataka(zipKod, zadnji);
        return "";

    }

    public String podaciZaZipUIntervalu() {
        listaMP = MeteoKlijent.podaciZaZipUIntervalu(intervalOd, intervalDo, zipKod);
        return "";

    }

    public String naprijed(String pozovi) {
        if (pozovi.equals("zaSveZipove")) {
            zaSveZipove();
        }
        if (pozovi.equals("podaciZaZipUIntervalu")) {
            podaciZaZipUIntervalu();
        }
        if (pozovi.equals("nZadjnihMeteoPodataka")) {
            nZadjnihMeteoPodataka();
        }
        if (pozovi.equals("nNajcescihZipova")) {
            nNajcescihZipova();
        }

        System.out.println("NAPRIJED");
        if (kreniOd < listaMP.size() - brojPodataka) {
            kreniOd = kreniOd + brojPodataka;
            System.out.println("raspo " + brojPodataka);
        } else if (((listaMP.size() - kreniOd - 1) != brojPodataka) && (brojPodataka == pocetniBrojPodataka)) {
            System.out.println(brojPodataka + "+++++++");
            brojPodataka = listaMP.size() - kreniOd;
            System.out.println(brojPodataka + "+++++++");
        }
        return "";
    }

    public String nazad(String pozovi) {
        System.out.println("POZIVIII " + pozovi);
        if (pozovi.equals("zaSveZipove")) {
            zaSveZipove();
        }
        if (pozovi.equals("podaciZaZipUIntervalu")) {
            podaciZaZipUIntervalu();
        }
        if (pozovi.equals("nZadjnihMeteoPodataka")) {
            nZadjnihMeteoPodataka();
        }
        if (pozovi.equals("nNajcescihZipova")) {
            nNajcescihZipova();
        }
        System.out.println("NAZAD");
        if (brojPodataka != pocetniBrojPodataka) {

            brojPodataka = pocetniBrojPodataka;
        }
        if (kreniOd >= brojPodataka) {
            kreniOd = kreniOd - brojPodataka;
        }
        return "";
    }

    public int getKreniOd() {
        return kreniOd;
    }

    public int getBrojPodataka() {
        return brojPodataka;
    }
    public void reset(){
        kreniOd = 0;
        pocetniBrojPodataka=5;
    }
}
