package com.cinema.cinema.booking.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a");
    
    private DateTimeUtil() {}
    
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "";
    }
    
    public static String formatTime(LocalTime time) {
        return time != null ? time.format(TIME_FORMATTER) : "";
    }
    
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATETIME_FORMATTER) : "";
    }
    
    public static boolean isFutureDate(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }
    
    public static boolean isFutureDateTime(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isAfter(LocalDateTime.now());
    }
}