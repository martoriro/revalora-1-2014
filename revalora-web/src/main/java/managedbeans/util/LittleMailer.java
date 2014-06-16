/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbeans.util;

import entities.Account;
import entities.ClimateStudy;
import entities.Contact;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Properties;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author mariowise
 */
@Named(value = "littleMailer")
@RequestScoped
public class LittleMailer {
    
    private final String basePath = "http://localhost:8080/revalora-web/faces/survey/climate.xhtml";

    public void sendClimateStudyInvitation(Contact contact, ClimateStudy study) {
        try {
            Crypto cypher = new Crypto();
            String uri = cypher.crypt(contact.getEmail() + "||" + study.getId().toString());
            
            sendMessage(
                contact.getEmail(), 
                "Has sido invitado a participar del estudio #" + study.getId() + " - " + study.getName(), 
                "Has sido invitado a participar del estudio #" + study.getId() + " - " + study.getName() + ". " + 
                "Creado para la organización " + study.getProject().getOrganization().getName() + " por el experto " + study.getCreator().getNames() + ".\n" + 
                "El Sistema Revalora te solicita que respondas las preguntas en el siguiente enlace:\n\n" +
                basePath + "?survey=" + uri
            );
        } catch (Exception ex) {
            System.out.println("* Error: LittleMailer::sendInvitationToSurvey: No ha sido posible enviar el correo.");
        }
    }
    
    public String sendPasswordRecoveryMessage(Account account) {
        SecureRandom random = new SecureRandom();
        String newPassword = new BigInteger(130, random).toString(32);
        newPassword = newPassword.substring(2, 7).toUpperCase();
        try {
            sendMessage(account.getEmail(),
            "¿Olvidó su contraseña para ingresar al sistema Revalora?",
            "Estimado " + account.getFirstName() + ",\n\n" +
            "Su contraseña ha sido reestablecida: " + newPassword + "\n" +
            "Puede ingresar con esta nueva contraseña y cambiarla "+
            "desde el menú de Perfil de su cuenta.\n\n" + 
            "Si usted no ha solicitado este cambio, favor comunicarse con " + 
            "algún administrador del sistema.");
            return newPassword;
        } catch (Exception ex) {
            return null;
        }        
    }
    
    // Privates    
    private void sendMessage(String email, String messageSubject, String messageBody) {
        final String username = "sistema.revalora@gmail.com";
        final String password = "ihc12014";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                }
          });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("sistema.revalora@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject(messageSubject);
            message.setText(messageBody);
            Transport.send(message);
            System.out.println("Mailer: email send success to " + email);
        } 
        catch (MessagingException e) {
            System.out.println("Mailer: error on email send process.");
            throw new RuntimeException(e);
        }
    }
    
    private String sha256(String base) {
        System.out.println("Calling Account.sha256 (" + base + ")");
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
            
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    
}
