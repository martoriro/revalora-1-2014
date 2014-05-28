/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sessionbeans;

import entities.Answer;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author pingeso
 */
@Local
public interface AnswerFacadeLocal {

    void create(Answer answer);

    void edit(Answer answer);

    void remove(Answer answer);

    Answer find(Object id);

    List<Answer> findAll();

    List<Answer> findRange(int[] range);

    int count();
    
}
