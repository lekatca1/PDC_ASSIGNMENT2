/**
 * Sends email notifications when booking events occur.
 *
 * @author xps1597
 */
package com.cinema.cinema.booking.observer;

import com.cinema.cinema.booking.models.Booking;

/**
 * Observer that sends email notifications for booking state changes.
 */
public class EmailNotificationObserver implements BookingObserver {
    
    /**
     * Notifies customer via email when a booking is created.
     */
    @Override
    public void onBookingCreated(Booking booking) {
        System.out.println(" Email: Booking created - ID: " + booking.getBookingId());
    }
    
    /**
     * Notifies customer via email when a booking is cancelled.
     */
    @Override
    public void onBookingCancelled(Booking booking) {
        System.out.println("Email: Booking cancelled - ID: " + booking.getBookingId());
    }
    
    /**
     * Notifies customer via email when a booking is confirmed.
     */
    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.println("Email: Booking confirmed - ID: " + booking.getBookingId());
    }
}