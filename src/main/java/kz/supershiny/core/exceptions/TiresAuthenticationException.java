/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kz.supershiny.core.exceptions;

/**
 *
 * @author aishmanov
 */
public class TiresAuthenticationException extends Exception {

    @Override
    public String getMessage() {
        return "Authentication failed!";
    }
    
}
