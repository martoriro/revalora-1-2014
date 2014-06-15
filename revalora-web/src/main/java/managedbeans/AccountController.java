package managedbeans;

import Validator.SameRutValidator;
import entities.Account;
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
import sessionbeans.AccountFacadeLocal;

@Named("accountController")
@SessionScoped
public class AccountController implements Serializable {

    @EJB
    private AccountFacadeLocal ejbFacade;
    private List<Account> items = null;
    private Account selected;

    private List<Account> filteredAccounts;

    public AccountController() {
    }

    public Account getSelected() {
        return selected;
    }

    public void setSelected(Account selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private AccountFacadeLocal getFacade() {
        return ejbFacade;
    }

    public Account prepareCreate() {
        selected = new Account();
        selected.setAccess(Boolean.TRUE);
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("AccountCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("AccountUpdated"));
    }

    public void updateAndGo(String destiny) {
        persist(PersistAction.UPDATE, "El usuario ha sido actualizado");
        JsfUtil.redirect(destiny);
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("AccountDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void updateAccess(String Rut) {
        if (Rut.equals(selected.getRut())) {
            JsfUtil.addErrorMessage("No puede Modificar la información de Acceso de Usted");
        } else {
            if (selected.getAccess()) {
                selected.setAccess(Boolean.FALSE);
            } else {
                selected.setAccess(Boolean.TRUE);
            }
            update();
        }
    }

    public List<Account> getItems() {
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
                    String rut = selected.getRut();
                    rut = rut.toUpperCase();
                    rut = rut.replace(".", "");
                    rut = rut.replace("-", "");
                    selected.setRut(rut);
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

    public Account getAccount(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<Account> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Account> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Account.class)
    public static class AccountControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AccountController controller = (AccountController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "accountController");
            return controller.getAccount(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Account) {
                Account o = (Account) object;
                return getStringKey(o.getRut());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Account.class.getName()});
                return null;
            }
        }

    }

    public List<Account> getFilteredAccounts() {
        return filteredAccounts;
    }

    public void setFilteredAccounts(List<Account> filteredAccounts) {
        this.filteredAccounts = filteredAccounts;
    }

    public List<Account> filterAccounts(String query) {
        List<Account> allAccounts = ejbFacade.findAll();
        List<Account> filteredAccounts = new ArrayList<Account>();

        for (int i = 0; i < allAccounts.size(); i++) {
            Account skin = allAccounts.get(i);
            if (skin.getNames().toLowerCase().startsWith(query)) {
                filteredAccounts.add(skin);
            }
        }

        return filteredAccounts;
    }
}
