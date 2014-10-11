/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.pages.error;

import kz.supershiny.web.wicket.pages.BasePage;
import kz.supershiny.web.wicket.pages.catalogue.CataloguePage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

/**
 *
 * @author aishmanov
 */
public class ErrorPage extends BasePage {

    public ErrorPage() {
        super();
        
        add(new BookmarkablePageLink("homeLink", CataloguePage.class));
        
        add(new BookmarkablePageLink("backHomeLink", CataloguePage.class));
    }
    
}
