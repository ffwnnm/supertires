package kz.supershiny.web.wicket.panels.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import kz.supershiny.core.model.Country;
import kz.supershiny.core.model.Manufacturer;
import kz.supershiny.core.model.Tire;
import kz.supershiny.core.model.TireImage;
import kz.supershiny.core.model.TireSize;
import kz.supershiny.core.model.TireType;
import kz.supershiny.core.services.ImageService;
import kz.supershiny.core.services.TireService;
import kz.supershiny.core.util.Constants;
import kz.supershiny.web.wicket.components.ConfirmationLink;
import kz.supershiny.web.wicket.components.TiresAutocompleteTextField;
import kz.supershiny.web.wicket.pages.admin.AdminBasePage;
import kz.supershiny.web.wicket.pages.catalogue.CataloguePage;
import kz.supershiny.web.wicket.panels.BasePanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

/**
 * NEW
 * 
 * @author kilrwhle
 */
public class CataloguePanel extends BasePanel {
    
    @SpringBean
    private TireService tireService;
    
    private List<TireType> typesList;
    private List<TireSize> sizesList;
    private List<Country> countriesList;
    private List<Manufacturer> manufacturersList;
    private List<String> modelsList;
    private Tire currentTire;
    
    private FeedbackPanel feed;
    private CatalogueEditorForm editor;
    
    public CataloguePanel(String id) {
        this(id, null);
    }
    
    public CataloguePanel(String id, Long tireId) {
        super(id);
        
        initData(tireId);
        
        feed = new FeedbackPanel("feedback");
        add(feed.setOutputMarkupPlaceholderTag(true).setVisible(false));
        
        add(new BookmarkablePageLink("editCatalogue", CataloguePage.class));
        
        editor = new CatalogueEditorForm("editorForm");
        add(editor.setOutputMarkupId(true));
    }
    
    /**
     * The Grand initializer
     * 
     * @param tireId 
     */
    private void initData(Long tireId) {
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
        currentTire = tireService.getTireWithImages(tireId);
        if(currentTire == null) {
            currentTire = new Tire();
        }
    }
    
    private class CatalogueEditorForm extends Form {
        
        private AutoCompleteTextField<Manufacturer> manufacturer;
        private AutoCompleteTextField<Country> country;
        private AutoCompleteTextField<String> modelName;
        private AutoCompleteTextField<TireSize> size;
        private DropDownChoice<TireType> type;
        private DropDownChoice<String> season;
        private TextField<BigDecimal> price;
        private TextField<Long> quantity;
        private ImageUploadPanel imagesPanel;

        public CatalogueEditorForm(String id) {
            super(id, new CompoundPropertyModel<Tire>(currentTire));
            
            addFormFields();
            addControls();
            
            imagesPanel = new ImageUploadPanel("imagesPanel", currentTire.getImages());
            add(imagesPanel.setOutputMarkupId(true));
            
//            add(new AjaxLink("createPreviews") {
//                
//                @Override
//                public void onClick(AjaxRequestTarget art) {
//                    tireService.createPreviews();
//                }
//            });
        }
        
        private void clearForm(AjaxRequestTarget target) {
            initData(null);
            editor.setDefaultModel(new CompoundPropertyModel<Tire>(currentTire));
            
            ImageUploadPanel newImagesPanel = new ImageUploadPanel("imagesPanel", currentTire.getImages());
            newImagesPanel.setOutputMarkupId(true);
            imagesPanel.replaceWith(newImagesPanel);
            imagesPanel = newImagesPanel;
            
            target.add(imagesPanel);
            target.add(editor);
        }
        
        private void removeTire(AjaxRequestTarget target) {
            if (currentTire != null && currentTire.getId() != null) {
                tireService.delete(currentTire);
                clearForm(target);
            }
        }

        @Override
        protected void onValidate() {
            super.onValidate();
            
            //set model values
            currentTire.setCountry(country.getConvertedInput());
            currentTire.setManufacturer(manufacturer.getConvertedInput());
            currentTire.setModelName(modelName.getConvertedInput());
            currentTire.setPrice(price.getConvertedInput());
            currentTire.setQuantity(quantity.getConvertedInput());
            currentTire.setSeason(season.getConvertedInput());
            currentTire.setSize(size.getConvertedInput());
            currentTire.setType(type.getConvertedInput());
            
            if (currentTire.isEmpty()) {
                editor.error(new StringResourceModel("error.emptyTire", CataloguePanel.this, null).getString());
            }
            
            feed.setVisible(this.hasError());
        }

        @Override
        protected void onSubmit() {
            //save non-persistent entities (new manufacturers, countries, etc)
            if(currentTire.getCountry().getId() == null) {
                tireService.save(currentTire.getCountry());
            }
            if(currentTire.getManufacturer().getId() == null) {
                tireService.save(currentTire.getManufacturer());
            }
            if(currentTire.getSize().getId() == null) {
                tireService.save(currentTire.getSize());
            }
            
            currentTire.setImages(new ArrayList<TireImage>());
            
            if (currentTire.getId() != null) {
                tireService.update(currentTire);
            } else {
                tireService.save(currentTire);
            }
            
            //set images for this tire
            List<TireImage> imagesForUpload = imagesPanel.getAllImages();
            if(imagesForUpload != null && !imagesForUpload.isEmpty()) {
                for(TireImage item : imagesForUpload) {
                    item.setTire(currentTire);
                    item.setFileName(ImageService.getPhotoFilename(currentTire.getId(), item.getFileName(), item.getImageSize()));
                }
            }
            currentTire.setImages(imagesForUpload);
            
            tireService.update(currentTire);
            
            setResponsePage(new AdminBasePage(new PageParameters().add("target", "catalogue").add("tireId", currentTire.getId())));
        }
        
        private void addControls() {
            //top
            add(new AjaxLink("clear") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    clearForm(target);
                }
            });
            add(new ConfirmationLink("remove", new StringResourceModel("ask.deletion", CataloguePanel.this, null).getString()) {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    removeTire(target);
                }
            });
            
            //bottom
            add(new AjaxLink("clear2") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    clearForm(target);
                }
            });
            add(new ConfirmationLink("remove2", new StringResourceModel("ask.deletion", CataloguePanel.this, null).getString()) {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    removeTire(target);
                }
            });
        }
        
        private void addFormFields() {
            manufacturer = new TiresAutocompleteTextField<Manufacturer>(
                    "manufacturer",
                    Manufacturer.class,
                    manufacturersList
            ){
                @Override
                public <C> IConverter<C> getConverter(Class<C> type) {
                    return new IConverter<C>() {

                        @Override
                        public C convertToObject(String string, Locale locale) throws ConversionException {
                            Manufacturer result = null;
                            if(string != null && !string.isEmpty()) {
                                int pos = manufacturersList.indexOf(new Manufacturer(string));
                                result = pos > -1 ? manufacturersList.get(pos) : new Manufacturer(string);
                                return (C) result;
                            }
                            editor.error(new StringResourceModel("error.invalidManufacturer", CataloguePanel.this, null).getString());
                            return null;
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
            country = new TiresAutocompleteTextField<Country>(
                    "country",
                    Country.class,
                    countriesList
            ) {
                @Override
                public <C> IConverter<C> getConverter(Class<C> type) {
                    return new IConverter<C>() {

                        @Override
                        public C convertToObject(String string, Locale locale) throws ConversionException {
                            Country result = null;
                            if(string != null && !string.isEmpty()) {
                                int pos = countriesList.indexOf(new Country(string));
                                result = pos > -1 ? countriesList.get(pos) : new Country(string);
                                return (C) result;
                            }
                            editor.error(new StringResourceModel("error.invalidCountry", CataloguePanel.this, null).getString());
                            return null;
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
            size = new TiresAutocompleteTextField<TireSize>(
                    "size",
                    TireSize.class,
                    sizesList
            ) {
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
                                    return (C) result;
                                }
                            }
                            editor.error(new StringResourceModel("error.invalidSize", CataloguePanel.this, null).getString());
                            return null;
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
            modelName = new TiresAutocompleteTextField<String>(
                    "modelName",
                    String.class,
                    modelsList
            );
            season = new DropDownChoice<String>("season", Constants.seasons) {
                @Override
                protected boolean localizeDisplayValues() {
                    return true;
                }
            };
            type = new DropDownChoice<TireType>("type", typesList == null ? new ArrayList<TireType>() : typesList);
            price = new TextField<BigDecimal>("price");
            quantity = new TextField<Long>("quantity");
            
            add(manufacturer.setRequired(true).setOutputMarkupId(true));
            add(country.setRequired(true).setOutputMarkupId(true));
            add(size.setRequired(true).setOutputMarkupId(true));
            add(modelName.setRequired(true).setOutputMarkupId(true));
            add(season.setRequired(true).setOutputMarkupId(true));
            add(type.setRequired(true).setOutputMarkupId(true));
            add(price.setRequired(true).setOutputMarkupId(true));
            add(quantity.setOutputMarkupId(true));
        }
        
    }
}