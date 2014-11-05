/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kgrcevic.web;

import javax.servlet.ServletException;

/**
 * Sljedeća iznimka se javlja kada korisnik ne upiše ispravno korisničko ime ili
 * lozinku.
 *
 * @author Kruno
 *
 */
public class NeuspjesnaPrijava_1 extends ServletException {

    /**
     * Creates a new instance of
     * <code>NeuspjesnaPrijava</code> without detail message.
     */
    public NeuspjesnaPrijava_1() {
    }

    /**
     * Constructs an instance of
     * <code>NeuspjesnaPrijava</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public NeuspjesnaPrijava_1(String msg) {
        super("NWTiS: " + msg);
    }
}
