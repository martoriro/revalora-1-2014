/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sessionbeans;

import entities.ContactGroup;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author sylar
 */
@Local
public interface ContactGroupFacadeLocal {

    void create(ContactGroup contactGroup);

    void edit(ContactGroup contactGroup);

    void remove(ContactGroup contactGroup);

    ContactGroup find(Object id);

    List<ContactGroup> findAll();

    List<ContactGroup> findRange(int[] range);

    int count();
    
}
