/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.pages;

import kz.supershiny.core.exceptions.TiresAuthenticationException;
import kz.supershiny.core.model.User;
import kz.supershiny.core.services.UserService;
import kz.supershiny.web.wicket.TiresApplication;
import kz.supershiny.web.wicket.pages.admin.AdminBasePage;
import kz.supershiny.web.wicket.pages.catalogue.CataloguePage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
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
    
    private FeedbackPanel feed;
    private User user;

    public LoginPage() {
        super();
        
        if((user = getUser()) != null) {
            setResponsePage(AdminBasePage.class);
            return;
        }
        
        add(new Link("homepage") {
            @Override
            public void onClick() {
                setResponsePage(CataloguePage.class);
            }
        });
        
        feed = new FeedbackPanel("loginFeedback");
        add(feed.setVisible(false).setOutputMarkupPlaceholderTag(true));
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
                setResponsePage(AdminBasePage.class);
            } catch (TiresAuthenticationException ex) {
                user = null;
                LoginPage.this.error(new StringResourceModel("error.authentication", LoginPage.this, null).getString());
                feed.setVisible(true);
                LOG.info("Authentication failed: " + login + " / " + password);
            }
        }
    }
}
