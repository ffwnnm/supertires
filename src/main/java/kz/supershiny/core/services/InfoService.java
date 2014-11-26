/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.core.services;

import java.math.BigInteger;
import java.util.List;
import kz.supershiny.core.model.BlogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kilrwhle
 */
@Service
public class InfoService extends JPAService {
    
    private static final Logger LOG = LoggerFactory.getLogger(InfoService.class);
    
    @Transactional(readOnly = true)
    public List<BlogEntry> getAllEntries() {
        List<BlogEntry> result;
        try {
            result = (List<BlogEntry>) em.createQuery("SELECT b FROM BlogEntry b ORDER BY b.date DESC")
                    .getResultList();
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to load all blog entries!");
        }
        return result;
    }

    @Transactional(readOnly = true)
    public Long getSessionsCount() {
        Long result = 0L;
        try {
            result = ((BigInteger) em.createNativeQuery("SELECT last_value FROM sessions").getSingleResult())
                    .longValue();
        } catch (Exception ex) {
            result = 0L;
            LOG.error("Unable to get sessions count!");
        }
        return result;
    }
}
