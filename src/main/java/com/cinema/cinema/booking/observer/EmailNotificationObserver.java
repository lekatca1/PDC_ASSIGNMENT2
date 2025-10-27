package com.cinema.cinema.booking.observer;

import com.cinema.cinema.booking.models.Booking;

public class EmailNotificationObserver implements BookingObserver {
    
    @Override
    public void onBookingCreated(Booking booking) {
        System.out.println("📧 Email: Booking created - ID: " + booking.getBookingId());
    }
    
    @Override
    public void onBookingCancelled(Booking booking) {
        System.out.println("📧 Email: Booking cancelled - ID: " + booking.getBookingId());
    }
    
    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.println("📧 Email: Booking confirmed - ID: " + booking.getBookingId());
    }
}