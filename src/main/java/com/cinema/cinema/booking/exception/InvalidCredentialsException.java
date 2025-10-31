/**
 *
 * @author xps1597
 */
package com.cinema.cinema.booking.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Invalid username or password");
    }
    
    public InvalidCredentialsException(String message) {
        super(message);
    }
}