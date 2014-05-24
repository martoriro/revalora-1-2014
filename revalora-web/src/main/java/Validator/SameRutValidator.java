/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Validator;

import entities.Account;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import sessionbeans.AccountFacadeLocal;

@FacesValidator("Validator.SameRutValidator")
public class SameRutValidator implements Validator {

    private String rut;
    
    @EJB
    private AccountFacadeLocal ejbFacade;
    private Account selected;
    
    public SameRutValidator() {
    }
    
    private AccountFacadeLocal getFacade() {
        return ejbFacade;
    }
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        rut = value.toString();
        if (!sameRut(rut)) {

            FacesMessage msg = new FacesMessage("Este RUT ya se encuentra registrado en el sistema", "Same Rut.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }

    public boolean sameRut(String rut) {
        boolean validacion = true;
        selected = getFacade().find(rut);
        if(selected != null){
            validacion = false;
        }
        return validacion;
    }
}