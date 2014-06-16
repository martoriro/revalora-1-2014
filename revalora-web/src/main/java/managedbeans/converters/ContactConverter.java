/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbeans.converters;

import entities.Contact;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import sessionbeans.ContactFacadeLocal;
 
@FacesConverter("contactConverter")
public class ContactConverter implements Converter {
 
    @EJB
    private ContactFacadeLocal ejbFacade;
    
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) 
            return ejbFacade.find(value);
        else
            return null;
    }
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null)
            return ((Contact) object).getEmail().toString();
        else 
            return null;
    }   
} 

