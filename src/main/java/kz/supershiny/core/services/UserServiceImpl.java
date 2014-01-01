/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.core.services;

import org.springframework.stereotype.Service;

/**
 *
 * @author kilrwhle
 */
@Service
public class UserServiceImpl extends JPAService implements UserService {

    @Override
    public void testMethod() {
        System.out.println("\t\t\tThis is a service test");
    }
    
}
