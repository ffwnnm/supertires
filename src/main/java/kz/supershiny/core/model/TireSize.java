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
    
    public final static String SIZE_REGEXP = "\\d{1,}/\\d{1,}/\\d{1,}";
    public final static Pattern TIRE_SIZE_PATTERN = Pattern.compile(SIZE_REGEXP);
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "VERBAL_SIZE", unique = true)
    private String sizeVerbal;
    @Column
    private Integer width;
    @Column
    private Integer height;
    @Column
    private Integer radius;

    public TireSize() {
    }

    public TireSize(Integer width, Integer height, Integer radius) {
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
    private void convert(String verbal, Integer width, Integer height, Integer radius) {
        if(verbal != null && !verbal.isEmpty()) {
            this.sizeVerbal = verbal;
            StringTokenizer st = new StringTokenizer(this.sizeVerbal, "/");
            this.width = Integer.valueOf(st.nextToken());
            this.height = Integer.valueOf(st.nextToken());
            this.radius = Integer.valueOf(st.nextToken());
        } else if(width != null && height != null && radius != null) {
            this.width = width;
            this.height = height;
            this.radius = radius;
            this.sizeVerbal = this.width.intValue() + "/" + this.height.intValue() + "/" + this.radius.intValue();
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
}
