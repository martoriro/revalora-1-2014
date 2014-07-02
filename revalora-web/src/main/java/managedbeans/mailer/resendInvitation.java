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
import java.util.HashSet;
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
import managedbeans.util.JsfUtil;
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

    private List<ClimateStudyInvitation> listInvitation = new ArrayList<>();
    private List<ClimateStudyInvitation> listInvReady = new ArrayList<>();
    private List<ClimateStudyInvitation> listRealTemp = new ArrayList<>();

    private final String basePath = "http://localhost:8080/revalora-web/faces/survey/climate.xhtml";

    public void resend() throws MessagingException {
        try {
            Crypto cypher = new Crypto();
            listInvitation = ejbFacade.findAll();
            System.out.println(listInvitation.size());
            
            //Obtenemos todas las encuestas que ya están listas
            for (int i = 0; i<listInvitation.size(); i++) {
                if (listInvitation.get(i).getContact() != null) {
                    String mailAux = listInvitation.get(i).getContact().getEmail();
                    long idClimateAux = listInvitation.get(i).getStudy().getId();
                    for(int h=0; h<listInvitation.size(); h++){
                        if((listInvitation.get(h).getState() != null) && (listInvitation.get(h).getContact() != null)){
                            if(listInvitation.get(h).getContact().getEmail().equals(mailAux)
                            && listInvitation.get(h).getStudy().getId().equals(idClimateAux)
                            && listInvitation.get(h).getState().equals("Listo")){
                                listInvReady.add(listInvitation.get(h));
                                break;
                            }
                        }
                    }
                }
            }
            
            //Mediante las encuestas que están listas, obtenemos las encuestas que han sido enviadas
            //pero que aún no están listas
            //Genera datos repetidos!
            List<ClimateStudyInvitation> listInvNotReady = new ArrayList<>();
            System.out.println("Encuestas que ya están listas: "+listInvReady.size());
            listInvNotReady = listInvitation;
            for(int i = 0; i<listInvReady.size(); i++){
                if(listInvReady.get(i).getContact() != null)
                    for(int j=0; j<listInvitation.size(); j++)
                        if(listInvitation.get(j).getContact() != null)
                            if(listInvitation.get(j).getContact().equals(listInvReady.get(i).getContact()) 
                            && listInvitation.get(j).getStudy().getId().equals(listInvReady.get(i).getStudy().getId()))
                                listInvNotReady.remove(listInvitation.get(j));
                            
            }
            System.out.println("Encuestas no listas(cantidad de encuestas enviadas sin ser respondidas): "+listInvNotReady.size());
            
            //Esta es la función que envía los correos de recordatorio
            //sendReminder y urisTemp se utiliza para no enviar más de una vez el mensaje
            //debido a duplicidad de los mismos datos
            List<String> urisTemp = new ArrayList<>();
            for(int i = 0; i<listInvNotReady.size(); i++){
                boolean sendReminder = true;
                if(listInvNotReady.get(i).getContact() != null){
                    String uri = cypher.crypt(listInvNotReady.get(i).getContact().getEmail() + "||" + listInvNotReady.get(i).getStudy().getId().toString());
                    
                    if(urisTemp.contains(uri) && !urisTemp.isEmpty())
                        sendReminder = false;
                    else
                        urisTemp.add(uri);
                    if(sendReminder){
                        String mensaje = "Estimado " + listInvNotReady.get(i).getContact().getName() + " aun no ha contestado la encuesta del estudio " + listInvNotReady.get(i).getStudy().getName() + ".\n"
                             + "El Sistema Revalora te solicita que respondas las preguntas en el siguiente enlace:\n\n" + basePath + "?survey=" + uri;
                        emailSessionBean.sendMail(listInvNotReady.get(i).getContact().getEmail(), "", "", "", "Recordatorio de Revalora", mensaje);
                        System.out.println(mensaje);
                    }     
                }
            }
           if(listInvNotReady.size()>0)
               JsfUtil.addSuccessMessage("Recordatorio enviado");
           if(listInvNotReady.isEmpty())
               JsfUtil.addSuccessMessage("Toas las encuestas enviadas están respondida, no es necesario enviar recordatorio");
        } catch (Exception ex) {
            Logger.getLogger(resendInvitation.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("* Error: LittleMailer::sendInvitationToSurvey: No ha sido posible enviar el correo.");
        }
    }

}

