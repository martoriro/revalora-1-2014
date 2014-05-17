/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbeans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import managedbeans.util.JsfUtil;
import managedbeans.util.SessionUtil;

/**
 *
 * @author pingeso
 */
@Named(value = "loginController")
@RequestScoped
public class LoginController {

    @Inject 
    SessionUtil sessionUtil;
    
    private String rut;
    
    private String password;
    
    /**
     * Creates a new instance of LoginController
     */
    public LoginController() {
    }

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
        this.password = password;
    }
    
    public void login() {
        System.out.println("Calling login for " + rut);

        if (sessionUtil.login(rut, password)) {
            System.out.println("SessionController: Usuaro logeado de forma exitosa: " + rut);
            JsfUtil.redirect(sessionUtil.route66());
        } 
        else {
            System.out.println("SessionController: Login fail");
            JsfUtil.addErrorMessage("El RUT y/o contraseña no coinciden");
        }
    }
    
    public void logout() {
        sessionUtil.logout();
        JsfUtil.redirect(sessionUtil.logout());
    }
    
    public boolean hasIdentity() {
        return sessionUtil.hasIdentity();
    }
}
