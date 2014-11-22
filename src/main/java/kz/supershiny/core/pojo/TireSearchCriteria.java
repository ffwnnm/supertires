/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.core.pojo;

import java.io.Serializable;
import kz.supershiny.core.model.Country;
import kz.supershiny.core.model.Manufacturer;
import kz.supershiny.core.model.TireType;
import kz.supershiny.core.util.Constants;

/**
 *
 * @author kilrwhle
 */
public class TireSearchCriteria extends SearchCriteria implements Serializable {
    
    //size
    private Float width;
    private Float height;
    private Float radius;
    private String size;
    
    private String season;
    private TireType type;
    private Country country;
    private Manufacturer manufacturer;
    private String sorting = Constants.SORT_ARTICLE_DESC;

    public TireSearchCriteria() {
    }
    
    @Override
    public void clear() {
        super.clear();
        init();
    }
    
    private void init() {
        width = null;
        height = null;
        radius = null;
        size = null;
        season = null;
        type = null;
        country = null;
        manufacturer = null;
        sorting = Constants.SORT_ARTICLE_DESC;
    }
    
    public boolean isEmpty() {
        return (sorting == null || sorting.isEmpty())
                &&
                (size == null || size.isEmpty())
                &&
                season == null && type == null
                &&
                manufacturer == null && country == null
                &&
                height == null && width == null && radius == null;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public TireType getType() {
        return type;
    }

    public void setType(TireType type) {
        this.type = type;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSorting() {
        return sorting;
    }

    public void setSorting(String sorting) {
        this.sorting = sorting;
    }
}
