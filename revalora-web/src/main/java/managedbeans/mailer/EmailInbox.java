/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans.mailer;

import com.sun.mail.imap.IMAPFolder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.AuthenticationFailedException;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import managedbeans.util.JsfUtil;
import managedbeans.util.SessionUtil;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import otherclasses.Email;

/**
 *
 * @author Gustavo Salvo Lara
 */
@Named(value = "emailInbox")
@ApplicationScoped
public class EmailInbox {

    private List<Email> selectedEmails;
    private String currentFolder = "INBOX";
    private String titleFolder = "Bandeja de Entrada";

    private String portOut = "465";	//puerto que se conecta al servidor de salida SMTP SSL
    private String portIn = "995";	//puerto que se conecta al servidor de entrada POP 
    private String hostOut = "smtp.gmail.com";	//Servidor SMTP de la cuenta
    private String hostIn = "imap.gmail.com";	//Servidor de entrada de la cuenta
    private String from;	//Email remitente del mensaje
    private String userName;	//Nombre de usuario de la cuenta para enviar email
    private String password;	//Contraseña de la cuenta de correo

    private List<Email> emails;
    private String emailContent;

    private TreeNode root;
    private TreeNode selectedFolder;
    
    @Inject
    private SessionUtil sessionUtil;

    private String subjectRead;
    private String fromEmailRead;
    private String sendDateRead;
    private String contentRead;

    @PostConstruct
    public void init() {
        root = new DefaultTreeNode("Root", null);
        emails = new ArrayList<Email>();
        try {
            setUserName();
            setPassword();
            loadFolders();
            readMailImap("INBOX");
        } catch (IOException ex) {
            Logger.getLogger(EmailInbox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName() {
        this.from = sessionUtil.getCurrentUser().getEmail();
        this.userName = sessionUtil.getCurrentUser().getEmail();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword() {
        this.password = sessionUtil.getCurrentUser().getEmailPassword();
    }

    /**
     * Recibe un conjunto de correos electrónico desde una bandeja de entrada
     * dado el protocolo de conexión IMAP. Se imprimen por consola los
     * resultados obtenidos. Falta implementar seguridad.
     *
     * @throws java.io.IOException
     */
    public void readMailImap(String readFolder) throws IOException {
        Properties props = System.getProperties();
        try {
            MimeMessage m;
            emails.clear();
            Session sesion = Session.getDefaultInstance(props, null);
            Store store = sesion.getStore("imaps");	//se define el protocolo de acceso
            store.connect(hostIn, userName, password);	//Se realiza la conexión
            Folder folder = store.getFolder(readFolder);
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
                m = (MimeMessage) msg[i];
                email.setIdMessage(m.getMessageID());
                if (msg[i].isSet(Flags.Flag.SEEN)) {
                    email.setSeen(true);
                } else {
                    email.setSeen(false);
                }
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

    public void loadFolders() {
        Properties props = System.getProperties();
        try {
            Session sesion = Session.getDefaultInstance(props, null);
            Store store = sesion.getStore("imaps");	//se define el protocolo de acceso
            try {
                store.connect(hostIn, userName, password);	//Se realiza la conexión
            } catch (AuthenticationFailedException afe){
                JsfUtil.redirect("/faces/roles/ProfileConfigEmail.xhtml");
            }
            Folder[] folders = store.getDefaultFolder().list("*");
            TreeNode inbox = new DefaultTreeNode("Bandeja de entrada", root);
            TreeNode folderCase1 = null;
            TreeNode folderCase2 = null;
            String nameFolderCase2 = null;

            for (Folder folder : folders) {
                IMAPFolder imf = (IMAPFolder) folder;
                if (imf.getParent().getName().equals("[Gmail]")) {
                    folderCase1 = new DefaultTreeNode(imf.getName(), root);
                }
//                else if(imf.getParent().getName().equals("")){
//                    folderCase2 = new DefaultTreeNode(imf.getName(), root); 
//                    nameFolderCase2= imf.getName();
//                }
//                else{
//                   if(imf.getParent().getName().equals(nameFolderCase2)){
//                       folderCase2.getChildren().add(new DefaultTreeNode(imf.getName()));
//                   }
//                }
//                
//                if ((folder.getType()& Folder.HOLDS_MESSAGES ) != 0) {
//                    System.out.println("El nombre de la carpeta es: "+imf.getName());
//                    System.out.println("El padre de la carpeta es: "+imf.getParent().getName());
//                    System.out.println(folder.getFullName() + ": " + folder.getMessageCount());
//                }
            }
            System.out.println("la cantidad de hijos es " + root.getChildCount());
            store.close();
        } catch (MessagingException me) {
            System.err.println(me.toString());
        }
    }

    /**
     * Función recursiva que se encarga de analizar el contenido de un email
     * todas partes multipart de un correo. Para cada una de ellas, se debe
     * especificar se debe saber si son text, img, etc (hay que agregar el
     * resto). Además, de acuerdo al formato del texto, no se podrá mostrar
     * correctamente por consola, habrá que mostrarlo en un jFrame o cuando ya
     * la aplicación esté lista
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
                        emailContent += part.getContentType();
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

    public void markAsRead() throws MessagingException {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Properties props = System.getProperties();
            //Validamos que se hayan seleccionado correos
            if (selectedEmails.isEmpty()) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Debe seleccionar un correo"));
                return;
            }
            //Volvemos a cargar todos los correos de INBOX
            //Seleccionamos aquellos que han sido escogidos para marcar leídos, comparándo los ids
            //Luego, los marcamos todos a la vez como leídos
            MimeMessage m;
            Session sesion = Session.getDefaultInstance(props, null);
            Store store = sesion.getStore("imaps");	//se define el protocolo de acceso
            store.connect(hostIn, userName, password);	//Se realiza la conexión
            Folder folder = store.getFolder(currentFolder);
            folder.open(Folder.READ_WRITE);
            Message[] msgOldFlags = folder.getMessages();
            for (int i = 0; i < msgOldFlags.length; i++) {
//                System.out.println("Se esta marcando como leído el correo '" + selectedEmails.get(i).getSubject() + "'");
                m = (MimeMessage) msgOldFlags[i];
                for (int j = 0; j < selectedEmails.size(); j++) {
                    if (m.getMessageID().equals(selectedEmails.get(j).getIdMessage())) {
                        folder.setFlags(new Message[]{msgOldFlags[i]}, new Flags(Flags.Flag.SEEN), true);
                        for (int k = 0; k < emails.size(); k++) {
                            if (emails.get(k).getIdMessage().equals(selectedEmails.get(j).getIdMessage())) {
                                emails.get(k).setSeen(true);
                            }
                        }
                    }

                }
//                Email email = new Email();
//                if (msgOldFlags[i].isSet(Flags.Flag.SEEN)) {
//                    email.setSeen(true);
//                } else {
//                    email.setSeen(false);
//                }
//                email.setIdMessage(m.getMessageID());
//                email.setSubject(msgOldFlags[i].getSubject());
//                email.setFrom(msgOldFlags[i].getFrom()[0].toString());
//                email.setSendDate(msgOldFlags[i].getSentDate());
//                analyzeMessage(msgOldFlags[i]);
//                email.setContent(emailContent);
//                emails.add(email);

            }
//            System.out.println("Se va a cerrar la conexión tras marcar como leídos");
            folder.close(true);
            store.close();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Correos marcados como leídos"));
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }

    public void markAsNoRead() throws MessagingException {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Properties props = System.getProperties();
            //Validamos que se hayan seleccionado correos
            if (selectedEmails.isEmpty()) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Debe seleccionar un correo"));
                return;
            }

            //Volvemos a cargar todos los correos de INBOX
            //Seleccionamos aquellos que han sido escogidos para marcar leídos, comparándo los ids
            //Luego, los marcamos todos a la vez como leídos
            MimeMessage m;
            Session sesion = Session.getDefaultInstance(props, null);
            Store store = sesion.getStore("imaps");	//se define el protocolo de acceso
            store.connect(hostIn, userName, password);	//Se realiza la conexión
            Folder folder = store.getFolder(currentFolder);
            folder.open(Folder.READ_WRITE);
            Message[] msgOldFlags = folder.getMessages();
            for (int i = 0; i < msgOldFlags.length; i++) {
//                System.out.println("Se esta marcando como no leído el correo '" + selectedEmails.get(i).getSubject() + "'");
                m = (MimeMessage) msgOldFlags[i];
                for (int j = 0; j < selectedEmails.size(); j++) {
                    if (m.getMessageID().equals(selectedEmails.get(j).getIdMessage())) {
                        folder.setFlags(new Message[]{msgOldFlags[i]}, new Flags(Flags.Flag.SEEN), false);
                        for (int k = 0; k < emails.size(); k++) {
                            if (emails.get(k).getIdMessage().equals(selectedEmails.get(j).getIdMessage())) {
                                emails.get(k).setSeen(false);
                            }
                        }
                    }

                }
            }
//            System.out.println("Se va a cerrar la conexión tras marcar como no leídos");
            folder.close(true);
            store.close();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Correos marcados como no leídos"));

        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }

    public void deleteMessage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Properties props = System.getProperties();
            if (selectedEmails.isEmpty()) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", "Debe seleccionar un correo"));
                return;
            }
            MimeMessage m;
            Session sesion = Session.getDefaultInstance(props, null);
            Store store = sesion.getStore("imaps");	//se define el protocolo de acceso
            store.connect(hostIn, userName, password);	//Se realiza la conexión
            Folder folder = store.getFolder(currentFolder);
            Folder deleteFolder = store.getFolder("[Gmail]/Papelera");
            deleteFolder.open(Folder.READ_WRITE);
            folder.open(Folder.READ_WRITE);
            Message[] msgOldFlags = folder.getMessages();
            Message[] toDelete = new Message[selectedEmails.size()];
            for (int i = 0; i < msgOldFlags.length; i++) {
                m = (MimeMessage) msgOldFlags[i];
                for (int j = 0; j < selectedEmails.size(); j++) {
                    if (m.getMessageID().equals(selectedEmails.get(j).getIdMessage())) {
                        toDelete[j] = msgOldFlags[i];
                        folder.setFlags(new Message[]{msgOldFlags[i]}, new Flags(Flags.Flag.DELETED), true);
                        for (int k = 0; k < emails.size(); k++) {
                            if (emails.get(k).getIdMessage().equals(selectedEmails.get(j).getIdMessage())) {
                                emails.remove(k);
                            }
                        }
                    }
                }
            }
            folder.copyMessages(toDelete, deleteFolder);
            folder.close(true);
            folder.close(true);
            store.close();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", "Correo(s) eliminados correctamente."));

        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }

    public void refresh() {
        try {
            readMailImap(currentFolder);
        } catch (IOException ex) {
            Logger.getLogger(EmailInbox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reading() {
        subjectRead = selectedEmails.get(0).getSubject();
        fromEmailRead= selectedEmails.get(0).getFrom();
        sendDateRead= selectedEmails.get(0).getDfDafault().format(selectedEmails.get(0).getSendDate());
        contentRead= selectedEmails.get(0).getContent();

    }

    public void onNodeSelect(NodeSelectEvent event) throws IOException {
        if (event.getTreeNode().toString().equals("Bandeja de entrada")) {
            currentFolder = "INBOX";
            titleFolder = "Bandeja de entrada";
        } else {
            currentFolder = "[Gmail]/" + (event.getTreeNode().toString());
            titleFolder = (event.getTreeNode().toString());
        }
        readMailImap(currentFolder);

        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public TreeNode getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(TreeNode selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public List<Email> getSelectedEmails() {
        return selectedEmails;
    }

    public void setSelectedEmails(List<Email> selectedEmails) {
        this.selectedEmails = selectedEmails;
    }

    public String getTitleFolder() {
        return titleFolder;
    }

    public void setTitleFolder(String titleFolder) {
        this.titleFolder = titleFolder;
    }

    public String getSubjectRead() {
        return subjectRead;
    }

    public void setSubjectRead(String subjectRead) {
        this.subjectRead = subjectRead;
    }

    public String getFromEmailRead() {
        return fromEmailRead;
    }

    public void setFromEmailRead(String fromEmailRead) {
        this.fromEmailRead = fromEmailRead;
    }

    public String getSendDateRead() {
        return sendDateRead;
    }

    public void setSendDateRead(String sendDateRead) {
        this.sendDateRead = sendDateRead;
    }

    public String getContentRead() {
        return contentRead;
    }

    public void setContentRead(String contentRead) {
        this.contentRead = contentRead;
    }

}
