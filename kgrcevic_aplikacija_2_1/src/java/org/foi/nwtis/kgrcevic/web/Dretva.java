/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kgrcevic.web;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.mail.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.foi.nwtis.jms.PorukaZaJMS;
import org.foi.nwtis.kgrcevic.konfiguracije.bp.BP_Konfiguracija;

/**
 * Klasa koja obrađuje i razvrstava mail poruke po folderima.
 *
 * @author Kruno
 */
public class Dretva extends Thread {

    static BP_Konfiguracija bpKonfig;
    private int nwtisPoruke = 0;
    private int neNwtisPoruke = 0;
    private int ukupno = 0;
    String sadrzajPoruke = "";
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY HH:mm:ss");
    private static List<String> listaFoldera = new ArrayList<String>();
    private static String defaultFolder;
    long end;
    long start;

    Dretva(BP_Konfiguracija bpKonfig) {
        this.bpKonfig = bpKonfig;

    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * run() je glavna metoda. Razvrstava mail poruke po folderima.
     *
     */
    @Override
    public void run() {
        Session session = null;
        Store store = null;
        Folder folder = null;
        Message message = null;
        Message[] messages = null;
        Object messagecontentObject = null;
        String sender = null;
        String subject = null;
        Multipart multipart = null;
        Part part = null;
        String contentType = null;



        while (true) {
            if (PokretacDretve.prekiniDretvu) {
                Thread.currentThread().interrupt();
                System.out.println("Dretva je prekinuta");
                break;
            }
            try {
                start = new Date().getTime();
                System.out.println("provjera poruka");



                session = Session.getDefaultInstance(System.getProperties(), null);
                store = session.getStore("imap");

                store.connect(bpKonfig.getSmtp_server(), bpKonfig.getAdresa_primatelj(), bpKonfig.getLozinka());
                folder = store.getDefaultFolder();
                folder = folder.getFolder("inbox");
                folder.open(Folder.READ_WRITE);
                messages = folder.getMessages();

                Folder[] folders = store.getDefaultFolder().list();

                listaFoldera = new ArrayList<String>();
                for (Folder f : folders) {
                    if (!((f.getName().equals("NWTiS_IspravnePoruke"))
                            || (f.getName().equals("NWTiS_NespravnePoruke"))
                            || (f.getName().equals("NWTiS_OstalePoruke")))) {
                        listaFoldera.add(f.getName());
                    }
                }
                defaultFolder = store.getDefaultFolder().getName();

                for (int messageNumber = 0; messageNumber < messages.length; messageNumber++) {
                    ukupno++;

                    message = messages[messageNumber];

                    String tipPoruke = message.getContentType();
                    System.out.println("Tip poruke: " + tipPoruke);


                    if (tipPoruke.startsWith("TEXT/PLAIN")) {
                        if (provjeraIspravnosti(message.getSubject())) {

                            prebaciPoruku(bpKonfig.getDirektorij_nwtis(), store, message, folder);
                            System.out.println("Premješteno u direktorij za NWTiS poruke");
                            nwtisPoruke++;
                        } else {
                            prebaciPoruku(bpKonfig.getDirektorij_neNwtis(), store, message, folder);
                            System.out.println("Premješteno u direktorij za neNWTiS poruke zbog krivog naslova");
                            System.out.println(message.getSubject() + " " + message.getSubject().toString());
                            neNwtisPoruke++;

                        }
                    } else {
                        prebaciPoruku(bpKonfig.getDirektorij_neNwtis(), store, message, folder);
                        System.out.println("Premješteno u direktorij za neNWTiS poruke zbog krivog formata ");
                        neNwtisPoruke++;
                    }

                }


                folder.close(true);

                store.close();
                end = new Date().getTime();
                long razlika = end - start;
                if (Integer.parseInt(bpKonfig.getInterval_mail()) * 1000 >= razlika) {
                    try {
                        System.out.println("Dretva spava " + ((Integer.parseInt(bpKonfig.getInterval_mail()) * 1000) - razlika));
                        this.sleep((Integer.parseInt(bpKonfig.getInterval_mail()) * 1000) - razlika);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Dretva.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } catch (MessagingException ex) {
                Logger.getLogger(Dretva.class.getName()).log(Level.SEVERE, null, ex);
            }
            sadrzajPoruke = "Vrijeme početka obrade: " + sdf.format(start) + ", vrijeme završetka obrade: "
                    + sdf.format(end) + ", broj NWTiS poruka: " + nwtisPoruke + ", broj neNWTiS poruka: "
                    + neNwtisPoruke + ", broj obrađenih poruka: " + ukupno;

            PorukaZaJMS porukazaJMS = new PorukaZaJMS(nwtisPoruke, neNwtisPoruke, ukupno, sdf.format(start), sdf.format(end));
            try {
                sendJMSMessageToNWTiS_kgrcevic_1(porukazaJMS);
                System.out.println("Poslano");
            } catch (NamingException ex) {
                System.out.println("Nije poslano");
            } catch (JMSException ex) {
                System.out.println("Nije poslano");
            }
        }
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * U sljedećoj metodi se provjerava odgovara li naslov poruke onom naslovu
     * iz kongif datoteke.
     */
    private boolean provjeraIspravnosti(String naslov) {
        if (naslov.equals(bpKonfig.getNaslov_poruke())) {
            return true;
        } else {
            return false;
        }

    }

    /**
     *
     * Metoda za premještaj poruke u odgovarajući folder.
     */
    private void prebaciPoruku(String odrediste, Store store, Message message, Folder folder) throws MessagingException {
        Folder noviFolder;
        noviFolder = store.getFolder(odrediste);
        if (!noviFolder.exists()) {
            noviFolder.create(Folder.HOLDS_MESSAGES);
        }
        noviFolder.open(Folder.READ_WRITE);
        System.out.println("Premješteno u: " + odrediste);

        Message[] poruke = new Message[1];
        poruke[0] = message;
        folder.copyMessages(poruke, noviFolder);

        noviFolder.close(false);
        message.setFlag(Flags.Flag.DELETED, true);
    }

    public static BP_Konfiguracija getBpKonfig() {
        return bpKonfig;
    }

    public static String getDefaultFolder() {
        return defaultFolder;
    }

    public static void setDefaultFolder(String defaultFolder) {
        Dretva.defaultFolder = defaultFolder;
    }

    public static List<String> getListaFoldera() {
        return listaFoldera;
    }

    public static void setListaFoldera(List<String> listaFoldera) {
        Dretva.listaFoldera = listaFoldera;
    }

    private javax.jms.Message createJMSMessageForjmsNWTiS_kgrcevic_1(javax.jms.Session session, PorukaZaJMS poruka) throws JMSException {
        // TODO create and populate message to send
        ObjectMessage om = session.createObjectMessage();
        om.setObject(poruka);
        return om;
    }

    private void sendJMSMessageToNWTiS_kgrcevic_1(PorukaZaJMS poruka) throws NamingException, JMSException {
        Context c = new InitialContext();
        ConnectionFactory cf = (ConnectionFactory) c.lookup("java:comp/env/jms/NWTiS_QF_kgrcevic_1");
        Connection conn = null;
        javax.jms.Session s = null;
        try {
            conn = cf.createConnection();
            s = conn.createSession(false, s.AUTO_ACKNOWLEDGE);
            Destination destination = (Destination) c.lookup("java:comp/env/jms/NWTiS_kgrcevic_1");
            MessageProducer mp = s.createProducer(destination);
            mp.send(createJMSMessageForjmsNWTiS_kgrcevic_1(s, poruka));
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (JMSException e) {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot close session", e);
                }
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}
