/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbeans.mailer;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import otherclasses.Email;

/**
 *
 * @author Gustavo Salvo Lara
 */
@Named(value = "readMail")
@RequestScoped
public class ReadMail {
    
    @ManagedProperty(value ="#{emailInbox.selectedEmails}")
    private List<Email> selectedEmail;
    @ManagedProperty(value ="#{emailInbox.titleFolder}" )
    private String subject;
    private String from;
    private String sendDate;
    private String content;
    
    public void reading(SelectEvent event){
        
        
        System.out.println(subject);
//        Email myEmail = selectedEmail.get(0);
//        this.setContent(myEmail.getContent());
//        this.setSubject(myEmail.getSubject());
//        this.setFrom(myEmail.getFrom());
//        this.setSendDate(myEmail.getDfDafault().format(myEmail.getSendDate()));
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Email> getSelectedEmail() {
        return selectedEmail;
    }

    public void setSelectedEmail(List<Email> selectedEmail) {
        this.selectedEmail = selectedEmail;
    }
    
    
    
}
