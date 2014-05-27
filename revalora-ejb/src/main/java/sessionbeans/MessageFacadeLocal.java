/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sessionbeans;

import entities.Message;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author sylar
 */
@Local
public interface MessageFacadeLocal {

    void create(Message message);

    void edit(Message message);

    void remove(Message message);

    Message find(Object id);

    List<Message> findAll();

    List<Message> findRange(int[] range);

    int count();
    
}
