/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.supershiny.web.wicket.pages;

import kz.supershiny.core.model.User;
import kz.supershiny.web.wicket.TiresSession;
import org.apache.wicket.markup.html.WebPage;

/**
 *
 * @author kilrwhle
 */
public class BasePage extends WebPage {

    public BasePage() {
        super();
    }
    
    protected User getUser() {
        return ((TiresSession) getSession()).getUser();
    }
    
    protected TiresSession getTiresSession() {
        return (TiresSession) getSession();
    }
}
