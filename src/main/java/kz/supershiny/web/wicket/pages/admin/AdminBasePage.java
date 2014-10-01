/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.pages.admin;

import java.util.Arrays;
import java.util.List;
import kz.supershiny.web.wicket.pages.BasePage;
import kz.supershiny.web.wicket.pages.LoginPage;
import kz.supershiny.web.wicket.pages.catalogue.CataloguePage;
import kz.supershiny.web.wicket.panels.BasePanel;
import kz.supershiny.web.wicket.panels.admin.AccountsPanel;
import kz.supershiny.web.wicket.panels.admin.BrandsPanel;
import kz.supershiny.web.wicket.panels.admin.CataloguePanel;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 * @author kilrwhle
 */
public class AdminBasePage extends BasePage {
    
    private BasePanel mainPanel;
    private WebMarkupContainer mainContainer;
    private AjaxLink accountsLink;
    private AjaxLink catalogueLink;
//    private AjaxLink proposalsLink;
    private AjaxLink manufacturersLink;
    private AjaxLink faqLink;
    private AjaxLink blogLink;
    private List<AjaxLink> menuLinks;
    
    public AdminBasePage() {
        this(new PageParameters());
    }

    public AdminBasePage(PageParameters params) {
        super();
        
        if (getUser() == null) {
            setResponsePage(LoginPage.class);
            return;
        }
        
        //header
        add(new Label("username", getUser().getUsername()));
        add(new Link("logout") {
            @Override
            public void onClick() {
                getTiresSession().setUser(null);
                setResponsePage(CataloguePage.class);
            }
        });
        add(new Link("homepage") {
            @Override
            public void onClick() {
                setResponsePage(CataloguePage.class);
            }
        });
        
        //links
        addLeftMenu();
        
        //main panel
        mainContainer = new WebMarkupContainer("mainContainer");
        if (params != null && !params.get("target").isNull() && !params.get("target").isEmpty()) {
            String target = params.get("target").toString();
            switch (target) {
                case "brands": mainPanel = new BrandsPanel("mainPanel");
                    break;
                case "catalogue": mainPanel = new CataloguePanel("mainPanel");
                    break;
                default: mainPanel = new AccountsPanel("mainPanel");
                    break;
            }
        } else {
            mainPanel = new AccountsPanel("mainPanel");
        }
        mainContainer.add(mainPanel);
        add(mainContainer.setOutputMarkupId(true));
    }
    
    private void addLeftMenu() {
        add(accountsLink = new AjaxLink("accountsLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                mainPanel = new AccountsPanel("mainPanel");
                mainContainer.replace(mainPanel);
                setActiveLink(accountsLink, target);
                target.add(mainContainer);
            }
        });
        add(catalogueLink = new AjaxLink("catalogueLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                mainPanel = new CataloguePanel("mainPanel");
                mainContainer.replace(mainPanel);
                setActiveLink(catalogueLink, target);
                target.add(mainContainer);
            }
        });
//        add(proposalsLink = new AjaxLink("proposalsLink") {
//            @Override
//            public void onClick(AjaxRequestTarget target) {
//                mainPanel = new AccountsPanel("mainPanel");
//                mainContainer.replace(mainPanel);
//                setActiveLink(proposalsLink, target);
//                target.add(mainContainer);
//            }
//        });
        add(manufacturersLink = new AjaxLink("manufacturersLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                mainPanel = new BrandsPanel("mainPanel");
                mainContainer.replace(mainPanel);
                setActiveLink(manufacturersLink, target);
                target.add(mainContainer);
            }
        });
        add(faqLink = new AjaxLink("faqLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                mainPanel = new AccountsPanel("mainPanel");
                mainContainer.replace(mainPanel);
                setActiveLink(faqLink, target);
                target.add(mainContainer);
            }
        });
        add(blogLink = new AjaxLink("blogLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                mainPanel = new AccountsPanel("mainPanel");
                mainContainer.replace(mainPanel);
                setActiveLink(blogLink, target);
                target.add(mainContainer);
            }
        });
        accountsLink.setOutputMarkupId(true);
        catalogueLink.setOutputMarkupId(true);
//        proposalsLink.setOutputMarkupId(true);
        manufacturersLink.setOutputMarkupId(true);
        faqLink.setOutputMarkupId(true);
        blogLink.setOutputMarkupId(true);
        
        menuLinks = Arrays.asList(accountsLink, catalogueLink, /*proposalsLink,*/ manufacturersLink, faqLink, blogLink);
    }
    
    private void setActiveLink(AjaxLink active, AjaxRequestTarget art) {
        for (AjaxLink link : menuLinks) {
            if (link.equals(active)) {
                link.add(AttributeModifier.replace("class", "list-group-item active"));
            } else {
                link.add(AttributeModifier.replace("class", "list-group-item"));
            }
            art.add(link);
        }
    }
    
}
