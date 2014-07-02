/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans.util;

import entities.Contact;
import entities.ContactGroup;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 *
 * @author Gustavo Salvo Lara
 */
@Stateful
public class EmailSessionBean implements EmailSessionBeanLocal{
        private String portOut ="465";  //puerto que se conecta al servidor de salida SMTP SSL
    private String portIn ="995";   //puerto que se conecta al servidor de entrada POP 
    private String hostOut = "smtp.gmail.com";  //Servidor SMTP de la cuenta
    private String hostIn = "pop.gmail.com";    //Servidor de entrada de la cuenta
    private String from;    //Email remitente del mensaje
    private String userName;    //Nombre de usuario de la cuenta para enviar email
    private String password;    //Contraseña de la cuenta de correo
        
        private Session session;
        private Properties props;
        private Authenticator auth;
        
        
        public void setProperties() {
            // Se configuran las propiedades para la conexión con el servidor de salida utilizando SSL
            props = new Properties();
            props.put("mail.smtp.host", hostOut); 
            props.put("mail.smtp.socketFactory.port", portOut);
            props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", portOut);

            //Se crea una clase para proteger los datos de conexión
            auth = new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password);
                }
            };

            //Se crea un objeto session que representa una sessión de correo
            session = Session.getInstance(props, auth);
            
        }
        
        /**
     * Envía un correo electrónico utilizando protocolo SMTP con SLL a un destinatario específico
     * Los parámetros de entrada carbonCopy, BlindCarbonCopy y attached pueden ser ingresados como
     * un String vacío dado que no nos necesarios y, en el caso de attached, no está implementado en 
     * esta versión. Además falta implementar el envío de correo a múltiples usuarios.
     * No se maneja el tema de la seguridad.
     * versión 1.0
     * 
     * @param to Dirección de correo electrónico destino
     * @param carbonCopy Con copia, dirección de correo secundaria para el envío del mensaje
     * @param BlindCarbonCopy Con copia oculta, dirección de correo electronica para el envío oculto del mensaje
     * @param attached Archivo adjunto para enviar al correo (no implementado)
     * @param subject Asunto del correo a enviar
     * @param body Contenido o mensaje del correo a enviar
     * @return -1 en caso de falla del envío ó 1 en caso de envío
     * @throws AddressException excepción lanzada cuando el formato de correo es considerado incorrecto
     * @throws MessagingException excepción lanzada por cualquier error del mensaje
     */
    public int sendMail(String to, String carbonCopy, String BlindCarbonCopy, String attached, String subject, String body) throws AddressException, MessagingException{
        
        //Se crea un objeto msg que representa el mensaje que se desea enviar
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from)); //se establece la dirección del remitente
        msg.setSubject(subject);    //se establece el asunto del mensaje
        msg.setSentDate(new Date());    //se establece la fecha de envío
        /*
         * Esto se debe modificar al momento de enviar como HTML o multiparte
         * para el primer Sprint se deja así *ver pag 27
         */
        msg.setText(body); //se establece el cuerpo del mensaje
        
        setProperties();
        
        /*
         * Se debe adaptar para poder enviar a un conjunto de personas
         * para el primer Sprint se deja así *ver pag 28
         */
        InternetAddress[] toAddresses = {new InternetAddress(to)};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        
        /*
         * Se debe configurar las direcciones de CC y BCC en caso que el usuario la ingrese
         */
        if(!carbonCopy.isEmpty()){
            msg.setRecipients(Message.RecipientType.CC, toAddresses);
        }
        if(!BlindCarbonCopy.isEmpty()){
            msg.setRecipients(Message.RecipientType.BCC, toAddresses);
        }
        /*
         * Se debe agregar el código para enviar archivos adjuntos
         * ver pag 67
         */
        try{
            Transport.send(msg);    // se envia el mensaje
            return 1;   //retorno para prueba unitaria
        }catch(MessagingException e){           
            System.out.println("Error al enviar el mensaje: "+e.getMessage());
            return -1;  //retorno para prueba unitaria
        }
         
    }
    
    @Override
    public void saveMailToDraft(String subject, String body, List<Contact> contacts, List<ContactGroup> groups) {
        try {
            Properties props = System.getProperties();
            Session sesion = Session.getDefaultInstance(props, null);
            MimeMessage m = new MimeMessage(session);
            Store store = sesion.getStore("imaps"); //se define el protocolo de acceso
            store.connect(hostIn, userName, password);  //Se realiza la conexión
            Folder folder = store.getFolder("[Gmail]/Borradores");
            
            //Sacamos emails que estén en contatos Y en grupos (para que no se repitan y envíen 2 veces a un mismo correo)
            //Los emails iguales, los marcamos como ""
            for(int j = 0; j<groups.size(); j++)
                for(int k = 0; k<groups.get(j).getContacts().size(); k++){
                    for(int i = 0; i<contacts.size(); i++)
                        if(groups.get(j).getContacts().get(k).getEmail().equals(contacts.get(i).getEmail())){
                           contacts.get(i).setEmail("");
                        }
                }
            
            //Agregamos las direcciones
            for(int i = 0; i<contacts.size(); i++)
                if(!contacts.get(i).getEmail().equals(""))
                     m.addRecipient(Message.RecipientType.TO, new InternetAddress(contacts.get(i).getEmail()));
            
            for(int j = 0; j<groups.size(); j++)
                for(int k = 0; k<groups.get(j).getContacts().size(); k++)
                    m.addRecipient(Message.RecipientType.TO, new InternetAddress(groups.get(j).getContacts().get(k).getEmail()));
            
            m.setFrom(new InternetAddress(userName));
            if(body != null)
                m.setText(body);
            if(subject != null)
                m.setSubject(subject);
            m.setSentDate(new Date());
            m.setFlag(Flags.Flag.DRAFT, true);
            folder.appendMessages(new Message[] {m});
            store.close();
        } catch (MessagingException ex) {
            System.err.println(ex.toString());
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.from = userName;
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isAuthenticated() {
        try {
            session.getStore().connect();
            return true;
        } catch (AuthenticationFailedException | NoSuchProviderException ex) {
            Logger.getLogger(EmailSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(EmailSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
}
