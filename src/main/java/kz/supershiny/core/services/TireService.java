/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.core.services;

import java.util.List;
import kz.supershiny.core.model.Country;
import kz.supershiny.core.model.Manufacturer;
import kz.supershiny.core.model.TireSize;
import kz.supershiny.core.model.TireType;
import org.springframework.stereotype.Service;

/**
 *
 * @author kilrwhle
 */
@Service
public class TireService extends JPAService {
    
    public List<TireType> getTypes() {
        List<TireType> result = null;
        try {
            result = em.createQuery("SELECT DISTINCT TT FROM TireType TT ORDER BY TT.typeName ASC")
                    .getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            result = null;
        }
        return result;
    }
    
    public List<String> getUniqueModels() {
        List<String> result = null;
        try {
            result = em.createQuery("SELECT DISTINCT T.modelName FROM Tire T ORDER BY T.modelName ASC")
                    .getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            result = null;
        }
        return result;
    }
    
    public List<Country> getUniqueCountries() {
        List<Country> result = null;
        try {
            result = em.createQuery("SELECT DISTINCT C FROM Country C ORDER BY C.name ASC")
                    .getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            result = null;
        }
        return result;
    }
    
    public List<Manufacturer> getUniqueManufacturers() {
        List<Manufacturer> result = null;
        try {
            result = em.createQuery("SELECT DISTINCT M FROM Manufacturer M ORDER BY M.companyName ASC")
                    .getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            result = null;
        }
        return result;
    }
    
    public List<TireSize> getUniqueSizes() {
        List<TireSize> result = null;
        try {
            result = em.createQuery("SELECT DISTINCT S FROM TireSize S ORDER BY S.sizeVerbal ASC")
                    .getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            result = null;
        }
        return result;
    }
}
