/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.pages.admin;

import java.util.ArrayList;
import java.util.List;
import kz.supershiny.core.model.Manufacturer;
import kz.supershiny.core.services.TireService;
import kz.supershiny.web.wicket.pages.BasePage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author kilrwhle
 */
public class ManufacturersEditorPage extends BasePage {
    
    @SpringBean
    private TireService tireService;
    
    private List<Manufacturer> manufacturers;
    private Manufacturer selected;
    private DropDownChoice companySelect;
    private ManufacturerEditor editor;

    public ManufacturersEditorPage() {
        super();
        
        initData(null);
        
        add(new BookmarkablePageLink("catalogLink", CatalogEditorPage.class));
        add(new BookmarkablePageLink("blogLink", BlogEntryPage.class));
        add(new BookmarkablePageLink("companyLink", ManufacturersEditorPage.class));
        
        companySelect = new DropDownChoice<Manufacturer>(
                        "companySelect", 
                        new PropertyModel<Manufacturer>(ManufacturersEditorPage.this, "selected"),
                        new PropertyModel<List<Manufacturer>>(ManufacturersEditorPage.this, "manufacturers")
                    );
        companySelect.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget art) {
                editor.setDefaultModelObject(selected);
                art.add(editor);
            }
        });
        add(companySelect.setOutputMarkupId(true));
        
        editor = new ManufacturerEditor("editorForm");
        add(editor.setOutputMarkupId(true));
    }
    
    private class ManufacturerEditor extends Form {
        
        private TextField companyName;
        private TextArea description;

        public ManufacturerEditor(String id) {
            super(id, new CompoundPropertyModel<Manufacturer>(selected));
            
            companyName = new TextField("companyName");
            add(companyName.setOutputMarkupId(true));
            description = new TextArea("description");
            add(description.setOutputMarkupId(true));
            
            add(new AjaxButton("save") {
                @Override
                public void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    if(selected.getId() == null) {
                        tireService.save(selected);
                    } else {
                        tireService.update(selected);
                    }
                    initData(selected);
                    form.setDefaultModelObject(selected);
                    target.add(editor);
//                    setResponsePage(ManufacturersEditorPage.class);
                }
            });
            add(new AjaxButton("clear") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    selected = new Manufacturer();
                    form.setDefaultModelObject(selected);
                    target.add(editor);
//                    target.add(companyName);
//                    target.add(description);
                }
            });
//            add(new AjaxButton("remove") {
//                @Override
//                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//                    tireService.delete(selected);
//                    selected = new Manufacturer();
//                    form.setDefaultModelObject(selected);
//                    target.add(editor);
////                    target.add(companyName);
////                    target.add(description);
//                }
//            });
        }
    }
    
    private void initData(Manufacturer manufacturer) {
        manufacturers = tireService.getUniqueManufacturers();
        if(manufacturers == null) manufacturers = new ArrayList<Manufacturer>();
        
        if(manufacturer == null) {
            selected = new Manufacturer();
        } else {
            selected = manufacturer;
        }
    }
}
