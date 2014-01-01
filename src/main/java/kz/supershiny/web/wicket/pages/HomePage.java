/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.supershiny.web.wicket.pages;

import kz.supershiny.core.services.UserServiceImpl;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author kilrwhle
 */
public class HomePage extends BasePage {
    
    @SpringBean
    private UserServiceImpl userService;

    public HomePage() {
        super();
        
        Label title = new Label("title", new StringResourceModel("titleHome", HomePage.this, null));
        add(title);
        
        userService.testMethod();
    }
    
}
