/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.panels;

import kz.supershiny.web.wicket.pages.LoginPage;
import kz.supershiny.web.wicket.pages.admin.AdminBasePage;
import kz.supershiny.web.wicket.pages.catalogue.CataloguePage;
import kz.supershiny.web.wicket.pages.general.BlogPage;
import kz.supershiny.web.wicket.pages.general.ContactsPage;
import kz.supershiny.web.wicket.pages.general.FAQPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.StringResourceModel;

/**
 * Logo + main menu.
 *
 * @author kilrwhle
 */
public final class TopPanel extends BasePanel {

    public TopPanel(String id) {
        super(id);
        
        add(new Link("logoLink") {
            @Override
            public void onClick() {
                setResponsePage(BlogPage.class);
            }
        });
        
        add(new BookmarkablePageLink("catalogueLink", CataloguePage.class));
        add(new BookmarkablePageLink("blogLink", BlogPage.class));
        add(new BookmarkablePageLink("aboutLink", ContactsPage.class));
        add(new BookmarkablePageLink("faqLink", FAQPage.class));
        add(
            new BookmarkablePageLink("loginLink", isLoggedIn()
                    ? AdminBasePage.class
                    : LoginPage.class)
                .add(new Label("loginLinkText", isLoggedIn() 
                        ? new StringResourceModel("admin", TopPanel.this, null).getString()
                        : new StringResourceModel("login", TopPanel.this, null).getString()))
        );
    }
}
