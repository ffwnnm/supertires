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
import kz.supershiny.web.wicket.pages.LoginPage;
import kz.supershiny.web.wicket.panels.admin.AdminMenuPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
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
        
        if(getUser() == null) {
            setResponsePage(LoginPage.class);
        }
        
        initData();
        
        add(new AdminMenuPanel("adminMenu"));
        
        companySelect = new DropDownChoice<Manufacturer>(
                        "companySelect", 
                        new PropertyModel<Manufacturer>(ManufacturersEditorPage.this, "selected"), 
                        new PropertyModel<List<Manufacturer>>(ManufacturersEditorPage.this, "manufacturers")
                    );
        companySelect.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget art) {
                selected = (Manufacturer) companySelect.getConvertedInput();
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
            super(id, new CompoundPropertyModel<Manufacturer>(selected == null ? new Manufacturer() : selected));
            
            companyName = new TextField("companyName");
            add(companyName.setOutputMarkupId(true));
            description = new TextArea("description");
            add(description.setOutputMarkupId(true));
            
            add(new AjaxButton("save") {
                @Override
                public void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    if(selected != null) {
                        if(selected.getId() == null) {
                            tireService.save(selected);
                        } else {
                            tireService.update(selected);
                        }
                        initData();
                        form.setDefaultModelObject(selected);
                        target.add(editor);
                        target.add(companySelect);
                    }
                }
            });
            add(new AjaxButton("clear") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    selected = null;
                    form.setDefaultModelObject(selected);
                    target.add(editor);
                    target.add(companySelect);
                }
            });
        }
    }
    
    private void initData() {
        manufacturers = tireService.getUniqueManufacturers();
        if(manufacturers == null) manufacturers = new ArrayList<Manufacturer>();
    }
}
