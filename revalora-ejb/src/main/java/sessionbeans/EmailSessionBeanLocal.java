/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sessionbeans;

import java.io.IOException;
import java.util.List;
import javax.ejb.Local;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import otherclasses.Email;

/**
 *
 * @author sylar
 */
@Local
public interface EmailSessionBeanLocal {
    public int sendMail(String to,String carbonCopy, 
            String BlindCarbonCopy, 
            String attached, 
            String subject, 
            String body) throws AddressException, MessagingException;
    
    public int readMailPop();
    
    public List<Email> readMailImap() throws IOException;
}
