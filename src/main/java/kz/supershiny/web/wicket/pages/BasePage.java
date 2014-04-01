/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.supershiny.web.wicket.pages;

import kz.supershiny.core.model.User;
import kz.supershiny.web.wicket.TiresSession;
import kz.supershiny.web.wicket.panels.MenuPanel;
import kz.supershiny.web.wicket.panels.TopPanel;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

/**
 *
 * @author kilrwhle
 */
public class BasePage extends WebPage {

    public BasePage() {
        super();
        
        add(new TopPanel("topPanel"));
        add(new MenuPanel("menuPanel"));
    }
    
    protected User getUser() {
        return ((TiresSession) getSession()).getUser();
    }
    
    protected TiresSession getTiresSession() {
        return (TiresSession) getSession();
    }
    
    @Override
    protected void onBeforeRender() {
        //SEO
        addOrReplace(new Label("title", getPageTitle()));
        
        Label desc = new Label("description", "");
        desc.add(new AttributeAppender("CONTENT", getDescription(), " "));
        addOrReplace(desc);
        
        Label keywords = new Label("keywords","");
        keywords.add(new AttributeAppender("CONTENT", getKeywords(), " "));
        addOrReplace(keywords);
        
        super.onBeforeRender();
    }

    public IModel getKeywords() {
        return new StringResourceModel("keywords", BasePage.this, null);
    }

    public IModel getDescription() {
        return new StringResourceModel("description", BasePage.this, null);
    }

    public IModel getPageTitle() {
        return new StringResourceModel("title", BasePage.this, null);
    }
}
