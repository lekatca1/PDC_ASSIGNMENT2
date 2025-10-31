/**
 *
 * @author xps1597
 */
package com.cinema.cinema.booking.exception;

public class BookingException extends RuntimeException {
    public BookingException(String message) {
        super(message);
    }
    
    public BookingException(String message, Throwable cause) {
        super(message, cause);
    }
}