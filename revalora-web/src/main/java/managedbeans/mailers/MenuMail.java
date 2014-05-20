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
        root = new TreeNode(root);
        root.
        TreeNode documents = new TreeNode("Documents", root);
        TreeNode pictures = new TreeNode("Pictures", root);
        TreeNode music = new TreeNode("Music", root);
        TreeNode work = new TreeNode("Work", documents);
        TreeNode primefaces = new TreeNode("PrimeFaces", documents);
//Documents
        TreeNode expenses = new TreeNode("document", "Expenses.doc", work);
        TreeNode resume = new TreeNode("document", "Resume.doc", work);
        TreeNode refdoc = new TreeNode("document", "RefDoc.pages", primefaces);
//Pictures
        TreeNode barca = new TreeNode("picture", "barcelona.jpg", pictures);
        TreeNode primelogo = new TreeNode("picture", "logo.jpg", pictures);
        TreeNode optimus = new TreeNode("picture", "optimus.png", pictures);
//Music
        TreeNode turkish = new TreeNode("Turkish", music);
        TreeNode cemKaraca = new TreeNode("Cem Karaca", turkish);
        TreeNode erkinKoray = new TreeNode("Erkin Koray", turkish);
        TreeNode mogollar = new TreeNode("Mogollar", turkish);
        TreeNode nemalacak = new TreeNode("mp3", "Nem Alacak Felek Benim", cemKaraca);
        TreeNode resimdeki = new TreeNode("mp3", "Resimdeki Goz Yaslari", cemKaraca);
        TreeNode copculer = new TreeNode("mp3", "Copculer", erkinKoray);
        TreeNode oylebirgecer = new TreeNode("mp3", "Oyle Bir Gecer", erkinKoray);
        TreeNode toprakana = new TreeNode("mp3", "Toprak Ana", mogollar);
        TreeNode bisiyapmali = new TreeNode("mp3", "Bisi Yapmali", mogollar);
    }

}
