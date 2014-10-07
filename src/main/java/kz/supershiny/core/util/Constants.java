/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.core.util;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author kilrwhle
 */
public class Constants {
    //Initial user data
    public static final String ADMIN_PHONE = "77011244791";
    public static final String ADMIN_PASS = "CatchMe1fUCan";
    public static final String ADMIN_UNAME = "Администратор";
    
    public static final String WINTER = "WINTER";
    public static final String SUMMER = "SUMMER";
    public static final String ALL_SEASONS = "ALL_SEASONS";
    
    public static final List<String> seasons = Arrays.asList(SUMMER, WINTER, ALL_SEASONS);
    
    public static final String IMAGE_PREVIEW_PREFIX = "main";
    public static final int MAX_IMAGES_UPLOAD_SIZE_KB = 2048;
    public static final int MAX_IMAGES_PER_ITEM = 5;
    public static final int ITEMS_PER_PAGE_SIMPLE = 20;
    public static final int ITEMS_PER_PAGE = 15;
    
    public static final String Y = "Y";
    public static final String N = "N";
}
