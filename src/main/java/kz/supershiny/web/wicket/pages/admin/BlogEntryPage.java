/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.pages.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import kz.supershiny.core.model.BlogEntry;
import kz.supershiny.core.services.InfoService;
import kz.supershiny.core.util.Constants;
import kz.supershiny.web.wicket.pages.BasePage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author kilrwhle
 */
public class BlogEntryPage extends BasePage {
    
    @SpringBean
    private InfoService infoService;
    
    private EditorForm editor;
    private PageableListView listView;
    private WebMarkupContainer listContainer;
    
    private List<BlogEntry> entries;
    private BlogEntry currentEntry;

    public BlogEntryPage() {
        super();
        
        entries = infoService.getAllEntries();
        if(entries == null) entries = new ArrayList<BlogEntry>();
        currentEntry = new BlogEntry();
        
        add(new BookmarkablePageLink("catalogLink", CatalogEditorPage.class));
        add(new BookmarkablePageLink("blogLink", BlogEntryPage.class));
        add(new BookmarkablePageLink("companyLink", ManufacturersEditorPage.class));
        
        add(editor = new EditorForm("blogEditor"));
        editor.setOutputMarkupId(true);
        
        listContainer = new WebMarkupContainer("listContainer");
        listView = new PageableListView(
                "entriesList", 
                new PropertyModel(BlogEntryPage.this, "entries"), 
                Constants.ITEMS_PER_PAGE_SIMPLE
        ) {
            @Override
            protected void populateItem(ListItem li) {
                final BlogEntry entry = (BlogEntry) li.getModelObject();
                li.add(new Label("entryDate", new PropertyModel(entry, "date")));
                li.add(new Label("entryHeader", new PropertyModel(entry, "header")).setEscapeModelStrings(false));
                li.add(new Label("entryBody", new PropertyModel(entry, "body")).setEscapeModelStrings(false));
                li.add(new AjaxLink("removeLink") {
                    @Override
                    public void onClick(AjaxRequestTarget art) {
                        infoService.delete(entry);
                        entries = infoService.getAllEntries();
                        listView.setList(entries);
                        art.add(listContainer);
                    }
                    @Override
                    protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                        super.updateAjaxAttributes(attributes);
                        AjaxCallListener ajaxCallListener = new AjaxCallListener();
                        ajaxCallListener.onPrecondition("return confirm('" 
                                + new StringResourceModel("confirm.deletion", BlogEntryPage.this, null).getString()
                                + "');");
                        attributes.getAjaxCallListeners().add(ajaxCallListener);
                    }
                });
            }
        };
        listContainer.add(listView.setOutputMarkupId(true));
        add(listContainer.setOutputMarkupId(true));
    }
    
    private class EditorForm extends Form {
        
        private TextField header;
        private TextArea body;

        public EditorForm(String id) {
            super(id, new CompoundPropertyModel<BlogEntry>(currentEntry));
            
            header = new TextField("header");
            add(header.setOutputMarkupId(true));
            body = new TextArea("body");
            add(body.setOutputMarkupId(true));
            
            add(new AjaxButton("clear") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    currentEntry = new BlogEntry();
                    EditorForm.this.setDefaultModelObject(currentEntry);
                    target.add(header);
                    target.add(body);
                }
            });
            add(new AjaxButton("save") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    currentEntry.setDate(new Date());
                    infoService.save(currentEntry);
                    
                    entries = infoService.getAllEntries();
                    listView.setList(entries);
                    target.add(listContainer);
                }
            });
        }
        
    }
}
