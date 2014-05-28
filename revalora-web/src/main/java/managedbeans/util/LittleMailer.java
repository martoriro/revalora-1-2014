/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbeans.util;

import entities.Account;
import entities.ClimateStudyInvitation;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
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
    
    private final String basePath = "http://localhost:8080/revalora-web/faces/survey/answer.xhtml";

    public void sendClimateStudyInvitation(Account account, ClimateStudyInvitation invitation) {
        String studyName = invitation.getStudy().getClimateStudy().getName();
        Date studyDeadline = invitation.getStudy().getClimateStudy().getEndAt();
                
        try {
            sendMessage(account.getEmail(),
            "Has sido invitado a participar en un estudio de clima organizacional llamado \"" + studyName + "\"", 
            "Estimado " + account.getFirstName() + ",\n\n" +
            "Has sido invitado a participar en el estudio de clima \"" + invitation + "\"\n" + 
            "Agradecemos puedas responder accediendo en el siguiente link antes de la fecha " + studyDeadline.toString() + "\n\n" + 
            basePath + "");
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
}
