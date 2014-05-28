/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans.mailers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import org.primefaces.event.SelectEvent;
import otherclasses.Email;

/**
 *
 * @author Gustavo Salvo Lara
 */
@Named(value = "emailInbox")
@ApplicationScoped
public class EmailInbox {

    private String portOut = "465";	//puerto que se conecta al servidor de salida SMTP SSL
    private String portIn = "995";	//puerto que se conecta al servidor de entrada POP 
    private String hostOut = "smtp.gmail.com";	//Servidor SMTP de la cuenta
    private String hostIn = "pop.gmail.com";	//Servidor de entrada de la cuenta
    private String from = "sistema.revalora@gmail.com";	//Email remitente del mensaje
    private final String userName = "sistema.revalora@gmail.com";	//Nombre de usuario de la cuenta para enviar email
    private final String password = "ihc12014";	//Contraseña de la cuenta de correo
    private List<Email> emails;
    private Email selectedEmail;
    private String emailContent;

    @PostConstruct
    public void init() {
        emails = new ArrayList<Email>();
        try {
            readMailImap();
        } catch (IOException ex) {
            Logger.getLogger(EmailInbox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Recibe un conjunto de correos electrónico desde una bandeja de entrada
     * dado el protocolo de conexión IMAP. Se imprimen por consola los
     * resultados obtenidos. Falta implementar seguridad.
     *
     * @throws java.io.IOException
     */
    public void readMailImap() throws IOException {
        Properties props = System.getProperties();
        try {
            emails.clear();
            Session sesion = Session.getDefaultInstance(props, null);
            Store store = sesion.getStore("imaps");	//se define el protocolo de acceso
            store.connect(hostIn, userName, password);	//Se realiza la conexión
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
//            se pueden seleccionar la cantidad de mensajes a recuperar
//            System.out.println("La cantidad de mensajes es:" + folder.getMessageCount());
            Message[] msg = folder.getMessages();	//se recuperan los mensajes del servidor
            for (int i = 0; i < msg.length; i++) {
                emailContent = new String();
//                System.out.println("Mensaje " + i + "\n"
//                        + "\tAsunto: " + msg[i].getSubject() + "\n"
//                        + "\tRemitente: " + msg[i].getFrom()[0] + "\n"
//                        + "\tFecha de Envío: " + msg[i].getSentDate() + "\n"
//                        + "\tContenido: " + msg[i].getContent());
                Email email = new Email();
                email.setSubject(msg[i].getSubject());
                email.setFrom(msg[i].getFrom()[0].toString());
                email.setSendDate(msg[i].getSentDate());
                analyzeMessage(msg[i]);
                email.setContent(emailContent);
                emails.add(email);

            }
            folder.close(false);	//como no se realiza ningún cambio a los mensajes es false
            store.close();
            //return receivedEmailList;
        } catch (MessagingException me) {
            System.err.println(me.toString());
            //return null;
        }
    }

    /*
     * Función recursiva que se encarga de analizar el contenido de un email todas
     * partes multipart de un correo. Para cada una de ellas, se debe especificar
     * se debe saber si son text, img, etc (hay que agregar el resto). Además,
     * de acuerdo al formato del texto, no se podrá mostrar correctamente por
     * consola, habrá que mostrarlo en un jFrame o cuando ya la aplicación
     * esté lista
     */
    public void analyzeMessage(Part part) {
        try {
            // Si es multipart, se analiza cada una de sus partes recursivamente.
            if (part.isMimeType("multipart/*")) {
                Multipart multipart;
                multipart = (Multipart) part.getContent();
                for (int j = 0; j < multipart.getCount(); j++) {
                    analyzeMessage(multipart.getBodyPart(j));
                }
            } else {
                //Si part es texto, se escribe
                if (part.isMimeType("text/*")) {
                    emailContent += part.getContent();
                    //System.out.println("---------------------------------");
                } else {
                    // Si es imagen, se guarda en fichero y se visualiza en JFrame
                    if (part.isMimeType("image/*")) {
                        emailContent += part.getContentType();
                        emailContent += part.getFileName();
                        //System.out.println("---------------------------------");
                    } else {
                        // Si no es ninguna de las anteriores, escribimos el tipo
                        emailContent +=  part.getContentType() ;
                        //System.out.println("---------------------------------");
                    }
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void refresh() {
        try {
            readMailImap();
        } catch (IOException ex) {
            Logger.getLogger(EmailInbox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public Email getSelectedEmail() {
        return selectedEmail;
    }

    public void setSelectedEmail(Email selectedEmail) {
        this.selectedEmail = selectedEmail;
    }

}
