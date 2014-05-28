/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sessionbeans;

import entities.ClimateStudyInvitation;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author pingeso
 */
@Local
public interface ClimateStudyInvitationFacadeLocal {

    void create(ClimateStudyInvitation climateStudyInvitation);

    void edit(ClimateStudyInvitation climateStudyInvitation);

    void remove(ClimateStudyInvitation climateStudyInvitation);

    ClimateStudyInvitation find(Object id);

    List<ClimateStudyInvitation> findAll();

    List<ClimateStudyInvitation> findRange(int[] range);

    int count();
    
}
