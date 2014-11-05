/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kgrcevic.ejb.sb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Kruno
 */
@Stateless
@LocalBean
public class KlijentServisa {

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8040/tlandeka_aplikacija_1/ServisTomo.wsdl")
    private ServisTomo_Service service;
}
