/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans.util;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.ejb.LocalBean;
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
@Stateless
public class EmailSessionBean implements EmailSessionBeanLocal{
        private String portOut ="465";	//puerto que se conecta al servidor de salida SMTP SSL
	private String portIn ="995";	//puerto que se conecta al servidor de entrada POP 
	private String hostOut = "smtp.gmail.com";	//Servidor SMTP de la cuenta
	private String hostIn = "pop.gmail.com";	//Servidor de entrada de la cuenta
	private String from = "sistema.revalora@gmail.com";	//Email remitente del mensaje
	private String userName = "sistema.revalora@gmail.com";	//Nombre de usuario de la cuenta para enviar email
	private String password = "ihc12014";	//Contraseña de la cuenta de correo
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
	 * @throws AddressException	excepción lanzada cuando el formato de correo es considerado incorrecto
	 * @throws MessagingException excepción lanzada por cualquier error del mensaje
	 */
    public int sendMail(String to, String carbonCopy, String BlindCarbonCopy, String attached, String subject, String body) throws AddressException, MessagingException{
    	// Se configuran las propiedades para la conexión con el servidor de salida utilizando SSL
    	Properties props = new Properties();
    	props.put("mail.smtp.host", hostOut); 
        props.put("mail.smtp.socketFactory.port", portOut);
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", portOut);
    	
       	//Se crea una clase para proteger los datos de conexión
    	Authenticator auth = new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        
        //Se crea un objeto session que representa una sessión de correo
        Session session = Session.getInstance(props, auth);
        
        //Se crea un objeto msg que representa el mensaje que se desea enviar
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));	//se establece la dirección del remitente
        msg.setSubject(subject);	//se establece el asunto del mensaje
        msg.setSentDate(new Date());	//se establece la fecha de envío
        /*
         * Esto se debe modificar al momento de enviar como HTML o multiparte
         * para el primer Sprint se deja así *ver pag 27
         */
        msg.setText(body); //se establece el cuerpo del mensaje
        
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
        	Transport.send(msg);	// se envia el mensaje
        	return 1;	//retorno para prueba unitaria
        }catch(MessagingException e){        	
        	System.out.println("Error al enviar el mensaje: "+e.getMessage());
        	return -1;	//retorno para prueba unitaria
        }
         
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}
