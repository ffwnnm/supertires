/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kz.supershiny.core.services;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;
import kz.supershiny.core.model.TireImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author kilrwhle
 */
@Service
public class ImageService extends JPAService {
    
    private static final Logger LOG = LoggerFactory.getLogger(ImageService.class);
    
    public static enum ImageSize {ORIGINAL, LARGE, SMALL};
    
    //photo
    public static final int LARGE_WIDTH = 600;
    public static final int LARGE_HEIGHT = 600;
    //thumb
    public static final int SMALL_WIDTH = 170;
    public static final int SMALL_HEIGHT = 170;
    
    /**
     * Creates a filename for a photo of a good.
     * Syntax: goodId-imagesize-filename
     * 
     * @param goodId
     * @param originalFilename
     * @param type original - 0, large - 1, small - 2
     * @return 
     */
    public static String getPhotoFilename(Long goodId, String originalFilename, ImageSize type) {
        if (goodId == null) return null;
        String prefix = "";
        switch (type) {
            case LARGE:
                prefix = ImageSize.LARGE.name();
                break;
            case SMALL:
                prefix = ImageSize.SMALL.name();
                break;
            default:
                prefix = ImageSize.ORIGINAL.name();
                break;
        }
        if (originalFilename == null || originalFilename.isEmpty()) {
            originalFilename = "photo";
        }
        return goodId.toString() + "-" + prefix + "-" + originalFilename;
    }
    
    /**
     * Creates thumb for image.
     *
     * @param originalData original image in byte array
     * @param type original - 0, large - 1, small - 2
     * @return resized image in byte array
     */
    public static byte[] resizeImage(byte[] originalData, ImageSize type) {
        //if original flag, then return original
        if (type.equals(ImageSize.ORIGINAL)) {
            return originalData;
        }

        BufferedImage originalImage = null;
        BufferedImage resizedImage = null;
        byte[] result = null;
        //convert bytes to BufferedImage
        try (InputStream in = new ByteArrayInputStream(originalData)) {
            originalImage = ImageIO.read(in);
        } catch (IOException ex) {
            LOG.error("Cannot convert byte array to BufferedImage!", ex);
            return null;
        }
        //get original size
        int scaledHeight = originalImage.getHeight();
        int scaledWidth = originalImage.getWidth();

        switch (type) {
            case LARGE:
                scaledWidth = LARGE_WIDTH;
                scaledHeight = LARGE_HEIGHT;
                break;
            case SMALL:
                scaledWidth = SMALL_WIDTH;
                scaledHeight = SMALL_HEIGHT;
                break;
            default:
                break;
        }
        //calculate aspect ratio
        float ratio = 1.0F * originalImage.getWidth() / originalImage.getHeight();
        if (ratio > 1.0F) {
            scaledHeight = (int) (scaledHeight / ratio);
        } else {
            scaledWidth = (int) (scaledWidth * ratio);
        }
        //resize with hints
        resizedImage = new BufferedImage(
                scaledWidth,
                scaledHeight,
                originalImage.getType() == 0
                ? BufferedImage.TYPE_INT_ARGB
                : originalImage.getType()
        );

        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //convert BufferedImage to bytes
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(resizedImage, "png", baos);
            baos.flush();
            result = baos.toByteArray();
        } catch (IOException ex) {
            LOG.error("Cannot convert BufferedImage to byte array!", ex);
        }
        return result;
    }
    
}
