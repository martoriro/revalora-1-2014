/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans.mailers;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.swing.tree.TreeNode;

/**
 *
 * @author Gustavo Salvo Lara
 */
@Named(value = "menuMail")
@RequestScoped
public class MenuMail {
    private TreeNode root;

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    /**
     * Creates a new instance of MenuMail
     */
    public MenuMail() {
    }

}
