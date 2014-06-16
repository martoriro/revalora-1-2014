/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbeansSurvey;

import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author Joel
 */
@Named(value = "createHighcharts")
@SessionScoped
public class createHighcharts implements Serializable {

    
    private ArrayList<Integer> priorities = new ArrayList<Integer>(14);
    private ArrayList<Integer> amount = new ArrayList<Integer>(14);
    /**
     * Creates a new instance of createHighcharts
     */
    @PostConstruct
    public void init(){
        //Prioridad a cada factor
        priorities.add(1);
        priorities.add(0);
        priorities.add(0);
        priorities.add(2);
        priorities.add(3);
        priorities.add(0);
        priorities.add(6);
        priorities.add(0);
        priorities.add(0);
        priorities.add(4);
        priorities.add(0);
        priorities.add(0);
        priorities.add(5);
        priorities.add(1);
        
        //Cantidad de personas que asigno la prioridad a cada factor
        amount.add(5);
        amount.add(1);
        amount.add(2);
        amount.add(0);
        amount.add(6);
        amount.add(7);
        amount.add(8);
        amount.add(1);
        amount.add(4);
        amount.add(1);
        amount.add(5);
        amount.add(3);
        amount.add(4);
        amount.add(2);
    }
    
    public createHighcharts() {
    }

    public ArrayList<Integer> getPriorities() {
        return priorities;
    }

    public void setPriorities(ArrayList<Integer> priorities) {
        this.priorities = priorities;
    }

    public ArrayList<Integer> getAmount() {
        return amount;
    }

    public void setAmount(ArrayList<Integer> amount) {
        this.amount = amount;
    }
    
    
}
