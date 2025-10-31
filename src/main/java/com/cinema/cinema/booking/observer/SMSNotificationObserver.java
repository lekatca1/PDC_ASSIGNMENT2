/**
 * Sends SMS notifications when booking events occur.
 *
 * @author xps1597
 */
package com.cinema.cinema.booking.observer;

import com.cinema.cinema.booking.models.Booking;

/**
 * Observer that sends SMS notifications for booking state changes.
 */
public class SMSNotificationObserver implements BookingObserver {
    
    /**
     * Notifies customer via SMS when a booking is created.
     */
    @Override
    public void onBookingCreated(Booking booking) {
        System.out.println(" SMS: Booking created - ID: " + booking.getBookingId());
    }
    
    /**
     * Notifies customer via SMS when a booking is cancelled.
     */
    @Override
    public void onBookingCancelled(Booking booking) {
        System.out.println(" SMS: Booking cancelled - ID: " + booking.getBookingId());
    }
    
    /**
     * Notifies customer via SMS when a booking is confirmed.
     */
    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.println(" SMS: Booking confirmed - ID: " + booking.getBookingId());
    }
}