/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.panels.catalogue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.apache.wicket.model.StringResourceModel;
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
    private WebMarkupContainer numbersContainer;
    private ListView numbers;
    private Image image;
    private AjaxLink prev, next;
    private int selected = 0;
    private int total = 0;

    public ImageViewerPanel(String id, Tire tire) {
        super(id);
        
        add(new Label("modal.title", new StringResourceModel("modal.photos", ImageViewerPanel.this, null).getString()
                                        + " " + tire.getManufacturer().getCompanyName() + " " + tire.getModelName())
        );

        images = new HashMap<Integer, TireImage>();
        int counter = 0;
        try {
            for (TireImage ti : tireService.getImagesForTire(tire, ImageService.ImageSize.LARGE)) {
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

        prev = new AjaxLink("prev") {
            @Override
            public void onClick(AjaxRequestTarget art) {
                if (selected > 0) {
                    switchImage(--selected, art);
                }
                art.add(prev);
                art.add(next);
            }
            @Override
            public boolean isEnabled() {
                return selected > 0;
            }
        };
        add(prev.setMarkupId("prev").setOutputMarkupPlaceholderTag(true));
        next = new AjaxLink("next") {
            @Override
            public void onClick(AjaxRequestTarget art) {
                if (selected < total - 1) {
                    switchImage(++selected, art);
                }
                art.add(prev);
                art.add(next);
            }
            @Override
            public boolean isEnabled() {
                return selected < total - 1;
            }
        };
        add(next.setMarkupId("next").setOutputMarkupPlaceholderTag(true));

        numbersContainer = new WebMarkupContainer("numbersContainer");
        add(numbersContainer.setOutputMarkupId(true));
        numbers = new ListView("numbers", new ArrayList(images.keySet())) {

            @Override
            protected void populateItem(final ListItem li) {
                final int index = (Integer) li.getModelObject();
                AjaxLink link = new AjaxLink("numberLink") {
                    @Override
                    public void onClick(AjaxRequestTarget art) {
                        selected = index;
                        switchImage(index, art);
                    }
                };
                link.add(new Label("number", index + 1));
                if (index == selected) {
                    link.add(AttributeModifier.replace("class", "btn btn-primary"));
                } else {
                    link.add(AttributeModifier.replace("class", "btn btn-default"));
                }
                li.add(link);
            }
        };
        numbersContainer.add(numbers.setOutputMarkupId(true));
    }
    
    private void switchImage(final int index, AjaxRequestTarget art) {
        image.setImageResource(new DynamicImageResource() {
            @Override
            protected byte[] getImageData(IResource.Attributes atrbts) {
                return images.get(index).getImageBody();
            }
        });
        art.add(image);
        art.add(prev);
        art.add(next);
        art.add(numbersContainer);
    }

}
