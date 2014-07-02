/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Validator;

import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("Validator.PrimeDateRangeValidator")
public class PrimeDateRangeValidator implements Validator {
     
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            // System.out.println("No hay fecha de término");
            return;
        }
        // System.out.println("Ejecutando validator");
        //Leave the null handling of startDate to required="true"
        Object startDateValue = component.getAttributes().get("startAt");
        if (startDateValue==null) {
            // System.out.println("No hay fecha de inicio");
            return;
        }
         
        Date startDate = (Date)startDateValue;
        Date endDate = (Date)value;
        
        if (endDate.before(startDate)) {
            // System.out.println("No pasa la validación");
            FacesMessage msg = new FacesMessage("No es posible que la fecha de término sea anterior a la fecha de inicio", "No es posible que la fecha de término sea anterior a la fecha de inicio");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
}