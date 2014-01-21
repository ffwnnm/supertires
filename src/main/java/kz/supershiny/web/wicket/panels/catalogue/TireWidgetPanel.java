/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.panels.catalogue;

import kz.supershiny.core.model.Tire;
import kz.supershiny.core.services.TireService;
import kz.supershiny.core.util.Base64Coder;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
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
                return Base64Coder.decode(tireService.getPreviewForTire(tire).getEncodedImage());
            }
        });
        
        add(mainImage);
    }
    
}
