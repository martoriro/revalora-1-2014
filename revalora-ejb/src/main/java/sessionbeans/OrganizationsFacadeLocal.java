/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sessionbeans;

import entities.Organizations;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author pingeso
 */
@Local
public interface OrganizationsFacadeLocal {

    void create(Organizations organizations);

    void edit(Organizations organizations);

    void remove(Organizations organizations);

    Organizations find(Object id);

    List<Organizations> findAll();

    List<Organizations> findRange(int[] range);

    int count();
    
}
