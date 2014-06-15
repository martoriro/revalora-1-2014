/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbeans.mailer;

import entities.Contact;
import entities.ContactGroup;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import managedbeans.util.JsfUtil;
import managedbeans.util.SessionUtil;
import otherclasses.Email;
import sessionbeans.util.EmailSessionBeanLocal;

/**
 *
 * @author sylar
 */
@Named(value = "emailController")
@SessionScoped
public class EmailController implements Serializable {

    private Email selected;
    
    @EJB
    private EmailSessionBeanLocal emailSessionBean;
    
    @Inject
    private SessionUtil sessionUtil;
    
    @Inject
    private EmailInbox emailInbox;
    
    public EmailController() {
    }

    public Email getSelected() {
        return selected;
    }

    public void setSelected(Email selected) {
        this.selected = selected;
    }
    
    public Email prepareCreate() {
        selected = new Email();
        return selected;
    }
    
    public void createSendSurvey(List<Contact> contacts, List<ContactGroup> cGroups, String subject, String content) {
        selected = prepareCreate();
        selected.setReceiverContactGroups(cGroups);
        selected.setReceiverContacts(contacts);
        selected.setSubject(subject);
        selected.setContent(content);
        create();
    }
    
    public void create() {
        try {
            createEmails();
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(EmailController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createEmails() throws MessagingException, IOException{
        send(ResourceBundle.getBundle("/Bundle").getString("MessageCreated"));
    }
    
    private void send(String successMessage) throws MessagingException, IOException{
        List<Contact> recipientContacts = selected.getReceiverContacts();
        List<ContactGroup> recipientGroups = selected.getReceiverContactGroups();
        
        if(recipientContacts!=null){
            for (int i = 0; i < recipientContacts.size(); i++) {
                sendMessage(recipientContacts.get(i).getEmail(),
                        selected.getSubject(),
                        selected.getContent(),
                        successMessage);
            }
        }
        
        if(recipientGroups!=null){
            for (int i = 0; i < recipientGroups.size(); i++) {
                List<Contact> tempContacts = recipientGroups.get(i).getContacts();
                for (int j = 0; j < tempContacts.size(); j++) {
                        sendMessage(tempContacts.get(i).getEmail(),
                        selected.getSubject(),
                        selected.getContent(),
                        successMessage);
                    
                }
            }
        }
    }

    public void sendMessage(String recipient, String subject, String content, String successMessage) throws IOException{
        
        try {
            emailSessionBean.sendMail(recipient,"","","", subject, content);
            JsfUtil.addSuccessMessage(successMessage);
        }catch(MessagingException ex){
            JsfUtil.addErrorMessage("Error al enviar el mensaje, intentelo más tarde");
        }
    }
     
    public void confirmSetingsAndGo(String destiny) {
        if (checkSettings()) {
            JsfUtil.redirect(destiny);
        } else {
            JsfUtil.redirect("/faces/roles/ProfileConfigEmail.xhtml");
        }        
    }
    
    public boolean checkSettings() {
        String email = sessionUtil.getCurrentUser().getEmail();
        String emailPass = sessionUtil.getCurrentUser().getEmailPassword();
        if ((email!=null) && (emailPass != null)) {
            Boolean go = !email.contentEquals("") && !emailPass.contentEquals(""); 
            if (go) {
                emailInbox.setUserName();
                emailInbox.setPassword();
                return go;  
            }            
        }
        return false;
    }    
}
