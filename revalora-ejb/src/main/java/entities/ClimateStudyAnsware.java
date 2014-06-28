/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author pingeso
 */
@Entity
public class ClimateStudyAnsware implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private Contact contact;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    private ClimateStudy climateStudy;
    
    private int question;
    
    private double answare;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public ClimateStudy getClimateStudy() {
        return climateStudy;
    }

    public void setClimateStudy(ClimateStudy climateStudy) {
        this.climateStudy = climateStudy;
    }

    public int getQuestion() {
        return question;
    }

    public void setQuestion(int question) {
        this.question = question;
    }

    public double getAnsware() {
        return answare;
    }

    public void setAnsware(int answare) {
        this.answare = answare;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClimateStudyAnsware)) {
            return false;
        }
        ClimateStudyAnsware other = (ClimateStudyAnsware) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ClimateStudyAnsware[ id=" + id + " ]";
    }
    
}
