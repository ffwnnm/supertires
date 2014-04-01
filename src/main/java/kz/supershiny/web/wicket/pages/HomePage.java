/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.supershiny.web.wicket.pages;

import java.util.ArrayList;
import java.util.List;
import kz.supershiny.core.model.BlogEntry;
import kz.supershiny.core.services.InfoService;
import kz.supershiny.core.util.Constants;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author kilrwhle
 */
public class HomePage extends BasePage {
    
    @SpringBean
    private InfoService infoService;
    
    private  final PageableListView listView;
    private final WebMarkupContainer listContainer;
    
    private List<BlogEntry> entries;

    public HomePage() {
        super();
        
        entries = infoService.getAllEntries();
        if(entries == null) entries = new ArrayList<BlogEntry>();
        
        listContainer = new WebMarkupContainer("listContainer");
        listView = new PageableListView(
                "entriesList", 
                new PropertyModel(HomePage.this, "entries"), 
                Constants.ITEMS_PER_PAGE_SIMPLE
        ) {
            @Override
            protected void populateItem(ListItem li) {
                final BlogEntry entry = (BlogEntry) li.getModelObject();
                li.add(new Label("entryDate", new PropertyModel(entry, "date")));
                li.add(new Label("entryHeader", new PropertyModel(entry, "header")).setEscapeModelStrings(false));
                li.add(new Label("entryBody", new PropertyModel(entry, "body")).setEscapeModelStrings(false));
            }
        };
        listContainer.add(listView);
        add(listContainer);
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

    @Override
    public IModel getKeywords() {
        return new StringResourceModel("keywords", HomePage.this, null);
    }

    @Override
    public IModel getDescription() {
        return new StringResourceModel("description", HomePage.this, null);
    }

    @Override
    public IModel getPageTitle() {
        return new StringResourceModel("title", HomePage.this, null);
    }
}
