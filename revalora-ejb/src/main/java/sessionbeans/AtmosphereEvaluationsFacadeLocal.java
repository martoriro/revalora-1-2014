/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sessionbeans;

import entities.AtmosphereEvaluations;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author pingeso
 */
@Local
public interface AtmosphereEvaluationsFacadeLocal {

    void create(AtmosphereEvaluations atmosphereEvaluations);

    void edit(AtmosphereEvaluations atmosphereEvaluations);

    void remove(AtmosphereEvaluations atmosphereEvaluations);

    AtmosphereEvaluations find(Object id);

    List<AtmosphereEvaluations> findAll();

    List<AtmosphereEvaluations> findRange(int[] range);

    int count();
    
}
