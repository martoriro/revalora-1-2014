/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sessionbeans.util;

import javax.ejb.LocalBean;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

/**
 *
 * @author sylar
 */
@LocalBean
public interface EmailSessionBeanLocal {
    public int sendMail(String to, 
                        String carbonCopy, 
                        String BlindCarbonCopy, 
                        String attached, 
                        String subject, 
                        String body)
            throws AddressException, MessagingException;
    
    public boolean isAuthenticated(); 
}
