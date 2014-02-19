/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.pages.catalogue;

import java.util.ArrayList;
import java.util.List;
import kz.supershiny.core.model.Tire;
import kz.supershiny.core.pojo.TireSearchCriteria;
import kz.supershiny.core.services.TireService;
import kz.supershiny.web.wicket.TiresApplication;
import kz.supershiny.web.wicket.pages.BasePage;
import kz.supershiny.web.wicket.panels.catalogue.CatalogSearchPanel;
import kz.supershiny.web.wicket.panels.catalogue.TireWidgetPanel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author aishmanov
 */
public class CataloguePage extends BasePage {
    
    @SpringBean
    private TireService tireService;
    
    private List<Tire> tires;

    public CataloguePage() {
        super();
        
        initData();
        
        add(new CatalogSearchPanel("searchPanel"));
        
        add(new ListView<Tire>("widgetsList", new PropertyModel<List<Tire>>(CataloguePage.this, "tires")) {
            @Override
            protected void populateItem(ListItem<Tire> li) {
                li.add(new TireWidgetPanel("widget", li.getModelObject()));
            }
        });
    }
    
    private void initData() {
        TireSearchCriteria criteria = ((TiresApplication) getApplication())
                            .getTiresSession().getTireSearchCriteria();
        tires = tireService.getTiresByCriteria(criteria);
        if(tires == null) tires = new ArrayList<Tire>();
    }
}
