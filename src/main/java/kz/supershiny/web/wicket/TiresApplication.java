/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.supershiny.web.wicket;

import java.util.Arrays;
import java.util.List;
import kz.supershiny.core.model.TireType;
import kz.supershiny.core.model.User;
import kz.supershiny.core.services.TireService;
import kz.supershiny.core.services.UserService;
import kz.supershiny.core.util.Constants;
import kz.supershiny.web.wicket.pages.HomePage;
import kz.supershiny.web.wicket.pages.admin.BlogEntryPage;
import kz.supershiny.web.wicket.pages.admin.CatalogEditorPage;
import kz.supershiny.web.wicket.pages.admin.ManufacturersEditorPage;
import kz.supershiny.web.wicket.pages.catalogue.CataloguePage;
import kz.supershiny.web.wicket.pages.catalogue.ProposalPage;
import kz.supershiny.web.wicket.pages.general.ContactsPage;
import kz.supershiny.web.wicket.pages.general.ManufacturerPage;
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
        //data initializers
        createInitialUser();
        initDictData();
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
        mountPage("/admin-catalog", CatalogEditorPage.class);
        mountPage("/admin-blog", BlogEntryPage.class);
        mountPage("/admin-company", ManufacturersEditorPage.class);
        mountPage("/catalogue", CataloguePage.class);
        mountPage("/contacts", ContactsPage.class);
        mountPage("/propose", ProposalPage.class);
        mountPage("/company", ManufacturerPage.class);
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
        } catch (Exception ex) {
            LOG.error("Failed to save initial user from TiresApplication");
        }
    }
    
    /**
     * Init data in required distionaries
     */
    private void initDictData() {
        ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        TireService ts = applicationContext.getBean(TireService.class);
        
        List<TireType> types = Arrays.asList(
                new TireType("Фрикционная"), 
                new TireType("Под шипы"), 
                new TireType("Шипованная"),
                new TireType("Симметричная"),
                new TireType("Асимметричная")
                );
        
        for(TireType t : types) {
            try {
                ts.save(t);
            } catch (Exception ex) {
                LOG.error("Failed to save tire type: " + t.getTypeName());
            }
        }
        
//        ts.save(new Manufacturer("Yokohama"));
//        ts.save(new Manufacturer("Hankook"));
//        ts.save(new Manufacturer("Maxxis"));
//        ts.save(new Manufacturer("Toyo"));
//        
//        ts.save(new TireSize("195/65/15"));
//        ts.save(new TireSize("205/65/15"));
//        ts.save(new TireSize("185/65/15"));
//        
//        ts.save(new Country("Корея"));
//        ts.save(new Country("Китай"));
//        ts.save(new Country("Филиппины"));
//        ts.save(new Country("Япония"));
//        ts.save(new Country("Германия"));
        
        ts = null;
    }
}
