/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.supershiny.web.wicket.panels;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author kilrwhle
 */
public final class LoginPanel extends Panel {
    
    private LoginForm form;

    public LoginPanel(String id) {
        super(id);
        
        form = new LoginForm("loginForm");
        add(form.setOutputMarkupId(true));
    }
    
    private class LoginForm extends Form {
        
        private TextField login;
        private PasswordTextField password;

        public LoginForm(String id) {
            super(id);
            
            login = new TextField("login");
            password = new PasswordTextField("password");
        }
        
    }
}
