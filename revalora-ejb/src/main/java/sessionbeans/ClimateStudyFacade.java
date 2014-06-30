/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sessionbeans;

import entities.ClimateStudy;
import entities.ClimateStudyAnsware;
import entities.ClimateStudyInvitation;
import entities.Contact;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author pingeso
 */
@Stateless
public class ClimateStudyFacade extends AbstractFacade<ClimateStudy> implements ClimateStudyFacadeLocal {
    @PersistenceContext(unitName = "cl.diinf_revalora-ejb_ejb_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @Override
    public boolean response(Contact contact, ClimateStudy study) {
        if((contact == null) || (study == null))
            return false;
        Query q = em.createQuery("SELECT cp FROM ClimateStudyParticipation cp JOIN cp.contact c JOIN cp.climateStudy s WHERE c.email = :email AND s.id = :studyId");
        q.setParameter("email", contact.getEmail());
        q.setParameter("studyId", study.getId());
        if(q.getResultList().size() == 0)
            return false;
        return true;
    }
    
    @Override
    public List<ClimateStudyAnsware> getAverages(ClimateStudy study) {
        Query q = em.createNativeQuery("select QUESTION as ID, QUESTION, AVG(ANSWARE) as ANSWARE, CONTACT_EMAIL from CLIMATESTUDYANSWARE where CLIMATESTUDY_ID = " + study.getId() + " group by QUESTION", ClimateStudyAnsware.class);
        return q.getResultList();
    }
    
    public void setValueProposal(ClimateStudy cs) {
        int[] valueProposal = new int[5];        
        
    }
    
    public int[] setNormalizedEvalValue(int[] values) {
        int[] normValues = new int[5];
        for (int i = 0; i < 4; i++) {
            normValues[i] = ((values[i]*10) + 20);
        }
        return normValues;
    }

    public ClimateStudyFacade() {
        super(ClimateStudy.class);
    }
    
}
