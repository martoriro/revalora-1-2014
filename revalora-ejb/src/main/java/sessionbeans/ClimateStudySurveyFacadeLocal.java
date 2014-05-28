/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sessionbeans;

import entities.ClimateStudySurvey;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author pingeso
 */
@Local
public interface ClimateStudySurveyFacadeLocal {

    void create(ClimateStudySurvey climateStudySurvey);

    void edit(ClimateStudySurvey climateStudySurvey);

    void remove(ClimateStudySurvey climateStudySurvey);

    ClimateStudySurvey find(Object id);

    List<ClimateStudySurvey> findAll();

    List<ClimateStudySurvey> findRange(int[] range);

    int count();
    
}
