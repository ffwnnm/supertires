/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.core.util;

import kz.supershiny.core.services.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author kilrwhle
 */
public class ServiceLocator {
    
    private ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    private UserService userService;

    public ServiceLocator() {
        
    }
    
}
