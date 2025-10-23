
/**
 *
 * @author xps1597
 */
package com.cinema.cinema.booking.models;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private String phoneNumber;
    private List<Booking> bookingHistory;
    private double totalSpent;
    
    public Customer() {
        super();
        this.bookingHistory = new ArrayList<>();
        this.totalSpent = 0.0;
    }
    
    public Customer(String username, String password, String email, 
                   String firstName, String lastName, String phoneNumber) {
        super(username, password, email, firstName, lastName);
        this.phoneNumber = phoneNumber;
        this.bookingHistory = new ArrayList<>();
        this.totalSpent = 0.0;
    }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public List<Booking> getBookingHistory() { return bookingHistory; }
    public void addBooking(Booking booking) { 
        this.bookingHistory.add(booking);
        this.totalSpent += booking.getTotalPrice();
    }
    
    public double getTotalSpent() { return totalSpent; }
    public void setTotalSpent(double totalSpent) { this.totalSpent = totalSpent; }
}
