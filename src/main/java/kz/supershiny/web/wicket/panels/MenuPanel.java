/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.panels;

import kz.supershiny.web.wicket.pages.HomePage;
import kz.supershiny.web.wicket.pages.admin.CatalogEditorPage;
import kz.supershiny.web.wicket.pages.catalogue.CataloguePage;
import kz.supershiny.web.wicket.pages.general.ContactsPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author kilrwhle
 */
public class MenuPanel extends Panel {

    public MenuPanel(String id) {
        super(id);
        
        add(new BookmarkablePageLink("homeLink", HomePage.class));
        add(new BookmarkablePageLink("catalogueLink", CataloguePage.class));
//        add(new BookmarkablePageLink("contactsLink", ContactsPage.class));
//        add(new BookmarkablePageLink("adminLink", CatalogEditorPage.class));
    }
    
}
