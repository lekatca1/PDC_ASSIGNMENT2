package com.cinema.cinema.booking.observer;

import com.cinema.cinema.booking.models.Booking;

public interface BookingObserver {
    void onBookingCreated(Booking booking);
    void onBookingCancelled(Booking booking);
    void onBookingConfirmed(Booking booking);
}