/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.panels.admin;

import java.util.ArrayList;
import java.util.List;
import kz.supershiny.core.model.Manufacturer;
import kz.supershiny.core.services.TireService;
import kz.supershiny.web.wicket.components.ConfirmationLink;
import kz.supershiny.web.wicket.pages.admin.AdminBasePage;
import kz.supershiny.web.wicket.panels.BasePanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kilrwhle
 */
public class BrandsPanel extends BasePanel {
    
    private static final Logger LOG = LoggerFactory.getLogger(BrandsPanel.class);
    
    @SpringBean
    private TireService tireService;
    
    private Manufacturer manufacturer;
    private List<Manufacturer> manufacturers;
    private AccountsForm brandsForm;
    private FeedbackPanel feed;

    public BrandsPanel(String id) {
        super(id);
        
        initData();
        
        add(feed = new FeedbackPanel("feedback"));
        feed.setVisible(false).setOutputMarkupId(true);
        
        add(brandsForm = new AccountsForm("brandsForm"));
        brandsForm.setOutputMarkupId(true);
    }
    
    private void initData() {
        manufacturers = tireService.getUniqueManufacturers();
        if (manufacturers == null) {
            manufacturers = new ArrayList<Manufacturer>();
        }
        manufacturer = new Manufacturer();
        manufacturers.add(manufacturer);
    }
    
    private class AccountsForm extends Form {
        
        private DropDownChoice manufacturersChoice;
        private TextField companyName;
        private TextArea description;

        public AccountsForm(String id) {
            super(id, new CompoundPropertyModel<Manufacturer>(manufacturer));
            
            companyName = new TextField("companyName");
            add(companyName.setRequired(true).setOutputMarkupId(true));
            
            description = new TextArea("description");
            add(description.setRequired(true).setOutputMarkupId(true));
            
            //controls
            addControls();
            
            manufacturersChoice = new DropDownChoice(
                    "manufacturers", 
                    new PropertyModel<Manufacturer>(BrandsPanel.this, "manufacturer"), 
                    new PropertyModel<List<Manufacturer>>(BrandsPanel.this, "manufacturers"), 
                    new IChoiceRenderer<Manufacturer>() {
                        @Override
                        public Object getDisplayValue(Manufacturer object) {
                            if (object == null || object.getId() == null) {
                                return new StringResourceModel("createNewManufacturer", BrandsPanel.this, null).getString();
                            }
                            return object.getCompanyName();
                        }
                        @Override
                        public String getIdValue(Manufacturer object, int index) {
                            if (object == null || object.getId() == null) {
                                return "-1";
                            }
                            return object.getId() + "";
                        }
                    }
            );
            manufacturersChoice.setNullValid(false);
            manufacturersChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    brandsForm.setModelObject(manufacturer);
                    target.add(companyName);
                    target.add(description);
                }
            });
            add(manufacturersChoice.setOutputMarkupId(true));
        }
        
        private void clearForm(AjaxRequestTarget target) {
            initData();
            brandsForm.setModelObject(manufacturer);
            target.add(companyName);
            target.add(description);
        }
        
        private void removeManufacturer(AjaxRequestTarget target) {
            if (manufacturer != null && manufacturer.getId() != null) {
                if (tireService.checkManufacturerToDelete(manufacturer)) {
                    tireService.delete(manufacturer);
                    clearForm(target);
                    manufacturersChoice.setChoices(manufacturers);
                    target.add(manufacturersChoice);
                } else {
                    target.appendJavaScript(
                            "alert('"
                            + new StringResourceModel("error.tiresExist", BrandsPanel.this, null).getString()
                            +"');"
                    );
                }
            }
        }

        @Override
        protected void onValidate() {
            super.onValidate();
            feed.setVisible(this.hasError());
        }

        @Override
        protected void onSubmit() {
            if (manufacturer.getId() != null) {
                tireService.update(manufacturer);
            } else {
                tireService.save(manufacturer);
            }
            setResponsePage(new AdminBasePage(new PageParameters().add("target", "brands")));
        }
        
        private void addControls() {
            //top
            add(new AjaxLink("clear") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    clearForm(target);
                }
            });
            add(new ConfirmationLink("remove", new StringResourceModel("ask.deletion", BrandsPanel.this, null).getString()) {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    removeManufacturer(target);
                }
            });
            
            //bottom
            add(new AjaxLink("clear2") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    clearForm(target);
                }
            });
            add(new ConfirmationLink("remove2", new StringResourceModel("ask.deletion", BrandsPanel.this, null).getString()) {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    removeManufacturer(target);
                }
            });
        }
    }
}