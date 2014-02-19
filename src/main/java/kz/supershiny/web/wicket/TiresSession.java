/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.supershiny.web.wicket;

import kz.supershiny.core.model.User;
import kz.supershiny.core.pojo.TireSearchCriteria;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

/**
 *
 * @author kilrwhle
 */
public class TiresSession extends WebSession {
    
    private User user;
    private TireSearchCriteria tireSearchCriteria;

    public TiresSession(Request request) {
        super(request);
        tireSearchCriteria = new TireSearchCriteria();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TireSearchCriteria getTireSearchCriteria() {
        return tireSearchCriteria;
    }

    public void setTireSearchCriteria(TireSearchCriteria tireSearchCriteria) {
        this.tireSearchCriteria = tireSearchCriteria;
    }
}
