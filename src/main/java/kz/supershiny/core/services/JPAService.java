/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.supershiny.core.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;

/**
 *
 * @author kilrwhle
 */
public abstract class JPAService {
    
    @PersistenceContext(unitName = "tires-pu")
    protected EntityManager em;

    public void save(Object object) {
        em.persist(object);
        em.flush();
    }

    public void update(Object object) {
        em.merge(object);
        em.flush();
    }

    public void delete(Object object) {
        em.remove(em.merge(object));
        em.flush();
    }

    public void refresh(Object object) {
        object = em.merge(object);
    }

    public Session getHibernateSession() {
        return em.unwrap(Session.class);
    }
}
