/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans.mailer;

import entities.ClimateStudyInvitation;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.mail.MessagingException;
import managedbeans.ClimateStudyController;
import managedbeans.ClimateStudyInvitationController;
import managedbeans.util.Crypto;
import sessionbeans.ClimateStudyInvitationFacadeLocal;
import sessionbeans.util.EmailSessionBeanLocal;

/**
 *
 * @author Gustavo Salvo Lara
 */
@Named(value = "resendInvitation")
@SessionScoped
public class resendInvitation implements Serializable {

    @EJB
    private EmailSessionBeanLocal emailSessionBean;
    private List<ClimateStudyInvitation> listInvitation = new ArrayList<ClimateStudyInvitation>();
    @EJB
    private ClimateStudyInvitationFacadeLocal ejbFacade;
    private ClimateStudyController csc;
    private final String basePath = "http://localhost:8080/revalora-web/faces/survey/climate.xhtml";

    public void resend() throws MessagingException {
        try {
            Crypto cypher = new Crypto();
            listInvitation = ejbFacade.findAll();
            System.out.println(listInvitation.size());
            for (ClimateStudyInvitation cs : listInvitation) {
                if (cs.getState() != null && cs.getContact() != null) {
                    if (!cs.getState().equals("Listo")) {
                        String uri = cypher.crypt(cs.getContact().getEmail() + "||" + cs.getStudy().getId().toString());
                        String mensaje = "Estimado " + cs.getContact().getName() + " aun no ha contestado la encuesta del estudio " + cs.getStudy().getName() + ".\n"+
                                "El Sistema Revalora te solicita que respondas las preguntas en el siguiente enlace:\n\n" + basePath + "?survey=" + uri;
                        //emailSessionBean.sendMail(cs.getContact().getEmail(), "", "", "", "Recordatorio de Revalora", mensaje);
                        System.out.println(mensaje);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("* Error: LittleMailer::sendInvitationToSurvey: No ha sido posible enviar el correo.");
        }
    }

}
