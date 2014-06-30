/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sessionbeans;

import entities.ClimateStudyParticipation;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author sylar
 */
@Local
public interface ClimateStudyParticipationFacadeLocal {

    void create(ClimateStudyParticipation climateStudyParticipation);

    void edit(ClimateStudyParticipation climateStudyParticipation);

    void remove(ClimateStudyParticipation climateStudyParticipation);

    ClimateStudyParticipation find(Object id);

    List<ClimateStudyParticipation> findAll();

    List<ClimateStudyParticipation> findRange(int[] range);

    int count();
    
}
