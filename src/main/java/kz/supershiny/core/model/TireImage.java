/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.core.model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kz.supershiny.core.util.Constants;
import org.hibernate.annotations.Type;

/**
 * Picture of a tire.
 *
 * @author kilrwhle
 */
@Entity
@Table(name = "images")
public class TireImage implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TIRE_ID")
    private Tire tire;
    @Column
    private String fileName;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "image_body")
    private String encodedImage;
    @Column(name = "is_preview")
    private String preview = Constants.N;

    public TireImage(Tire tire, String fileName, String encodedImage) {
        this.tire = tire;
        this.fileName = fileName;
        this.encodedImage = encodedImage;
        this.preview = Constants.N;
    }

    public TireImage(String fileName, String encodedImage) {
        this.fileName = fileName;
        this.encodedImage = encodedImage;
    }

    public TireImage() {
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.encodedImage != null ? this.encodedImage.hashCode() : 0);
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
        final TireImage other = (TireImage) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tire getTire() {
        return tire;
    }

    public void setTire(Tire tire) {
        this.tire = tire;
    }

    public String getEncodedImage() {
        return encodedImage;
    }

    public void setEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
    }
}
