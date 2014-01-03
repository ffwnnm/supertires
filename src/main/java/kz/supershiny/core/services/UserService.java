/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.core.services;

import kz.supershiny.core.model.User;
import org.springframework.stereotype.Service;

/**
 *
 * @author kilrwhle
 */
@Service
public class UserService extends JPAService {

    public void testMethod() {
        System.out.println("\t\t\tThis is a service test");
    }
    
    public User authenticate(String phone, String password) {
        User user = null;
        try {
            user = (User) em.createQuery("SELECT u FROM User u WHERE u.phone = :phone AND u.password = :password")
                .setParameter("phone", phone)
                .setParameter("password", password)
                .getSingleResult();
        } catch (Exception ex) {
            user = null;
            ex.printStackTrace();
        }
        return user;
    }
}
