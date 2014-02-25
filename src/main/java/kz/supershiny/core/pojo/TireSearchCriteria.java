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
public class TireSearchCriteria implements Serializable {
    
    //size
    private Integer width;
    private Integer height;
    private Integer radius;
    private String size;
    
    private String season;
    private TireType type;
    private Country country;
    private Manufacturer manufacturer;
    
    private long currentPage = 0;
    private long itemsTotal = 0;
    private long firstOffset = 0;
    private long itemsPerPage = Constants.ITEMS_PER_PAGE;

    public TireSearchCriteria() {
    }
    
    public boolean isEmpty() {
        return (size == null || size.isEmpty())
                &&
                season == null && type == null
                &&
                manufacturer == null && country == null;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public long getItemsTotal() {
        return itemsTotal;
    }

    public void setItemsTotal(long itemsTotal) {
        this.itemsTotal = itemsTotal;
    }

    public long getFirstOffset() {
        return firstOffset;
    }

    public void setFirstOffset(long firstOffset) {
        this.firstOffset = firstOffset;
    }

    public long getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(long itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
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
}
