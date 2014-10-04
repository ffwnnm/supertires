/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.panels.admin;

import java.util.ArrayList;
import java.util.List;
import kz.supershiny.core.model.Tire;
import kz.supershiny.core.model.TireImage;
import kz.supershiny.core.services.ImageService;
import kz.supershiny.web.wicket.components.ConfirmationLink;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.MultiFileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.util.lang.Bytes;

/**
 * Panel for uploading images for tires (admin part).
 *
 * @author aishmanov
 */
public class ImageUploadPanel extends Panel {

    private UploadForm uploadForm;
    private List<TireImage> images = new ArrayList<TireImage>();
    private TireImage preview;
    private Tire tire;
    private ListView imagesView;
    private WebMarkupContainer viewContainer;

    public ImageUploadPanel(String id, Tire tire) {
        super(id);
        
        this.tire = tire;
        this.preview = tire.getPreview();
        this.images = tire.getImages() == null ? new ArrayList<TireImage>() : tire.getImages();

        uploadForm = new UploadForm("uploadForm");
        add(uploadForm.setOutputMarkupId(true));

        //Display/edit images
        viewContainer = new WebMarkupContainer("viewContainer");
        imagesView = new ListView("imagesView", new PropertyModel(ImageUploadPanel.this, "images")) {
            @Override
            protected void populateItem(ListItem li) {
                final TireImage ti = (TireImage) li.getModelObject();
                final AjaxLink defaultLink;
                final AjaxLink removeLink;
                Image image = new Image("image", new DynamicImageResource() {
                    @Override
                    protected byte[] getImageData(IResource.Attributes atrbts) {
                        return ti.getImageBody();
                    }
                });
                defaultLink = new AjaxLink("defaultLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        for (TireImage item : images) {
                            item.setIsPreview(Boolean.FALSE);
                        }
                        ti.setIsPreview(Boolean.TRUE);
                        target.add(viewContainer);
                    }
                };
                if (ti.getIsPreview()) {
                    defaultLink.setEnabled(false);
                    defaultLink.add(new AttributeAppender("class", Model.of("btn btn-primary")));
                } else {
                    defaultLink.setEnabled(true);
                    defaultLink.add(new AttributeAppender("class", Model.of("btn btn-default")));
                }
                removeLink = new ConfirmationLink("removeLink", new StringResourceModel("ask.deletion", ImageUploadPanel.this, null).getString()) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        images.remove(ti);
                        target.add(viewContainer);
                    }
                };
                li.add(image);
                li.add(defaultLink);
                li.add(removeLink);
            }
        };
        viewContainer.add(imagesView.setOutputMarkupId(true));
        add(viewContainer.setOutputMarkupId(true));
    }

    //Upload images
    private class UploadForm extends Form {

        private MultiFileUploadField fileUpload;
        private List<FileUpload> uploads = new ArrayList<FileUpload>();

        public UploadForm(String id) {
            super(id);

            setMultiPart(true);
            setMaxSize(Bytes.kilobytes(8172));

            fileUpload = new MultiFileUploadField("fileUpload", new PropertyModel<List<FileUpload>>(this, "uploads"));
            fileUpload.add(new AjaxFormSubmitBehavior("onchange") {
                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    processUploads();
                    target.add(fileUpload);
                    target.add(uploadForm);
                    target.add(viewContainer);
                }
            });
            add(fileUpload.setOutputMarkupId(true));
        }

        public List<FileUpload> getUploads() {
            return uploads;
        }

        private void processUploads() {
            for (FileUpload item : uploads) {
                images.add(new TireImage(null, item.getClientFileName(), ImageService.ImageSize.ORIGINAL, item.getBytes()));
            }
            if (uploads != null) {
                uploads.clear();
            } else {
                uploads = new ArrayList<FileUpload>();
            }
        }
    }
}
