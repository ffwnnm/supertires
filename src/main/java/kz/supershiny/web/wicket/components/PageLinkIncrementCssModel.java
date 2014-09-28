/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.components;

import java.io.Serializable;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.model.IModel;

/**
 *
 * @author kilrwhle
 */
public class PageLinkIncrementCssModel implements IModel<String>, Serializable {

    protected final IPageable pageable;
    private final long pageNumber;

    public PageLinkIncrementCssModel(IPageable pageable,
            long pageNumber) {
        this.pageable = pageable;
        this.pageNumber = pageNumber;
    }

    @Override
    public String getObject() {
        return isEnabled() ? "" : "disabled";
    }

    @Override
    public void setObject(String object) {
    }

    @Override
    public void detach() {
    }

    public boolean isEnabled() {
        if (pageNumber < 0) {
            return !isFirst();
        } else {
            return !isLast();
        }
    }

    public boolean isFirst() {
        return pageable.getCurrentPage() <= 0;
    }

    public boolean isLast() {
        return pageable.getCurrentPage()
                >= (pageable.getPageCount() - 1);
    }
}
