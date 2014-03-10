/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.pages.admin;

import kz.supershiny.web.wicket.pages.BasePage;
import kz.supershiny.web.wicket.pages.LoginPage;
import kz.supershiny.web.wicket.panels.admin.AdminMenuPanel;

/**
 *
 * @author kilrwhle
 */
public class ProposalsViewPage extends BasePage {

    public ProposalsViewPage() {
        super();
        
        if(getUser() == null) {
            setResponsePage(LoginPage.class);
        }
        
        add(new AdminMenuPanel("adminMenu"));
    }
    
}