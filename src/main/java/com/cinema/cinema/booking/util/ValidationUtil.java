/**
 * Utility class for input validation operations.
 *
 * @author xps1597
 */
package com.cinema.cinema.booking.util;

import java.util.regex.Pattern;

/**
 * Provides helper methods for validating user input data.
 */
public class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10,15}$");
    
    /**
     * Private constructor to prevent instantiation.
     */
    private ValidationUtil() {}
    
    /**
     * Validates email address format.
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validates phone number format (10-15 digits).
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.replaceAll("[\\s-]", "")).matches();
    }
    
    /**
     * Validates username (3-20 alphanumeric characters).
     */
    public static boolean isValidUsername(String username) {
        return username != null && username.matches("^[a-zA-Z0-9]{3,20}$");
    }
    
    /**
     * Validates password (minimum 6 characters).
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
    
    /**
     * Checks if string is not null or empty.
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
}