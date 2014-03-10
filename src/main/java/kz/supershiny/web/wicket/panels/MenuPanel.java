/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.panels;

import kz.supershiny.core.model.User;
import kz.supershiny.web.wicket.TiresApplication;
import kz.supershiny.web.wicket.pages.HomePage;
import kz.supershiny.web.wicket.pages.LoginPage;
import kz.supershiny.web.wicket.pages.admin.CatalogEditorPage;
import kz.supershiny.web.wicket.pages.catalogue.CataloguePage;
import kz.supershiny.web.wicket.pages.catalogue.ProposalPage;
import kz.supershiny.web.wicket.pages.general.ContactsPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;

/**
 *
 * @author kilrwhle
 */
public class MenuPanel extends Panel {
    
    private User user;

    public MenuPanel(String id) {
        super(id);
        
        user = ((TiresApplication) getApplication()).getTiresSession().getUser();
        
        add(new BookmarkablePageLink("homeLink", HomePage.class));
        add(new BookmarkablePageLink("catalogueLink", CataloguePage.class));
//        add(new BookmarkablePageLink("proposeLink", ProposalPage.class));
        add(new BookmarkablePageLink("loginLink", LoginPage.class)
                .add(new Label("adminPart", new StringResourceModel(
                                    user == null ? "signin" : "admin", 
                                    MenuPanel.this, 
                                    null
                                ).getString())));
    }
    
}
