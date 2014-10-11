/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.components;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author kilrwhle
 */
public class BootstrapModalWindow extends Panel {

    private WebMarkupContainer wmc;

    public static String MODAL_PANEL_ID = "modalPanel";

    public BootstrapModalWindow(String id, BootstrapModalPanel modalPanel) {
        super(id);

        wmc = new WebMarkupContainer("wmc") {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                tag.put("id", getMarkupId());

                super.onComponentTag(tag);
            }
        ;
        };
        add(wmc);

        wmc.add(modalPanel);
    }

    public String getModalWindowId() {
        return wmc.getMarkupId();
    }

}
