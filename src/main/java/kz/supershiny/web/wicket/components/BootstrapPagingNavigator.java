/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigation;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationIncrementLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigationLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;

/**
 *
 * @author kilrwhle
 */
public class BootstrapPagingNavigator extends AjaxPagingNavigator {

    public BootstrapPagingNavigator(String id, IPageable pageable) {
        super(id, pageable);
    }

    @Override
    protected PagingNavigation newNavigation(String id,
            IPageable pageable, IPagingLabelProvider labelProvider) {
        return new AjaxPagingNavigation(id, pageable, labelProvider) {
            @Override
            protected LoopItem newItem(int iteration) {
                LoopItem item = super.newItem(iteration);

                // add css for enable/disable link
                long pageIndex = getStartIndex() + iteration;
                item.add(new AttributeModifier("class",
                        new PageLinkCssModel(pageable,
                                pageIndex, "active")));

                return item;
            }
        };
    }

    @Override
    protected AbstractLink newPagingNavigationLink(String id,
            IPageable pageable, int pageNumber) {
        ExternalLink navCont = new ExternalLink(id + "Cont", (String) null);

        // add css for enable/disable link
        long pageIndex = pageable.getCurrentPage() + pageNumber;
        navCont.add(new AttributeModifier("class", new PageLinkCssModel(pageable,
                pageIndex, "disabled")));

        // change original wicket-link, so that it always generates href
        navCont.add(new AjaxPagingNavigationLink(id, pageable, pageNumber) {
            @Override
            protected void disableLink(ComponentTag tag) {
            }
        });
        return navCont;
    }

    @Override
    protected AbstractLink newPagingNavigationIncrementLink(String id,
            IPageable pageable, int increment) {
        ExternalLink navCont = new ExternalLink(id + "Cont", (String) null);

        // add css for enable/disable link
        long pageIndex = pageable.getCurrentPage() + increment;
        navCont.add(new AttributeModifier("class",
                new PageLinkIncrementCssModel(pageable, pageIndex)));

        // change original wicket-link, so that it always generates href
        navCont.add(new AjaxPagingNavigationIncrementLink(id,
                pageable, increment) {
                    @Override
                    protected void disableLink(ComponentTag tag) {
                    }
                });
        return navCont;
    }

}
