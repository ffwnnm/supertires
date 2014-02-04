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
    public static final String ADMIN_PASS = "asd";
    public static final String ADMIN_UNAME = "Администратор";
    
    public static final String WINTER = "WINTER";
    public static final String SUMMER = "SUMMER";
    public static final String ALL_SEASONS = "ALL_SEASONS";
    
    public static final List<String> seasons = Arrays.asList(SUMMER, WINTER, ALL_SEASONS);
    
    public static final int MAX_IMAGES_PER_ITEM = 5;
    public static final int ITEMS_PER_PAGE_SIMPLE = 20;
    
    public static final String Y = "Y";
    public static final String N = "N";
}
