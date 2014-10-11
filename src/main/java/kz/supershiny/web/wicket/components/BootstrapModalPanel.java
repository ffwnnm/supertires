/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author kilrwhle
 */
public abstract class BootstrapModalPanel extends Panel {

    public BootstrapModalPanel(String id, String header) {
        super(id);

        //Modal window header
        Label label = new Label("header", header);
        add(label);

        //the windows content
        add(BootstrapModalPanel.this.getContent("content"));

        //Buttons
        AjaxLink cancel = new AjaxLink("cancel") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                BootstrapModalPanel.this.onCancel(target);
            }
        };
        add(cancel);

        AjaxLink submit = new AjaxLink("submit") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                BootstrapModalPanel.this.onSubmit(target);
            }
        };
        add(submit);
    }

    public abstract Panel getContent(String id);

    public abstract void onCancel(AjaxRequestTarget target);

    public abstract void onSubmit(AjaxRequestTarget target);

}
