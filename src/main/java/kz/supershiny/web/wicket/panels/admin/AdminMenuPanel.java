/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.panels.admin;

import kz.supershiny.web.wicket.pages.admin.BlogEntryPage;
import kz.supershiny.web.wicket.pages.admin.CatalogEditorPage;
import kz.supershiny.web.wicket.pages.admin.ManufacturersEditorPage;
import kz.supershiny.web.wicket.pages.admin.ProposalsViewPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author kilrwhle
 */
public class AdminMenuPanel extends Panel {

    public AdminMenuPanel(String id) {
        super(id);
        
        add(new BookmarkablePageLink("catalogLink", CatalogEditorPage.class));
        add(new BookmarkablePageLink("blogLink", BlogEntryPage.class));
        add(new BookmarkablePageLink("companyLink", ManufacturersEditorPage.class));
        add(new BookmarkablePageLink("proposalsLink", ProposalsViewPage.class));
    }
    
}
