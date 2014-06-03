/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans.mailer;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Gustavo Salvo Lara
 */
@Named(value = "sendMail")
@RequestScoped
public class SendMail {

    private String to;
    private String cc;
    private String subject;
    private UploadedFile file;
    private String message;

   

    public List<String> completeText(String to) {
        List<String> aux = new ArrayList<String>();
        aux.add("Hola");
        aux.add("como");
        aux.add("estas");
        aux.add("asdfasdf");
        List<String> results = new ArrayList<String>();
        for (int i = 0; i < aux.size(); i++) {
            if (aux.get(i).contains(to)) {
                results.add(aux.get(i));
            }
            
        }
        return results;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    

}
