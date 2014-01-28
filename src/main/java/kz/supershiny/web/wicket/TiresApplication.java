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
import kz.supershiny.core.exceptions.TiresPersistException;
import kz.supershiny.core.util.Constants;
import kz.supershiny.web.wicket.pages.admin.AdminPage;
import kz.supershiny.web.wicket.pages.HomePage;
import kz.supershiny.web.wicket.pages.catalogue.ProposalPage;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author kilrwhle
 */
public class TiresApplication extends WebApplication {
    
    private static final Logger LOG = LoggerFactory.getLogger(TiresApplication.class);

    @Override
    protected void init() {
        super.init();
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
        
        createInitialUser();
//        initDictData();
        
        //mount pages
        mountPages();
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
    
    private void mountPages() {
        mountPage("/home", HomePage.class);
        mountPage("/control", AdminPage.class);
        mountPage("/propose", ProposalPage.class);
    }

    private void createInitialUser() {
        User user = new User();
        user.setPhone(Constants.ADMIN_PHONE);
        user.setPassword(Constants.ADMIN_PASS);
        user.setUsername(Constants.ADMIN_UNAME);
        
        ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        UserService us = applicationContext.getBean(UserService.class);
        try {
            us.saveUser(user);
        } catch (TiresPersistException ex) {
            LOG.error("Failed to save initial user from TiresApplication");
        }
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
