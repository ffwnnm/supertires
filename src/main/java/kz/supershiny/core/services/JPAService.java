/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.supershiny.core.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kilrwhle
 */
public abstract class JPAService {
    
    @PersistenceContext
    protected EntityManager em;
    
    private static final Logger LOG = LoggerFactory.getLogger(JPAService.class);

    @Transactional
    public void save(Object object) {
        try {
            em.persist(object);
            em.flush();
        } catch (Exception ex) {
            LOG.error("Failed to save object: " + object.toString());
        }
    }

    @Transactional
    public void update(Object object) {
        try {
            em.merge(object);
            em.flush();
        } catch (Exception ex) {
            LOG.error("Failed to update object: " + object.toString());
        }
    }

    @Transactional
    public void delete(Object object) {
        try {
            object = em.merge(object);  //reattach the entity first
            em.remove(object);
            em.flush();
        } catch (Exception ex) {
            LOG.error("Failed to delete object: " + object.toString());
        }
    }

    @Transactional
    public void refresh(Object object) {
        try {
            object = em.merge(object);
            em.flush();
        } catch (Exception ex) {
            LOG.error("Failed to refresh object: " + object.toString());
        }
    }

    public Session getHibernateSession() {
        return em.unwrap(Session.class);
    }
}
