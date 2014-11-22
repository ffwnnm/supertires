/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.components;

import kz.supershiny.core.pojo.SearchCriteria;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Paging panel for all kinds of SearchCriteria
 *
 * @author kilrwhle
 */
public abstract class AjaxPagingPanel extends Panel {
    
    private WebMarkupContainer globalContainer;
    private ListView numbers;
    private AjaxLink prev;
    private AjaxLink sprev;
    private AjaxLink next;
    private AjaxLink snext;

    public AjaxPagingPanel(String id, final SearchCriteria criteria) {
        super(id);
        
        globalContainer = new WebMarkupContainer("globalContainer");
        add(globalContainer.setOutputMarkupId(true));
        
        next = new AjaxLink("next") {
            @Override
            public void onClick(AjaxRequestTarget art) {
                criteria.next();
                refreshPaging(art, criteria);
            }
            @Override
            public boolean isEnabled() {
                return !criteria.isLastPage();
            }
        };
        globalContainer.add(next.setOutputMarkupId(true));
        
        prev = new AjaxLink("prev") {
            @Override
            public void onClick(AjaxRequestTarget art) {
                criteria.prev();
                refreshPaging(art, criteria);
            }
            @Override
            public boolean isEnabled() {
                return !criteria.isFirstPage();
            }
        };
        globalContainer.add(prev.setOutputMarkupId(true));
        
        snext = new AjaxLink("snext") {
            @Override
            public void onClick(AjaxRequestTarget art) {
                criteria.snext();
                refreshPaging(art, criteria);
            }
            @Override
            public boolean isEnabled() {
                return !criteria.isLastPage();
            }
        };
        globalContainer.add(snext.setOutputMarkupId(true));
        
        sprev = new AjaxLink("sprev") {
            @Override
            public void onClick(AjaxRequestTarget art) {
                criteria.sprev();
                refreshPaging(art, criteria);
            }
            @Override
            public boolean isEnabled() {
                return !criteria.isFirstPage();
            }
        };
        globalContainer.add(sprev.setOutputMarkupId(true));
        
        numbers = new ListView("numbers", criteria.getNumbers()) {
            
            @Override
            protected void populateItem(ListItem li) {
                final long pageNum = (Long) li.getModelObject();
                final Label number = new Label("number", pageNum + "");
                final AjaxLink numberLink = new AjaxLink("numberLink") {
                    @Override
                    public void onClick(AjaxRequestTarget art) {
                        criteria.toPage(pageNum);
                        refreshPaging(art, criteria);
                    }
                };
                if (pageNum != criteria.getCurrent()) {
                    numberLink.setEnabled(true);
                    li.add(AttributeModifier.remove("class"));
                } else {
                    numberLink.setEnabled(false);
                    li.add(AttributeModifier.append("class", "active"));
                }
                numberLink.add(number);
                li.add(numberLink);
            }
        };
        globalContainer.add(numbers.setOutputMarkupId(true));
    }
    
    private void refreshPaging(AjaxRequestTarget art, SearchCriteria criteria) {
        updateListItems(art);
        globalContainer.replace(numbers.setList(criteria.getNumbers()));
//        prev.add(AttributeModifier.remove("class"));
//        sprev.add(AttributeModifier.remove("class"));
//        next.add(AttributeModifier.remove("class"));
//        snext.add(AttributeModifier.remove("class"));
//        if (criteria.isFirstPage()) {
//            prev.add(AttributeModifier.append("class", "disabled"));
//            sprev.add(AttributeModifier.append("class", "disabled"));
//        }
//        if (criteria.isLastPage()) {
//            next.add(AttributeModifier.append("class", "disabled"));
//            snext.add(AttributeModifier.append("class", "disabled"));
//        }
        art.add(next, prev, snext, sprev, globalContainer);
    }
    
    //all magic is made here
    public abstract void updateListItems(AjaxRequestTarget art);
}
