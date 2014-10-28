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
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
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

        private DropDownChoice<String> season;
        private DropDownChoice<Manufacturer> manufacturer;
        private DropDownChoice<Float> width;
        private DropDownChoice<Float> height;
        private DropDownChoice<Float> radius;
        private DropDownChoice<String> sorting;

        public CatalogSearchForm(String id) {
            super(id, new CompoundPropertyModel<TireSearchCriteria>(criteria));

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
            sorting = new DropDownChoice<String>("sorting", Constants.sortings) {
                @Override
                protected boolean localizeDisplayValues() {
                    return true;
                }
            };
            manufacturer = new DropDownChoice<Manufacturer>("manufacturer", uniqueManufacturers);

            add(season.setOutputMarkupId(true));
            add(sorting.setOutputMarkupId(true));
            add(manufacturer.setOutputMarkupId(true));

            add(new Link("clear") {
                @Override
                public void onClick() {
                    criteria = new TireSearchCriteria();
                    CatalogSearchForm.this.onSubmit();
                }
            });
            add(new AjaxSubmitLink("search") {});
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
        uniqueManufacturers = tireService.getUniqueManufacturers();
        uniqueRadius = tireService.getUniqueRadius();
        uniqueWidth = tireService.getUniqueWidth();
        uniqueHeight = tireService.getUniqueHeight();

        if (uniqueManufacturers == null) {
            uniqueManufacturers = new ArrayList<Manufacturer>();
        }
        if (uniqueRadius == null) {
            uniqueRadius = new ArrayList<Float>();
        }
        if (uniqueWidth == null) {
            uniqueWidth = new ArrayList<Float>();
        }
        if (uniqueHeight == null) {
            uniqueHeight = new ArrayList<Float>();
        }

        criteria = ((TiresApplication) getApplication())
                .getTiresSession()
                .getTireSearchCriteria();
    }
}
