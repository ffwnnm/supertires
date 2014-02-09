/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.panels.catalogue;

import kz.supershiny.core.model.Tire;
import kz.supershiny.core.services.TireService;
import kz.supershiny.core.util.Base64Coder;
import kz.supershiny.core.util.Constants;
import kz.supershiny.web.wicket.pages.HomePage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Main panel for tires listing
 *
 * @author kilrwhle
 */
public class TireWidgetPanel extends Panel {
    
    @SpringBean
    private TireService tireService;
    
    private final Tire tire;
    private Image mainImage;

    public TireWidgetPanel(String id, Tire pTire) {
        super(id);
        
        this.tire = pTire;
        
        mainImage = new Image("preview", new DynamicImageResource() {
            @Override
            protected byte[] getImageData(IResource.Attributes atrbts) {
                return Base64Coder.decodeLines(tireService.getPreviewForTire(tire).getEncodedImage());
            }
        });
        add(mainImage);
        
        add(new Label("price", new PropertyModel(tire, "price")));
        add(new Link("manufacturerLink") {
            @Override
            public void onClick() {
                //TODO: implement manufacturer page!
                setResponsePage(HomePage.class);
            }
        }.add(new Label("manufacturer", tire.getManufacturer().getCompanyName())));
        add(new Label("modelName", tire.getModelName()));
        add(new Label("sizeCentimeters", tire.getSize().getSizeVerbal()));
        add(new Label("sizeInches", tire.getSize().getSizeVerbal()));
        add(new Label("season", new StringResourceModel(tire.getSeason(), TireWidgetPanel.this, null).getString()));
        
        String iconURL = "images/";
        if(Constants.SUMMER.equals(tire.getSeason())) iconURL += "summer.jpg";
        if(Constants.WINTER.equals(tire.getSeason())) iconURL += "winter.jpg";
        if(Constants.ALL_SEASONS.equals(tire.getSeason())) iconURL += "allseasons.jpg";
        add(new ContextImage("seasonIcon", iconURL));
        
        add(new Label("type", tire.getType().getTypeName()));
        add(new Label("country", tire.getCountry().getName()));
        add(new Label("tireId", tire.getId()));
    }
    
}
