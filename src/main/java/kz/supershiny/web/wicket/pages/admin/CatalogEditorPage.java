/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.pages.admin;

import kz.supershiny.core.model.Tire;
import kz.supershiny.web.wicket.pages.BasePage;
import kz.supershiny.web.wicket.pages.LoginPage;
import kz.supershiny.web.wicket.panels.admin.AdminMenuPanel;
import kz.supershiny.web.wicket.panels.admin.CatalogEditorPanel;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author aishmanov
 */
public class CatalogEditorPage extends BasePage {
    
    private Panel itemEditor;
    private Panel listPanel;
    
    public CatalogEditorPage() {
        this(null);
    }

    public CatalogEditorPage(Tire tire) {
        super();
        
        if (!isLoggedIn()) {
            setResponsePage(LoginPage.class);
        }
        
        add(new AdminMenuPanel("adminMenu"));
        
        itemEditor = new CatalogEditorPanel("editorPanel", tire);
        add(itemEditor.setOutputMarkupId(true));
        add(listPanel.setOutputMarkupId(true));
    }
    
}
