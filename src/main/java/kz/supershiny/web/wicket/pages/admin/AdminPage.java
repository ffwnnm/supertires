/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.pages.admin;

import kz.supershiny.core.model.Tire;
import kz.supershiny.web.wicket.pages.BasePage;
import kz.supershiny.web.wicket.panels.admin.CatalogEditorPanel;

/**
 *
 * @author aishmanov
 */
public class AdminPage extends BasePage {
    
    private Tire currentTire;
    
    private CatalogEditorPanel itemEditor;

    public AdminPage() {
        super();
        
        currentTire = new Tire();
        
        itemEditor = new CatalogEditorPanel("contentPanel");
        
        add(itemEditor.setOutputMarkupId(true));
    }
    
}
