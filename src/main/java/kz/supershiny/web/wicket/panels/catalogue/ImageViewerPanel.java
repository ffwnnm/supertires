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
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
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

    private static final Logger LOG = LoggerFactory.getLogger(ImageViewer.class);

    @SpringBean
    private TireService tireService;

    private Map<Integer, TireImage> images;
    private ListView numbers;

    public ImageViewerPanel(String id, Tire tire) {
        super(id);

        images = new HashMap<Integer, TireImage>();
        int counter = 1;
        try {
            for (TireImage ti : tireService.getImagesForTire(tire, ImageService.ImageSize.LARGE)) {
                images.put(counter, ti);
                counter++;
            }
        } catch (NullPointerException ex) {
            LOG.error("Empty images list!");
        }

        add(new Label("tireModel", tire.getModelName()));

        Image mainImage;
        if (!images.isEmpty()) {
            mainImage = new Image("image", new DynamicImageResource() {
                @Override
                protected byte[] getImageData(IResource.Attributes atrbts) {
                    return images.get(1).getImageBody();
                }
            });
            add(mainImage);
        } else {
            add(new ContextImage("image", "images/default-preview.png"));
        }

        add(new AjaxLink("prev") {

            @Override
            public void onClick(AjaxRequestTarget art) {

            }
        });
        add(new AjaxLink("next") {

            @Override
            public void onClick(AjaxRequestTarget art) {

            }
        });

        numbers = new ListView("numbers", new ArrayList(images.keySet())) {

            @Override
            protected void populateItem(ListItem li) {
                li.add(new AjaxLink("numberLink") {

                    @Override
                    public void onClick(AjaxRequestTarget art) {

                    }
                }.add(new Label("number", (Integer) li.getModelObject())));
            }
        };
        add(numbers.setOutputMarkupId(true));
    }

}
