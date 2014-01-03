/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.panels;

import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author kilrwhle
 */
public final class TopPanel extends Panel {

    public TopPanel(String id) {
        super(id);
        
        add(new LoginPanel("loginPanel"));
    }
}
