/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.panels.catalogue;

import java.util.ArrayList;
import java.util.List;
import kz.supershiny.core.model.Manufacturer;
import kz.supershiny.core.pojo.TireSearchCriteria;
import kz.supershiny.core.services.TireService;
import kz.supershiny.core.util.Constants;
import kz.supershiny.web.wicket.TiresApplication;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
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
    
//    private List<String> uniqueSizes;
    private List<Manufacturer> uniqueManufacturers;
    private List<Float> uniqueWidth;
    private List<Float> uniqueHeight;
    private List<Float> uniqueRadius;
    private TireSearchCriteria criteria;
    private CatalogSearchForm searchForm;

    public CatalogSearchPanel(String id) {
        super(id);
        
        initData();
        
        searchForm = new CatalogSearchForm("searchForm");
        add(searchForm.setOutputMarkupId(true));
    }
    
    private class CatalogSearchForm extends Form {
        
//        private AutoCompleteTextField<String> size;
        private DropDownChoice<String> season;
        private DropDownChoice<Manufacturer> manufacturer;
        private DropDownChoice<Float> width;
        private DropDownChoice<Float> height;
        private DropDownChoice<Float> radius;

        public CatalogSearchForm(String id) {
            super(id, new CompoundPropertyModel<TireSearchCriteria>(criteria));
            
//            size = new AutoCompleteTextField<String>("size") {
//                @Override
//                protected Iterator<String> getChoices(String string) {
//                    if(string == null || string.trim().isEmpty()) {
//                        return uniqueSizes.iterator();
//                    }
//                    ArrayList<String> choices = new ArrayList<String>();
//                    for(String size : uniqueSizes) {
//                        if(size.toUpperCase().startsWith(string.toUpperCase())) {
//                            choices.add(size);
//                        }
//                    }
//                    return choices.iterator();
//                }
//            };
//            add(size.setOutputMarkupId(true));
            
            width = new DropDownChoice<Float>("width", uniqueWidth);
            height = new DropDownChoice<Float>("height", uniqueHeight);
            radius = new DropDownChoice<Float>("radius", uniqueRadius);
            add(width.setOutputMarkupId(true));
            add(height.setOutputMarkupId(true));
            add(radius.setOutputMarkupId(true));
            
            season = new DropDownChoice<String>("season", Constants.seasons) {
                @Override
                protected boolean localizeDisplayValues() {
                    return true;
                }
            };
            manufacturer = new DropDownChoice<Manufacturer>("manufacturer", uniqueManufacturers);
            
            add(season.setOutputMarkupId(true));
            add(manufacturer.setOutputMarkupId(true));
            
            add(new Link("clear") {
                @Override
                public void onClick() {
                    criteria = new TireSearchCriteria();
                    CatalogSearchForm.this.onSubmit();
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
//        uniqueSizes = tireService.getUniqueVerbalSizes();
        uniqueManufacturers = tireService.getUniqueManufacturers();
        uniqueRadius = tireService.getUniqueRadius();
        uniqueWidth = tireService.getUniqueWidth();
        uniqueHeight = tireService.getUniqueHeight();
        
//        if(uniqueSizes == null) uniqueSizes = new ArrayList<String>();
        if(uniqueManufacturers == null) uniqueManufacturers = new ArrayList<Manufacturer>();
        if(uniqueRadius == null) uniqueRadius = new ArrayList<Float>();
        if(uniqueWidth == null) uniqueWidth = new ArrayList<Float>();
        if(uniqueHeight == null) uniqueHeight = new ArrayList<Float>();
        
        criteria = ((TiresApplication) getApplication())
                        .getTiresSession()
                        .getTireSearchCriteria();
    }
}
