/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.panels.catalogue;

import kz.supershiny.core.model.Tire;
import kz.supershiny.core.model.TireImage;
import kz.supershiny.core.services.TireService;
import kz.supershiny.web.wicket.components.ConfirmationLink;
import kz.supershiny.web.wicket.pages.BasePage;
import kz.supershiny.web.wicket.pages.admin.AdminBasePage;
import kz.supershiny.web.wicket.pages.general.ManufacturerPage;
import kz.supershiny.web.wicket.panels.BasePanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Main panel for tires listing
 *
 * @author kilrwhle
 */
public class TireWidgetPanel extends BasePanel {

    @SpringBean
    private TireService tireService;

    private final Tire tire;
    private Image mainImage;
    private AjaxLink previewLink;

    public TireWidgetPanel(String id, Tire pTire) {
        super(id);

        this.tire = pTire;

        final TireImage preview = tireService.getPreviewForTire(tire);
        if (preview != null) {
            previewLink = new AjaxLink("previewLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    ((BasePage) TireWidgetPanel.this.getPage()).showModal(new ImageViewerPanel("bsModalContent", tire), target);
                }
            };
            mainImage = new Image("preview", new DynamicImageResource() {
                @Override
                protected byte[] getImageData(IResource.Attributes atrbts) {
                    return preview.getImageBody();
                }
            });
            previewLink.add(mainImage);
            add(previewLink);
        } else {
            previewLink = new AjaxLink("previewLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {}
            };
            previewLink.add(new ContextImage("preview", "images/default-preview.png"));
            add(previewLink);
        }

        add(new Label("price", new PropertyModel(tire, "price")));
        add(new Label("price2", new PropertyModel(tire, "price")));
        add(new Link("manufacturerLink") {
            @Override
            public void onClick() {
                setResponsePage(new ManufacturerPage(tire.getManufacturer()));
            }
        }.add(new Label("manufacturer", tire.getManufacturer().getCompanyName())));
        add(new Label("modelName", tire.getModelName()));
        add(new Label("sizeCentimeters", tire.getSize().getSizeVerbal()));
        add(new Label("season", new StringResourceModel(tire.getSeason(), TireWidgetPanel.this, null).getString()));

        add(new Label("type", tire.getType().getTypeName()));
        add(new Label("country", tire.getCountry().getName()));
        add(new Label("tireId", tire.getId()));

        //admin catalog edit buttons
        add(new ConfirmationLink("adminRemove", new StringResourceModel("ask.deletion", TireWidgetPanel.this, null).getString()) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                tireService.delete(tire);
                target.add(getPage());
            }
        }.setVisible(isLoggedIn()));

        add(new Link("adminEdit") {
            @Override
            public void onClick() {
                setResponsePage(new AdminBasePage(new PageParameters().add("target", "catalogue").add("tireId", tire.getId())));
            }
        }.setVisible(isLoggedIn()));

        add(new ConfirmationLink("adminRemove2", new StringResourceModel("ask.deletion", TireWidgetPanel.this, null).getString()) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                tireService.delete(tire);
                target.add(getPage());
            }
        }.setVisible(isLoggedIn()));

        add(new Link("adminEdit2") {
            @Override
            public void onClick() {
                setResponsePage(new AdminBasePage(new PageParameters().add("target", "catalogue").add("tireId", tire.getId())));
            }
        }.setVisible(isLoggedIn()));
    }

}
