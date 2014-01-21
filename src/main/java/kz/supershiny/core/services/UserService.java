/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.core.services;

import javax.annotation.Resource;
import kz.supershiny.core.model.User;
import kz.supershiny.core.util.Crypter;
import kz.supershiny.core.exceptions.TiresAuthenticationException;
import kz.supershiny.core.exceptions.TiresPersistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author kilrwhle
 */
@Service
public class UserService extends JPAService {

    @Resource
    JPAService jpa;
    
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public void testMethod() {
        System.out.println("\t\t\tThis is a service test");
    }

    public void saveUser(User user) throws TiresPersistException {
        try {
            //turn plain password into hash
            String rawPassword = user.getPassword();
            user.setPassword(Crypter.encryptString(rawPassword));
            jpa.save(user);
        } catch (Exception ex) {
            LOG.error("Unable to save User: " + user, ex);
            throw new TiresPersistException();
        }
    }
    
    public User authenticate(String phone, String password) throws TiresAuthenticationException {
        User user = null;
        try {
            user = (User) em.createQuery("SELECT u FROM User u WHERE u.phone = :phone")
                .setParameter("phone", phone)
                .getSingleResult();
        } catch (Exception ex) {
            user = null;
            LOG.error("Authentication error: Unable to get User by phone: " + phone, ex);
        }
        if(user != null) {
            if(Crypter.checkString(password, user.getPassword())) {
                return user;
            } else {
                user = null;
                LOG.error("Authentication error: Invalid password: " + phone + ", pass= " + password);
            }
        }
        if(user == null) {
            throw new TiresAuthenticationException();
        }
        return user;
    }
}
