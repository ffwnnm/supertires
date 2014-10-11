/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.pages.error;

import kz.supershiny.web.wicket.pages.BasePage;
import kz.supershiny.web.wicket.pages.catalogue.CataloguePage;
import kz.supershiny.web.wicket.pages.general.BlogPage;
import kz.supershiny.web.wicket.pages.general.ContactsPage;
import kz.supershiny.web.wicket.pages.general.FAQPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

/**
 *
 * @author kilrwhle
 */
public class Error404Page extends BasePage {

    public Error404Page() {
        super();
        
        add(new BookmarkablePageLink("homeLink", CataloguePage.class));
        
        add(new BookmarkablePageLink("catalogueLink", CataloguePage.class));
        add(new BookmarkablePageLink("blogLink", BlogPage.class));
        add(new BookmarkablePageLink("aboutLink", ContactsPage.class));
        add(new BookmarkablePageLink("faqLink", FAQPage.class));
    }
    
}
