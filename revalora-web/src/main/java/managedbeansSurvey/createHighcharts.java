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
    
    private ArrayList<Double> amountPriority1 = new ArrayList<>(14);
    private ArrayList<Double> amountPriority2 = new ArrayList<>(14);
    private ArrayList<Double> amountPriority3 = new ArrayList<>(14);
    private ArrayList<Double> amountPriority4 = new ArrayList<>(14);
    private ArrayList<Double> amountPriority5 = new ArrayList<>(14);
    private ArrayList<Double> amountPriority6 = new ArrayList<>(14);
    private ArrayList<Double> factorPonderation = new ArrayList<>(14);
    private ArrayList<Double> values = new ArrayList<>(5);
    private ArrayList<Double> normalizedValues = new ArrayList<>(5);
    private double ponderation;
    private double n;
    /**
     * Creates a new instance of createHighcharts
     */
    @PostConstruct
    public void init(){
        // Prioridad 1
        amountPriority1.add((double) 2);
        amountPriority1.add((double) 2);
        amountPriority1.add((double) 8);
        amountPriority1.add((double) 3);
        amountPriority1.add((double) 0);
        amountPriority1.add((double) 0);
        amountPriority1.add((double) 0);
        amountPriority1.add((double) 1);
        amountPriority1.add((double) 0);
        amountPriority1.add((double) 6);
        amountPriority1.add((double) 1);
        amountPriority1.add((double) 0);
        amountPriority1.add((double) 2);
        amountPriority1.add((double) 4);
        
        // Calcular la cantidad total de encuestados mediante las elecciones de cualquiera de los arreglos.
        for (int i = 0; i < 14; i++) {
            n+=amountPriority1.get(i);
        }
        
        // Prioridad 2
        amountPriority2.add((double) 1);
        amountPriority2.add((double) 4);
        amountPriority2.add((double) 0);
        amountPriority2.add((double) 5);
        amountPriority2.add((double) 0);
        amountPriority2.add((double) 1);
        amountPriority2.add((double) 0);
        amountPriority2.add((double) 3);
        amountPriority2.add((double) 1);
        amountPriority2.add((double) 2);
        amountPriority2.add((double) 0);
        amountPriority2.add((double) 10);
        amountPriority2.add((double) 0);
        amountPriority2.add((double) 2);
        
        // Prioridad 3
        amountPriority3.add((double) 2);
        amountPriority3.add((double) 0);
        amountPriority3.add((double) 2);
        amountPriority3.add((double) 1);
        amountPriority3.add((double) 1);
        amountPriority3.add((double) 2);
        amountPriority3.add((double) 0);
        amountPriority3.add((double) 2);
        amountPriority3.add((double) 0);
        amountPriority3.add((double) 2);
        amountPriority3.add((double) 2);
        amountPriority3.add((double) 2);
        amountPriority3.add((double) 4);
        amountPriority3.add((double) 9);
        
        // Prioridad 4
        amountPriority4.add((double) 0);
        amountPriority4.add((double) 4);
        amountPriority4.add((double) 3);
        amountPriority4.add((double) 3);
        amountPriority4.add((double) 1);
        amountPriority4.add((double) 1);
        amountPriority4.add((double) 2);
        amountPriority4.add((double) 1);
        amountPriority4.add((double) 2);
        amountPriority4.add((double) 3);
        amountPriority4.add((double) 1);
        amountPriority4.add((double) 3);
        amountPriority4.add((double) 4);
        amountPriority4.add((double) 1);
        
        // Prioridad 5
        amountPriority5.add((double) 5);
        amountPriority5.add((double) 3);
        amountPriority5.add((double) 2);
        amountPriority5.add((double) 0);
        amountPriority5.add((double) 2);
        amountPriority5.add((double) 1);
        amountPriority5.add((double) 1);
        amountPriority5.add((double) 0);
        amountPriority5.add((double) 0);
        amountPriority5.add((double) 1);
        amountPriority5.add((double) 3);
        amountPriority5.add((double) 4);
        amountPriority5.add((double) 4);
        amountPriority5.add((double) 3);
        
        // Prioridad 6
        amountPriority6.add((double) 3);
        amountPriority6.add((double) 3);
        amountPriority6.add((double) 3);
        amountPriority6.add((double) 1);
        amountPriority6.add((double) 1);
        amountPriority6.add((double) 1);
        amountPriority6.add((double) 3);
        amountPriority6.add((double) 3);
        amountPriority6.add((double) 2);
        amountPriority6.add((double) 5);
        amountPriority6.add((double) 0);
        amountPriority6.add((double) 0);
        amountPriority6.add((double) 2);
        amountPriority6.add((double) 2);
        
        for (int i = 0; i < 14; i++) {
            ponderation = amountPriority1.get(i) + amountPriority2.get(i)/2 + amountPriority3.get(i)/3 + amountPriority4.get(i)/4 + amountPriority5.get(i)/5 + amountPriority6.get(i)/6;
            System.out.println("Factor "+(i+1)+": "+ponderation);
            factorPonderation.add(ponderation);
        }
        // Amor
        values.add(factorPonderation.get(1) + factorPonderation.get(3) + factorPonderation.get(9));
        // Rectitud
        values.add(factorPonderation.get(0) + factorPonderation.get(2) + factorPonderation.get(4) + factorPonderation.get(5) + factorPonderation.get(7) + factorPonderation.get(11));
        // Paz
        values.add(factorPonderation.get(8) + factorPonderation.get(12) + factorPonderation.get(13));
        // Verdad
        values.add(factorPonderation.get(6));
        // No violencia
        values.add(factorPonderation.get(10));
        
        // Valores normalizados
        normalizedValues.add(values.get(0) * 21.8182 / n + 30);
        normalizedValues.add(values.get(1) * 16.3265 / n + 30);
        normalizedValues.add(values.get(2) * 21.8182 / n + 30);
        normalizedValues.add(values.get(3) * 40 / n + 30);
        normalizedValues.add(values.get(4) * 40 / n + 30);
        }
    
    public createHighcharts() {
    }

    public ArrayList<Double> getAmountPriority1() {
        return amountPriority1;
    }

    public void setAmountPriority1(ArrayList<Double> amountPriority1) {
        this.amountPriority1 = amountPriority1;
    }

    public ArrayList<Double> getAmountPriority2() {
        return amountPriority2;
    }

    public void setAmountPriority2(ArrayList<Double> amountPriority2) {
        this.amountPriority2 = amountPriority2;
    }

    public ArrayList<Double> getAmountPriority3() {
        return amountPriority3;
    }

    public void setAmountPriority3(ArrayList<Double> amountPriority3) {
        this.amountPriority3 = amountPriority3;
    }

    public ArrayList<Double> getAmountPriority4() {
        return amountPriority4;
    }

    public void setAmountPriority4(ArrayList<Double> amountPriority4) {
        this.amountPriority4 = amountPriority4;
    }

    public ArrayList<Double> getAmountPriority5() {
        return amountPriority5;
    }

    public void setAmountPriority5(ArrayList<Double> amountPriority5) {
        this.amountPriority5 = amountPriority5;
    }

    public ArrayList<Double> getAmountPriority6() {
        return amountPriority6;
    }

    public void setAmountPriority6(ArrayList<Double> amountPriority6) {
        this.amountPriority6 = amountPriority6;
    }

    public ArrayList<Double> getValues() {
        return values;
    }

    public void setValues(ArrayList<Double> values) {
        this.values = values;
    }

    public ArrayList<Double> getNormalizedValues() {
        return normalizedValues;
    }

    public void setNormalizedValues(ArrayList<Double> normalizedValues) {
        this.normalizedValues = normalizedValues;
    }

}
