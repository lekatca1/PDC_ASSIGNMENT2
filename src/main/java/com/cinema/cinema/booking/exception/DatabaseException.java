/**
 *
 * @author xps1597
 */
package com.cinema.cinema.booking.exception;

public class DatabaseException extends Exception {
    public DatabaseException(String message) {
        super(message);
    }
    
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}