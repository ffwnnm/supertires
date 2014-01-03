/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.supershiny.web.wicket.panels;

import kz.supershiny.core.model.User;
import kz.supershiny.core.services.UserService;
import kz.supershiny.web.wicket.TiresApplication;
import kz.supershiny.web.wicket.pages.HomePage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author kilrwhle
 */
public final class LoginPanel extends Panel {
    
    @SpringBean
    private UserService userService;
    
    private User user;

    public LoginPanel(String id) {
        super(id);
        
        user = ((TiresApplication) getApplication()).getTiresSession().getUser();
        
        add(new LoginForm("loginForm").setVisible(user == null));
        
        add(new Label("username", user == null ? "" : user.getUsername()));
        add(new Link("logout") {
            @Override
            public void onClick() {
                ((TiresApplication) getApplication()).getTiresSession().invalidate();
                setResponsePage(HomePage.class);
            }
        }.setVisible(user != null));
    }
    
    private class LoginForm extends Form {
        
        private String login = "";
        private String password = "";

        public LoginForm(String id) {
            super(id);
            
            add(new TextField("login", new PropertyModel(LoginForm.this, "login")));
            add(new PasswordTextField("password", new PropertyModel(LoginForm.this, "password")));
        }

        @Override
        protected void onSubmit() {
            ((TiresApplication) getApplication())
                    .getTiresSession()
                    .setUser(userService.authenticate(login, password));
            setResponsePage(HomePage.class);
        }
        
    }
}
