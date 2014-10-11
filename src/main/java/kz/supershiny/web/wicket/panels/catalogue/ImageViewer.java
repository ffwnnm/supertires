/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.web.wicket.panels.catalogue;

import kz.supershiny.core.model.Tire;
import kz.supershiny.web.wicket.components.BootstrapModalPanel;
import kz.supershiny.web.wicket.panels.DefaultContentPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

/**
 *
 * @author aishmanov
 */
public class ImageViewer extends BootstrapModalPanel {

    private final Tire tire;

    public ImageViewer(String id, String header, Tire tire) {
        super(id, header);
        this.tire = tire;
    }

    @Override
    public Panel getContent(String id) {
        if (tire == null) {
            return new DefaultContentPanel(id);
        }
        return new ImageViewerPanel(id, tire);
    }

    @Override
    public void onCancel(AjaxRequestTarget target) {
    }

    @Override
    public void onSubmit(AjaxRequestTarget target) {
    }
}
