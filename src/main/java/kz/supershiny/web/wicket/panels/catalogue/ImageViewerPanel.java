/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.panels.catalogue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.text.SimpleAttributeSet;
import kz.supershiny.core.model.Tire;
import kz.supershiny.core.model.TireImage;
import kz.supershiny.core.services.ImageService;
import kz.supershiny.core.services.TireService;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kilrwhle
 */
public class ImageViewerPanel extends Panel {

    private static final Logger LOG = LoggerFactory.getLogger(ImageViewerPanel.class);

    @SpringBean
    private TireService tireService;

    private Map<Integer, TireImage> images;
    private ListView numbers;
    private Image image;
    private int selected = 0;
    private int total = 0;

    public ImageViewerPanel(String id, Tire tire) {
        super(id);

        images = new HashMap<Integer, TireImage>();
        int counter = 0;
        try {
            for (TireImage ti : tireService.getImagesForTire(tire, ImageService.ImageSize.ORIGINAL)) {
                images.put(counter, ti);
                counter++;
            }
        } catch (NullPointerException ex) {
            LOG.error("Empty images list!");
        }
        
        total = images.size();

        if (!images.isEmpty()) {
            image = new Image("image", new DynamicImageResource() {
                @Override
                protected byte[] getImageData(IResource.Attributes atrbts) {
                    return images.get(selected).getImageBody();
                }
            });
            add(image.setOutputMarkupId(true));
        } else {
            add(new ContextImage("image", "images/default-preview.png"));
        }

        add(new AjaxLink("prev") {

            @Override
            public void onClick(AjaxRequestTarget art) {
                if (selected > 0) {
                    switchImage(--selected, art);
                }
            }
        });
        add(new AjaxLink("next") {

            @Override
            public void onClick(AjaxRequestTarget art) {
                if (selected < total - 1) {
                    switchImage(++selected, art);
                }
            }
        });

        numbers = new ListView("numbers", new ArrayList(images.keySet())) {

            @Override
            protected void populateItem(final ListItem li) {
                final int index = (Integer) li.getModelObject();
                AjaxLink link = new AjaxLink("numberLink") {

                    @Override
                    public void onClick(AjaxRequestTarget art) {
                        switchImage(index, art);
                    }
                };
                link.add(new Label("number", index + 1));
//                if (index == selected) {
//                    link.add(AttributeModifier.replace("class", "btn btn-primary"));
//                } else {
//                    link.add(AttributeModifier.replace("class", "btn btn-default"));
//                }
                li.add(link);
            }
        };
        add(numbers.setOutputMarkupId(true));
    }
    
    private void switchImage(final int index, AjaxRequestTarget art) {
        image.setImageResource(new DynamicImageResource() {
            @Override
            protected byte[] getImageData(IResource.Attributes atrbts) {
                return images.get(index).getImageBody();
            }
        });
        art.add(image);
    }

}
