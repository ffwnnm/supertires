/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.supershiny.web.wicket.pages.catalogue;

import kz.supershiny.core.model.Tire;
import kz.supershiny.web.wicket.pages.BasePage;

/**
 *
 * @author kilrwhle
 */
public class HomePage extends BasePage {

    public HomePage() {
        this(null);
    }
    
    //Костыль
    public HomePage(Tire tire) {
        super();
    }
}
