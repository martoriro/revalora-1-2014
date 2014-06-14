/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package otherclasses;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

@ManagedBean
@SessionScoped
public class BreadCrumb {
	private MenuModel menuModel = new DefaultMenuModel();

	public BreadCrumb(){
	}
	
	public MenuModel getMenuModel() {
		return menuModel;
	}

	public void setMenuModel(MenuModel menuModel) {
		this.menuModel = menuModel;
	}
	
	public String navigateIndex(){
		// Initialize
		this.menuModel = new DefaultMenuModel();
		
		// Create index menuItem
		DefaultMenuItem index = new DefaultMenuItem();
		index.setValue("Index");
		index.setCommand("#{breadCrumb.navigateIndex}");
		index.setUrl("index.xhtml");
		
		// Add menuItems
		this.menuModel.addElement(index);
		
		return "index";
	}
        
        public void navigate(String Pagina,String url){
            // Create index menuItem
            DefaultMenuItem element = new DefaultMenuItem();
            element.setValue(Pagina);
            //index.setCommand("#{breadCrumb.navigateIndex}");
            element.setUrl(url);

            // Add menuItems
            this.menuModel.addElement(element);

            //return Pagina;
        }
}
