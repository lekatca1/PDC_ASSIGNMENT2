/**
 *
 * @author xps1597
 */
package com.cinema.cinema.booking.dao;

import com.cinema.cinema.booking.models.Booking;
import com.cinema.cinema.booking.exception.DatabaseException;
import java.util.List;

public interface BookingDAO {
    List<Booking> getBookingsByCustomerId(int customerId) throws DatabaseException;
    Booking getBookingById(int bookingId) throws DatabaseException;
    int createBooking(Booking booking) throws DatabaseException;
    boolean updateBookingStatus(int bookingId, Booking.BookingStatus status) throws DatabaseException;
    List<Booking> getAllBookings() throws DatabaseException;
}