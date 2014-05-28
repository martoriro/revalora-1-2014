package managedbeans;

import entities.Contact;
import entities.ContactGroup;
import entities.Message;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import managedbeans.util.JsfUtil;
import managedbeans.util.JsfUtil.PersistAction;
import managedbeans.util.SessionUtil;
import sessionbeans.util.EmailSessionBean;
import sessionbeans.MessageFacadeLocal;

@Named("messageController")
@SessionScoped
public class MessageController implements Serializable {


    @EJB private MessageFacadeLocal ejbFacadeLocal;
    private List<Message> items = null;
    private Message selected;

    @Inject
    private SessionUtil session;
    
    @EJB
    private EmailSessionBean emailSessionBean;
    
    public MessageController() {
    }

    public Message getSelected() {
        return selected;
    }

    public void setSelected(Message selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private MessageFacadeLocal getFacade() {
        return ejbFacadeLocal;
    }

    public Message prepareCreate() {
        selected = new Message();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
//        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MessageCreated"));
        try {
            createEmails();
            if (!JsfUtil.isValidationFailed()) {
                items = null;    // Invalidate list of items to trigger re-query.
            }
        } catch (MessagingException ex) {
            Logger.getLogger(MessageController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MessageUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MessageDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Message> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
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

    public Message getMessage(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Message> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Message> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass=Message.class)
    public static class MessageControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MessageController controller = (MessageController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "messageController");
            return controller.getMessage(getKey(value));
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
            if (object instanceof Message) {
                Message o = (Message) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Message.class.getName()});
                return null;
            }
        }

    }
    
    public void createEmails() throws MessagingException, IOException{
        send(ResourceBundle.getBundle("/Bundle").getString("MessageCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    private void send(String successMessage) throws MessagingException, IOException{
        List<Contact> recipientContacts = selected.getReceiverContacts();
        List<ContactGroup> recipientGroups = selected.getReceiverContactGroups();
        
        if(recipientContacts!=null){
            for (int i = 0; i < recipientContacts.size(); i++) {
                sendMessage(recipientContacts.get(i).getEmail(),
                        selected.getSubject(),
                        selected.getContent(),
                        successMessage);
            }
        }
        
        if(recipientGroups!=null){
            for (int i = 0; i < recipientGroups.size(); i++) {
                List<Contact> tempContacts = recipientGroups.get(i).getContacts();
                for (int j = 0; j < tempContacts.size(); j++) {
                        sendMessage(tempContacts.get(i).getEmail(),
                        selected.getSubject(),
                        selected.getContent(),
                        successMessage);
                    
                }
            }
        }
    }

    public void sendMessage(String recipient, String subject, String content, String successMessage) throws IOException, MessagingException{
        String resultMessage = "";

        try {
                emailSessionBean.sendMail(recipient,"","","", subject, content);
                JsfUtil.addSuccessMessage(successMessage);
        }catch(MessagingException ex){
                JsfUtil.addErrorMessage("Error al enviar el mensaje, intentelo más tarde");
        } 
//        catch (InterruptedException ex) {
//            Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        finally{
//                request.setAttribute("Message",resultMessage);
//                getServletContext().getRequestDispatcher("/Result.jsp").forward(request, response);
//        }
//        }
    }
    
}
