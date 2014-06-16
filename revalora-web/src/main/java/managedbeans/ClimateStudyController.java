package managedbeans;

import entities.ClimateStudy;
import entities.ClimateStudyInvitation;
import entities.Contact;
import entities.ContactGroup;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import managedbeans.mailer.EmailController;
import managedbeans.util.Crypto;
import managedbeans.util.JsfUtil;
import managedbeans.util.JsfUtil.PersistAction;
import managedbeans.util.LittleMailer;
import managedbeans.util.SessionUtil;
import otherclasses.Email;
import sessionbeans.ClimateStudyFacadeLocal;
import sessionbeans.util.EmailSessionBeanLocal;

@Named("climateStudyController")
@SessionScoped
public class ClimateStudyController implements Serializable {

    @EJB
    private ClimateStudyFacadeLocal ejbFacade;
    private List<ClimateStudy> items = null;
    private ClimateStudy selected;

    private String surveyParam;
    private String[] survey = new String[70];
    
    @Inject private SessionUtil sessionUtil;
    @Inject private ProjectController projectController;
    @Inject private ClimateStudyInvitationController climateStudyInvitationController;
    @Inject private EmailController emailController;
    @Inject private LittleMailer littleMailer; // El mejor de los mailers
    @Inject private ContactController contactController;
    
    @EJB private EmailSessionBeanLocal emailFacade;


    public ClimateStudyController() {
    }

    public ClimateStudy getSelected() {
        return selected;
    }

    public void setSelected(ClimateStudy selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ClimateStudyFacadeLocal getFacade() {
        return ejbFacade;
    }

    public String getSurveyParam() {
        return surveyParam;
    }

    public void setSurveyParam(String surveyParam) throws NoSuchAlgorithmException, 
            InvalidKeySpecException, InvalidKeyException, 
            UnsupportedEncodingException, IOException, 
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        
        this.surveyParam = surveyParam;
        Crypto cypher = new Crypto();
        String raw = cypher.decrypt(surveyParam);
        String [] rawData = raw.split("\\|\\|");
        selected = getClimateStudy(Long.parseLong(rawData[1]));
        contactController.setSelected(contactController.getContact(rawData[0]));
    }

    public String[] getSurvey() {
        return survey;
    }

    public void setSurvey(String[] survey) {
        this.survey = survey;
    }

    public ClimateStudy prepareCreate() {
        selected = new ClimateStudy();
        selected.setCreator(sessionUtil.getCurrentUser());
        selected.setProject(projectController.getSelected());
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        if (validateDate()) {
            persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ClimateStudyCreated"));
            if (!JsfUtil.isValidationFailed()) {
                items = null;    // Invalidate list of items to trigger re-query.
            }
        } else {
            JsfUtil.addErrorMessage("La fecha de termino no puede ser posterior al termino del proyecto");
        }
    }

    public void update() {
        if (validateDate()) {
            persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ClimateStudyUpdated"));
        } else {
            JsfUtil.addErrorMessage("La fecha de termino no puede ser posterior al termino del proyecto");
        }
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ClimateStudyDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<ClimateStudy> getItems() {
        projectController.refreshSelected();
        items = projectController.getSelected().getClimateStudies();
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction == PersistAction.CREATE) {
                    getFacade().create(selected);
                } else if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public ClimateStudy getClimateStudy(java.lang.Long id) {
        return getFacade().find(id);
    }
    
    public void refreshSelected() {
        selected = getClimateStudy(selected.getId());
    }

    public List<ClimateStudy> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ClimateStudy> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = ClimateStudy.class)
    public static class ClimateStudyControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ClimateStudyController controller = (ClimateStudyController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "climateStudyController");
            return controller.getClimateStudy(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof ClimateStudy) {
                ClimateStudy o = (ClimateStudy) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ClimateStudy.class.getName()});
                return null;
            }
        }

    }

    public void climateStudyIndex() {
        JsfUtil.redirect("/faces/roles/experto/climateStudy/index.xhtml");
    }

    public void sendInvitations() {
        System.out.println("Enviando invitaciones");
        try {
            for (Contact contact : selected.getContacts()) {
                _sendInvitations(contact);
            }
            for (ContactGroup group : selected.getGroups()) {
                for (Contact contact : group.getContacts()) {
                    _sendInvitations(contact);
                }
            }
            refreshSelected();
        } catch (Exception ex) {
            JsfUtil.addErrorMessage("Ha ocurrido un error y algunas de las invitaciones no se han enviado. Inténtelo mas tarde");
        }
        System.out.println("- Invitaciones enviadas");
        JsfUtil.addSuccessMessage("Las invitaciones han sido enviadas");
    }

    public boolean validateDate() {
        boolean validate = false;
        if (selected.getProject().getEndAt() != null && selected.getEndAt() != null) {
            if (selected.getProject().getEndAt().compareTo(selected.getEndAt()) > 0) {
                if (selected.getProject().getStartAt().compareTo(selected.getEndAt()) < 0) {
                    validate = true;
                }
            }
        }
        return validate;
    }

    public void _sendInvitations(Contact contact) throws MessagingException {
        System.out.println("- Enviadno mensaje a " + contact.getName() + " (" + contact.getEmail() + ")");
        littleMailer.sendClimateStudyInvitation(contact, selected);
        
        ClimateStudyInvitation invitation = new ClimateStudyInvitation();
        invitation.setContact(contact);
        invitation.setDate(new Date());
        invitation.setStudy(selected);
        invitation.setState("Enviado");
        selected.getInvitations().add(invitation);
        getFacade().edit(selected);
    }



    
    public void submit() {
        System.out.println("Procesando encuesta");
        
    }
    

}
