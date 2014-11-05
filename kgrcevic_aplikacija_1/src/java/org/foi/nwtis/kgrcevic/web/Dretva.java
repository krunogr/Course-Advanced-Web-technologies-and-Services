/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kgrcevic.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.wxbug.api.LiveWeatherData;
import org.foi.nwtis.kgrcevic.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.kgrcevic.web.ws.klijenti.WeatherBugKlijent;

/**
 *
 *
 *
 * Dretva za dohvaćanje meteo podatka u intervalu definiranom u konfiguracijskoj
 * datoteci.
 *
 * @author Kruno
 *
 */
public class Dretva extends Thread {

    public static BP_Konfiguracija bpKonfig;
    private int interval;
    private long razlika = 0;
    public static String komanda = "";
    public static boolean mail = false;
    public static boolean pauza = false;
    long pocetno_vrijeme = new Date().getTime();
    public static String usernn;
    private int odrzava = 2000;

    public Dretva(BP_Konfiguracija bpKonfig) {
        this.bpKonfig = bpKonfig;
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        System.out.println("Baza " + bpKonfig.getServer_database() + bpKonfig.getUserDatabase());
        System.out.println(bpKonfig.getUserUsername() + "   " + bpKonfig.getUserPassword());
        System.out.println("Interval: " + bpKonfig.getInterval());
        System.out.println("WeatherBugCode:" + bpKonfig.getWeatherBugCode());
        System.out.println(bpKonfig.getDriver_database());
        usernn = bpKonfig.getUserUsername();
        while (true) {
            if (PokretacDretve.prekiniDretvu) {
               Thread.currentThread().interrupt();
               System.out.println("Dretva je prekinuta");
               break;
            }
            odrzavanje();
            if (pauza == false) {
                preuzimanjeZipova();
            }

        }
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    private void odrzavanje() {
        try {
            this.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Dretva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * u funkciji preuzimanjeZipova() se dohvaćaju zip kodovi za one koji se
     * nalaze u bazi podataka. Nakon što se dohvati meteopodaci za spomenute zip
     * kodove, isti se zapisuju u bazu.
     *
     * Također se "podešava" interval obzirom na vrijeme trajanja dohvaćanja
     * podataka.
     */
    private void preuzimanjeZipova() {
        try {
            System.out.println("Preuzimanje zip kodova");
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY HH:mm:ss");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.YYYY");
            Connection con;
            Statement stmt;
            Statement stmt_provjera;
            Statement stmt_upis;
            interval = Integer.parseInt(bpKonfig.getInterval());
            long start = new Date().getTime();
            String putanja = bpKonfig.getServer_database() + bpKonfig.getUserDatabase();
            try {
                Class.forName(bpKonfig.getDriver_database());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Dretva.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(putanja);
            con = DriverManager.getConnection(putanja, bpKonfig.getUserUsername(), bpKonfig.getUserPassword());
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM kgrcevic_zipcodes");


            stmt_provjera = con.createStatement();
            String provjera = "CREATE TABLE IF NOT EXISTS kgrcevic_meteopodaci (zipzahtjevani varchar(11) NOT NULL,zipvraceni varchar(11) NOT NULL,grad varchar(40) NOT NULL,temperatura varchar(20) NOT NULL,vlaga varchar(20) NOT NULL,tlak varchar(30) NOT NULL,vjetar_brzina varchar(20) NOT NULL,vjetar_smjer varchar(20) NOT NULL,vremenska_zona varchar(20) NOT NULL,kisa_stopa varchar(20) NOT NULL,geo_sir varchar(20) NOT NULL,geo_duz varchar(20) NOT NULL,vrijeme_preuzimanja varchar(20) NOT NULL,datum_preuzimanja date NOT NULL)";

            stmt_provjera.execute(provjera);
            stmt_upis = con.createStatement();


            while (rs.next()) {
                WeatherBugKlijent wbk = new WeatherBugKlijent();
                LiveWeatherData lwd = wbk.dajMeteoPodatke(rs.getString(1));
                System.out.println(
                        "Zip(zahtjevani): " + rs.getString(1) + " Zip(vraceni): " + lwd.getZipCode() + " Grad: " + lwd.getCity()
                        + " Temperatura: " + lwd.getTemperature() + " Vlaga: " + lwd.getHumidity() + " Tlak: " + lwd.getPressure()
                        + " Brzina vjetra: " + lwd.getWindSpeed() + " Smjer vjetra: " + lwd.getWindDirection() + " Vremenska zona: " + lwd.getTimeZone()
                        + " Stopa kiše(mm): " + lwd.getRainRate());


                String upis = "INSERT INTO kgrcevic_meteopodaci (zipzahtjevani, zipvraceni,grad,temperatura,vlaga,tlak,vjetar_brzina,vjetar_smjer,vremenska_zona,kisa_stopa, geo_sir, geo_duz, vrijeme_preuzimanja, datum_preuzimanja) VALUES('" + rs.getString(1) + "','" + lwd.getZipCode() + "','" + lwd.getCity() + "','" + lwd.getTemperature() + "','" + lwd.getHumidity() + "','" + lwd.getPressure() + "','" + lwd.getWindSpeed() + "','" + lwd.getWindDirection() + "','" + lwd.getTimeZone() + "','" + lwd.getRainRate() + "','" + lwd.getLatitude() + "','" + lwd.getLongitude() + "','" + sdf.format(new Date().getTime()) + "','" + sdf2.format(new Date()) + "')";
                stmt_upis.execute(upis);

            }
            rs.close();
            con.close();
            stmt.close();
            stmt_provjera.close();
            stmt_upis.close();
            long end = new Date().getTime();
            razlika = end - start;

            if (interval * 1000 >= razlika) {
                try {
                    System.out.println("Dretva spava");
                    System.out.println((interval * 1000) - razlika);
                    this.sleep(((interval * 1000) - razlika) - odrzava);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Dretva.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("Vrijeme spavanja dretve: " + (((interval * 1000) - razlika) - odrzava));
        } catch (SQLException ex) {
            Logger.getLogger(Dretva.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
