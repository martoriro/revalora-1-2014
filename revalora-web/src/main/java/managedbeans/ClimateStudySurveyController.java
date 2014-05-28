package managedbeans;

import entities.ClimateStudySurvey;
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
import managedbeans.util.JsfUtil;
import managedbeans.util.JsfUtil.PersistAction;
import sessionbeans.ClimateStudySurveyFacadeLocal;

@Named("climateStudySurveyController")
@SessionScoped
public class ClimateStudySurveyController implements Serializable {

    @EJB
    private ClimateStudySurveyFacadeLocal ejbFacade;
    private List<ClimateStudySurvey> items = null;
    private ClimateStudySurvey selected;
    
    @Inject
    private ClimateStudyController climateStudyController;

    public ClimateStudySurveyController() {
    }

    public ClimateStudySurvey getSelected() {
        return selected;
    }

    public void setSelected(ClimateStudySurvey selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ClimateStudySurveyFacadeLocal getFacade() {
        return ejbFacade;
    }

    public ClimateStudySurvey prepareCreate() {
        selected = new ClimateStudySurvey();
        selected.setClimateStudy(climateStudyController.getSelected());
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ClimateStudySurveyCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ClimateStudySurveyUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ClimateStudySurveyDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<ClimateStudySurvey> getItems() {
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

    public ClimateStudySurvey getClimateStudySurvey(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<ClimateStudySurvey> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ClimateStudySurvey> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = ClimateStudySurvey.class)
    public static class ClimateStudySurveyControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ClimateStudySurveyController controller = (ClimateStudySurveyController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "climateStudySurveyController");
            return controller.getClimateStudySurvey(getKey(value));
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
            if (object instanceof ClimateStudySurvey) {
                ClimateStudySurvey o = (ClimateStudySurvey) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ClimateStudySurvey.class.getName()});
                return null;
            }
        }

    }

    
    
}
