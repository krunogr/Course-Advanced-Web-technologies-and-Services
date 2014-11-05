/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kgrcevic.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.kgrcevic.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.kgrcevic.web.*;

/**
 *
 *
 *
 * Servlet koji ima nekoliko različitih urlova te zaprima zahtjev i prosljeđuje
 * korisnika na određenu stranicu. Također, kontrolira upitane podatke te se
 * spaja na bazu i provjerava korisnika.
 *
 * @author Kruno
 */
@WebServlet(name = "Kontroler", urlPatterns = {"/Kontroler", "/Dokumentacija", "/PregledDnevnika", "/PregledMeteoPodataka", "/PregledZahtjeva", "/Index", "/PrijavaKorisnika", "/ProvjeraKorisnika", "/OdjavaKorisnika"})
public class Kontroler extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String zahtjev = request.getServletPath();
        String odrediste = null;
        HttpSession sesija = null;
        switch (zahtjev) {
            case "/PregledDnevnika":
                odrediste = "/zasticeno/pregledDnevnika.jsp";
                break;
            case "/ProvjeraKorisnika":
                System.out.println("aaaaaaaaaaaaaaaaaaaaaaa");
                String korIme = request.getParameter("j_username");
                String lozinka = request.getParameter("j_password");
                if (korIme == null || korIme.trim().length() == 0
                        || lozinka == null || lozinka.trim().length() == 0) {
                    throw new NeuspjesnaPrijava_2("Nije uneseno kor. ime ili lozinka");
                } else if (!provjeraKorisnika(korIme, lozinka)) {
                    throw new NeuspjesnaPrijava_1("Neispravno kor. ime ili lozinka");
                } else {
                    sesija = request.getSession();

                    Korisnik korisnik = new Korisnik(korIme, "Grcevic", "Kruno",
                            request.getRemoteAddr(), sesija.getId(), 1);

                    sesija.setAttribute("korisnik", korisnik);


                    odrediste = "/zasticeno/index.jsp";
                }
                break;
            case "/PrijavaKorisnika":
                odrediste = "/login.jsp";
                break;
            case "/PregledMeteoPodataka":
                odrediste = "/zasticeno/pregledMeteoPodataka.jsp";
                break;
            case "/PregledZahtjeva":
                odrediste = "/zasticeno/pregledZahtjeva.jsp";
                break;
            case "/Index":
                odrediste = "/zasticeno/index.jsp";
                break;
            case "/Dokumentacija":
                odrediste = "/zasticeno/dokumentacija.html";
                break;
            case "/OdjavaKorisnika":
                odrediste = "/odjavaKorisnika.jsp";
                break;
            default:
                ServletException up = new ServletException("Nepoznat zahtjev");
                throw up;
        }
        String kontekst = request.getContextPath();
        RequestDispatcher rd = this.getServletContext().getRequestDispatcher(odrediste);
        rd.forward(request, response);

    }

    /**
     * u sljedećoj funkciji se spaja na bazu te provjerava ispravnost
     * korisničkih podataka
     *
     */
    private boolean provjeraKorisnika(String user, String passwd) {
        boolean statusProvjerePodataka = false;
        try {
            BP_Konfiguracija bpKonfig = ((BP_Konfiguracija) this.getServletContext().getAttribute("BP_Konfiguracija"));
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

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
