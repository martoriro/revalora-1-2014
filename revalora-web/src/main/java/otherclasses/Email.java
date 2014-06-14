/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package otherclasses;

import entities.Contact;
import entities.ContactGroup;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author sylar
 */
public class Email {
    private String idMessage;
    private String subject;
    private String from;
    private List<Contact> receiverContacts;
    private List<ContactGroup> receiverContactGroups;
    private String content;
    private Date sendDate;
    private DateFormat dfDafault = DateFormat.getInstance();
    
    private boolean seen;
    private boolean delete;
    
    public DateFormat getDfDafault() {
        return dfDafault;
    }

    public void setDfDafault(DateFormat dfDafault) {
        this.dfDafault = dfDafault;
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

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Contact> getReceiverContacts() {
        return receiverContacts;
    }

    public void setReceiverContacts(List<Contact> receiverContacts) {
        this.receiverContacts = receiverContacts;
    }

    public List<ContactGroup> getReceiverContactGroups() {
        return receiverContactGroups;
    }

    public void setReceiverContactGroups(List<ContactGroup> receiverContactGroups) {
        this.receiverContactGroups = receiverContactGroups;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public String getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(String idMessage) {
        this.idMessage = idMessage;
    }
    
   
    
}
