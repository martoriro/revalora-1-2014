/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbeans.util;

import otherclasses.Email;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.ejb.LocalBean;
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
@LocalBean
public class EmailSessionBean {
        private String portOut ="465";	//puerto que se conecta al servidor de salida SMTP SSL
	private String portIn ="995";	//puerto que se conecta al servidor de entrada POP 
	private String hostOut = "smtp.gmail.com";	//Servidor SMTP de la cuenta
	private String hostIn = "pop.gmail.com";	//Servidor de entrada de la cuenta
	private String from = "sistema.revalora@gmail.com";	//Email remitente del mensaje
	private final String userName = "sistema.revalora@gmail.com";	//Nombre de usuario de la cuenta para enviar email
	private final String password = "ihc12014";	//Contraseña de la cuenta de correo
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
    public int sendMail(String to,String carbonCopy, String BlindCarbonCopy, String attached, String subject, String body) throws AddressException, MessagingException{
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
    
    /*
    * Función recursiva que se encarga de analizar el contenido de un email todas
    * partes multipart de un correo. Para cada una de ellas, se debe especificar
    * se debe saber si son text, img, etc (hay que agregar el resto). Además,
    * de acuerdo al formato del texto, no se podrá mostrar correctamente por
    * consola, habrá que mostrarlo en un jFrame o cuando ya la aplicación
    * esté lista
    */
    public static void analyzeMessage (Part part) {
        try {
            
          // Si es multipart, se analiza cada una de sus partes recursivamente.
            if (part.isMimeType("multipart/*"))
            {
                Multipart multipart;
                multipart = (Multipart) part.getContent();

                for (int j = 0; j < multipart.getCount(); j++) {
                    analyzeMessage(multipart.getBodyPart(j));
                }
            }
            else
            {
              //Si part es texto, se escribe
                if (part.isMimeType("text/*")) {
                    System.out.println(part.getContent()+"\n");
                    //System.out.println("---------------------------------");
                }
                else
                {
                  // Si es imagen, se guarda en fichero y se visualiza en JFrame
                    if (part.isMimeType("image/*"))
                    {
                        System.out.println("Imagen " + part.getContentType() +"\n");
                        System.out.println("Nombre Imagen: " + part.getFileName()+"\n");
                        //System.out.println("---------------------------------");
                    }
                    else
                    {
                      // Si no es ninguna de las anteriores, escribimos el tipo
                        System.out.println("Tipo recibido: " + part.getContentType()+"\n");
                        //System.out.println("---------------------------------");
                    }
                }
            }
        } catch (MessagingException e){
            e.printStackTrace();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * Recibe un conjunto de correos electrónico desde una bandeja de entrada dado el protocolo
     * de conexión POP.
     * Se imprimen por consola los resultados obtenidos.
     * Falta implementar seguridad.
     * @return 1 en el caso que la operación sea un éxito ó -1 en caso que falle
     */
   
    public int readMailPop(){
    	// Se configuran las propiedades para la conexión con el servidor de entrada utilizando SSL
    	
    	Properties props = System.getProperties();
    	props.setProperty("mail.pop3.starttls.enable", "false");	// Deshabilitamos TLS
    	props.setProperty("mail.pop3.socketFactory.class","javax.net.ssl.SSLSocketFactory" );	// Hay que usar SSL
    	props.setProperty("mail.pop3.socketFactory.fallback", "false");
    	props.setProperty("mail.pop3.port",portIn);	// Puerto 995 para conectarse.
    	props.setProperty("mail.pop3.socketFactory.port", portIn);
    	
    	try{
    	
    		Session sesion = Session.getDefaultInstance(props, null);
    		Store store = sesion.getStore("pop3");	//se define el protocolo de acceso
    		store.connect(hostIn, userName, password);	//Se realiza la conexión
    		Folder folder = store.getFolder("INBOX");
    		folder.open(Folder.READ_ONLY);
    		//se pueden seleccionar la cantidad de mensajes a recuperar
    		System.out.println("La cantidad de mensajes es:"+folder.getMessageCount());
    		Message[] msg = folder.getMessages();	//se recuperan los mensajes del servidor

    		for(int i = 0; i< msg.length;i++){
    			
    			System.out.println("Mensaje " + i + "\n"+ 
    								"\tAsunto: " + msg[i].getSubject() + "\n"+
    								"\tRemitente: " + msg[i].getFrom()[0] + "\n" +
    								"\tFecha de Envío:" + msg[i].getSentDate() + "\n"+
    								"\tContenido: ");
                        analyzeMessage(msg[i]);
    			
    		}
    		folder.close(false);	//como no se realiza ningún cambio a los mensajes es false
    		store.close();
    		return 1;
    	}catch(MessagingException me){
    		System.err.println(me.toString());
    		return -1;
    	}
    }
    
    /**
     * Recibe un conjunto de correos electrónico desde una bandeja de entrada dado el protocolo
     * de conexión IMAP.
     * Se imprimen por consola los resultados obtenidos.
     * Falta implementar seguridad.
     * @return 1 en el caso que la operación sea un éxito ó -1 en caso que falle
     */
    public List<Email> readMailImap() throws IOException{
// Se configuran las propiedades para la conexión con el servidor de entrada utilizando SSL
    	
    	Properties props = System.getProperties();
    	/*props.setProperty("mail.imap.starttls.enable", "false");	// Deshabilitamos TLS
    	props.setProperty("mail.imap.socketFactory.class","javax.net.ssl.SSLSocketFactory" );	// Hay que usar SSL
    	props.setProperty("mail.imap.socketFactory.fallback", "false");
    	props.setProperty("mail.imap.port",portIn);	// Puerto 995 para conectarse.
    	props.setProperty("mail.imap.socketFactory.port", portIn);*/
    	
    	try{
    	
    		Session sesion = Session.getDefaultInstance(props, null);
    		Store store = sesion.getStore("imaps");	//se define el protocolo de acceso
    		store.connect(hostIn, userName, password);	//Se realiza la conexión
    		Folder folder = store.getFolder("INBOX");
    		folder.open(Folder.READ_ONLY);
    		//se pueden seleccionar la cantidad de mensajes a recuperar
    		System.out.println("La cantidad de mensajes es:"+folder.getMessageCount());
    		Message[] msg = folder.getMessages();	//se recuperan los mensajes del servidor
                List<Email> receivedEmailList = null;
    		for(int i = 0; i< msg.length;i++){
                    System.out.println("Mensaje " + i + "\n"+ 
                                                        "\tAsunto: " + msg[i].getSubject() + "\n"+
                                                        "\tRemitente: " + msg[i].getFrom()[0] + "\n" +
                                                        "\tFecha de Envío: " + msg[i].getSentDate() + "\n"+
                                                        "\tContenido: " + msg[i].getContent());
                    Email email = new Email();
                    email.setSubject(msg[i].getSubject());
                    email.setFrom(msg[i].getFrom()[0].toString());
                    email.setSendDate(msg[i].getSentDate());
                    receivedEmailList.add(email);
                    analyzeMessage(msg[i]);
    		}
    		folder.close(false);	//como no se realiza ningún cambio a los mensajes es false
    		store.close();
    		return receivedEmailList;
    	}catch(MessagingException me){
    		System.err.println(me.toString());
    		return null;
    	}
    }
}
