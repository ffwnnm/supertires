/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.supershiny.core.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kilrwhle
 */
public abstract class JPAService {
    
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(Object object) {
        try {
            em.persist(object);
            em.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Transactional
    public void update(Object object) {
        try {
            em.merge(object);
            em.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Transactional
    public void delete(Object object) {
        try {
            em.remove(em.merge(object));
            em.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void refresh(Object object) {
        try {
            object = em.merge(object);
            em.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Session getHibernateSession() {
        return em.unwrap(Session.class);
    }
}
