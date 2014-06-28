/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sessionbeans;

import entities.ClimateStudy;
import entities.ClimateStudyAnsware;
import entities.Contact;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author pingeso
 */
@Local
public interface ClimateStudyFacadeLocal {

    void create(ClimateStudy climateStudy);

    void edit(ClimateStudy climateStudy);

    void remove(ClimateStudy climateStudy);

    ClimateStudy find(Object id);

    List<ClimateStudy> findAll();

    List<ClimateStudy> findRange(int[] range);

    int count();
    
    boolean response(Contact contact, ClimateStudy study);
    
    List<ClimateStudyAnsware> getAverages(ClimateStudy study);
            
}
