/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.supershiny.web.wicket;

import kz.supershiny.core.model.Country;
import kz.supershiny.core.model.Manufacturer;
import kz.supershiny.core.model.TireSize;
import kz.supershiny.core.model.TireType;
import kz.supershiny.core.model.User;
import kz.supershiny.core.services.TireService;
import kz.supershiny.core.services.UserService;
import kz.supershiny.web.wicket.pages.HomePage;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author kilrwhle
 */
public class TiresApplication extends WebApplication {

    @Override
    protected void init() {
        super.init();
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        
        createInitialUser();
        initDictData();
        
        //mount pages
        mountPage("/home", HomePage.class);
    }
    
    public TiresSession getTiresSession() {
        return (TiresSession) TiresSession.get();
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new TiresSession(request);
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }

    private void createInitialUser() {
        User user = new User();
        user.setPhone("7774137482");
        user.setPassword("qwpo4294");
        user.setUsername("admin");
        
        User user1 = new User();
        user1.setPhone("123");
        user1.setPassword("qwe");
        user1.setUsername("admin");
        
        ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        UserService us = applicationContext.getBean(UserService.class);
        us.save(user);
        us.save(user1);
    }
    
    /**
     * For debug purposes
     */
    private void initDictData() {
        ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        TireService ts = applicationContext.getBean(TireService.class);
        
        ts.save(new TireType("Фрикционная"));
        ts.save(new TireType("Под шипы"));
        ts.save(new TireType("Шипованная"));
        
        ts.save(new Manufacturer("Yokohama"));
        ts.save(new Manufacturer("Hankook"));
        ts.save(new Manufacturer("Maxxis"));
        ts.save(new Manufacturer("Toyo"));
        
        ts.save(new TireSize("195/65/15"));
        ts.save(new TireSize("205/65/15"));
        ts.save(new TireSize("185/65/15"));
        
        ts.save(new Country("Корея"));
        ts.save(new Country("Китай"));
        ts.save(new Country("Филиппины"));
        ts.save(new Country("Япония"));
        ts.save(new Country("Германия"));
    }
}
