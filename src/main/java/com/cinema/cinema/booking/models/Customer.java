package com.cinema.cinema.booking.models;

import java.time.LocalDateTime;

/**
 * Customer class representing a cinema customer
 * Extends User to inherit basic user properties
 * @author xps1597
 */
public class Customer extends User {
    private String phone;
    private int loyaltyPoints;
    private String membershipLevel;
    private LocalDateTime dateJoined;
    
    /**
     * Default constructor
     * Initializes customer with default values
     */
    public Customer() {
        super();
        this.loyaltyPoints = 0;
        this.membershipLevel = "BRONZE";
        this.dateJoined = LocalDateTime.now();
    }
    
    /**
     * Constructor with basic customer information
     * @param username Customer's username
     * @param password Customer's password
     * @param firstName Customer's first name
     * @param lastName Customer's last name
     * @param email Customer's email address
     * @param phone Customer's phone number
     */
    public Customer(String username, String password, String firstName, String lastName, 
                   String email, String phone) {
        super(username, password, email, firstName, lastName);
        this.phone = phone;
        this.loyaltyPoints = 0;
        this.membershipLevel = "BRONZE";
        this.dateJoined = LocalDateTime.now();
    }
    
    /**
     * Constructor with userId for database operations
     * CRITICAL: This constructor is needed for proper database integration
     * @param userId Database user ID
     * @param username Customer's username
     * @param password Customer's password
     * @param firstName Customer's first name
     * @param lastName Customer's last name
     * @param email Customer's email address
     * @param phone Customer's phone number
     */
    public Customer(int userId, String username, String password, String firstName, 
                   String lastName, String email, String phone) {
        super(userId, username, password, email, firstName, lastName, "CUSTOMER");
        this.phone = phone;
        this.loyaltyPoints = 0;
        this.membershipLevel = "BRONZE";
        this.dateJoined = LocalDateTime.now();
    }
    
    // Getters and Setters
    
    /**
     * Get customer's phone number
     * @return Phone number as String
     */
    public String getPhone() { 
        return phone; 
    }
    
    /**
     * Set customer's phone number
     * @param phone Phone number to set
     */
    public void setPhone(String phone) { 
        this.phone = phone; 
    }
    
    /**
     * Get customer's loyalty points
     * @return Current loyalty points
     */
    public int getLoyaltyPoints() { 
        return loyaltyPoints; 
    }
    
    /**
     * Set customer's loyalty points
     * @param loyaltyPoints Points to set
     */
    public void setLoyaltyPoints(int loyaltyPoints) { 
        this.loyaltyPoints = loyaltyPoints; 
    }
    
    /**
     * Get customer's membership level (BRONZE, SILVER, GOLD, etc.)
     * @return Membership level as String
     */
    public String getMembershipLevel() { 
        return membershipLevel; 
    }
    
    /**
     * Set customer's membership level
     * @param membershipLevel Level to set
     */
    public void setMembershipLevel(String membershipLevel) { 
        this.membershipLevel = membershipLevel; 
    }
    
    /**
     * Get date when customer joined
     * @return LocalDateTime of join date
     */
    public LocalDateTime getDateJoined() { 
        return dateJoined; 
    }
    
    /**
     * Set customer's join date
     * @param dateJoined Join date to set
     */
    public void setDateJoined(LocalDateTime dateJoined) { 
        this.dateJoined = dateJoined; 
    }
}