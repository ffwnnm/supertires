/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.panels.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import kz.supershiny.core.model.TireImage;
import kz.supershiny.core.services.ImageService;
import kz.supershiny.core.services.ImageService.ImageSize;
import kz.supershiny.web.wicket.components.ConfirmationLink;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
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
    private List<TireImage> currentImages = new ArrayList<TireImage>();
    private Set<String> deletedImages = new HashSet<String>();
    private ListView imagesView;
    private WebMarkupContainer viewContainer;

    public ImageUploadPanel(String id, List<TireImage> images) {
        super(id);
        
        initData(images);

        uploadForm = new UploadForm("uploadForm");
        add(uploadForm.setOutputMarkupId(true));

        //Display/edit images
        viewContainer = new WebMarkupContainer("viewContainer");
        //images doesn't exist, wicket uses getter getImages() of ImageUploadPanel
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
                        setPreviewImage(ti.getFileName());
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
                removeLink = new ConfirmationLink("removeLink", new StringResourceModel("ask.deletionPhoto", ImageUploadPanel.this, null).getString()) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        deletedImages.add(ti.getFileName());
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
    
    private void initData(List<TireImage> images) {
        currentImages = images;
        deletedImages = new HashSet<String>();
        if (currentImages == null) {
            currentImages = new ArrayList<TireImage>();
        }
    }
    
    //sets the image with this filename as default for all sizes
    public void setPreviewImage(String imgName) {
        for (TireImage item : currentImages) {
            if (item.getFileName().equals(imgName)) {
                item.setIsPreview(Boolean.TRUE);
            } else {
                item.setIsPreview(Boolean.FALSE);
            }
        }
    }

    //gets only LARGE images
    public List<TireImage> getImages() {
        List<TireImage> larges = new ArrayList<TireImage>();
        for (TireImage img : currentImages) {
            if (img.getImageSize().equals(ImageService.ImageSize.LARGE) && !deletedImages.contains(img.getFileName())) {
                larges.add(img);
            }
        }
        return larges;
    }
    
    //returns the whole list of images of all sizes for persistence
    public List<TireImage> getAllImages() {
        List<TireImage> result = new ArrayList<TireImage>();
        for (TireImage img : currentImages) {
            if (!deletedImages.contains(img.getFileName())) {
                result.add(img);
            }
        }
        return result;
    }

    //Upload images
    private class UploadForm extends Form {

        private FileUploadField fileUpload;
        private List<FileUpload> uploads = new ArrayList<FileUpload>();

        public UploadForm(String id) {
            super(id);

            setMultiPart(true);
            setMaxSize(Bytes.kilobytes(8172));
 
            fileUpload = new FileUploadField("fileUpload", new PropertyModel<List<FileUpload>>(this, "uploads"));
            add(fileUpload.setOutputMarkupId(true));
            
            add(new AjaxSubmitLink("uploadButton") {

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    if (uploads != null) {
                        processUploads();
                    }
                    target.add(fileUpload);
                    target.add(uploadForm);
                    target.add(viewContainer);
                }
                
            });
        }

        @Override
        protected void onSubmit() {
            
        }

        public List<FileUpload> getUploads() {
            return uploads;
        }

        //all magic is done here
        private void processUploads() {
            for (FileUpload item : uploads) {
                //add original
                currentImages.add(new TireImage(null, item.getClientFileName(), ImageService.ImageSize.ORIGINAL, item.getBytes()));
                //create and add large sized image
                byte[] imgBytes;
                imgBytes = ImageService.resizeImage(item.getBytes(), ImageSize.LARGE);
                currentImages.add(new TireImage(null, item.getClientFileName(), ImageService.ImageSize.LARGE, imgBytes));
                //create and add small sized image
                imgBytes = ImageService.resizeImage(item.getBytes(), ImageSize.SMALL);
                currentImages.add(new TireImage(null, item.getClientFileName(), ImageService.ImageSize.SMALL, imgBytes));
            }
            if (uploads != null) {
                uploads.clear();
            } else {
                uploads = new ArrayList<FileUpload>();
            }
        }
    }
}
