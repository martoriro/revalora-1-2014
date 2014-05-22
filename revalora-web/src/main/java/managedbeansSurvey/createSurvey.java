/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbeansSurvey;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Joel
 */
@Named(value = "createSurvey")
@SessionScoped
public class createSurvey implements Serializable {

    private Date DSurvStart;
    private String rutParticipant;

    private List<Integer> factors = new ArrayList<Integer>(14);
    private List<Integer> priorities = new ArrayList<Integer>(6);
    List<String> statements = new ArrayList<String>();
    private String statement1;
    private String statement2;
    private String statement3;
    private String statement4;
    private String statement5;
    private String statement6;
    private String statement7;
    private String statement8;
    private String statement9;
    private String statement10;
    private String statement11;
    private String statement12;
    private String statement13;
    private String statement14;

    /**
     * Creates a new instance of createSurvey
     */
    public createSurvey() {
    }
    
    public void createList(){
        statements.add(statement1);
        statements.add(statement2);
        statements.add(statement3);
        statements.add(statement4);
        statements.add(statement5);
        statements.add(statement6);
        statements.add(statement7);
        statements.add(statement8);
        statements.add(statement9);
        statements.add(statement10);
        statements.add(statement11);
        statements.add(statement12);
        statements.add(statement13);
        statements.add(statement14);
    }

    public Date getDSurvStart() {
        return DSurvStart;
    }

    public void setDSurvStart(Date DSurvStart) {
        this.DSurvStart = DSurvStart;
    }

    public String getRutParticipant() {
        return rutParticipant;
    }

    public void setRutParticipant(String rutParticipant) {
        this.rutParticipant = rutParticipant;
    }

    public List<Integer> getFactors() {
        return factors;
    }

    public void setFactors(List<Integer> factors) {
        this.factors = factors;
    }

    public List<Integer> getPriorities() {
        return priorities;
    }

    public void setPriorities(List<Integer> priorities) {
        this.priorities = priorities;
    }

    public List<String> getStatements() {
        return statements;
    }

    public void setStatements(List<String> statements) {
        this.statements = statements;
    }

    public String getStatement1() {
        return statement1;
    }

    public void setStatement1(String statement1) {
        this.statement1 = statement1;
    }

    public String getStatement2() {
        return statement2;
    }

    public void setStatement2(String statement2) {
        this.statement2 = statement2;
    }

    public String getStatement3() {
        return statement3;
    }

    public void setStatement3(String statement3) {
        this.statement3 = statement3;
    }

    public String getStatement4() {
        return statement4;
    }

    public void setStatement4(String statement4) {
        this.statement4 = statement4;
    }

    public String getStatement5() {
        return statement5;
    }

    public void setStatement5(String statement5) {
        this.statement5 = statement5;
    }

    public String getStatement6() {
        return statement6;
    }

    public void setStatement6(String statement6) {
        this.statement6 = statement6;
    }

    public String getStatement7() {
        return statement7;
    }

    public void setStatement7(String statement7) {
        this.statement7 = statement7;
    }

    public String getStatement8() {
        return statement8;
    }

    public void setStatement8(String statement8) {
        this.statement8 = statement8;
    }

    public String getStatement9() {
        return statement9;
    }

    public void setStatement9(String statement9) {
        this.statement9 = statement9;
    }

    public String getStatement10() {
        return statement10;
    }

    public void setStatement10(String statement10) {
        this.statement10 = statement10;
    }

    public String getStatement11() {
        return statement11;
    }

    public void setStatement11(String statement11) {
        this.statement11 = statement11;
    }

    public String getStatement12() {
        return statement12;
    }

    public void setStatement12(String statement12) {
        this.statement12 = statement12;
    }

    public String getStatement13() {
        return statement13;
    }

    public void setStatement13(String statement13) {
        this.statement13 = statement13;
    }

    public String getStatement14() {
        return statement14;
    }

    public void setStatement14(String statement14) {
        this.statement14 = statement14;
    }
    
    
    
}
