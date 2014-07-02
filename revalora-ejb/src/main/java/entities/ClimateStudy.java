/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author pingeso
 */
@Entity
public class ClimateStudy implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull
    @Size(min=1, message="El campo Nombre no puede estar vacío")
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Temporal(TemporalType.DATE) 
    private Date startAt;
    
    @Temporal(TemporalType.DATE) 
    private Date endAt;
    
    @ManyToOne
    private Account creator;
    
    @ManyToMany
    private List<Contact> contacts; // Contact participants
    
    @ManyToMany
    private List<ContactGroup> groups; // Group participants
    
    @ManyToOne
    private Project project;
    
    @OneToMany
    private List<ClimateStudyInvitation> invitations;
    
    @OneToMany(mappedBy = "climateStudy")
    private List<ClimateStudyAnsware> answares;
    
    @OneToMany(mappedBy = "climateStudy")
    private List<ClimateStudyParticipation> participations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public Account getCreator() {
        return creator;
    }

    public void setCreator(Account creator) {
        this.creator = creator;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<ContactGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<ContactGroup> groups) {
        this.groups = groups;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<ClimateStudyInvitation> getInvitations() {
        if(invitations == null)
            invitations = new ArrayList<ClimateStudyInvitation>();
        return invitations;
    }

    public void setInvitations(List<ClimateStudyInvitation> invitations) {
        this.invitations = invitations;
    }

    public List<ClimateStudyAnsware> getAnswares() {
        if(answares == null)
            answares = new ArrayList<ClimateStudyAnsware>();
        return answares;
    }

    public void setAnswares(List<ClimateStudyAnsware> answares) {
        this.answares = answares;
    }

    public List<ClimateStudyParticipation> getParticipations() {
        if(participations == null)
            participations = new ArrayList<ClimateStudyParticipation>();
        return participations;
    }

    public void setParticipations(List<ClimateStudyParticipation> participations) {
        this.participations = participations;
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
        if (!(object instanceof ClimateStudy)) {
            return false;
        }
        ClimateStudy other = (ClimateStudy) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ClimateStudy[ id=" + id + " ]";
    }
    
}
