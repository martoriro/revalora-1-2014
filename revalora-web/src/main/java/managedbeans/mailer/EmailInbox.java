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
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
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

    @ManagedProperty(value = "#{readMail.selectedEmail}")
    private List<Email> selectedEmailsInbox;
    private String currentFolder="Bandeja de Entrada";
    private String titleFolder = "Bandeja de Entrada";
    
    private String portOut = "465";	//puerto que se conecta al servidor de salida SMTP SSL
    private String portIn = "995";	//puerto que se conecta al servidor de entrada POP 
    private String hostOut = "smtp.gmail.com";	//Servidor SMTP de la cuenta
    private String hostIn = "imap.gmail.com";	//Servidor de entrada de la cuenta
    private String from = "sistema.revalora@gmail.com";	//Email remitente del mensaje
    private final String userName = "sistema.revalora@gmail.com";	//Nombre de usuario de la cuenta para enviar email
    private final String password = "ihc12014";	//Contraseña de la cuenta de correo
    private List<Email> emails;
//    private List<Email> selectedEmails;
    private String emailContent;

    private TreeNode root;
    private TreeNode selectedFolder;

    @PostConstruct
    public void init() {
        root = new DefaultTreeNode("Root", null);

        emails = new ArrayList<Email>();
        try {
            loadFolders();
            readMailImap("INBOX");
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
            store.connect(hostIn, userName, password);	//Se realiza la conexión
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
        System.out.println("La lista contiene: mcl");
//        try {
//            FacesContext context = FacesContext.getCurrentInstance();
//            Properties props = System.getProperties();
//
//            //Validamos que se hayan seleccionado correos
//            if (selectedEmailsInbox.isEmpty()) {
//                context.addMessage(null, new FacesMessage("No se han seleccionado correos", ""));
//                return;
//            }
//
//            //Volvemos a cargar todos los correos de INBOX
//            //Seleccionamos aquellos que han sido escogidos para marcar leídos, comparándo los ids
//            //Luego, los marcamos todos a la vez como leídos
//            emails.clear();
//            MimeMessage m;
//            Session sesion = Session.getDefaultInstance(props, null);
//            Store store = sesion.getStore("imaps");	//se define el protocolo de acceso
//            store.connect(hostIn, userName, password);	//Se realiza la conexión
//            Folder folder = store.getFolder(currentFolder);
//            folder.open(Folder.READ_WRITE);
//            Message[] msgOldFlags = folder.getMessages();
//            for (int i = 0; i < msgOldFlags.length; i++) {
//                System.out.println("Se esta marcando como leído el correo '" + selectedEmailsInbox.get(i).getSubject() + "'");
//                m = (MimeMessage) msgOldFlags[i];
//                for (int j = 0; j < selectedEmailsInbox.size(); j++) {
//                    if (m.getMessageID().equals(selectedEmailsInbox.get(j).getIdMessage())) {
//                        folder.setFlags(new Message[]{msgOldFlags[i]}, new Flags(Flags.Flag.SEEN), true);
//                    }
//                }
//            }
//            System.out.println("Se va a cerrar la conexión tras marcar como leídos");
//            folder.close(true);
//            store.close();
//            context.addMessage(null, new FacesMessage("Correos marcados como leídos", ""));
//        } catch (Exception ex) {
//             System.err.println(ex.toString());
//        }
    }

    public void markAsNoRead() throws MessagingException {
        System.out.println("La lista contiene: ");
//        try {
//            FacesContext context = FacesContext.getCurrentInstance();
//            Properties props = System.getProperties();
//            //Validamos que se hayan seleccionado correos
//            if (selectedEmailsInbox.isEmpty()) {
//                context.addMessage(null, new FacesMessage("No se han seleccionado correos", ""));
//                return;
//            }
//
//            //Volvemos a cargar todos los correos de INBOX
//            //Seleccionamos aquellos que han sido escogidos para marcar leídos, comparándo los ids
//            //Luego, los marcamos todos a la vez como leídos
//            emails.clear();
//            MimeMessage m;
//            Session sesion = Session.getDefaultInstance(props, null);
//            Store store = sesion.getStore("imaps");	//se define el protocolo de acceso
//            store.connect(hostIn, userName, password);	//Se realiza la conexión
//            Folder folder = store.getFolder(currentFolder);
//            folder.open(Folder.READ_WRITE);
//            Message[] msgOldFlags = folder.getMessages();
//            for (int i = 0; i < msgOldFlags.length; i++) {
//                System.out.println("Se esta marcando como no leído el correo '" + selectedEmailsInbox.get(i).getSubject() + "'");
//                m = (MimeMessage) msgOldFlags[i];
//                for (int j = 0; j < selectedEmailsInbox.size(); j++) {
//                    if (m.getMessageID().equals(selectedEmailsInbox.get(j).getIdMessage())) {
//                        folder.setFlags(new Message[]{msgOldFlags[i]}, new Flags(Flags.Flag.SEEN), false);
//                    }
//                }
//            }
//            System.out.println("Se va a cerrar la conexión tras marcar como no leídos");
//            folder.close(true);
//            store.close();
//            context.addMessage(null, new FacesMessage("Correos marcados como no leídos", ""));
//        } catch (Exception ex) {
//            System.err.println(ex.toString());
//        }
    }

    public void refresh() {
        try {
            readMailImap(currentFolder);
        } catch (IOException ex) {
            Logger.getLogger(EmailInbox.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
//    public Email getSelectedEmail() {
//        return selectedEmail;
//    }
//
//    public void setSelectedEmail(Email selectedEmail) {
//        this.selectedEmail = selectedEmail;
//    }
//    public List<Email> getSelectedEmails() {
//        return selectedEmails;
//    }
//
//    public void setSelectedEmails(List<Email> selectedEmails) {
//        this.selectedEmails = selectedEmails;
//    }
    public String getTitleFolder() {
        return titleFolder;
    }

    public void setTitleFolder(String titleFolder) {
        this.titleFolder = titleFolder;
    }


}
