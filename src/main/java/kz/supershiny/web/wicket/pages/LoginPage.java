/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.pages;

import kz.supershiny.core.exceptions.TiresAuthenticationException;
import kz.supershiny.core.model.User;
import kz.supershiny.core.services.UserService;
import kz.supershiny.web.wicket.TiresApplication;
import kz.supershiny.web.wicket.pages.admin.CatalogEditorPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kilrwhle
 */
public class LoginPage extends BasePage {
    
    private static final Logger LOG = LoggerFactory.getLogger(LoginPage.class);
    
    @SpringBean
    private UserService userService;
    
    private User user;

    public LoginPage() {
        super();
        
        if((user = getUser()) != null) {
            setResponsePage(CatalogEditorPage.class);
        }
        
        add(new FeedbackPanel("loginFeedback").setOutputMarkupId(true));
        add(new LoginForm("loginForm").setVisible(user == null));
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
            User user;
            try {
                user = userService.authenticate(login, password);
                ((TiresApplication) getApplication())
                        .getTiresSession()
                        .setUser(user);
                setResponsePage(CatalogEditorPage.class);
            } catch (TiresAuthenticationException ex) {
                user = null;
                LoginPage.this.error(new StringResourceModel("error.authentication", LoginPage.this, null).getString());
                LOG.info("Authentication failed: " + login + " / " + password);
            }
        }
    }
}
