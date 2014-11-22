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
import kz.supershiny.web.wicket.components.AjaxPagingPanel;
import kz.supershiny.web.wicket.pages.BasePage;
import kz.supershiny.web.wicket.panels.catalogue.CatalogSearchPanel;
import kz.supershiny.web.wicket.panels.catalogue.TireWidgetPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
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
    private ListView<Tire> tiresView;
    private WebMarkupContainer tiresContainer, pagingContainerTop, pagingContainerBottom;
    private AjaxPagingPanel pagingTop, pagingBottom;

    public CataloguePage() {
        super();

        initData();

        add(new CatalogSearchPanel("searchPanel") {
            @Override
            public void refreshListData(AjaxRequestTarget art) {
                getCriteria().resetPaging();
                refreshTiresList(art);
            }
        });
        
        pagingContainerTop = new WebMarkupContainer("pagingContainerTop");
        add(pagingContainerTop.setOutputMarkupId(true));
        pagingContainerBottom = new WebMarkupContainer("pagingContainerBottom");
        add(pagingContainerBottom.setOutputMarkupId(true));

        tiresContainer = new WebMarkupContainer("tiresContainer");
        pagingTop = new AjaxPagingPanel("pagingTop", getTiresSession().getTireSearchCriteria()) {
            @Override
            public void updateListItems(AjaxRequestTarget art) {
                refreshTiresList(art);
            }
        };
        pagingContainerTop.add(pagingTop.setOutputMarkupId(true));
        
        pagingBottom = new AjaxPagingPanel("pagingBottom", getTiresSession().getTireSearchCriteria()) {
            @Override
            public void updateListItems(AjaxRequestTarget art) {
                refreshTiresList(art);
            }
        };
        pagingContainerBottom.add(pagingBottom.setOutputMarkupId(true));
        
        tiresContainer.add(pagingContainerTop);
        tiresContainer.add(pagingContainerBottom);
        
        if (getCriteria().getResultSize() > 0) {
            tiresContainer.add(new Label("empty").setVisible(false));
            tiresView = new ListView<Tire>("widgetsList", new PropertyModel<List<Tire>>(CataloguePage.this, "tires")) {
                
                @Override
                protected void populateItem(ListItem<Tire> li) {
                    li.add(new TireWidgetPanel("widget", li.getModelObject()));
                }
            };
            tiresContainer.add(tiresView.setOutputMarkupId(true));
            
            pagingContainerTop.add(new Label("itemsCountTop", new PropertyModel(getCriteria(), "total")).setOutputMarkupId(true));
            pagingContainerBottom.add(new Label("itemsCountBottom", new PropertyModel(getCriteria(), "total")).setOutputMarkupId(true));
        } else {
            tiresContainer.add(new Label("empty", new StringResourceModel("catalog.empty", CataloguePage.this, null).getString()).setVisible(true));
            tiresContainer.add(new Label("widgetsList").setVisible(false));
            pagingContainerTop.setVisible(false);
            pagingContainerBottom.setVisible(false);
        }

        add(tiresContainer.setOutputMarkupId(true));
    }
    
    private void refreshTiresList(AjaxRequestTarget art) {
        initData();
        getCriteria().refreshPaging();
        art.add(tiresContainer);
        art.add(pagingContainerTop);
        art.add(pagingContainerBottom);
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
