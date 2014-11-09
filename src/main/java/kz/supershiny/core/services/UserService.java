/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.core.services;

import java.util.List;
import kz.supershiny.core.exceptions.TiresAuthenticationException;
import kz.supershiny.core.exceptions.TiresPersistException;
import kz.supershiny.core.model.User;
import kz.supershiny.core.util.Crypter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kilrwhle
 */
@Service
public class UserService extends JPAService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Transactional(readOnly = false)
    public void saveUser(User user) throws TiresPersistException {
        try {
            //turn username in base64
//            String rawName = user.getUsername();
//            user.setUsername(Base64Coder.encodeString(rawName));
            save(user);
        } catch (Exception ex) {
            LOG.error("Unable to save User: " + user);
            throw new TiresPersistException();
        }
    }

    public User authenticate(String phone, String password) throws TiresAuthenticationException {
        User user = null;
        if (phone != null && !phone.isEmpty()) {
            try {
                Integer hash = Integer.valueOf(User.hashLogin(phone));
                user = (User) em.createQuery("SELECT u FROM User u WHERE u.loginHash = :hash")
                        .setParameter("hash", hash)
                        .getSingleResult();
            } catch (Exception ex) {
                user = null;
                LOG.error("Authentication error: Unable to get User by phone: " + phone);
            }
        }
        if (user != null) {
            if (Crypter.checkString(password, user.getPassword())) {
//                user.setUsername(Base64Coder.decodeString(user.getUsername()));
                return user;
            } else {
                user = null;
                LOG.error("Authentication error: Invalid password: " + phone + ", pass= " + password);
            }
        }
        if (user == null) {
            throw new TiresAuthenticationException();
        }
        return user;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        List<User> users = null;
        try {
            users = (List<User>) em.createQuery("SELECT u FROM User u")
                    .getResultList();
        } catch (Exception ex) {
            users = null;
            LOG.error("Unable to get all Users!");
        }
        return users;
    }

    @Transactional(readOnly = false)
    public void countSession() {
        try {
            em.createNativeQuery("select nextval('sessions')").getSingleResult();
            em.flush();
        } catch (Exception ex) {
            LOG.error("Session counter failed!");
        }
    }
}
