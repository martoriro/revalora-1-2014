/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans.mailer;

import entities.ClimateStudyInvitation;
import entities.ClimateStudyParticipation;
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
import sessionbeans.ClimateStudyParticipationFacadeLocal;
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
    @EJB
    private ClimateStudyInvitationFacadeLocal ejbFacade;

    private List<ClimateStudyInvitation> listInvitation = new ArrayList<ClimateStudyInvitation>();
    private List<ClimateStudyInvitation> listReal = new ArrayList<ClimateStudyInvitation>();

    private final String basePath = "http://localhost:8080/revalora-web/faces/survey/climate.xhtml";

    public void resend() throws MessagingException {
        try {
            Crypto cypher = new Crypto();
            listInvitation = ejbFacade.findAll();
            System.out.println(listInvitation.size());

            for (int j = 0; j<listInvitation.size(); j++) {
                if (listInvitation.get(j).getState() != null && listInvitation.get(j).getContact() != null) {
                    long idClimateAux = listInvitation.get(j).getStudy().getId();
                    String mailAux = listInvitation.get(j).getContact().getEmail();
                    boolean isReady = false;
                    System.out.println("inicio primer bucle: "+idClimateAux + " " + mailAux + " " + listInvitation.get(j).getState());
                    for (int i = 0; i<listInvitation.size(); i++) {
                        System.out.println(listInvitation.get(i).getStudy().getId()+ " " + listInvitation.get(i).getContact().getEmail() + " " + listInvitation.get(i).getState());
                        if(listInvitation.get(i).getStudy().getId()== idClimateAux && listInvitation.get(i).getContact().getEmail().equals(mailAux) && listInvitation.get(i).getState().equals("Listo")){
                            
                            System.out.println("Esta LISTO");
                            isReady = true;
                            break;
                        }                        
                    }
//                    if(!isReady){
//                        listReal.add(cs);
//                    }
                    
//                    if (!cs.getState().equals("Listo")) {
//                        String uri = cypher.crypt(cs.getContact().getEmail() + "||" + cs.getStudy().getId().toString());
//                        
//                        String mensaje = "Estimado " + cs.getContact().getName() + " aun no ha contestado la encuesta del estudio " + cs.getStudy().getName() + ".\n"
//                                + "El Sistema Revalora te solicita que respondas las preguntas en el siguiente enlace:\n\n" + basePath + "?survey=" + uri;
//                        //emailSessionBean.sendMail(cs.getContact().getEmail(), "", "", "", "Recordatorio de Revalora", mensaje);
//                        System.out.println(mensaje);
//                    }
                }                
            }
            System.out.println("hola");
            for(ClimateStudyInvitation lr: listReal){
                System.out.println(lr.getStudy().getId() + " " + lr.getContact().getEmail() + lr.getState());
            }
        } catch (Exception ex) {
            System.out.println("* Error: LittleMailer::sendInvitationToSurvey: No ha sido posible enviar el correo.");
        }
    }

}
