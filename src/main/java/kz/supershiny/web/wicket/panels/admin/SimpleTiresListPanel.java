/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.panels.admin;

import java.util.ArrayList;
import java.util.List;
import kz.supershiny.core.model.Tire;
import kz.supershiny.core.services.TireService;
import kz.supershiny.core.util.Constants;
import kz.supershiny.web.wicket.pages.admin.AdminPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Упрощенный список товаров, по клику доступно редактирование
 *
 * @author aishmanov
 */
public class SimpleTiresListPanel extends Panel {
    
    @SpringBean
    private TireService tireService;
    
    private List<Tire> tires;
    private TiresListForm listForm;

    public SimpleTiresListPanel(String id) {
        super(id);
        
        initData();
        
        listForm = new TiresListForm("tiresListForm");
        add(listForm.setOutputMarkupId(true));
    }
    
    private void initData() {
        tires = tireService.getAllTires();
        if(tires == null) tires = new ArrayList<Tire>();
    }
    
    private class TiresListForm extends Form {
        
        private PageableListView<Tire> tiresListView;
        private WebMarkupContainer listContainer;

        public TiresListForm(String id) {
            super(id);
            
            listContainer = new WebMarkupContainer("listContainer");
            add(listContainer.setOutputMarkupId(true));
            
            tiresListView = new PageableListView<Tire>(
                    "tiresListView", 
                    new PropertyModel<List<Tire>>(SimpleTiresListPanel.this, "tires"), 
                    Constants.ITEMS_PER_PAGE_SIMPLE
            ) {
                @Override
                protected void populateItem(ListItem<Tire> li) {
                    final Tire tire = li.getModelObject();
                    
                    final AjaxLink removeLink = new AjaxLink("removeLink") {
                        @Override
                        public void onClick(AjaxRequestTarget art) {
                            tireService.delete(tire);
                            initData();
                            art.add(listContainer);
                        }
                        @Override
                        protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                            super.updateAjaxAttributes(attributes);
                            AjaxCallListener ajaxCallListener = new AjaxCallListener();
                            ajaxCallListener.onPrecondition("return confirm('" 
                                    + new StringResourceModel("confirm.deletion", SimpleTiresListPanel.this, null).getString()
                                    + "');");
                            attributes.getAjaxCallListeners().add(ajaxCallListener);
                        }
                    };
                    
                    final Link manufacturerLink = new Link("manufacturerLink") {
                        @Override
                        public void onClick() {
                            
                            //TODO: implement!
                            System.out.println("Manufacturer: " + tire.getManufacturer().getCompanyName());
                        }
                    };
                    manufacturerLink.add(new Label("manufacturer", tire.getManufacturer().getCompanyName()));
                    
                    final Link modelLink = new Link("modelLink") {
                        @Override
                        public void onClick() {
                            setResponsePage(new AdminPage(tire));
                        }
                    };
                    modelLink.add(new Label("model", tire.getModelName()));
                    
                    li.add(new Label("num", (li.getIndex() + 1) + ""));
                    li.add(manufacturerLink);
                    li.add(modelLink);
                    li.add(new Label("size", tire.getSize().getSizeVerbal()));
                    li.add(new Label("season", new StringResourceModel(tire.getSeason(), SimpleTiresListPanel.this, null).getString()));
                    li.add(removeLink);
                }
            };
            listContainer.add(new AjaxPagingNavigator("navigator", tiresListView));
            listContainer.add(tiresListView);
        }
        
    }
}
