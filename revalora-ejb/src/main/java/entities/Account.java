/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author pingeso
 */
@Entity
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    @Size(min = 1, message="Debe colocar un RUT")    
    private String rut;
    
    @NotNull
    @Size (min = 4, message = "Su contraseña debe ser de al menos 4 caracteres")
    private String password;
    
    @NotNull
    @Size(min=1, message= "El campo Nombre no puede estar vacío")
    @Pattern(regexp="^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]+$", message="El campo nombre solo puede contener letras.")
    private String firstName;
    
    @NotNull
    @Size(min=1, message= "El campo Apellido no puede estar vacío")
    @Pattern(regexp="^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]+$", message="El campo Apellido solo puede contener letras.")
    private String lastName;
    
    @NotNull
    private String gender;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Past(message = "Coloque una fecha de nacimiento anterior al día de hoy")
    @NotNull(message= "Debe colocar una fecha de nacimiento")
    private Date birthdate;
    
    private String address;
    
    @Pattern(regexp = "^[0-9]+$", message = "Ingrese solo números e.g = 56991234567")
    private String phone;
    
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$" , message = "Ingrese un correo valido, e.g.=mail@mail.com")
    private String email;
    
    private String position;
    
    private boolean access;
    
    @OneToMany
    private List<Contact> contacts;
    
    @OneToMany
    private List<ContactGroup> contactgroups;
        
    @JoinColumn(nullable = false)
    @ManyToOne
    private AccountType accountType;
    
    @ManyToMany(mappedBy = "accounts")
    @JoinTable(name="ACCOUNT_PROJECT",
        joinColumns={@JoinColumn(name="projects_ID")}, 
        inverseJoinColumns={@JoinColumn(name="accounts_RUT")})
    private List<Project> projects;

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = this.sha256(password);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    
    public boolean getAccess(){
        return access;
    }
    
    public void setAccess(boolean access){
        this.access = access;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<ContactGroup> getContactgroups() {
        return contactgroups;
    }

    public void setContactgroups(List<ContactGroup> contactgroups) {
        this.contactgroups = contactgroups;
    }

    public List<Project> getProjects() {
        if(projects == null)
                projects = new ArrayList<Project>();
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rut != null ? rut.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rut fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.rut == null && other.rut != null) || (this.rut != null && !this.rut.equals(other.rut))) {
            return false;
        }
        return true;
    }
    
    private String sha256(String base) {
        System.out.println("Calling Account.sha256 (" + base + ")");
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
            
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public java.lang.String toString() {
        return rut;
    }
    
    public java.lang.String getNames() {
        return firstName + " " + lastName;
    }
    
    public java.lang.String getAccessName(){
        String accessName = "";
        if(access){
            accessName = "Habilitado";
        }
        else{
            accessName = "Deshabilitado";
        }
        return accessName;
    }
    
}
