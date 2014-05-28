/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package otherclasses;

import java.text.DateFormat;
import java.util.Date;

public class Email {
    
    private String subject;
    private String from;
    private Date sendDate;
    private String content;
    private DateFormat dfDafault = DateFormat.getInstance();

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
}
