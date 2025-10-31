/**
 *
 * @author xps1597
 */
package com.cinema.cinema.booking.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("User not found: " + username);
    }
    
    public UserNotFoundException(int userId) {
        super("User not found with ID: " + userId);
    }
}