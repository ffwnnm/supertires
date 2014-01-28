/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.panels;

import kz.supershiny.web.wicket.pages.HomePage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author kilrwhle
 */
public final class TopPanel extends Panel {

    public TopPanel(String id) {
        super(id);
        
        add(new Link("logoLink") {
            @Override
            public void onClick() {
                setResponsePage(HomePage.class);
            }
        });
    }
}
