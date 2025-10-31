/**
 * Observer interface for monitoring booking events.
 *
 * @author xps1597
 */
package com.cinema.cinema.booking.observer;

import com.cinema.cinema.booking.models.Booking;

/**
 * Defines the contract for observers that react to booking state changes.
 */
public interface BookingObserver {
    
    /**
     * Called when a new booking is created.
     */
    void onBookingCreated(Booking booking);
    
    /**
     * Called when a booking is cancelled.
     */
    void onBookingCancelled(Booking booking);
    
    /**
     * Called when a booking is confirmed.
     */
    void onBookingConfirmed(Booking booking);
}