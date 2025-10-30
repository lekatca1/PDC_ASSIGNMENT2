package com.cinema.cinema.booking.models;

/**
 * Base User class for all user types in the cinema system
 * Provides common properties for Customer and Admin
 * @author xps1597
 */
public class User {
    // User identification
    private int userId;           // Database primary key
    private String username;      // Unique username for login
    private String password;      // User's password (should be hashed in production)
    
    // Personal information
    private String email;         // User's email address
    private String firstName;     // User's first name
    private String lastName;      // User's last name
    
    // User classification
    private String userType;      // Type: "CUSTOMER" or "ADMIN"
    
    /**
     * Default constructor
     */
    public User() {
        this.userId = 0;  // Initialize to 0, will be set from database
    }
    
    /**
     * Constructor without userId (for registration before database insert)
     * @param username User's username
     * @param password User's password
     * @param email User's email
     * @param firstName User's first name
     * @param lastName User's last name
     */
    public User(String username, String password, String email,
               String firstName, String lastName) {
        this.userId = 0;  // Will be set by database
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = "CUSTOMER";  // Default type
    }
    
    /**
     * Full constructor with userId (for database retrieval)
     * CRITICAL: This constructor must be used when loading users from database
     * @param userId Database user ID
     * @param username User's username
     * @param password User's password
     * @param email User's email
     * @param firstName User's first name
     * @param lastName User's last name
     * @param userType User type (CUSTOMER or ADMIN)
     */
    public User(int userId, String username, String password, String email,
               String firstName, String lastName, String userType) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
    }
    
    // Getters and Setters
    
    /**
     * Get user's database ID
     * CRITICAL: This must return a valid ID (> 0) for bookings to work
     * @return User ID from database
     */
    public int getUserId() { 
        return userId; 
    }
    
    /**
     * Set user's database ID
     * CRITICAL: Must be called when loading user from database
     * @param userId User ID to set
     */
    public void setUserId(int userId) { 
        this.userId = userId; 
    }
    
    /**
     * Get username
     * @return Username as String
     */
    public String getUsername() { 
        return username; 
    }
    
    /**
     * Set username
     * @param username Username to set
     */
    public void setUsername(String username) { 
        this.username = username; 
    }
    
    /**
     * Get password
     * @return Password as String
     */
    public String getPassword() { 
        return password; 
    }
    
    /**
     * Set password
     * @param password Password to set
     */
    public void setPassword(String password) { 
        this.password = password; 
    }
    
    /**
     * Get email address
     * @return Email as String
     */
    public String getEmail() { 
        return email; 
    }
    
    /**
     * Set email address
     * @param email Email to set
     */
    public void setEmail(String email) { 
        this.email = email; 
    }
    
    /**
     * Get first name
     * @return First name as String
     */
    public String getFirstName() { 
        return firstName; 
    }
    
    /**
     * Set first name
     * @param firstName First name to set
     */
    public void setFirstName(String firstName) { 
        this.firstName = firstName; 
    }
    
    /**
     * Get last name
     * @return Last name as String
     */
    public String getLastName() { 
        return lastName; 
    }
    
    /**
     * Set last name
     * @param lastName Last name to set
     */
    public void setLastName(String lastName) { 
        this.lastName = lastName; 
    }
    
    /**
     * Get user type (CUSTOMER or ADMIN)
     * @return User type as String
     */
    public String getUserType() { 
        return userType; 
    }
    
    /**
     * Set user type
     * @param userType User type to set
     */
    public void setUserType(String userType) { 
        this.userType = userType; 
    }
    
    /**
     * Get full name (first + last)
     * @return Full name as String
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
}