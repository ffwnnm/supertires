/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.pages.catalogue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kz.supershiny.core.model.Tire;
import kz.supershiny.core.pojo.TireSearchCriteria;
import kz.supershiny.core.services.TireService;
import kz.supershiny.core.util.Constants;
import kz.supershiny.web.wicket.TiresApplication;
import kz.supershiny.web.wicket.pages.BasePage;
import kz.supershiny.web.wicket.panels.catalogue.CatalogSearchPanel;
import kz.supershiny.web.wicket.panels.catalogue.TireWidgetPanel;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 *
 * @author aishmanov
 */
public class CataloguePage extends BasePage {
    
    @SpringBean
    private TireService tireService;
    
    private List<Tire> tires;
    private DataView<Tire> tiresDataView;
    private WebMarkupContainer tiresContainer;

    public CataloguePage() {
        super();
        
        initData();
        
        add(new CatalogSearchPanel("searchPanel"));
        
        IDataProvider catalogProvider = new IDataProvider() {

            @Override
            public IModel model(final Object object) {
                return new LoadableDetachableModel() {
                    @Override
                    protected Tire load() {
                        return (Tire) object;
                    }
                };
            };

            @Override
            public Iterator iterator(long first, long count) {
                getCriteria().setItemsPerPage(count);
                getCriteria().setFirstOffset(first);
                return tireService.getTiresByCriteria(getCriteria()).iterator();
            }

            @Override
            public long size() {
                return tireService.countTiresByCriteria(getCriteria());
            }

            @Override
            public void detach() {}
        };
        
        tiresContainer = new WebMarkupContainer("tiresContainer");
        tiresDataView = new DataView<Tire>("widgetsList", catalogProvider, Constants.ITEMS_PER_PAGE) {
            @Override
            protected void populateItem(Item<Tire> item) {
                item.add(new TireWidgetPanel("widget", item.getModelObject()));
            }
        };
        tiresContainer.add(tiresDataView.setOutputMarkupId(true));
        tiresContainer.add(new AjaxPagingNavigator("navigator1", tiresDataView));
        tiresContainer.add(new AjaxPagingNavigator("navigator2", tiresDataView));
        add(tiresContainer.setOutputMarkupId(true));
    }
    
    private void initData() {
        tires = tireService.getTiresByCriteria(getCriteria());
        if(tires == null) tires = new ArrayList<Tire>();
    }
    
    private TireSearchCriteria getCriteria() {
        return ((TiresApplication) getApplication())
                            .getTiresSession().getTireSearchCriteria();
    }
}
