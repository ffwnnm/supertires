/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.supershiny.web.wicket.pages;

import kz.supershiny.web.wicket.panels.ContentPanel;
import kz.supershiny.web.wicket.panels.TopPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.StringResourceModel;

/**
 *
 * @author kilrwhle
 */
public class HomePage extends BasePage {

    public HomePage() {
        super();
        
        Label title = new Label("title", new StringResourceModel("titleHome", HomePage.this, null));
        add(title);
        
        add(new TopPanel("topPanel"));
        add(new ContentPanel("contentPanel"));
    }
    
}
