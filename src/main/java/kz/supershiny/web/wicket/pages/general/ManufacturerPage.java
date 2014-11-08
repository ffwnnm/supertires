/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.pages.general;

import kz.supershiny.core.model.Manufacturer;
import kz.supershiny.web.wicket.pages.BasePage;
import kz.supershiny.web.wicket.pages.catalogue.CataloguePage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.StringResourceModel;

/**
 *
 * @author kilrwhle
 */
public class ManufacturerPage extends BasePage {
    
    private Manufacturer manufacturer;
    private Label name, description;

    public ManufacturerPage() {
        setResponsePage(CataloguePage.class);
    }
    
    public ManufacturerPage(Manufacturer manufacturer) {
        super();
        
        this.manufacturer = manufacturer;
        if (this.manufacturer == null) {
            setResponsePage(CataloguePage.class);
            return;
        }
        
        name = new Label("manufacturerName", this.manufacturer.getCompanyName());
        add(name);
        
        add(new Link("homepage") {
            @Override
            public void onClick() {
                setResponsePage(CataloguePage.class);
            }
        });
        
        description = new Label(
                "manufacturerDescription", 
                this.manufacturer.getDescription() == null 
                        ? "<h2>" + new StringResourceModel("emptyDescription", ManufacturerPage.this, null).getString() + "</h2>"
                        : this.manufacturer.getDescription()
        );
        add(description.setEscapeModelStrings(false));
    }
}
