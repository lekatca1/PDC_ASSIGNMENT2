/**
 * Utility class for date and time formatting operations.
 *
 * @author xps1597
 */
package com.cinema.cinema.booking.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Provides helper methods for formatting and validating dates and times.
 */
public class DateTimeUtil {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a");
    
    /**
     * Private constructor to prevent instantiation.
     */
    private DateTimeUtil() {}
    
    /**
     * Formats a date as "MMM dd, yyyy".
     */
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "";
    }
    
    /**
     * Formats a time as "hh:mm a".
     */
    public static String formatTime(LocalTime time) {
        return time != null ? time.format(TIME_FORMATTER) : "";
    }
    
    /**
     * Formats a date-time as "MMM dd, yyyy at hh:mm a".
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATETIME_FORMATTER) : "";
    }
    
    /**
     * Checks if the given date is in the future.
     */
    public static boolean isFutureDate(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }
    
    /**
     * Checks if the given date-time is in the future.
     */
    public static boolean isFutureDateTime(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isAfter(LocalDateTime.now());
    }
}