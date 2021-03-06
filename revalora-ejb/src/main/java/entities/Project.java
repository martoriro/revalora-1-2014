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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author mariowise
 */
@Entity
public class Project implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Size(min=1, message="El campo Nombre no puede estar vacío")
    private String name;
    
    @NotNull(message="Es necesario proporcionar una fecha de Comienzo para el proyecto")
    @Temporal(TemporalType.DATE) 
    private Date startAt; 
    
    @NotNull(message="Es necesario proporcionar una fecha de Termino para el proyecto")
    @Temporal(TemporalType.DATE) 
    private Date endAt;
    
    @NotNull(message="Es necesario seleccionar una organización")
    @ManyToOne
    private Organization organization;
    
    @ManyToMany
    @JoinTable(name="ACCOUNT_PROJECT",
            joinColumns={@JoinColumn(name="projects_ID")}, 
            inverseJoinColumns={@JoinColumn(name="accounts_RUT")})
    private List<Account> accounts;
    
    @OneToMany(mappedBy = "project")
    private List<ClimateStudy> climateStudies;
    
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

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<Account> getAccounts() {
        if(accounts == null)
            accounts = new ArrayList<Account>();
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<ClimateStudy> getClimateStudies() {
        return climateStudies;
    }

    public void setClimateStudies(List<ClimateStudy> climateStudies) {
        this.climateStudies = climateStudies;
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
        if (!(object instanceof Project)) {
            return false;
        }
        Project other = (Project) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
