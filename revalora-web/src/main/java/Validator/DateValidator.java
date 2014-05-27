/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Validator;

import java.util.Calendar;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("Validator.DateValidator")
public class DateValidator implements Validator {

    public DateValidator() {
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }
        Date date = (Date) value;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date present = cal.getTime();
        if (date.compareTo(present) < 0) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Fecha Inválida.", "La fecha no puede haber pasado."));
        }
        try {
            UIInput startDateComponent = (UIInput) component.getAttributes().get("startAt");
            if (!startDateComponent.isValid()) {
                return;
            }
            Date startDate = (Date) startDateComponent.getValue();
            if (startDate == null) {
                return;
            }
            if (startDate.after(date)) {
                startDateComponent.setValid(false);
                throw new ValidatorException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "La fecha de inicio está después de la fecha de término.", null));
            }
        } catch (ValidatorException e) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "La fecha de término debe ser posterior al inicio.", null));
        } catch (Exception e) {
        }
    }
}
