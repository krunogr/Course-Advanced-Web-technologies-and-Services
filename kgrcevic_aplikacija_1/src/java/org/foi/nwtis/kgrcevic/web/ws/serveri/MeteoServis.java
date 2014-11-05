/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kgrcevic.web.ws.serveri;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.ServletContext;
import javax.xml.ws.*;
import javax.xml.ws.handler.MessageContext;
import net.wxbug.api.LiveWeatherData;
import org.foi.nwtis.kgrcevic.konfiguracije.Konfiguracija;
import org.foi.nwtis.kgrcevic.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.kgrcevic.web.Dretva;
import static org.foi.nwtis.kgrcevic.web.Dretva.bpKonfig;
import org.foi.nwtis.kgrcevic.web.MeteoPodatak;
import org.foi.nwtis.kgrcevic.web.slusaci.SlusacAplikacije;
import org.foi.nwtis.kgrcevic.web.ws.klijenti.WeatherBugKlijent;

/**
 * Vlastiti servis koji ima nekoliko operacija.
 *
 * @author Kruno
 *
 *
 */
@WebService(serviceName = "MeteoServis")
public class MeteoServis {

    String putanja;
    String user;
    String pass;
    String driver;
    ArrayList<MeteoPodatak> zips = new ArrayList<>();

    /**
     * metoda ucitajPodatke() se poziva kada je potrebno spajanje na bazu te
     * učitava potrebne podatke.
     *
     */
    private void ucitajPodatke() {
        BP_Konfiguracija bpKonfig = (BP_Konfiguracija) SlusacAplikacije.context.getAttribute("BP_Konfiguracija");
        putanja = bpKonfig.getServer_database() + bpKonfig.getUserDatabase();
        user = bpKonfig.getUserUsername();
        pass = bpKonfig.getUserPassword();
        driver = bpKonfig.getDriver_database();

    }

    /**
     * ZaJedanZip dohvaća meteo podatke za jedan zip
     *
     */
    @WebMethod(operationName = "zaJedanZip")
    public LiveWeatherData zaJedanZip(@WebParam(name = "zip") String zip) {
        WeatherBugKlijent wbk = new WeatherBugKlijent();
        LiveWeatherData lwd = wbk.dajMeteoPodatke(zip);
        return lwd;

    }

    /**
     * ZaSveZipove dohvaća sve zipove iz baze za koji se preuzimaju meteo podaci
     *
     */
    @WebMethod(operationName = "zaSveZipove")
    public ArrayList<MeteoPodatak> zaSveZipove() {

        try {
            ucitajPodatke();
            Connection con;
            Statement stmt;

            try {
                Class.forName(driver);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MeteoServis.class.getName()).log(Level.SEVERE, null, ex);
            }

            con = DriverManager.getConnection(putanja, user, pass);
            stmt = con.createStatement();
            String upit = "SELECT * FROM kgrcevic_zipcodes";
            try (ResultSet rs = stmt.executeQuery(upit)) {
                zips.clear();
                while (rs.next()) {
                    MeteoPodatak mp = new MeteoPodatak();
                    mp.setZipZahtjevani(rs.getString("ZIP"));
                    zips.add(mp);
                }
                stmt.close();
            }
            con.close();
            return zips;

        } catch (SQLException ex) {
            Logger.getLogger(MeteoServis.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * sljedećoj operaciji predajemo određen zip te broj meteopodataka koji
     * želimo dohvatiti
     *
     */
    @WebMethod(operationName = "nZadjnihMeteoPodataka")
    public ArrayList<MeteoPodatak> nZadnjihMeteoPodataka(@WebParam(name = "zip") String zip, @WebParam(name = "broj") String broj) {
        try {
            ucitajPodatke();
            Connection con;
            Statement stmt;
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MeteoServis.class.getName()).log(Level.SEVERE, null, ex);
            }

            con = DriverManager.getConnection(putanja, user, pass);
            stmt = con.createStatement();
            String upit = "SELECT * FROM kgrcevic_meteopodaci WHERE zipzahtjevani ='" + zip + "' ORDER BY vrijeme_preuzimanja DESC LIMIT " + broj;
            try (ResultSet rs = stmt.executeQuery(upit)) {
                zips.clear();
                while (rs.next()) {
                    MeteoPodatak mp = new MeteoPodatak();
                    mp.setZipZahtjevani(rs.getString("zipzahtjevani"));
                    mp.setZipVraceni(rs.getString("zipvraceni"));
                    mp.setGrad(rs.getString("grad"));
                    mp.setKisa_stopa(rs.getString("kisa_stopa"));
                    mp.setTemperatura(rs.getString("temperatura"));
                    mp.setTlak(rs.getString("tlak"));
                    mp.setVjetar_brzina(rs.getString("vjetar_brzina"));
                    mp.setVjetar_smjer(rs.getString("vjetar_smjer"));
                    mp.setVlaga(rs.getString("vlaga"));
                    mp.setVrem_zona(rs.getString("vremenska_zona"));
                    mp.setVrijeme_preuzimanja(rs.getString("vrijeme_preuzimanja"));
                    zips.add(mp);


                }
                System.out.println(zips);
                stmt.close();
            }
            con.close();
            return zips;

        } catch (SQLException ex) {
            Logger.getLogger(MeteoServis.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * operacija nNajcescihZipova daje onaj broj najčešćih zipova koji smo mu
     * predali
     */
    @WebMethod(operationName = "nNajcescihZipova")
    public ArrayList<MeteoPodatak> nNajcescihZipKodova(@WebParam(name = "broj") String broj) {
        try {
            ucitajPodatke();
            Connection con;
            Statement stmt;

            try {
                Class.forName(driver);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MeteoServis.class.getName()).log(Level.SEVERE, null, ex);
            }


            con = DriverManager.getConnection(putanja, user, pass);
            stmt = con.createStatement();
            String upit = "SELECT *, COUNT(*) AS brojac FROM kgrcevic_meteopodaci GROUP BY zipzahtjevani ORDER BY brojac DESC LIMIT " + broj;
            try (ResultSet rs = stmt.executeQuery(upit)) {
                zips.clear();
                while (rs.next()) {
                    MeteoPodatak mp = new MeteoPodatak();
                    mp.setZipZahtjevani(rs.getString("zipzahtjevani"));
                    mp.setZipVraceni(rs.getString("zipvraceni"));
                    mp.setGrad(rs.getString("grad"));
                    mp.setKisa_stopa(rs.getString("kisa_stopa"));
                    mp.setTemperatura(rs.getString("temperatura"));
                    mp.setTlak(rs.getString("tlak"));
                    mp.setVjetar_brzina(rs.getString("vjetar_brzina"));
                    mp.setVjetar_smjer(rs.getString("vjetar_smjer"));
                    mp.setVlaga(rs.getString("vlaga"));
                    mp.setVrem_zona(rs.getString("vremenska_zona"));
                    mp.setVrijeme_preuzimanja(rs.getString("vrijeme_preuzimanja"));
                    mp.setBroj_preuzimanja(rs.getString("brojac"));
                    zips.add(mp);

                }
                stmt.close();
            }
            con.close();
            return zips;

        } catch (SQLException ex) {
            Logger.getLogger(MeteoServis.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * sljedeća operacija nam vraća meteopodatke za određen zip kod u određenom
     * vremenskom intervalu
     *
     */
    @WebMethod(operationName = "podaciZaZipUIntervalu")
    public ArrayList<MeteoPodatak> podaciZaZipUIntervalu(@WebParam(name = "dateod") String dateod, @WebParam(name = "datedo") String datedo, @WebParam(name = "zip") String zip) {
        try {
            ucitajPodatke();
            Connection con;
            Statement stmt;
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MeteoServis.class.getName()).log(Level.SEVERE, null, ex);
            }

            con = DriverManager.getConnection(putanja, user, pass);
            stmt = con.createStatement();
            String upit = "SELECT * FROM kgrcevic_meteopodaci WHERE vrijeme_preuzimanja >'" + dateod + "' AND vrijeme_preuzimanja <'" + datedo + "' AND zipzahtjevani='" + zip + "'";
            try (ResultSet rs = stmt.executeQuery(upit)) {
                zips.clear();
                while (rs.next()) {
                    MeteoPodatak mp = new MeteoPodatak();
                    mp.setZipZahtjevani(rs.getString("zipzahtjevani"));
                    mp.setZipVraceni(rs.getString("zipvraceni"));
                    mp.setGrad(rs.getString("grad"));
                    mp.setKisa_stopa(rs.getString("kisa_stopa"));
                    mp.setTemperatura(rs.getString("temperatura"));
                    mp.setTlak(rs.getString("tlak"));
                    mp.setVjetar_brzina(rs.getString("vjetar_brzina"));
                    mp.setVjetar_smjer(rs.getString("vjetar_smjer"));
                    mp.setVlaga(rs.getString("vlaga"));
                    mp.setVrem_zona(rs.getString("vremenska_zona"));
                    mp.setVrijeme_preuzimanja(rs.getString("vrijeme_preuzimanja"));
                    zips.add(mp);

                }
                System.out.println(zips);
                stmt.close();
            }
            con.close();
            return zips;

        } catch (SQLException ex) {
            Logger.getLogger(MeteoServis.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
