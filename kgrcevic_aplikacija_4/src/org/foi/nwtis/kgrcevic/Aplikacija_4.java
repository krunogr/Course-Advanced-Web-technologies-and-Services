/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kgrcevic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author Kruno
 */
public class Aplikacija_4 {

    public static void main(String[] args) throws UnknownHostException {
        if (args.length == 0) {
            System.out.println("Nedovoljan broj argumenata");
        } else {




            String adresaSocketServera = "127.0.0.1";
            int portSocketServera = 8555;
            String komanda = "";
            for (int i = 0; i < args.length; i++) {
                komanda += args[i] + " ";
            }
            System.out.println(komanda);
            try {
                Socket socket = new Socket(adresaSocketServera, portSocketServera);
                InputStream inStream = socket.getInputStream();
                OutputStream outStream = socket.getOutputStream();

                outStream.write(komanda.getBytes());
                outStream.flush();

                socket.shutdownOutput();


                //objekt StringBuilder koja će čitati odgovor (rezultat koji vrati server) servera znak po znak
                StringBuilder odgovor = new StringBuilder();
                boolean provjera = false;
                while (true) {
                    int znak = inStream.read();

                    if (znak == -1) {
                         break;
                    }
                    odgovor.append((char) znak);
                    
                }
                System.out.println("odgovor: " + odgovor);




            } catch (IOException ex) {
                System.out.println("ERROR");
            }

        }
    }
}
