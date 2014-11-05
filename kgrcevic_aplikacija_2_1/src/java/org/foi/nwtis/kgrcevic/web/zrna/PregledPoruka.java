/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kgrcevic.web.zrna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.mail.AuthenticationFailedException;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.FolderNotFoundException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.ReadOnlyFolderException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.StoreClosedException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.foi.nwtis.kgrcevic.konfiguracije.Konfiguracija;
import org.foi.nwtis.kgrcevic.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.kgrcevic.web.Dretva;
import org.foi.nwtis.kgrcevic.web.Poruka;
import org.foi.nwtis.kgrcevic.web.slusaci.SlusacAplikacije;

/**
 * Zrno za prikaz svih mail poruka obzirom na foldere.
 *
 * @author Kruno
 */
@ManagedBean
@SessionScoped
public class PregledPoruka {

    private String emailposluzitelj;
    private String user;
    private String pass;
    public static  List<Poruka> listaPoruka = new ArrayList<Poruka>();
    private String porukaID;
    private Poruka poruka;
    private static List<String> listaFoldera = new ArrayList<String>();
    private String odabranFolder = Dretva.getDefaultFolder();
    private boolean first;
    private String trenutniFolder = "";
    private int brojPoruka;
    private int kreniOd;
    public static Poruka odabranaPoruka;

    public PregledPoruka() {

        BP_Konfiguracija bpkonfig = SlusacAplikacije.getBpKonfig();
        emailposluzitelj = bpkonfig.getSmtp_server();
        user = bpkonfig.getAdresa_primatelj();
        pass = bpkonfig.getLozinka();
        listaFoldera = Dretva.getListaFoldera();
        odabranFolder = Dretva.getDefaultFolder();;
        brojPoruka = Integer.parseInt(bpkonfig.getBrojPoruka());
        first = true;

    }

    /**
     * Sljedeća metoda se poziva nakon svake akcije kako bi se prikaz poruka
     * osvježio.
     *
     */
    private void downloadMessages() {
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

        listaPoruka = new ArrayList<Poruka>();
        try {
            session = Session.getDefaultInstance(System.getProperties(), null);

            store = session.getStore("imap");

            store.connect(emailposluzitelj, user, pass);

            // Get a handle on the default folder
            folder = store.getDefaultFolder();

            if (!trenutniFolder.equals(odabranFolder)) {
                first = true;
            }

            if (odabranFolder == null || odabranFolder.equals("")) {
                odabranFolder = "inbox";
            }

            // Retrieve the "Inbox"
            folder = folder.getFolder(odabranFolder);

            trenutniFolder = odabranFolder;

            //Reading the Email Index in Read Mode
            folder.open(Folder.READ_ONLY);

            // Retrieve the messages
            messages = folder.getMessages();

            int broj;
            if (first || (kreniOd > messages.length - 1)) {
                kreniOd = messages.length - 1;
            }

            broj = kreniOd - brojPoruka;
            if (broj < 0) {
                broj = 0;
            }

            // Loop over all of the messages
            for (int messageNumber = kreniOd; messageNumber > broj; messageNumber--) {
                // Retrieve the next message to be read
                message = messages[messageNumber];

                // Retrieve the message content
                messagecontentObject = message.getContent();

                sender = ((InternetAddress) message.getFrom()[0]).getPersonal();

                // If the "personal" information has no entry, check the address for the sender information

                if (sender == null) {
                    sender = ((InternetAddress) message.getFrom()[0]).getAddress();
                }

                // Get the subject information
                subject = message.getSubject();

                String[] zaglavlje = message.getHeader("Message-ID");
                String messId = "";
                if (zaglavlje != null && zaglavlje.length > 0) {
                    messId = zaglavlje[0];
                }
                String sadrzaj = message.getContent().toString().replace("\n", "<br />");
                Poruka poruka = new Poruka(messId, message.getSentDate(), sender, subject, message.getContentType(),
                        sadrzaj, message.getFlags(), false, false);

                listaPoruka.add(poruka);

            }
            // Close the folder
            folder.close(true);

            // Close the message store
            store.close();
        } catch (AuthenticationFailedException e) {
            e.printStackTrace();
        } catch (FolderClosedException e) {
            e.printStackTrace();
        } catch (FolderNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (ReadOnlyFolderException e) {
            e.printStackTrace();
        } catch (StoreClosedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String naprijed() {
        int a = kreniOd - brojPoruka;
        if (a > 0) {
            setKreniOd(a);
        }
        first = false;
        System.out.println("Napred : " + a);
        return "";
    }

    public String nazad() {
        int a = kreniOd + brojPoruka;
        setKreniOd(a);
        System.out.println("Nazad : " + a);
        return "";
    }

    public String pregledPoruke2() {
        return "OK";
    }

    public String pocetniFolder() {
        return "";
    }

    public String getEmailposluzitelj() {
        return emailposluzitelj;
    }

    public void setEmailposluzitelj(String emailposluzitelj) {
        this.emailposluzitelj = emailposluzitelj;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public List<Poruka> getListaPoruka() {
        downloadMessages();
        return listaPoruka;
    }

    public void setListaPoruka(List<Poruka> listaPoruka) {
        this.listaPoruka = listaPoruka;
    }

    public String getPorukaID() {
        return porukaID;
    }

    public void setPorukaID(String porukaID) {
        for (Poruka p : listaPoruka) {
            if (p.getId().equals(porukaID)) {
                odabranaPoruka = p;
            }
        }
        System.out.println("Poruka ID: " + odabranaPoruka.getId());
        this.porukaID = porukaID;
    }

    /**
     * brisiPoruku se poziva kada želimo obrisati neku poruku te poziva funkciju
     * za brisanje poruke.
     *
     */
    public String brisiPoruku() {
        for (Poruka p : listaPoruka) {
            if (p.getId().equals(porukaID)) {
                deleteMailMessage();
                downloadMessages();
            }
        }
        return "";
    }

    public Poruka getPoruka() {
        return poruka;
    }

    public void setPoruka(Poruka poruka) {
        this.poruka = poruka;
    }

    public List<String> getListaFoldera() {
        return listaFoldera;
    }

    public void setListaFoldera(List<String> listaFoldera) {
        this.listaFoldera = listaFoldera;
    }

    public String getPocetniFolder() {
        return odabranFolder;
    }

    public void setPocetniFolder(String pocetniFolder) {
        this.odabranFolder = pocetniFolder;
    }

    public int getBrojPoruka() {
        return brojPoruka;
    }

    public void setBrojPoruka(int brojPoruka) {
        this.brojPoruka = brojPoruka;
    }

    public int getKreniOd() {
        return kreniOd;
    }

    public void setKreniOd(int kreniOd) {
        this.kreniOd = kreniOd;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public String getOdabranFolder() {
        return odabranFolder;
    }

    public void setOdabranFolder(String odabranFolder) {
        this.odabranFolder = odabranFolder;
    }

    public String getTrenutniFolder() {
        return trenutniFolder;
    }

    public void setTrenutniFolder(String trenutniFolder) {
        this.trenutniFolder = trenutniFolder;
    }

    public String odabranFolder() {
        return "";
    }

    public Poruka getOdabranaPoruka() {
        return odabranaPoruka;
    }

    public void setOdabranaPoruka(Poruka odabranaPoruka) {
        this.odabranaPoruka = odabranaPoruka;
    }

    /**
     *
     * Sljedeća metoda briše poruku iz foldera.
     */
    public void deleteMailMessage() {
        try {
            java.util.Properties properties = System.getProperties();
            properties.put("mail.smtp.host", emailposluzitelj);
            Session session =
                    Session.getInstance(properties, null);

            Store store = session.getStore("imap");
            store.connect(emailposluzitelj, user, pass);
            Folder folder = store.getFolder(trenutniFolder);

            folder.open(Folder.READ_WRITE);
            Message[] messages = folder.getMessages();
            Message message = null;
            for (int messageNumber = 0; messageNumber < messages.length; messageNumber++) {
                message = messages[messageNumber];
                String[] zaglavlje = message.getHeader("Message-ID");
                String messId = "";
                if (zaglavlje != null && zaglavlje.length > 0) {
                    messId = zaglavlje[0];
                }
                if (messId.equals(porukaID)) {
                    message.setFlag(Flags.Flag.DELETED, true);
                }
            }
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (SendFailedException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
