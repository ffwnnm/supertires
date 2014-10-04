/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.panels.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import kz.supershiny.core.model.Country;
import kz.supershiny.core.model.Manufacturer;
import kz.supershiny.core.model.Tire;
import kz.supershiny.core.model.TireImage;
import kz.supershiny.core.model.TireSize;
import kz.supershiny.core.model.TireType;
import kz.supershiny.core.services.TireService;
import kz.supershiny.core.util.Constants;
import kz.supershiny.web.wicket.panels.BasePanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.MultiFileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.lang.Bytes;

/**
 *
 * @author kilrwhle
 */
public final class CatalogEditorPanel extends BasePanel {
    
    @SpringBean
    private TireService tireService;
    
    private List<TireType> typesList;
    private List<TireSize> sizesList;
    private List<Country> countriesList;
    private List<Manufacturer> manufacturersList;
    private List<String> modelsList;
    private Tire currentTire;
    
    private CatalogEditorPanel.EditTireForm editor;
    private List<FileUpload> imagesForUpload;
    private MultiFileUploadField fileUpload;
    
    public CatalogEditorPanel(String id) {
        this(id, null);
    }

    public CatalogEditorPanel(String id, Tire tire) {
        super(id);
        
        initDictData(tire);
        
        editor = new CatalogEditorPanel.EditTireForm("editTireForm", currentTire);
        editor.setMultiPart(true);
        editor.setMaxSize(Bytes.kilobytes(Constants.MAX_IMAGES_UPLOAD_SIZE_KB));
        
        add(editor.setOutputMarkupId(true));
    }
    
    /**
     * The Grand initializer
     * 
     * @param tire 
     */
    private void initDictData(Tire tire) {
        //dicts
        typesList = tireService.getTypes();
        sizesList = tireService.getUniqueSizes();
        countriesList = tireService.getUniqueCountries();
        manufacturersList = tireService.getUniqueManufacturers();
        modelsList = tireService.getUniqueModels();
        
        if(typesList == null) typesList = new ArrayList<TireType>();
        if(sizesList == null) sizesList = new ArrayList<TireSize>();
        if(countriesList == null) countriesList = new ArrayList<Country>();
        if(manufacturersList == null) manufacturersList = new ArrayList<Manufacturer>();
        if(modelsList == null) modelsList = new ArrayList<String>();
        
        //selected tire
        if(tire == null) {
            currentTire = new Tire();
        } else {
            currentTire = tireService.getTireWithImages(tire.getId());
        }
    }
    
    private class EditTireForm extends Form<Tire> {
        
        private AutoCompleteTextField<Manufacturer> manufacturer;
        private AutoCompleteTextField<Country> country;
        private AutoCompleteTextField<String> modelName;
        private AutoCompleteTextField<TireSize> size;
        private DropDownChoice<TireType> type;
        private DropDownChoice<String> season;
        private TextField<BigDecimal> price;
        private TextField<Long> quantity;
        private AjaxButton saveButton;
        private AjaxButton clearButton;
        private FeedbackPanel feedback;
        private UploadForm uploadForm;
        private ViewForm viewForm;

        public EditTireForm(String id, Tire tire) {
            super(id, new CompoundPropertyModel<Tire>(tire));
            
            //Images upload form
            uploadForm = new CatalogEditorPanel.UploadForm("uploadForm");
            add(uploadForm.setOutputMarkupId(true));
            
            //Images view and edit form
            viewForm = new CatalogEditorPanel.ViewForm("viewForm");
            add(viewForm.setOutputMarkupId(true));

            manufacturer = new AutoCompleteTextField<Manufacturer>("manufacturer"){
                @Override
                protected Iterator<Manufacturer> getChoices(String string) {
                    if(string.isEmpty()) return Collections.emptyListIterator();
                    if(string.trim().isEmpty()) return manufacturersList.iterator();
                    ArrayList<Manufacturer> choices = new ArrayList<Manufacturer>();
                    for(Manufacturer man : manufacturersList) {
                        if(man.getCompanyName().toUpperCase().startsWith(string.toUpperCase())) {
                            choices.add(man);
                        }
                    }
                    return choices.iterator();
                }

                @Override
                public <C> IConverter<C> getConverter(Class<C> type) {
                    return new IConverter<C>() {

                        @Override
                        public C convertToObject(String string, Locale locale) throws ConversionException {
                            Manufacturer result = null;
                            if(string != null && !string.isEmpty()) {
                                int pos = manufacturersList.indexOf(new Manufacturer(string));
                                result = pos > -1 ? manufacturersList.get(pos) : new Manufacturer(string);
                            } else {
                                editor.error(new StringResourceModel("error.invalidManufacturer", CatalogEditorPanel.this, null).getString());
                            }
                            return (C) result;
                        }

                        @Override
                        public String convertToString(C c, Locale locale) {
                            if(c == null) {
                                return "";
                            }
                            return c.toString();
                        }
                    };
                }
            };
            country = new AutoCompleteTextField<Country>("country") {
                @Override
                protected Iterator<Country> getChoices(String string) {
                    if(string.isEmpty()) return Collections.emptyListIterator();
                    if(string.trim().isEmpty()) return countriesList.iterator();
                    ArrayList<Country> choices = new ArrayList<Country>();
                    for(Country country : countriesList) {
                        if(country.getName().toUpperCase().startsWith(string.toUpperCase())) {
                            choices.add(country);
                        }
                    }
                    return choices.iterator();
                }

                @Override
                public <C> IConverter<C> getConverter(Class<C> type) {
                    return new IConverter<C>() {

                        @Override
                        public C convertToObject(String string, Locale locale) throws ConversionException {
                            Country result = null;
                            if(string != null && !string.isEmpty()) {
                                int pos = countriesList.indexOf(new Country(string));
                                result = pos > -1 ? countriesList.get(pos) : new Country(string);
                            } else {
                                editor.error(new StringResourceModel("error.invalidCountry", CatalogEditorPanel.this, null).getString());
                            }
                            return (C) result;
                        }

                        @Override
                        public String convertToString(C c, Locale locale) {
                            if(c == null) {
                                return "";
                            }
                            return c.toString();
                        }
                    };
                }
            };
            size = new AutoCompleteTextField<TireSize>("size") {
                @Override
                protected Iterator<TireSize> getChoices(String string) {
                    if(string.isEmpty()) return Collections.emptyListIterator();
                    if(string.trim().isEmpty()) return sizesList.iterator();
                    ArrayList<TireSize> choices = new ArrayList<TireSize>();
                    for(TireSize size : sizesList) {
                        if(size.getSizeVerbal().toUpperCase().startsWith(string.toUpperCase())) {
                            choices.add(size);
                        }
                    }
                    return choices.iterator();
                }
                @Override
                public <C> IConverter<C> getConverter(Class<C> type) {
                    return new IConverter<C>() {

                        @Override
                        public C convertToObject(String string, Locale locale) throws ConversionException {
                            TireSize result = null;
                            if(string != null) {
                                string = string.replace(" ", "").replace(",", ".").replace("./", "/");
                                if(TireSize.TIRE_SIZE_PATTERN.matcher(string).matches()) {
                                    int pos = sizesList.indexOf(new TireSize(string));
                                    result = pos > -1 ? sizesList.get(pos) : new TireSize(string);
                                }
                            } else {
                                editor.error(new StringResourceModel("error.invalidSize", CatalogEditorPanel.this, null).getString());
                            }
                            return (C) result;
                        }

                        @Override
                        public String convertToString(C c, Locale locale) {
                            if(c == null) {
                                return "";
                            }
                            return c.toString();
                        }
                    };
                }
            };
            modelName = new AutoCompleteTextField<String>("modelName") {
                @Override
                protected Iterator<String> getChoices(String string) {
                    if(string.isEmpty()) return Collections.emptyListIterator();
                    if(string.trim().isEmpty()) return modelsList.iterator();
                    ArrayList<String> choices = new ArrayList<String>();
                    for(String modelName : modelsList) {
                        if(modelName.toUpperCase().startsWith(string.toUpperCase())) {
                            choices.add(modelName);
                        }
                    }
                    return choices.iterator();
                }
            };
            season = new DropDownChoice<String>("season", Constants.seasons) {
                @Override
                protected boolean localizeDisplayValues() {
                    return true;
                }
            };
            type = new DropDownChoice<TireType>("type", typesList == null ? new ArrayList<TireType>() : typesList);
            price = new TextField<BigDecimal>("price");
            quantity = new TextField<Long>("quantity");
            
            saveButton = new AjaxButton("save") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    Tire tire = (Tire) this.getForm().getModelObject();
                    if(tire == null) {
                        error(new StringResourceModel("error.modelObjectNull", CatalogEditorPanel.this, null).getString());
                        return;
                    }
                    //save non-persistent entities (new manufacturers, countries, etc)
                    if(tire.getCountry().getId() == null) {
                        tireService.save(tire.getCountry());
                    }
                    if(tire.getManufacturer().getId() == null) {
                        tireService.save(tire.getManufacturer());
                    }
                    if(tire.getSize().getId() == null) {
                        tireService.save(tire.getSize());
                    }
                    //set images for this tire
                    if(imagesForUpload != null && !imagesForUpload.isEmpty()) {
                        for(FileUpload item : imagesForUpload) {
//                            TireImage ti = new TireImage(
//                                    item.getClientFileName(),
//                                    Base64Coder.encodeLines(item.getBytes())
//                                );
//                            if(ti.getFileName().startsWith(Constants.IMAGE_PREVIEW_PREFIX)) {
//                                ti.setPreview(Constants.Y);
//                            }
//                            tire.addImage(ti);
                        }
                    }
                    
                    //save or update tire
                    if(tire.getId() == null) {
                        tireService.save(tire);
                    } else {
                        tireService.update(tire);
                    }
                    
                    initDictData(tire);
                    
//                    editor.setDefaultModel(new CompoundPropertyModel<Tire>(tire));
//                    target.add(editor);
//                    target.add(viewForm);
//                    target.add(uploadForm);
//                    target.add(getPage());
                    
                    //TODO: use AJAX instead ^
                }
            };
            clearButton = new AjaxButton("clear") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    editor.setDefaultModel(new CompoundPropertyModel<Tire>(currentTire = new Tire()));
                    viewForm.imagesView.setList(currentTire.getImages());
                    target.add(editor);
                    target.add(viewForm);
                    target.add(uploadForm);
                }
            };
            feedback = new FeedbackPanel("feedback");

            add(manufacturer.setOutputMarkupId(true));
            add(country.setOutputMarkupId(true));
            add(size.setOutputMarkupId(true));
            add(modelName.setOutputMarkupId(true));
            add(season.setOutputMarkupId(true));
            add(type.setOutputMarkupId(true));
            add(price.setOutputMarkupId(true));
            add(quantity.setOutputMarkupId(true));
            add(saveButton);
            add(clearButton);
            add(feedback.setOutputMarkupId(true));
        }
    }
    
    //Upload images
    private class UploadForm extends Form {

        public UploadForm(String id) {
            super(id);
            
            imagesForUpload = new ArrayList<FileUpload>();
            fileUpload = new MultiFileUploadField(
                    "fileUpload", 
                    new PropertyModel<List<FileUpload>>(CatalogEditorPanel.this, "imagesForUpload"),
                    Constants.MAX_IMAGES_PER_ITEM
                ) {
                @Override
                public boolean isMultiPart() {
                    return true;
                }
            };
            add(fileUpload);
        }       
    }
    
    //Display images
    private class ViewForm extends Form {
        
        private ListView imagesView;
        private WebMarkupContainer viewContainer;

        public ViewForm(String id) {
            super(id);
            viewContainer = new WebMarkupContainer("viewContainer");
            imagesView = new ListView("imagesView", new PropertyModel(currentTire, "images")) {
                @Override
                protected void populateItem(ListItem li) {
                    final TireImage ti = (TireImage) li.getModelObject();
                    Image image = new Image("image", new DynamicImageResource() {
                        @Override
                        protected byte[] getImageData(IResource.Attributes atrbts) {
//                            return Base64Coder.decodeLines(ti.getEncodedImage());
                            return null;
                        }
                    });
                    AjaxLink removeLink = new AjaxLink("removeImage") {
                        @Override
                        public void onClick(AjaxRequestTarget art) {
                            tireService.removeImage(ti);
                            art.add(viewContainer);
                        }
                    };
                    li.add(image);
                    li.add(removeLink);
                }
            };
            viewContainer.add(imagesView.setOutputMarkupId(true));
            add(viewContainer.setOutputMarkupId(true));
        }
        
    }
}
