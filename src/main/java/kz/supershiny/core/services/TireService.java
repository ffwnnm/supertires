/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.core.services;

import java.util.HashMap;
import java.util.List;
import javax.persistence.Query;
import kz.supershiny.core.model.Country;
import kz.supershiny.core.model.Manufacturer;
import kz.supershiny.core.model.Tire;
import kz.supershiny.core.model.TireImage;
import kz.supershiny.core.model.TireSize;
import kz.supershiny.core.model.TireType;
import kz.supershiny.core.pojo.TireSearchCriteria;
import kz.supershiny.core.util.Constants;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kilrwhle
 */
@Service
public class TireService extends JPAService {
    
    private static final Logger LOG = LoggerFactory.getLogger(TireService.class);
    
    @Transactional(readOnly = true)
    public List<Tire> getAllTires() {
        List<Tire> result = null;
        try {
            result = em.createQuery("SELECT T FROM Tire T ORDER BY T.id DESC")
                    .getResultList();
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to load all tires!");
        }
        return result;
    }
    
    @Transactional(readOnly = true)
    public List<Tire> getTiresByCriteria(TireSearchCriteria criteria) {
        List<Tire> result = null;
        if(criteria == null || criteria.isEmpty()) {
            try {
                result = em.createQuery("SELECT T FROM Tire T ORDER BY T.id DESC")
                        .getResultList();
            } catch (Exception ex) {
                result = null;
                LOG.error("Unable to load all tires!");
            }
        } else {    
            String header = "SELECT T FROM Tire T";
            String footer = "ORDER BY T.id DESC";
            String conditions = "WHERE 1=1 ";
            HashMap<String, Object> predicates = new HashMap<String, Object>();
            
            if(criteria.getSize() != null && !criteria.getSize().isEmpty()) {
                header += " JOIN T.size S ";
                conditions += "AND S.sizeVerbal = :size ";
                predicates.put("size", criteria.getSize());
            }
            if(criteria.getSeason() != null && !criteria.getSeason().isEmpty()) {
                conditions += "AND T.season = :season ";
                predicates.put("season", criteria.getSeason());
            }
//            if(criteria.getType() != null) {
//                header += " JOIN T.type TP ";
//                conditions += "AND TP.typeName = :type ";
//                predicates.put("type", criteria.getType().getTypeName());
//            }
            if(criteria.getManufacturer() != null) {
                header += " JOIN T.manufacturer M ";
                conditions += "AND M.companyName = :manufacturer ";
                predicates.put("manufacturer", criteria.getManufacturer().getCompanyName());
            }
//            if(criteria.getCountry() != null) {
//                header += " JOIN T.country C ";
//                conditions += "AND C.name = :country ";
//                predicates.put("country", criteria.getCountry().getName());
//            }
            
            Query query = em.createQuery(header + conditions + footer);
            for(String key : predicates.keySet()) {
                query.setParameter(key, predicates.get(key));
            }
            
            try {
                result = query.getResultList();
            } catch (Exception ex) {
                result = null;
                LOG.error("Unable to get tires by criteria!");
            }
        }
        return result;
    }
    
    @Transactional(readOnly = true)
    public Tire getTireWithImages(Long id) {
        Tire result = null;
        try {
            result = (Tire) em.createQuery("SELECT T FROM Tire T WHERE T.id = :id")
                    .setParameter("id", id)
                    .getSingleResult();
            Hibernate.initialize(result.getImages());
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to load tire with images!");
        }
        return result;
    }
    
    @Transactional(readOnly = false)
    public void removeImage(TireImage image) {
        if(image == null) return;
        Tire tire = image.getTire();
        tire.removeImage(image);
        try {
            super.delete(image);
            super.refresh(tire);
        } catch (Exception ex) {
            LOG.error("Unable to delete image!");
        }
    }
    
    @Transactional(readOnly = true)
    public List<TireType> getTypes() {
        List<TireType> result = null;
        try {
            result = em.createQuery("SELECT DISTINCT TT FROM TireType TT ORDER BY TT.typeName ASC")
                    .getResultList();
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to load tire types!");
        }
        return result;
    }
    
    @Transactional(readOnly = true)
    public List<String> getUniqueModels() {
        List<String> result = null;
        try {
            result = em.createQuery("SELECT DISTINCT T.modelName FROM Tire T ORDER BY T.modelName ASC")
                    .getResultList();
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to load unique models!");
        }
        return result;
    }
    
    @Transactional(readOnly = true)
    public List<Country> getUniqueCountries() {
        List<Country> result = null;
        try {
            result = em.createQuery("SELECT DISTINCT C FROM Country C ORDER BY C.name ASC")
                    .getResultList();
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to load unique countries!");
        }
        return result;
    }
    
    @Transactional(readOnly = true)
    public List<Manufacturer> getUniqueManufacturers() {
        List<Manufacturer> result = null;
        try {
            result = em.createQuery("SELECT DISTINCT M FROM Manufacturer M ORDER BY M.companyName ASC")
                    .getResultList();
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to load unique manufacturers!");
        }
        return result;
    }
    
    @Transactional(readOnly = true)
    public List<TireSize> getUniqueSizes() {
        List<TireSize> result = null;
        try {
            result = em.createQuery("SELECT DISTINCT S FROM TireSize S ORDER BY S.sizeVerbal ASC")
                    .getResultList();
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to load unique sizes!");
        }
        return result;
    }
    
    @Transactional(readOnly = true)
    public List<String> getUniqueVerbalSizes() {
        List<String> result = null;
        try {
            result = em.createQuery("SELECT DISTINCT S.sizeVerbal FROM TireSize S ORDER BY S.sizeVerbal ASC")
                    .getResultList();
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to load unique verbal sizes!");
        }
        return result;
    }
    
    @Transactional(readOnly = true)
    public List<TireImage> getImagesForTire(Tire tire) {
        List<TireImage> result = null;
        try {
            result = em.createQuery("SELECT i FROM TireImage i WHERE i.tire = :tire ORDER BY i.id ASC")
                    .setParameter("tire", tire)
                    .getResultList();
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to load images for tire: " + tire, ex);
        }
        return result;
    }
    
    @Transactional(readOnly = true)
    public TireImage getPreviewForTire(Tire tire) {
        TireImage result = null;
        if(tire == null) return result;
        
        try {
            List results = em.createQuery(
                    "SELECT i FROM TireImage i WHERE i.tire = :tire AND i.preview = :preview ORDER BY i.id DESC"
                    )
                    .setParameter("tire", tire)
                    .setParameter("preview", Constants.Y)
                    .setMaxResults(1).getResultList();
            result = (TireImage) results.get(0);
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to get preview by filename for tire: " + tire);
        }
        return result;
    }
}
