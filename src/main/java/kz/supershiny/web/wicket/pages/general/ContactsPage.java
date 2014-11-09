/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.pages.general;

import kz.supershiny.web.wicket.pages.BasePage;
import kz.supershiny.web.wicket.pages.catalogue.CataloguePage;
import org.apache.wicket.markup.html.link.Link;

/**
 *
 * @author aishmanov
 */
public class ContactsPage extends BasePage {

    public ContactsPage() {
        super();
        
        add(new Link("homepage") {
            @Override
            public void onClick() {
                setResponsePage(CataloguePage.class);
            }
        });
    }
    
}
