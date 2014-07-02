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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import managedbeans.util.JsfUtil;
import managedbeans.util.SessionUtil;
import org.primefaces.component.autocomplete.AutoComplete;
import otherclasses.Email;
import sessionbeans.util.EmailSessionBeanLocal;

/**
 *
 * @author sylar
 */
@Named(value = "emailController")
@SessionScoped
public class EmailController implements Serializable {

    private Email selected,draft;
    private List<Contact> contacts = new ArrayList<>();
    private List<ContactGroup> groups = new ArrayList<>();
    private boolean disableDraft = false;
    
    @EJB
    private EmailSessionBeanLocal emailSessionBean;
    
    @Inject
    private SessionUtil sessionUtil;
    
    @Inject
    private EmailInbox emailInbox;
    
    public EmailController() {
    }

    public Email getDraft() {
        return draft;
    }

    public void setDraft(Email draft) {
        this.draft = draft;
    }

    public Email getSelected() {
        return selected;
    }

    public void setSelected(Email selected) {
        this.selected = selected;
    }
    
    public Email prepareCreate() {
        emailSessionBean.setUserName(sessionUtil.getCurrentUser().getEmail());
        emailSessionBean.setPassword(sessionUtil.getCurrentUser().getEmailPassword());
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
    
    public Email prepareCreateRevalora() {
        emailSessionBean.setUserName("sistema.revalora@gmail.com");
        emailSessionBean.setPassword("ihc12014");
        selected = new Email();
        return selected;
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
        //Para que no se guarde en borradores
        disableDraft = true;
        
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
        //Reinicializamos valores
        contacts = new ArrayList<>();
        groups = new ArrayList<>();
        selected = new Email();
        disableDraft = false;
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

    public void handleSelectedContact(AjaxBehaviorEvent theEvent) {
        AutoComplete autoComplete = (AutoComplete) theEvent.getComponent();
        contacts = (List<Contact>) autoComplete.getValue();
    }
    
    public void handleSelectedGroup(AjaxBehaviorEvent theEvent) {
        AutoComplete autoComplete = (AutoComplete) theEvent.getComponent();
        groups = (List<ContactGroup>) autoComplete.getValue();
    }    
    
    public void deactivateDraft(){
        disableDraft = true;
    }
    
    public void activateDraft(){
        disableDraft = false;
    }
    
    public void saveToDraft() {
        try {
            //Esperamos por si la ventana se cerró al apretar enviar.
            //Si es así, disableDraft será true, y se guardará como borrador
            TimeUnit.SECONDS.sleep(2);
            System.out.println("desactivar draft: "+disableDraft);
            if(!disableDraft){
                if(!selected.getSubject().isEmpty() || !selected.getContent().isEmpty() || contacts.isEmpty() || !groups.isEmpty()){
                    emailSessionBean.saveMailToDraft(selected.getSubject(), selected.getContent(), contacts, groups);
                    //Reinicializamos valores
                    contacts = new ArrayList<>();
                    groups = new ArrayList<>();
                    selected = new Email();
                    JsfUtil.addSuccessMessage("Se ha guardado el mensaje como borrador");
                }
            }
            contacts = new ArrayList<>();
            groups = new ArrayList<>();
            selected = new Email();
        }catch(InterruptedException ex){
            JsfUtil.addErrorMessage("Error al guardar como borrador, intentelo más tarde");
        }
    }
}
