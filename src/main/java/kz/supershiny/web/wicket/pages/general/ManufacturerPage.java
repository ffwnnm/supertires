/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.pages.general;

import kz.supershiny.core.model.Manufacturer;
import kz.supershiny.web.wicket.pages.BasePage;
import org.apache.wicket.markup.html.basic.Label;

/**
 *
 * @author kilrwhle
 */
public class ManufacturerPage extends BasePage {
    
    private Manufacturer manufacturer;

    public ManufacturerPage() {
        super();
    }
    
    public ManufacturerPage(Manufacturer manufacturer) {
        super();
        
        this.manufacturer = manufacturer;
        
        add(new Label("companyName", this.manufacturer.getCompanyName()));
        add(new Label("description", this.manufacturer.getDescription()).setEscapeModelStrings(false));
        
        //add logo!
    }
}
