/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kgrcevic.web;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.foi.nwtis.kgrcevic.konfiguracije.bp.BP_Konfiguracija;

/**
 * Servlet koji pokreće dretvu.
 * 
 * @author Kruno
 */
@WebServlet(name = "PokretacDretve", urlPatterns = {"/PokretacDretve"}, loadOnStartup=1)
public class PokretacDretve extends HttpServlet {

     private Dretva dretva; 
    public static boolean prekiniDretvu = false;
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
        response.setContentType("text/html;charset=UTF-8");

    }

    @Override
    public void destroy() {
        if (dretva != null) {
            dretva.interrupt();
            dretva = null;
         prekiniDretvu=true;
        }
       

        super.destroy();
    }
    
     

    /**
     * init() dohvaća atribut, pokreće dretve i prosljeđuje im bpKonfig.
     *
     */
    @Override
    public void init() throws ServletException {

        BP_Konfiguracija bpKonfig = ((BP_Konfiguracija) this.getServletContext().getAttribute("BP_Konfiguracija"));
        dretva = new Dretva(bpKonfig);
        dretva.start();


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
