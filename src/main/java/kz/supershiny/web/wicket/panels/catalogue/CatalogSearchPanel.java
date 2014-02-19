/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.panels.catalogue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
//import kz.supershiny.core.model.Country;
import kz.supershiny.core.model.Manufacturer;
//import kz.supershiny.core.model.TireType;
import kz.supershiny.core.pojo.TireSearchCriteria;
import kz.supershiny.core.services.TireService;
import kz.supershiny.core.util.Constants;
import kz.supershiny.web.wicket.TiresApplication;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author aishmanov
 */
public class CatalogSearchPanel extends Panel {
    
    @SpringBean
    private TireService tireService;
    
    private List<String> uniqueSizes;
//    private List<TireType> uniqueTypes;
    private List<Manufacturer> uniqueManufacturers;
//    private List<Country> uniqueCountries;
    private TireSearchCriteria criteria;
    private CatalogSearchForm searchForm;

    public CatalogSearchPanel(String id) {
        super(id);
        
        initData();
        
        searchForm = new CatalogSearchForm("searchForm");
        add(searchForm.setOutputMarkupId(true));
    }
    
    private class CatalogSearchForm extends Form {
        
        private AutoCompleteTextField<String> size;
        private DropDownChoice<String> season;
//        private DropDownChoice<TireType> type;
        private DropDownChoice<Manufacturer> manufacturer;
//        private DropDownChoice<Country> country;

        public CatalogSearchForm(String id) {
            super(id, new CompoundPropertyModel<TireSearchCriteria>(criteria));
            
            size = new AutoCompleteTextField<String>("size") {
                @Override
                protected Iterator<String> getChoices(String string) {
                    if(string == null || string.trim().isEmpty()) {
                        return uniqueSizes.iterator();
                    }
                    ArrayList<String> choices = new ArrayList<String>();
                    for(String size : uniqueSizes) {
                        if(size.toUpperCase().startsWith(string.toUpperCase())) {
                            choices.add(size);
                        }
                    }
                    return choices.iterator();
                }
            };
            add(size.setOutputMarkupId(true));
            
            season = new DropDownChoice<String>("season", Constants.seasons) {
                @Override
                protected boolean localizeDisplayValues() {
                    return true;
                }
            };
//            type = new DropDownChoice<TireType>("type", uniqueTypes);
            manufacturer = new DropDownChoice<Manufacturer>("manufacturer", uniqueManufacturers);
//            country = new DropDownChoice<Country>("country", uniqueCountries);
            
            add(season.setOutputMarkupId(true));
//            add(type.setOutputMarkupId(true));
            add(manufacturer.setOutputMarkupId(true));
//            add(country.setOutputMarkupId(true));
            
            add(new Button("clear") {
                @Override
                public void onSubmit() {
                    criteria = new TireSearchCriteria();
                }
            });
            add(new Button("search"));
        }

        @Override
        protected void onSubmit() {
            ((TiresApplication) getApplication())
                            .getTiresSession()
                            .setTireSearchCriteria(criteria);
            setResponsePage(getPage().getClass());
        }
        
    }
    
    private void initData() {
        uniqueSizes = tireService.getUniqueVerbalSizes();
//        uniqueCountries = tireService.getUniqueCountries();
//        uniqueTypes = tireService.getTypes();
        uniqueManufacturers = tireService.getUniqueManufacturers();
        
        if(uniqueSizes == null) uniqueSizes = new ArrayList<String>();
//        if(uniqueCountries == null) uniqueCountries = new ArrayList<Country>();
//        if(uniqueTypes == null) uniqueTypes = new ArrayList<TireType>();
        if(uniqueManufacturers == null) uniqueManufacturers = new ArrayList<Manufacturer>();
        
        criteria = ((TiresApplication) getApplication())
                        .getTiresSession()
                        .getTireSearchCriteria();
    }
}
