/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author kilrwhle
 */
@Entity
@Table(name = "TIRES")
public class Tire implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "MODEL_NAME")
    private String modelName;
    @Column
    private String season;
    @Column
    private BigDecimal price;
    @Column
    private Long quantity;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SIZE_ID")
    private TireSize size;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE_ID")
    private TireType type;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COUNTRY_ID")
    private Country country;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MANUFACTURER_ID")
    private Manufacturer manufacturer;

    public Tire() {
        this.quantity = 0L;
        this.price = BigDecimal.ZERO;
    }

    public Tire(TireSize size, String modelName, String season, BigDecimal price, Long quantity, TireType type, Country country, Manufacturer manufacturer) {
        this.size = size;
        this.modelName = modelName;
        this.season = season;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
        this.country = country;
        this.manufacturer = manufacturer;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.modelName != null ? this.modelName.hashCode() : 0);
        hash = 89 * hash + (this.season != null ? this.season.hashCode() : 0);
        hash = 89 * hash + (this.size != null ? this.size.hashCode() : 0);
        hash = 89 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 89 * hash + (this.country != null ? this.country.hashCode() : 0);
        hash = 89 * hash + (this.manufacturer != null ? this.manufacturer.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tire other = (Tire) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tire{" + "modelName=" + modelName + ", season=" + season + ", size=" + size + ", type=" + type + ", country=" + country + ", manufacturer=" + manufacturer + '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public TireSize getSize() {
        return size;
    }

    public void setSize(TireSize size) {
        this.size = size;
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
