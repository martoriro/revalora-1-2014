/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sessionbeans;

import entities.ClimateStudy;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

    public ClimateStudyFacade() {
        super(ClimateStudy.class);
    }
    
}
