/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.panels;

import kz.supershiny.core.model.User;
import kz.supershiny.web.wicket.TiresApplication;
import kz.supershiny.web.wicket.TiresSession;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author kilrwhle
 */
public class BasePanel extends Panel {

    public BasePanel(String id) {
        super(id);
    }
    
    protected User getUser() {
        return ((TiresSession) getSession()).getUser();
    }
    
    protected boolean isLoggedIn() {
        return getUser() != null;
    }
    
    protected TiresSession getTiresSession() {
        return (TiresSession) getSession();
    }
    
    protected TiresApplication getTiresApplication() {
        return (TiresApplication) getApplication();
    }
}
