/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kgrcevic.mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import org.foi.nwtis.jms.PorukaZaJMS;
import org.foi.nwtis.kgrcevic.sb.SessionBean;

/**
 * Klasa za primanje JMS poruka.
 * @author Kruno
 */
@MessageDriven(mappedName = "jms/NWTiS_kgrcevic_1", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class PrimanjeJMS implements MessageListener {
    
    public PrimanjeJMS() {
    }
    
       @Override
    public void onMessage(Message message) {
        ObjectMessage msg = null;

        if (message instanceof ObjectMessage) {
            try {
                msg = (ObjectMessage) message;
                System.out.println("Stigla MailMessage poruka: " + message.getJMSMessageID() + " " + new java.util.Date(message.getJMSTimestamp()));
                PorukaZaJMS poruka = (PorukaZaJMS) msg.getObject();
                if (poruka != null) {
                    SessionBean.setPoruka(poruka);
                    System.out.println("JMS stigao");
                } else {
                    System.out.println("MailJMS je null");
                }
            } catch (JMSException ex) {
                ex.printStackTrace();
            }
        }
    }
}
