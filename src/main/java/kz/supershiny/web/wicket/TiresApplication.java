/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.supershiny.web.wicket;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import kz.supershiny.core.model.TireType;
import kz.supershiny.core.model.User;
import kz.supershiny.core.services.TireService;
import kz.supershiny.core.services.UserService;
import kz.supershiny.core.util.Constants;
import kz.supershiny.web.wicket.pages.LoginPage;
import kz.supershiny.web.wicket.pages.admin.AdminBasePage;
import kz.supershiny.web.wicket.pages.catalogue.CataloguePage;
import kz.supershiny.web.wicket.pages.error.Error404Page;
import kz.supershiny.web.wicket.pages.error.ErrorPage;
import kz.supershiny.web.wicket.pages.general.BlogPage;
import kz.supershiny.web.wicket.pages.general.ContactsPage;
import kz.supershiny.web.wicket.pages.general.FAQPage;
import kz.supershiny.web.wicket.pages.general.ManufacturerPage;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.protocol.http.servlet.ServletWebResponse;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.settings.IExceptionSettings;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author kilrwhle
 */
public class TiresApplication extends WebApplication {
    
    private static final Logger LOG = LoggerFactory.getLogger(TiresApplication.class);
    private static final List<String> botAgents = Arrays.asList(
        //Only LOWER CASE!
        "googlebot", "msnbot", "slurp", "yandex", "stackrambler", "bingbot"
    );

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
        ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        UserService userService = applicationContext.getBean(UserService.class);
        userService.countSession();
        return new TiresSession(request);
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return CataloguePage.class;
    }
    
    private void mountPages() {
        mount(new TiresPageMapper("/catalog", CataloguePage.class));
        mount(new TiresPageMapper("/admin", AdminBasePage.class));
        mount(new TiresPageMapper("/blog", BlogPage.class));
        mount(new TiresPageMapper("/about", ContactsPage.class));
        mount(new TiresPageMapper("/faq", FAQPage.class));
        mount(new TiresPageMapper("/company", ManufacturerPage.class));
        mount(new TiresPageMapper("/login", LoginPage.class));
        mount(new TiresPageMapper("/error", ErrorPage.class));
        mount(new TiresPageMapper("/404", Error404Page.class));
        getApplicationSettings().setPageExpiredErrorPage(ErrorPage.class);
        getApplicationSettings().setAccessDeniedPage(Error404Page.class);
        getApplicationSettings().setInternalErrorPage(ErrorPage.class);
        getRequestCycleSettings().setTimeout(Duration.seconds(40));  //set request timeout (if backend is slow)
        getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
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
    }
 
    //decide whether the client is a bot
    public static boolean isAgent(final String agent) {
        if(agent != null) {
            LOG.error("User-agent is: " + agent);
            return botAgents.contains(agent.toLowerCase());
        }
        return false;
    }
    
    //Remove jsessionid for bots
    protected WebResponse newWebResponse(final WebRequest webRequest, final HttpServletResponse httpServletResponse){
        return new ServletWebResponse((ServletWebRequest)webRequest, httpServletResponse) {

          @Override
          public String encodeURL(CharSequence url) {
              return isRobot(webRequest) ? url.toString() : super.encodeURL(url);
          }

          @Override
          public String encodeRedirectURL(CharSequence url) {
              return isRobot(webRequest) ? url.toString() : super.encodeRedirectURL(url);
          }

          private boolean isRobot(WebRequest request) {
              final String agent = webRequest.getHeader("User-Agent");
              return isAgent(agent);
          }
      };
    }
}
