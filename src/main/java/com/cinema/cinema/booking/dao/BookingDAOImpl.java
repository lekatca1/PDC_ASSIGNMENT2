package com.cinema.cinema.booking.dao;

import com.cinema.cinema.booking.database.DatabaseConnection;
import com.cinema.cinema.booking.exception.DatabaseException;
import com.cinema.cinema.booking.models.Booking;
import com.cinema.cinema.booking.models.Seat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {
    
    @Override
    public List<Booking> getBookingsByCustomerId(int customerId) throws DatabaseException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM BOOKINGS WHERE customer_id = ? ORDER BY booking_date DESC";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setCustomerId(rs.getInt("customer_id"));
                booking.setShowtimeId(rs.getInt("showtime_id"));
                booking.setTotalPrice(rs.getDouble("total_price"));
                booking.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
                booking.setStatus(Booking.BookingStatus.valueOf(rs.getString("status")));
                booking.setPaymentMethod(rs.getString("payment_method"));
                booking.setTransactionId(rs.getString("transaction_id"));
                
                // Get seats for this booking
                booking.setBookedSeats(getSeatsForBooking(booking.getBookingId(), conn));
                
                bookings.add(booking);
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving bookings: " + e.getMessage(), e);
        }
        
        return bookings;
    }
    
    @Override
    public Booking getBookingById(int bookingId) throws DatabaseException {
        String sql = "SELECT * FROM BOOKINGS WHERE booking_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setCustomerId(rs.getInt("customer_id"));
                booking.setShowtimeId(rs.getInt("showtime_id"));
                booking.setTotalPrice(rs.getDouble("total_price"));
                booking.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
                booking.setStatus(Booking.BookingStatus.valueOf(rs.getString("status")));
                booking.setPaymentMethod(rs.getString("payment_method"));
                booking.setTransactionId(rs.getString("transaction_id"));
                
                booking.setBookedSeats(getSeatsForBooking(booking.getBookingId(), conn));
                
                return booking;
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving booking: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public int createBooking(Booking booking) throws DatabaseException {
        String sql = "INSERT INTO BOOKINGS (customer_id, showtime_id, total_price, status, payment_method, transaction_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, booking.getCustomerId());
            stmt.setInt(2, booking.getShowtimeId());
            stmt.setDouble(3, booking.getTotalPrice());
            stmt.setString(4, booking.getStatus().toString());
            stmt.setString(5, booking.getPaymentMethod());
            stmt.setString(6, booking.getTransactionId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DatabaseException("Creating booking failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int bookingId = generatedKeys.getInt(1);
                    booking.setBookingId(bookingId);
                    
                    // Insert booked seats
                    insertBookingSeats(bookingId, booking.getBookedSeats(), conn);
                    
                    return bookingId;
                } else {
                    throw new DatabaseException("Creating booking failed, no ID obtained.");
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error creating booking: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean updateBookingStatus(int bookingId, Booking.BookingStatus status) throws DatabaseException {
        String sql = "UPDATE BOOKINGS SET status = ? WHERE booking_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.toString());
            stmt.setInt(2, bookingId);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error updating booking status: " + e.getMessage(), e);
        }
    }
    
private List<Seat> getSeatsForBooking(int bookingId, Connection conn) throws SQLException {
    List<Seat> seats = new ArrayList<>();
    String sql = "SELECT s.* FROM SEATS s " +
                 "JOIN BOOKING_SEATS bs ON s.seat_id = bs.seat_id " +
                 "WHERE bs.booking_id = ?";
    
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, bookingId);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Seat seat = new Seat();
            seat.setSeatId(rs.getInt("seat_id"));
            seat.setSeatNumber(rs.getString("seat_number"));
            seat.setSeatType(Seat.SeatType.valueOf(rs.getString("seat_type")));
            seats.add(seat);
        }
    }
    
    return seats;
}
    
    private void insertBookingSeats(int bookingId, List<Seat> seats, Connection conn) throws SQLException {
        String sql = "INSERT INTO BOOKING_SEATS (booking_id, seat_id, price) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Seat seat : seats) {
                stmt.setInt(1, bookingId);
                stmt.setInt(2, seat.getSeatId());
                stmt.setDouble(3, 12.50); // You can calculate actual price
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
}