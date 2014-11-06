/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.components;

import kz.supershiny.web.wicket.panels.BasePanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

/**
 *
 * @author AIshmanov
 */
public class RichTextEditor extends BasePanel {
    
    private TextField input;
    private String content;

    public RichTextEditor(String id) {
        this(id, "");
    }

    public RichTextEditor(String id, String content) {
        super(id);
        
        this.content = content;
        
        input = new TextField("editor-input", new PropertyModel(RichTextEditor.this, "content"));
        add(input.setMarkupId("editor-input").setOutputMarkupId(true));
    }
    
    public String getPrettyContent() {
        return content = input.getConvertedInput() == null ? null : input.getConvertedInput().toString();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
