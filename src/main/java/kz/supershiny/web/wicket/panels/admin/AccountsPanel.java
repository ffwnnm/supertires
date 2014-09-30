/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.panels.admin;

import java.util.ArrayList;
import java.util.List;
import kz.supershiny.core.exceptions.TiresPersistException;
import kz.supershiny.core.model.User;
import kz.supershiny.core.services.UserService;
import kz.supershiny.web.wicket.components.ConfirmationLink;
import kz.supershiny.web.wicket.pages.admin.AdminBasePage;
import kz.supershiny.web.wicket.panels.BasePanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kilrwhle
 */
public class AccountsPanel extends BasePanel {
    
    private static final Logger LOG = LoggerFactory.getLogger(AccountsPanel.class);
    
    @SpringBean
    private UserService userService;
    
    private User user;
    private List<User> users;
    private AccountsForm accountsForm;

    public AccountsPanel(String id) {
        super(id);
        
        initData();
        
        add(new FeedbackPanel("feedback"));
        
        add(accountsForm = new AccountsForm("accountsForm"));
        accountsForm.setOutputMarkupId(true);
    }
    
    private void initData() {
        users = userService.getAllUsers();
        if (users == null) {
            users = new ArrayList<User>();
        }
        user = new User();
        users.add(user);
    }
    
    private class AccountsForm extends Form {
        
        private DropDownChoice accounts;
        private TextField username;
        private TextField phone;
        private PasswordTextField password;
        private PasswordTextField password2;

        public AccountsForm(String id) {
            super(id, new CompoundPropertyModel<User>(user));
            
            username = new TextField("username");
            add(username.setRequired(true).setOutputMarkupId(true));
            
            phone = new TextField("phone");
            add(phone.setRequired(true).setOutputMarkupId(true));
            
            add(password = new PasswordTextField("password"));
            password.setRequired(true).setOutputMarkupId(true);
            
            add(password2 = new PasswordTextField("password2", Model.of("")));
            password2.setRequired(true).setOutputMarkupId(true);
            
            //controls
            addControls();
            
            accounts = new DropDownChoice(
                    "accounts", 
                    new PropertyModel<User>(AccountsPanel.this, "user"), 
                    new PropertyModel<List<User>>(AccountsPanel.this, "users"), 
                    new IChoiceRenderer<User>() {
                        @Override
                        public Object getDisplayValue(User object) {
                            if (object == null || object.getId() == null) {
                                return new StringResourceModel("createNewUser", AccountsPanel.this, null).getString();
                            }
                            return object.getPhone() + " " + object.getUsername();
                        }
                        @Override
                        public String getIdValue(User object, int index) {
                            if (object == null || object.getId() == null) {
                                return "-1";
                            }
                            return object.getId() + "";
                        }
                    }
            );
            accounts.setNullValid(false);
            accounts.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    accountsForm.setModelObject(user);
                    target.add(username);
                    target.add(phone);
                }
            });
            add(accounts.setOutputMarkupId(true));
        }
        
        private void addControls() {
            //top
            add(new AjaxLink("clear") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    initData();
                    accountsForm.setModelObject(user);
                    target.add(username);
                    target.add(phone);
                }
            });
            add(new ConfirmationLink("remove", new StringResourceModel("ask.deletion", AccountsPanel.this, null).getString()) {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    if (user != null && user.getId() != null && !getUser().equals(user)) {
                        userService.delete(user);
                        initData();
                        accounts.setChoices(users);
                        target.add(username);
                        target.add(phone);
                        target.add(accounts);
                    }
                }
            });
            
            //bottom
            add(new AjaxLink("clear2") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    user = new User();
                    accountsForm.setModelObject(user);
                    target.add(username);
                    target.add(phone);
                }
            });
            add(new ConfirmationLink("remove2", new StringResourceModel("ask.deletion", AccountsPanel.this, null).getString()) {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    if (user != null && user.getId() != null && !getUser().equals(user)) {
                        userService.delete(user);
                        initData();
                        accounts.setChoices(users);
                        target.add(username);
                        target.add(phone);
                        target.add(accounts);
                    }
                }
            });
        }

        @Override
        protected void onValidate() {
            super.onValidate();
            if (password.getConvertedInput() == null || password2.getConvertedInput() == null
                    ||
                !password.getConvertedInput().equals(password2.getConvertedInput())
            ) {
                error(new StringResourceModel("error.pass", AccountsPanel.this, null).getString());
            }
        }

        @Override
        protected void onSubmit() {
            if (user.getId() != null) {
                userService.update(user);
            } else {
                try {
                    userService.saveUser(user);
                } catch (TiresPersistException ex) {
                    error(new StringResourceModel("error.save", AccountsPanel.this, null).getString());
                    LOG.error("Unable to save new user from admin part!");
                    return;
                }
            }
            setResponsePage(AdminBasePage.class);
        }
        
    }
}