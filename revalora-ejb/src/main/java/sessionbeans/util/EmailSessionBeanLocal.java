/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sessionbeans.util;

import entities.Contact;
import entities.ContactGroup;
import java.util.List;
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
    
    public String getUserName();
    
    public void setUserName(String userName);
    
    public String getPassword();
    
    public void setPassword(String password);
    
    public boolean isAuthenticated();
    
    public void saveMailToDraft(String subject, String body, List<Contact> contacts, List<ContactGroup> groups);
}
