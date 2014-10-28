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
    public static final String ADMIN_PHONE = "123";
    public static final String ADMIN_PASS = "123";
    public static final String ADMIN_UNAME = "Администратор";
    
    //seasons
    public static final String WINTER = "WINTER";
    public static final String SUMMER = "SUMMER";
    public static final String ALL_SEASONS = "ALL_SEASONS";
    
    public static final List<String> seasons = Arrays.asList(SUMMER, WINTER, ALL_SEASONS);
    
    //sorting
    public static final String SORT_PRICE_ASC = "PRICE_ASC";
    public static final String SORT_PRICE_DESC = "PRICE_DESC";
    public static final String SORT_RADIUS_ASC = "RADIUS_ASC";
    public static final String SORT_RADIUS_DESC = "RADIUS_DESC";
    public static final String SORT_ARTICLE_DESC = "ARTICLE_DESC";
    
    public static final List<String> sortings = Arrays.asList(SORT_ARTICLE_DESC, SORT_PRICE_DESC, SORT_PRICE_ASC, SORT_RADIUS_ASC, SORT_RADIUS_DESC);
    
    public static final int MAX_IMAGES_UPLOAD_SIZE_KB = 8192;
    public static final int MAX_IMAGES_PER_ITEM = 10;
    public static final int ITEMS_PER_PAGE = 10;
    
    public static final String Y = "Y";
    public static final String N = "N";
}
