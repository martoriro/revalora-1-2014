/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sessionbeans;

import entities.Projects;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author pingeso
 */
@Local
public interface ProjectsFacadeLocal {

    void create(Projects projects);

    void edit(Projects projects);

    void remove(Projects projects);

    Projects find(Object id);

    List<Projects> findAll();

    List<Projects> findRange(int[] range);

    int count();
    
}
