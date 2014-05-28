package managedbeans;

import entities.ContactGroup;
import java.io.Serializable;
import java.util.ArrayList;
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
import javax.inject.Named;
import managedbeans.util.JsfUtil;
import managedbeans.util.JsfUtil.PersistAction;
import sessionbeans.ContactGroupFacadeLocal;

@Named("contactGroupController")
@SessionScoped
public class ContactGroupController implements Serializable {

    @EJB
    private ContactGroupFacadeLocal ejbFacadeLocal;
    private List<ContactGroup> items = null;
    private ContactGroup selected;

    public ContactGroupController() {
    }

    public ContactGroup getSelected() {
        return selected;
    }

    public void setSelected(ContactGroup selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ContactGroupFacadeLocal getFacade() {
        return ejbFacadeLocal;
    }

    public ContactGroup prepareCreate() {
        selected = new ContactGroup();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ContactGroupCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ContactGroupUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ContactGroupDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<ContactGroup> getItems() {
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

    public ContactGroup getContactGroup(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<ContactGroup> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ContactGroup> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = ContactGroup.class)
    public static class ContactGroupControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ContactGroupController controller = (ContactGroupController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "contactGroupController");
            return controller.getContactGroup(getKey(value));
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
            if (object instanceof ContactGroup) {
                ContactGroup o = (ContactGroup) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ContactGroup.class.getName()});
                return null;
            }
        }

    }
    
    public List<ContactGroup> filterContactGroups(String query) {
        List<ContactGroup> allContactGroup = ejbFacadeLocal.findAll();
        List<ContactGroup> filteredContactGroup = new ArrayList<ContactGroup>();
        
        for(int i = 0; i < allContactGroup.size(); i++) {
            ContactGroup skin = allContactGroup.get(i);
            if(skin.getName().toLowerCase().startsWith(query)) {
                filteredContactGroup.add(skin);
            }
        }
        
        return filteredContactGroup;
    }

}
