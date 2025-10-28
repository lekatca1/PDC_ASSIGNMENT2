package com.cinema.cinema.booking.models;

public class Customer extends User {
    private String phone;
    private int loyaltyPoints;
    private String membershipLevel;
    
    public Customer() {
        super();
        this.loyaltyPoints = 0;
        this.membershipLevel = "BRONZE";
    }
    
    public Customer(String username, String password, String firstName, String lastName, 
                   String email, String phone) {
        super(username, password, email, firstName, lastName);
        this.phone = phone;
        this.loyaltyPoints = 0;
        this.membershipLevel = "BRONZE";
    }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public int getLoyaltyPoints() { return loyaltyPoints; }
    public void setLoyaltyPoints(int loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }
    
    public String getMembershipLevel() { return membershipLevel; }
    public void setMembershipLevel(String membershipLevel) { this.membershipLevel = membershipLevel; }
}