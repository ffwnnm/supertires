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
import kz.supershiny.core.model.TireSize;
import kz.supershiny.core.model.TireType;
import kz.supershiny.core.services.TireService;
import kz.supershiny.core.util.Constants;
import kz.supershiny.web.wicket.components.TiresAutocompleteTextField;
import kz.supershiny.web.wicket.pages.catalogue.CataloguePage;
import kz.supershiny.web.wicket.panels.BasePanel;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.ComponentDetachableModel;
import org.apache.wicket.model.ComponentPropertyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.hibernate.cfg.ComponentPropertyHolder;

/**
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
        this(id, -1L);
    }
    
    public CataloguePanel(String id, long tireId) {
        super(id);
        
        initDictData(tireId);
        buildPanel();
    }
    
    /**
     * The Grand initializer
     * 
     * @param tireId 
     */
    private void initDictData(long tireId) {
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
        if(tireId == -1L) {
            currentTire = new Tire();
        } else {
            currentTire = tireService.getTireWithImages(tireId);
        }
    }
    
    private void buildPanel() {
        feed = new FeedbackPanel("feedback");
        add(feed.setOutputMarkupId(true).setVisible(false));
        
        add(new BookmarkablePageLink("editCatalogue", CataloguePage.class));
        
        editor = new CatalogueEditorForm("editorForm");
        add(editor.setOutputMarkupId(true));
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
            
            imagesPanel = new ImageUploadPanel("imagesPanel", currentTire);
            add(imagesPanel.setOutputMarkupId(true));
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
                            } else {
                                editor.error(new StringResourceModel("error.invalidManufacturer", CataloguePanel.this, null).getString());
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
                            } else {
                                editor.error(new StringResourceModel("error.invalidCountry", CataloguePanel.this, null).getString());
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
                                }
                            } else {
                                editor.error(new StringResourceModel("error.invalidSize", CataloguePanel.this, null).getString());
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
            
            add(manufacturer.setOutputMarkupId(true));
            add(country.setOutputMarkupId(true));
            add(size.setOutputMarkupId(true));
            add(modelName.setOutputMarkupId(true));
            add(season.setOutputMarkupId(true));
            add(type.setOutputMarkupId(true));
            add(price.setOutputMarkupId(true));
            add(quantity.setOutputMarkupId(true));
        }
        
    }
}