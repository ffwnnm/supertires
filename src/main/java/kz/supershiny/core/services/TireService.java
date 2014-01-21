/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.core.services;

import java.util.List;
import kz.supershiny.core.model.Country;
import kz.supershiny.core.model.Manufacturer;
import kz.supershiny.core.model.Tire;
import kz.supershiny.core.model.TireImage;
import kz.supershiny.core.model.TireSize;
import kz.supershiny.core.model.TireType;
import kz.supershiny.core.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author kilrwhle
 */
@Service
public class TireService extends JPAService {
    
    private static final Logger LOG = LoggerFactory.getLogger(TireService.class);
    
    public List<TireType> getTypes() {
        List<TireType> result = null;
        try {
            result = em.createQuery("SELECT DISTINCT TT FROM TireType TT ORDER BY TT.typeName ASC")
                    .getResultList();
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to load tire types!", ex);
        }
        return result;
    }
    
    public List<String> getUniqueModels() {
        List<String> result = null;
        try {
            result = em.createQuery("SELECT DISTINCT T.modelName FROM Tire T ORDER BY T.modelName ASC")
                    .getResultList();
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to load unique models!", ex);
        }
        return result;
    }
    
    public List<Country> getUniqueCountries() {
        List<Country> result = null;
        try {
            result = em.createQuery("SELECT DISTINCT C FROM Country C ORDER BY C.name ASC")
                    .getResultList();
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to load unique countries!", ex);
        }
        return result;
    }
    
    public List<Manufacturer> getUniqueManufacturers() {
        List<Manufacturer> result = null;
        try {
            result = em.createQuery("SELECT DISTINCT M FROM Manufacturer M ORDER BY M.companyName ASC")
                    .getResultList();
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to load unique manufacturers!", ex);
        }
        return result;
    }
    
    public List<TireSize> getUniqueSizes() {
        List<TireSize> result = null;
        try {
            result = em.createQuery("SELECT DISTINCT S FROM TireSize S ORDER BY S.sizeVerbal ASC")
                    .getResultList();
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to load unique sizes!", ex);
        }
        return result;
    }
    
    public List<TireImage> getImagesForTire(Tire tire) {
        List<TireImage> result = null;
        try {
            result = em.createQuery("SELECT i FROM TireImage i WHERE i.tire = :tire")
                    .setParameter("tire", tire)
                    .getResultList();
        } catch (Exception ex) {
            result = null;
            LOG.error("Unable to load images for tire: " + tire, ex);
        }
        return result;
    }
    
    public TireImage getPreviewForTire(Tire tire) {
        TireImage result = null;
        String filename = null;
        
        if(tire == null) return null;
        
        try {
            result = (TireImage) em.createQuery(
                    "SELECT i FROM TireImage i WHERE i.tire = :tire AND i.preview = :preview"
                    )
                    .setParameter("tire", tire)
                    .setParameter("preview", Constants.Y)
                    .getResultList();
        } catch (Exception ex) {
            filename = null;
            LOG.error("Unable to get preview by filename for tire: " + tire, ex);
        }
        return result;
    }
    
    public void test() {
        String ss = (String) em.createNativeQuery("select image_body from images where id = 18;")
                .getSingleResult();
        System.out.println("///////////////////////////////////////////////");
        System.out.println(ss);
        System.out.println("///////////////////////////////////////////////");
    }
}
