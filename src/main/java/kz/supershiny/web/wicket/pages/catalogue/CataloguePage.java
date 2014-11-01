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
import kz.supershiny.web.wicket.components.BootstrapPagingNavigator;
import kz.supershiny.web.wicket.pages.BasePage;
import kz.supershiny.web.wicket.panels.catalogue.CatalogSearchPanel;
import kz.supershiny.web.wicket.panels.catalogue.TireWidgetPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
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
    private WebMarkupContainer paging1, paging2;

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
            public void detach() {
            }
        };

        tiresContainer = new WebMarkupContainer("tiresContainer");
        paging1 = new WebMarkupContainer("paging1");
        paging2 = new WebMarkupContainer("paging2");
        tiresContainer.add(paging1);
        tiresContainer.add(paging2);
        if (catalogProvider.size() > 0) {
            tiresContainer.add(new Label("empty").setVisible(false));
            tiresDataView = new DataView<Tire>("widgetsList", catalogProvider, Constants.ITEMS_PER_PAGE) {
                @Override
                protected void populateItem(Item<Tire> item) {
                    item.add(new TireWidgetPanel("widget", item.getModelObject()));
                }
            };
            tiresContainer.add(tiresDataView.setOutputMarkupId(true));
            paging1.add(new BootstrapPagingNavigator("navigator1", tiresDataView));
            paging2.add(new BootstrapPagingNavigator("navigator2", tiresDataView));
            paging1.add(new Label("itemsCount1", catalogProvider.size() + ""));
            paging2.add(new Label("itemsCount2", catalogProvider.size() + ""));
        } else {
            tiresContainer.add(new Label("empty", new StringResourceModel("catalog.empty", CataloguePage.this, null).getString()).setVisible(true));
            tiresContainer.add(new Label("widgetsList").setVisible(false));
            paging1.setVisible(false);
            paging2.setVisible(false);
        }

        add(tiresContainer.setOutputMarkupId(true));
    }

    private void initData() {
        tires = tireService.getTiresByCriteria(getCriteria());
        if (tires == null) {
            tires = new ArrayList<Tire>();
        }
    }

    private TireSearchCriteria getCriteria() {
        return getTiresSession().getTireSearchCriteria();
    }
}
