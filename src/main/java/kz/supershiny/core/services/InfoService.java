/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.core.services;

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
            result = (List<BlogEntry>) em.createQuery("SELECT b FROM BlogEntry b")
                    .getResultList();
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to load all blog entries!");
        }
        return result;
    }
}
