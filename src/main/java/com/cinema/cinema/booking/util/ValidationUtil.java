package com.cinema.cinema.booking.util;

import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10,15}$");
    
    private ValidationUtil() {}
    
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.replaceAll("[\\s-]", "")).matches();
    }
    
    public static boolean isValidUsername(String username) {
        return username != null && username.matches("^[a-zA-Z0-9]{3,20}$");
    }
    
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
    
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
}