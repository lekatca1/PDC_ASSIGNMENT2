/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cinema.cinema.booking.models;

/**
 *
 * @author xps1597
 */

import java.time.LocalDateTime;
import java.util.List;

public class Booking {
    public enum BookingStatus {
        CONFIRMED, CANCELLED, PENDING, EXPIRED
    }
    
    private int bookingId;
    private int customerId;
    private int showtimeId;
    private List<Seat> bookedSeats;
    private double totalPrice;
    private LocalDateTime bookingDate;
    private BookingStatus status;
    private String paymentMethod;
    private String transactionId;
    
    public Booking() {
        this.bookingDate = LocalDateTime.now();
        this.status = BookingStatus.PENDING;
    }
    
    public Booking(int customerId, int showtimeId, List<Seat> bookedSeats, double totalPrice) {
        this();
        this.customerId = customerId;
        this.showtimeId = showtimeId;
        this.bookedSeats = bookedSeats;
        this.totalPrice = totalPrice;
    }
    
    // Getters and Setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    public int getShowtimeId() { return showtimeId; }
    public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }
    
    public List<Seat> getBookedSeats() { return bookedSeats; }
    public void setBookedSeats(List<Seat> bookedSeats) { this.bookedSeats = bookedSeats; }
    
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    
    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }
    
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public int getNumberOfSeats() {
        return bookedSeats != null ? bookedSeats.size() : 0;
    }
}