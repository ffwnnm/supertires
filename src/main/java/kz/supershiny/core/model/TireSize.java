/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.core.model;

import java.io.Serializable;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author kilrwhle
 */
@Entity
@Table(name = "TIRESIZES")
public class TireSize implements Serializable {
    
    public final static String SIZE_REGEXP = "(\\d+(\\.\\d{0,1})?)/(\\d+(\\.\\d{0,1})?)/(\\d+(\\.\\d{0,1})?)";
    public final static Pattern TIRE_SIZE_PATTERN = Pattern.compile(SIZE_REGEXP);
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "VERBAL_SIZE", unique = true)
    private String sizeVerbal;
    @Column(precision = 1)
    private Float width;
    @Column(precision = 1)
    private Float height;
    @Column(precision = 1)
    private Float radius;

    public TireSize() {
    }

    public TireSize(Float width, Float height, Float radius) {
        convert(null, width, height, radius);
    }

    public TireSize(String sizeVerbal) {
        convert(sizeVerbal, null, null, null);
    }
    
    /**
     * Converts verbal or number representation of size to number or verbal respectively
     * 
     * @param verbal
     * @param width
     * @param height
     * @param radius 
     */
    private void convert(String verbal, Float width, Float height, Float radius) {
        if(verbal != null && !verbal.isEmpty()) {
            this.sizeVerbal = verbal;
            StringTokenizer st = new StringTokenizer(this.sizeVerbal, "/");
            this.width = Float.valueOf(st.nextToken());
            this.height = Float.valueOf(st.nextToken());
            this.radius = Float.valueOf(st.nextToken());
        } else if(width != null && height != null && radius != null) {
            this.width = width;
            this.height = height;
            this.radius = radius;
            this.sizeVerbal = this.width.floatValue() + "/" + this.height.floatValue() + "/" + this.radius.floatValue();
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (this.sizeVerbal != null ? this.sizeVerbal.hashCode() : 0);
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
        final TireSize other = (TireSize) obj;
        if ((this.sizeVerbal == null) ? (other.sizeVerbal != null) : !this.sizeVerbal.equals(other.sizeVerbal)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return sizeVerbal;
    }
    
    public String getWidthAsString() {
        return width == null ? "" : width.toString();
    }
    
    public String getHeightAsString() {
        return height == null ? "" : height.toString();
    }
    
    public String getRadiusAsString() {
        return radius == null ? "" : radius.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSizeVerbal() {
        return sizeVerbal;
    }

    public void setSizeVerbal(String sizeVerbal) {
        this.sizeVerbal = sizeVerbal;
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
}
