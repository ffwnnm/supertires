/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.panels;

import kz.supershiny.core.model.User;
import kz.supershiny.web.wicket.TiresApplication;
import kz.supershiny.web.wicket.pages.HomePage;
import kz.supershiny.web.wicket.pages.catalogue.CataloguePage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author kilrwhle
 */
public final class TopPanel extends Panel {
    
    private WebMarkupContainer userAccountContainer;
    private User user;

    public TopPanel(String id) {
        super(id);
        
        add(new Link("logoLink") {
            @Override
            public void onClick() {
                setResponsePage(CataloguePage.class);
            }
        });
        
        user = ((TiresApplication) getApplication()).getTiresSession().getUser();
        
        userAccountContainer = new WebMarkupContainer("userAccountContainer");
        add(userAccountContainer.setVisible(user != null));
        
        userAccountContainer.add(new Link("logoutLink") {
            @Override
            public void onClick() {
                user = null;
                getSession().invalidateNow();
                setResponsePage(HomePage.class);
            }
        });
        userAccountContainer.add(new Label(
                    "username", 
                    user == null ? "" : user.getUsername()
                ));
    }
}
