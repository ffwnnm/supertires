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
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Projections;
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
    public Tire getTireById(Long tireId) {
        if (tireId == null) return null;
        Tire result = null;
        try {
            result = (Tire) em.createQuery("SELECT T FROM Tire T WHERE T.id = :id")
                    .setParameter("id", tireId)
                    .getSingleResult();
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to get Tire by id: " + tireId);
        }
        return result;
    }
    
    
    @Transactional(readOnly = true)
    public long countTiresByCriteria(TireSearchCriteria criteria) {
        Long result;
        if(criteria == null || criteria.isEmpty()) {
            try {
                result = (Long) em.createQuery("SELECT COUNT(T.id) FROM Tire T")
                        .getSingleResult();
            } catch (Exception ex) {
                result = 0L;
                LOG.error("Unable to count all tires!");
            }
        } else {
            String header = "SELECT COUNT(T.id) FROM Tire T";
            String conditions = " WHERE 1=1 ";
            HashMap<String, Object> predicates = new HashMap<String, Object>();
            
            if(criteria.getHeight() != null || criteria.getWidth() != null || criteria.getRadius() != null) {
                header += " JOIN T.size S ";
                if(criteria.getHeight() != null) {
                    conditions += "AND S.height = :height ";
                    predicates.put("height", criteria.getHeight());
                }
                if(criteria.getWidth()!= null) {
                    conditions += "AND S.width = :width ";
                    predicates.put("width", criteria.getWidth());
                }
                if(criteria.getRadius()!= null) {
                    conditions += "AND S.radius = :radius ";
                    predicates.put("radius", criteria.getRadius());
                }
            }
            if(criteria.getSeason() != null && !criteria.getSeason().isEmpty()) {
                conditions += "AND T.season = :season ";
                predicates.put("season", criteria.getSeason());
            }
            if(criteria.getManufacturer() != null) {
                header += " JOIN T.manufacturer M ";
                conditions += "AND M.companyName = :manufacturer ";
                predicates.put("manufacturer", criteria.getManufacturer().getCompanyName());
            }
            
            Query query = em.createQuery(header + conditions);
            for(String key : predicates.keySet()) {
                query.setParameter(key, predicates.get(key));
            }
            
            try {
                result = (Long) query.getSingleResult();
            } catch (Exception ex) {
                result = 0L;
                LOG.error("Unable to count tires by criteria!");
            }
        }
        return result;
    }
    
    @Transactional(readOnly = true)
    public List<Tire> getTiresByCriteria(TireSearchCriteria criteria) {
        if (criteria == null) return null;
        
        List<Tire> result = null;
        
        criteria.setTotal(countTiresByCriteria(criteria));  //count results
        
        if (criteria.isEmpty()) {
            try {
                result = em.createQuery("SELECT T FROM Tire T ORDER BY T.id DESC")
                        .setMaxResults((int) criteria.getPageSize())
                        .setFirstResult((int) criteria.getBegin())
                        .getResultList();
            } catch (Exception ex) {
                result = null;
                LOG.error("Unable to load all tires!");
            }
        } else {
            String header = "SELECT T FROM Tire T";
            String footer = " ORDER BY T.id DESC";  //sorting by default, ARTICLE_DESC
            String conditions = " WHERE 1=1 ";
            HashMap<String, Object> predicates = new HashMap<String, Object>();
            
            if(criteria.getHeight() != null || criteria.getWidth() != null || criteria.getRadius() != null) {
                if(criteria.getHeight() != null) {
                    conditions += " AND T.size.height = :height ";
                    predicates.put("height", criteria.getHeight());
                }
                if(criteria.getWidth()!= null) {
                    conditions += " AND T.size.width = :width ";
                    predicates.put("width", criteria.getWidth());
                }
                if(criteria.getRadius()!= null) {
                    conditions += " AND T.size.radius = :radius ";
                    predicates.put("radius", criteria.getRadius());
                }
            }
            if(criteria.getSeason() != null && !criteria.getSeason().isEmpty()) {
                conditions += "AND T.season = :season ";
                predicates.put("season", criteria.getSeason());
            }
            if(criteria.getManufacturer() != null) {
                header += " JOIN T.manufacturer M ";
                conditions += "AND M.companyName = :manufacturer ";
                predicates.put("manufacturer", criteria.getManufacturer().getCompanyName());
            }
            //sorting
            if(criteria.getSorting() != null && !Constants.SORT_ARTICLE_DESC.equals(criteria.getSorting())) {
                switch (criteria.getSorting()) {
                    case Constants.SORT_PRICE_ASC: footer = " ORDER BY T.price ASC";
                        break;
                    case Constants.SORT_PRICE_DESC: footer = " ORDER BY T.price DESC";
                        break;
                    case Constants.SORT_RADIUS_ASC: footer = " ORDER BY T.size.radius ASC";
                        break;
                    case Constants.SORT_RADIUS_DESC: footer = " ORDER BY T.size.radius DESC";
                        break;
                    default: footer = " ORDER BY T.id DESC";
                        break;
                }
            }
            
            Query query = em.createQuery(header + conditions + footer);
            for(String key : predicates.keySet()) {
                query.setParameter(key, predicates.get(key));
            }
            
            try {
                result = query
                        .setMaxResults((int) criteria.getPageSize())
                        .setFirstResult((int) criteria.getBegin())
                        .getResultList();
            } catch (Exception ex) {
                result = null;
                LOG.error("Unable to get tires by criteria!");
            }
        }
        
        criteria.setResultSize(result == null ? 0L : result.size());
        
        return result;
    }
    
    @Transactional(readOnly = true)
    public Tire getTireWithImages(Long id) {
        if (id == null) return null;
        Tire result;
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
    public List<TireImage> getImagesForTire(Tire tire, ImageService.ImageSize size) {
        List<TireImage> result = null;
        try {
            result = em.createQuery("SELECT i FROM TireImage i WHERE i.tire = :tire AND i.imageSize = :size ORDER BY i.id ASC")
                    .setParameter("tire", tire)
                    .setParameter("size", size)
                    .getResultList();
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to load images for tire: " + tire, ex);
        }
        return result;
    }
    
    /**
     * Returns 1st SMALL image with flag preview == true
     * 
     * @param tire
     * @return 
     */
    @Transactional(readOnly = true)
    public TireImage getPreviewForTire(Tire tire) {
        TireImage result = null;
        if(tire == null) return result;
        
        try {
            List results = em.createQuery(
                    "SELECT i FROM TireImage i WHERE i.tire = :tire AND i.imageSize = :size AND i.isPreview IS TRUE ORDER BY i.id DESC"
                    )
                    .setParameter("tire", tire)
                    .setParameter("size", ImageService.ImageSize.SMALL)
                    .setMaxResults(1).getResultList();
            result = (TireImage) results.get(0);
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to get preview by filename for tire: " + tire);
        }
        return result;
    }
    
    @Transactional(readOnly = true)
    public List<Float> getUniqueWidth() {
        List<Float> result = null;
        try {
            result = em.createQuery("SELECT DISTINCT S.width FROM TireSize S ORDER BY S.width ASC")
                    .getResultList();
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to load unique width!");
        }
        return result;
    }
    
    @Transactional(readOnly = true)
    public List<Float> getUniqueRadius() {
        List<Float> result = null;
        try {
            result = em.createQuery("SELECT DISTINCT S.radius FROM TireSize S ORDER BY S.radius ASC")
                    .getResultList();
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to load unique radius!");
        }
        return result;
    }
    
    @Transactional(readOnly = true)
    public List<Float> getUniqueHeight() {
        List<Float> result = null;
        try {
            result = em.createQuery("SELECT DISTINCT S.height FROM TireSize S ORDER BY S.height ASC")
                    .getResultList();
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to load unique height!");
        }
        return result;
    }
    
    /**
     * Return true if we can delete manufacturer (if no Tire is connected to it)
     * 
     * @param m
     * @return 
     */
    @Transactional(readOnly = true)
    public boolean checkManufacturerToDelete(Manufacturer m) {
        if (m == null || m.getId() == null) {
            return true;
        }
        long result = -1L;
        try {
            result = (Long) em.createQuery("SELECT COUNT(t.id) FROM Tire t WHERE t.manufacturer = :m")
                    .setParameter("m", m)
                    .getSingleResult();
            return result == 0;
        } catch (Exception ex) {
            LOG.error("Unable to count tires of manufacturer: " + m.getCompanyName());
        }
        return false;
    }
    
    /**
     * Temporary method for creating previews for existing images
     */
//    @Transactional(readOnly = false)
//    public void createPreviews() {
//        List<TireImage> images = em.createQuery("SELECT img FROM TireImage img").getResultList();
//        if (images != null) {
//            String filename;
//            byte[] body;
//            for (TireImage item : images) {
//                System.out.println(item.getFileName());
//                
//                filename = ImageService.getPhotoFilename(item.getTire().getId(), item.getFileName(), ImageService.ImageSize.LARGE);
//                body = ImageService.resizeImage(item.getImageBody(), ImageService.ImageSize.LARGE);
//                TireImage newItem1 = new TireImage(item.getTire(), filename, ImageService.ImageSize.LARGE, body);
//                filename = ImageService.getPhotoFilename(item.getTire().getId(), item.getFileName(), ImageService.ImageSize.SMALL);
//                body = ImageService.resizeImage(item.getImageBody(), ImageService.ImageSize.SMALL);
//                TireImage newItem2 = new TireImage(item.getTire(), filename, ImageService.ImageSize.SMALL, body);
//                
//                newItem1.setIsPreview(item.getIsPreview());
//                newItem2.setIsPreview(item.getIsPreview());
//                
//                save(newItem1);
//                save(newItem2);
//            }
//        }
//    }
}
