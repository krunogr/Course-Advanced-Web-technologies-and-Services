/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kgrcevic.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.wxbug.api.LiveWeatherData;
import org.foi.nwtis.kgrcevic.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.kgrcevic.web.ws.klijenti.WeatherBugKlijent;

/**
 *
 * Dretva koja predstavlja primitivni server te osluškuje port i čeka da se
 * spoji klijent odnosno da pošalje komandu. Nakon što primi komandu, provjerava
 * njezinu ispravnost te vraća odgovor klijentu.
 *
 * @author Kruno
 */
public class PrimitivniServer extends Thread {

    private BP_Konfiguracija bpKonfig;
    private int interval;
    private long razlika = 0;
    public static String komanda = "";
    public static boolean mail = false;
    public static boolean pauza = false;
    long pocetno_vrijeme = new Date().getTime();
    String userKomande = "kruno";
    String passwdKomande = "123456";
    private boolean statusProvjerePodataka;
    Socket klijent;
    Pattern pattern;
    Matcher m;
    boolean status;
    boolean pauzirano = false;
    boolean startano = true;
    private boolean kill = false;
    private String odgovor = "";
    OutputStream os = null;
    String izvrsen = "";
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY HH:mm:ss");
    private long vrijemePocetkaStanja;
    private long trajanjeStanja;
    String vrijeme_izvrsavanja;
    private int brojPrimljenih = 0;
    private int brojNeispravnih = 0;
    private int brojIzvrsenih = 0;

    public PrimitivniServer(BP_Konfiguracija bpKonfig) {
        this.bpKonfig = bpKonfig;
        vrijemePocetkaStanja = new Date().getTime();
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {

        try {
            String port = bpKonfig.getPort();
            System.out.println("Port" + port);
            if (port == null) {
                System.out.println("Nema postavke za port!");
            }


            ServerSocket ss = new ServerSocket(Integer.parseInt(port));

            while (true) {
                if (PokretacDretve.prekiniDretvu) {
                    Thread.currentThread().interrupt();
                    System.out.println("Dretva je prekinuta");
                    break;
                }
                if (kill == true) {
                    break;
                }
                System.out.println("Čekanje na spajanje klijenta");
                klijent = ss.accept();
                obrada(klijent);

            }

        } catch (IOException ex) {
            Logger.getLogger(PrimitivniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * u sljedećoj metodi se obrađuje komanda. Nakon što primitivni poslužitelj
     * primi komandu od klijenta provjerava se njezina ispravnost, njezin
     * sadržaj i provjeravaju se korisnički podaci navedeni u komandi.
     *
     */
    public void obrada(Socket klijent) throws IOException {

        InputStream in = null;
        OutputStream os = null;
        in = klijent.getInputStream();
        os = klijent.getOutputStream();
        StringBuilder sb = new StringBuilder();



        while (true) {
            int i = in.read();
            if (i == -1) {
                break;
            }
            sb.append((char) i);
        }

        komanda = sb.toString().trim();
        System.out.println("ISPIS komande (PRIMITIVAC): " + sb);
        vrijeme_izvrsavanja = sdf.format(new Date().getTime());
        brojPrimljenih++;

        /**
         *
         * ukoliko komanda započinje sa USER i sadrži PASSWD pretpostavlja se
         * kako je komanda administratorska te se kreće u provjeru sintakse.
         * Ukoliko je sa sintaksom sve u redu provjerava se postoji li korisnik
         * sa tim podacima u bazi.
         */
        if (komanda.startsWith("USER") && komanda.contains("PASSWD")) {
            String sintaksa = "^USER ([a-z0-9_-]{3,16}); PASSWD ([a-zA-Z0-9_-]{6,18});( (PAUSE|START|STOP|((ADD ZIP|TEST ZIP) ([0-9]{5})));)?$";
            pattern = Pattern.compile(sintaksa);
            m = pattern.matcher(komanda);
            status = m.matches();
            if (status) {
                userKomande = m.group(1);
                passwdKomande = m.group(2);

                if (m.group(4) == null) {
                    if (provjeraKorisnika(userKomande, passwdKomande)) {
                        odgovor = "OK 10";
                        izvrsen = "true";
                        System.out.println("Autentikacija je uspješna! ODGOVOR ZA KLIJENTA: " + odgovor);
                    } else {
                        odgovor = "OK 30";
                        izvrsen = "true";
                        System.out.println("Pogrešno korisničko ime ili lozinka! ODGOVOR ZA KLIJENTA: " + odgovor);
                    }
                } else {
                    if (provjeraKorisnika(userKomande, passwdKomande)) {
                        String ulaz = (m.group(5) != null) ? m.group(6) : m.group(4);
                        switch (ulaz) {
                            case "PAUSE":
                                if (pauzirano == false) {
                                    Dretva.pauza = true;
                                    pauzirano = true;
                                    startano = false;
                                    odgovor = "OK 10";
                                    izvrsen = "true";
                                    brojIzvrsenih++;
                                    System.out.println("Pauzirano preuzimanje meteo podataka! ODGOVOR ZA KLIJENTA: " + odgovor);
                                    brojIzvrsenih++;
                                } else {
                                    izvrsen = "false";
                                    odgovor = "OK 40";
                                    System.out.println("Dretva je već pauzirana! ODGOVOR ZA KLIJENTA: " + odgovor);
                                }
                                break;
                            case "START":
                                if (startano == false) {
                                    Dretva.pauza = false;
                                    odgovor = "OK 10";
                                    System.out.println("Pokrenuto je preuzimanje meteo podataka! ODGOVOR ZA KLIJENTA:" + odgovor);
                                    startano = true;
                                    pauzirano = false;
                                    izvrsen = "true";
                                    brojIzvrsenih++;
                                } else {
                                    odgovor = "OK 41";
                                    System.out.println("Dretva je već startana! ODGOVOR ZA KLIJENTA: " + odgovor);
                                    izvrsen = "false";
                                }
                                break;
                            case "STOP":
                                if (kill == false) {
                                    Dretva.pauza = true;
                                    kill = true;
                                    odgovor = "OK 10";
                                    System.out.println("Ubijen primitivniServer! ODGOVOR ZA KLIJENTA:" + odgovor);
                                    izvrsen = "true";
                                    brojIzvrsenih++;
                                } else {
                                    odgovor = "OK 42";
                                    System.out.println("Primitivac je već ubijen! ODGOVOR ZA KLIJENTA: " + odgovor);
                                    izvrsen = "false";
                                }
                                break;
                            case "ADD ZIP":
                                dodajZip(Integer.parseInt(m.group(7)));
                                break;
                            case "TEST ZIP":
                                testirajZip(Integer.parseInt(m.group(7)));
                                break;
                        }
                        postaviSadrzajPoruke();
                    } else {
                        odgovor = "OK 43";
                        System.out.println("Neispravno korisničko ime ili lozinka! ODGOVOR ZA KLIJENTA: " + odgovor);
                        izvrsen = "false";
                    }
                }
            } else {
                odgovor = "OK 43";
                System.out.println("Neisprava komanda! ODGOVOR ZA KLIJENTA: " + odgovor);
                izvrsen = "false";
                brojNeispravnih++;
            }

        } /**
         * ukoliko komanda počinje sa USER i sadrži GET ZIP pretpostavlja se
         * kako je to korisnička komanda te se ispituje sintaksa.
         *
         */
        else if (komanda.startsWith("USER") && komanda.contains("GET ZIP")) {
            String sintaksa = "^USER [a-z0-9_-]{3,16}; GET ZIP ([0-9]{5});$";
            pattern = Pattern.compile(sintaksa);
            m = pattern.matcher(komanda);
            status = m.matches();
            if (status) {
                try {
                    Connection con;
                    Statement stmt;
                    String putanja = bpKonfig.getServer_database() + bpKonfig.getUserDatabase();
                    Class.forName(bpKonfig.getDriver_database());
                    con = DriverManager.getConnection(putanja, bpKonfig.getUserUsername(), bpKonfig.getUserPassword());
                    stmt = con.createStatement();
                    String upit = "SELECT * FROM kgrcevic_zipcodes WHERE ZIP='" + m.group(1) + "'";

                    ResultSet rs = stmt.executeQuery(upit);
                    boolean flag = false;
                    String zipKod = "";
                    while (rs.next()) {
                        zipKod = rs.getString("ZIP");
                        flag = true;
                    }
                    if (flag == false) {
                        odgovor = "OK 43";
                        izvrsen = "true";
                    } else {
                        WeatherBugKlijent wbk = new WeatherBugKlijent();
                        LiveWeatherData lwd = wbk.dajMeteoPodatke(zipKod);
                        String temp = "";
                        String vlaga = "";
                        String tlak = "";
                        String geosir = "";
                        String geoduz = "";
                        String vrijednost = lwd.getTemperature();

                        /**
                         *
                         * sljedeći if uvijeti pomažu u pretvaranju dobivenog
                         * oblika podataka u traženi decimalni broj
                         */
                        if (Double.parseDouble(vrijednost) < -9) {
                            temp = vrijednost.substring(0, 6);
                        } else if (Double.parseDouble(vrijednost) > -9 && Double.parseDouble(vrijednost) < 0) {
                            temp = "-0" + vrijednost.substring(1, 5);
                        } else if (Double.parseDouble(vrijednost) > 0 && Double.parseDouble(vrijednost) < 9) {
                            temp = "0" + vrijednost.substring(0, 4);
                        } else if (Double.parseDouble(vrijednost) > 9) {
                            temp = vrijednost;
                            temp = temp.substring(0, 4) + "0";
                        }
                        vlaga = lwd.getHumidity() + ".00";
                        tlak = (Double.parseDouble(vrijednost) < 1000) ? lwd.getPressure() : "0" + lwd.getPressure();
                        geosir = "0" + Double.toString(lwd.getLatitude()).substring(0, 9);
                        geoduz = (lwd.getLongitude() < -100) ? Double.toString(lwd.getLongitude()).substring(0, 8)
                                : ("-0" + Double.toString(lwd.getLongitude()).substring(1, 9));

                        odgovor = "OK 10 " + "TEMP " + temp + " VLAGA " + vlaga + " TLAK "
                                + tlak + " GEOSIR " + geosir + " GEODUZ " + geoduz;
                        izvrsen = "true";
                        brojIzvrsenih++;


                    }
                    stmt.close();
                    con.close();

                } catch (SQLException ex) {
                    Logger.getLogger(Dretva.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Dretva.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                odgovor = "OK 43";
                System.out.println("Neisprava komanda! ODGOVOR ZA KLIJENTA: " + odgovor);
                izvrsen = "false";
                brojNeispravnih++;
            }




        } else {
            odgovor = "OK 43";
            System.out.println("Neisprava komanda! ODGOVOR ZA KLIJENTA: " + odgovor);
            izvrsen = "false";
            brojNeispravnih++;
        }

        vratiOdgovor(odgovor, os);
        upisiUDnevnik(komanda, sdf.format(new Date().getTime()));
        in.close();
    }

    /**
     * postaviSadrzajPoruke se poziva ukoliko je pristigla administratorska
     * komanda. Stvara poruku koja će se poslati mailom. Poziva funkciju za
     * slanje maila.
     *
     */
    public void postaviSadrzajPoruke() {
        String poruka = "";

        trajanjeStanja = new Date().getTime() - vrijemePocetkaStanja;
        vrijemePocetkaStanja = new Date().getTime();
        System.out.println("MAIL: Vrijeme izvršavanja komande: " + vrijeme_izvrsavanja
                + ", Trajanje prethodnog stanja: " + trajanjeStanja + ", Broj primljenih komandi: " + brojPrimljenih
                + ", Broj neispravnih komandi " + brojNeispravnih + ", Broj izvršenih komandi: " + brojIzvrsenih);

        poruka = "Vrijeme izvršavanja komande: " + vrijeme_izvrsavanja
                + ", Trajanje prethodnog stanja: " + trajanjeStanja + ", Broj primljenih komandi: " + brojPrimljenih
                + ", Broj neispravnih komandi " + brojNeispravnih + ", Broj izvršenih komandi: " + brojIzvrsenih;

        saljiMail(poruka);
    }

    /**
     * vratiOdgovor se poziva nakon svake komande te prosljeđuje odgovor
     * klijentu
     *
     */
    public void vratiOdgovor(String poruka, OutputStream os) {
        try {
            os.write(poruka.getBytes());

            os.flush();
            os.close();
        } catch (IOException ex) {
            Logger.getLogger(PrimitivniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.

    }

    /**
     * u sljedećoj funkciji se spaja na bazu te provjerava ispravnost
     * korisničkih podataka
     *
     */
    private boolean provjeraKorisnika(String user, String passwd) {
        try {
            Connection con;
            Statement stmt;
            String putanja = bpKonfig.getServer_database() + bpKonfig.getUserDatabase();
            try {
                Class.forName(bpKonfig.getDriver_database());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Dretva.class.getName()).log(Level.SEVERE, null, ex);
            }

            con = DriverManager.getConnection(putanja, bpKonfig.getUserUsername(), bpKonfig.getUserPassword());
            stmt = con.createStatement();
            String upit = "SELECT user,password FROM kgrcevic_korisnici where " + "user=" + "'" + user + "' AND password=" + "'" + passwd + "'";

            ResultSet rs = stmt.executeQuery(upit);

            statusProvjerePodataka = false;

            while (rs.next()) {

                statusProvjerePodataka = true;
            }
            rs.close();
            con.close();
            stmt.close();


        } catch (SQLException ex) {
            Logger.getLogger(Dretva.class.getName()).log(Level.SEVERE, null, ex);
        }
        return statusProvjerePodataka;

    }

    /**
     *
     * sljedeća funkcija se spaja na bazu i provjerava postoji li već zip kod u
     * bazi. Ukoliko ne postoji dodaje ga.
     */
    public void dodajZip(int zip) {
        try {
            Connection con;
            Statement stmt;
            Statement stmt2;
            String putanja = bpKonfig.getServer_database() + bpKonfig.getUserDatabase();
            Class.forName(bpKonfig.getDriver_database());
            con = DriverManager.getConnection(putanja, bpKonfig.getUserUsername(), bpKonfig.getUserPassword());
            stmt = con.createStatement();
            String dodajZipUpit = "INSERT INTO  kgrcevic_zipcodes (ZIP, korisnik) VALUES('" + zip + "','kruno')";
            stmt2 = con.createStatement();
            String upit = "SELECT zip FROM kgrcevic_zipcodes WHERE ZIP='" + zip + "'";

            ResultSet rs = stmt2.executeQuery(upit);
            boolean flag = false;
            while (rs.next()) {
                odgovor = "OK 42";
                flag = true;
            }
            if (flag == false) {
                odgovor = "OK 10";
                stmt.execute(dodajZipUpit);
                System.out.println("Dodan je novi zip. Zip: " + zip + "ODGOVOR ZA KLIJENTA : " + odgovor);
                brojIzvrsenih++;
                izvrsen = "true";

            } else {
                System.out.println("Zahtjevani zip već postoji! ODGOVOR ZA KLIJENTA : " + odgovor);
                izvrsen = "false";
            }
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Dretva.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Dretva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * spaja se na bazu te provjerava postoji li određeni zip kod u bazi.
     */
    private void testirajZip(int zip) {
        try {
            Connection con;
            Statement stmt;
            String putanja = bpKonfig.getServer_database() + bpKonfig.getUserDatabase();
            Class.forName(bpKonfig.getDriver_database());
            con = DriverManager.getConnection(putanja, bpKonfig.getUserUsername(), bpKonfig.getUserPassword());
            stmt = con.createStatement();
            String upit = "SELECT zip FROM kgrcevic_zipcodes WHERE ZIP='" + zip + "'";

            ResultSet rs = stmt.executeQuery(upit);
            boolean flag = false;
            while (rs.next()) {
                {
                    odgovor = "OK 10";
                    flag = true;
                }
            }
            if (flag == false) {
                odgovor = "OK 44";
                System.out.println("Za " + zip + " se ne preuzimaju meteopodaci ODGOVOR ZA KLIJENTA : " + odgovor);
                brojIzvrsenih++;


            } else {
                System.out.println("Za " + zip + " se preuzimaju meteopodaci ODGOVOR ZA KLIJENTA : " + odgovor);
                brojIzvrsenih++;
            }
            izvrsen = "true";
            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Dretva.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Dretva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * upisiDnevnik se spaja na bazu te zapisuje komandu i njezino vrijeme.
     *
     */
    private void upisiUDnevnik(String komanda, String vrijeme) {
        try {
            Connection con;
            Statement stmt;
            Statement stmt2;

            int brojac = 0;
            String putanja = bpKonfig.getServer_database() + bpKonfig.getUserDatabase();
            Class.forName(bpKonfig.getDriver_database());
            con = DriverManager.getConnection(putanja, bpKonfig.getUserUsername(), bpKonfig.getUserPassword());
            stmt2 = con.createStatement();
            ResultSet rs = stmt2.executeQuery("SELECT * FROM kgrcevic_zahtjevi");

            while (rs.next()) {
                brojac++;
            }
            brojac++;

            stmt = con.createStatement();
            String upis = "INSERT INTO kgrcevic_zahtjevi (id, datum, komanda, izvrsen) VALUES('" + brojac + "','" + vrijeme + "','" + komanda + "','" + izvrsen + "')";
            stmt.execute(upis);

            stmt.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Dretva.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Dretva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * sljedeća funkcija šalje email poruku sadržaja određenog u funkciji
     * postaviSadrzajPoruke. Podatke povlači iz konfiguracijske datoteke.
     */
    public void saljiMail(String poruka) {
        try {

            String primatelj = bpKonfig.getAdresa_primatelj();
            String posiljatelj = bpKonfig.getAdresa_posiljatelj();
            String smtp_server = bpKonfig.getSmtp_server();
            String smtp_port = bpKonfig.getSmtp_port();
            String naslov = bpKonfig.getNaslov_poruke();

            InternetAddress to = new InternetAddress(primatelj);
            InternetAddress from = new InternetAddress(posiljatelj);
            Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", smtp_server);
            properties.setProperty("mail.smtp.port", smtp_port);
            Session session = Session.getDefaultInstance(properties);

            MimeMessage message = new MimeMessage(session);

            message.setSentDate(new Date());
            message.setSender(from);
            message.setFrom(from);
            message.setRecipient(Message.RecipientType.TO, to);
            message.setSubject(naslov);
            message.setContent(poruka, "text/plain;charset=utf-8");

            Transport.send(message);
            System.out.println("Mail poslan");
        } catch (MessagingException ex) {
            System.out.println("Mail nije poslan");
        }
    }
}
