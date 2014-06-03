/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbeans.converters;

import entities.ContactGroup;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import sessionbeans.ContactGroupFacadeLocal;
 
@FacesConverter("contactGroupConverter")
public class ContactGroupConverter implements Converter {
 
    @EJB
    private ContactGroupFacadeLocal ejbFacade;
    
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0)
            return ejbFacade.find(Long.parseLong(value));
        else
            return null;
    }
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null)
            return ((ContactGroup) object).getId().toString();
        else 
            return null;
    }   
} 

