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
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author kilrwhle
 */
public class HomePage extends BasePage {
    
    @SpringBean
    private InfoService infoService;
    
    private PageableListView listView;
    private WebMarkupContainer listContainer;
    
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
                li.add(new Label("entryHeader", new PropertyModel(entry, "header")));
                li.add(new Label("entryBody", new PropertyModel(entry, "body")));
            }
        };
        listContainer.add(listView);
        add(listContainer);
    }
}
