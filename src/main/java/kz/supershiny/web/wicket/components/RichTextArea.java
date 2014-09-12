/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.components;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 *
 * @author AIshmanov
 */
public class RichTextArea extends Panel {
    
    private String content;

    public RichTextArea(String id) {
        super(id);
        
        //TODO: how to bind content to this JS stuff?
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        JavaScriptResourceReference js = new JavaScriptResourceReference(RichTextArea.class, "bootstrap-wysiwyg.js");
        response.render(JavaScriptReferenceHeaderItem.forReference(js));
        CssResourceReference css = new CssResourceReference(RichTextArea.class, "wysiwyg.css");
        response.render(JavaScriptReferenceHeaderItem.forReference(css));
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
