/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.core.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kz.supershiny.core.services.ImageService.ImageSize;
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
    
    @Column(name = "filename")
    private String fileName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "image_size")
    private ImageSize imageSize;
    
    @Lob
    @Column(name = "image_body")
    private byte[] imageBody;
    
    @Column(name = "preview")
    @Type(type = "yes_no")
    private Boolean isPreview = Boolean.FALSE;

    public TireImage(Tire tire, String fileName, ImageSize imageSize, byte[] imageBody) {
        this.tire = tire;
        this.fileName = fileName;
        this.imageSize = imageSize;
        this.imageBody = imageBody;
    }

    public TireImage() {
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 59 * hash + (this.fileName != null ? this.fileName.hashCode() : 0);
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
        if (this.tire != other.tire && (this.tire == null || !this.tire.equals(other.tire))) {
            return false;
        }
        if ((this.fileName == null) ? (other.fileName != null) : !this.fileName.equals(other.fileName)) {
            return false;
        }
        return true;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ImageSize getImageSize() {
        return imageSize;
    }

    public void setImageSize(ImageSize imageSize) {
        this.imageSize = imageSize;
    }

    public byte[] getImageBody() {
        return imageBody;
    }

    public void setImageBody(byte[] imageBody) {
        this.imageBody = imageBody;
    }

    public Boolean getIsPreview() {
        return isPreview;
    }

    public void setIsPreview(Boolean isPreview) {
        this.isPreview = isPreview;
    }
}
