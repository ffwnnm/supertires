package kz.supershiny.web.wicket.components;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author kilrwhle
 */
public class ModalBehavior extends Behavior {

    private String modalWindowId;

    public ModalBehavior(String modalWindowId) {
        this.modalWindowId = modalWindowId;
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forScript("$('#" + modalWindowId + "').modal()", "modalbhvr_id"));
        super.renderHead(component, response);
    }

    @Override
    public void onComponentTag(Component component, ComponentTag tag) {
        tag.put("data-toggle", "modal");
        tag.put("href", "#" + modalWindowId);

        super.onComponentTag(component, tag);
    }
}
