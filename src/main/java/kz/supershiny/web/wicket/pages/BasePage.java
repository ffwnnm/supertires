/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.supershiny.web.wicket.pages;

import kz.supershiny.core.model.User;
import kz.supershiny.web.wicket.TiresApplication;
import kz.supershiny.web.wicket.TiresSession;
import kz.supershiny.web.wicket.panels.BottomPanel;
import kz.supershiny.web.wicket.panels.EmptyPanel;
import kz.supershiny.web.wicket.panels.TopPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

/**
 *
 * @author kilrwhle
 */
public class BasePage extends WebPage {
    
    private Panel panelModal;
    private WebMarkupContainer bsModal;

    public BasePage() {
        super();
        
        add(new TopPanel("topPanel"));
        add(new BottomPanel("bottomPanel"));
        
        bsModal = new WebMarkupContainer("bsModal");
        add(bsModal.setOutputMarkupPlaceholderTag(true).setMarkupId("bsModal"));
        
        panelModal = new EmptyPanel("bsModalContent");
        bsModal.add(panelModal.setOutputMarkupPlaceholderTag(true));
    }
    
    public void showModal(Panel panel, AjaxRequestTarget art) {
        bsModal.replace(panel);
        art.add(bsModal);
        art.appendJavaScript("showModal();");
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
        return new StringResourceModel("base.keywords", BasePage.this, null);
    }

    public IModel getDescription() {
        return new StringResourceModel("base.description", BasePage.this, null);
    }

    public IModel getPageTitle() {
        return new StringResourceModel("base.title", BasePage.this, null);
    }
    
//    @Override
//    public void renderHead(IHeaderResponse response) {
//        super.renderHead(response);
//        List<String> jsRefs = Arrays.asList("bootstrap-wysiwyg.js", "bootstrap.min.js", "jquery-1.10.2.js", "jquery.hotkeys.js", "supershiny.js");
//        List<String> cssRefs = Arrays.asList("bootstrap.min.css", "glyphicons-free.css", "wysiwyg.css", "supershiny.css");
//        for (String ref : jsRefs) {
//            response.render(JavaScriptReferenceHeaderItem.forReference(new JavaScriptResourceReference(BasePage.class, ref)));
//        }
//        for (String ref : cssRefs) {
//            response.render(JavaScriptReferenceHeaderItem.forReference(new CssResourceReference(BasePage.class, ref)));
//        }
//    }
}
