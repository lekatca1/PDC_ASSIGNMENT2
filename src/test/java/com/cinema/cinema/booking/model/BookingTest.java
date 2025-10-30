package com.cinema.cinema.booking.model;

import com.cinema.cinema.booking.models.Booking;
import com.cinema.cinema.booking.models.Seat;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for Booking model class
 */
public class BookingTest {
    
    private Booking booking;
    
    @BeforeEach
    public void setUp() {
        booking = new Booking();
    }
    
    @AfterEach
    public void tearDown() {
        booking = null;
    }
    
    @Test
    @DisplayName("Test creating new booking with defaults")
    public void testNewBooking_Defaults() {
        assertEquals(0, booking.getBookingId());
        assertEquals(0, booking.getCustomerId());
        assertEquals(0, booking.getShowtimeId());
        assertEquals(0.0, booking.getTotalPrice(), 0.01);
    }
    
    @Test
    @DisplayName("Test setting and getting booking ID")
    public void testSetAndGetBookingId() {
        booking.setBookingId(123);
        assertEquals(123, booking.getBookingId());
    }
    
    @Test
    @DisplayName("Test setting and getting customer ID")
    public void testSetAndGetCustomerId() {
        booking.setCustomerId(456);
        assertEquals(456, booking.getCustomerId());
    }
    
    @Test
    @DisplayName("Test setting and getting showtime ID")
    public void testSetAndGetShowtimeId() {
        booking.setShowtimeId(789);
        assertEquals(789, booking.getShowtimeId());
    }
    
    @Test
    @DisplayName("Test setting and getting total price")
    public void testSetAndGetTotalPrice() {
        booking.setTotalPrice(45.50);
        assertEquals(45.50, booking.getTotalPrice(), 0.01);
    }
    
    @Test
    @DisplayName("Test setting and getting booking date")
    public void testSetAndGetBookingDate() {
        LocalDateTime now = LocalDateTime.now();
        booking.setBookingDate(now);
        assertEquals(now, booking.getBookingDate());
    }
    
    @Test
    @DisplayName("Test setting and getting booking status")
    public void testSetAndGetStatus() {
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        assertEquals(Booking.BookingStatus.CONFIRMED, booking.getStatus());
    }
    
    @Test
    @DisplayName("Test all booking status values")
    public void testBookingStatus_AllValues() {
        booking.setStatus(Booking.BookingStatus.PENDING);
        assertEquals(Booking.BookingStatus.PENDING, booking.getStatus());
        
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        assertEquals(Booking.BookingStatus.CONFIRMED, booking.getStatus());
        
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        assertEquals(Booking.BookingStatus.CANCELLED, booking.getStatus());
    }
    
    @Test
    @DisplayName("Test setting and getting payment method")
    public void testSetAndGetPaymentMethod() {
        booking.setPaymentMethod("Credit Card");
        assertEquals("Credit Card", booking.getPaymentMethod());
    }
    
    @Test
    @DisplayName("Test setting and getting transaction ID")
    public void testSetAndGetTransactionId() {
        booking.setTransactionId("TXN123456789");
        assertEquals("TXN123456789", booking.getTransactionId());
    }
    
    @Test
    @DisplayName("Test adding seats to booking")
    public void testSetAndGetBookedSeats() {
        List<Seat> seats = new ArrayList<>();
        Seat seat1 = new Seat();
        seat1.setSeatId(1);
        seat1.setSeatNumber("A1");
        
        Seat seat2 = new Seat();
        seat2.setSeatId(2);
        seat2.setSeatNumber("A2");
        
        seats.add(seat1);
        seats.add(seat2);
        
        booking.setBookedSeats(seats);
        
        assertEquals(2, booking.getBookedSeats().size());
        assertEquals("A1", booking.getBookedSeats().get(0).getSeatNumber());
        assertEquals("A2", booking.getBookedSeats().get(1).getSeatNumber());
    }
    
    @Test
    @DisplayName("Test getting number of seats")
    public void testGetNumberOfSeats() {
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Seat seat = new Seat();
            seat.setSeatId(i);
            seat.setSeatNumber("A" + i);
            seats.add(seat);
        }
        
        booking.setBookedSeats(seats);
        assertEquals(3, booking.getNumberOfSeats());
    }
    
    @Test
    @DisplayName("Test number of seats with empty list")
    public void testGetNumberOfSeats_EmptyList() {
        booking.setBookedSeats(new ArrayList<>());
        assertEquals(0, booking.getNumberOfSeats());
    }
    
    @Test
    @DisplayName("Test complete booking creation")
    public void testCompleteBooking() {
        List<Seat> seats = new ArrayList<>();
        Seat seat = new Seat();
        seat.setSeatId(1);
        seat.setSeatNumber("A5");
        seat.setSeatType(Seat.SeatType.VIP);
        seats.add(seat);
        
        LocalDateTime bookingDate = LocalDateTime.of(2025, 10, 28, 14, 30);
        
        booking.setBookingId(100);
        booking.setCustomerId(5);
        booking.setShowtimeId(10);
        booking.setTotalPrice(15.00);
        booking.setBookingDate(bookingDate);
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        booking.setPaymentMethod("Credit Card");
        booking.setTransactionId("TXN987654321");
        booking.setBookedSeats(seats);
        
        assertEquals(100, booking.getBookingId());
        assertEquals(5, booking.getCustomerId());
        assertEquals(10, booking.getShowtimeId());
        assertEquals(15.00, booking.getTotalPrice(), 0.01);
        assertEquals(bookingDate, booking.getBookingDate());
        assertEquals(Booking.BookingStatus.CONFIRMED, booking.getStatus());
        assertEquals("Credit Card", booking.getPaymentMethod());
        assertEquals("TXN987654321", booking.getTransactionId());
        assertEquals(1, booking.getNumberOfSeats());
        assertEquals(Seat.SeatType.VIP, booking.getBookedSeats().get(0).getSeatType());
    }
    
    @Test
    @DisplayName("Test booking with multiple VIP seats")
    public void testBooking_MultipleVIPSeats() {
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Seat seat = new Seat();
            seat.setSeatId(i);
            seat.setSeatNumber("V" + i);
            seat.setSeatType(Seat.SeatType.VIP);
            seats.add(seat);
        }
        
        booking.setBookedSeats(seats);
        
        assertEquals(5, booking.getNumberOfSeats());
        for (Seat seat : booking.getBookedSeats()) {
            assertEquals(Seat.SeatType.VIP, seat.getSeatType());
        }
    }
    
    @Test
    @DisplayName("Test booking with mixed seat types")
    public void testBooking_MixedSeatTypes() {
        List<Seat> seats = new ArrayList<>();
        
        Seat regularSeat = new Seat();
        regularSeat.setSeatId(1);
        regularSeat.setSeatNumber("A1");
        regularSeat.setSeatType(Seat.SeatType.REGULAR);
        seats.add(regularSeat);
        
        Seat premiumSeat = new Seat();
        premiumSeat.setSeatId(2);
        premiumSeat.setSeatNumber("P1");
        premiumSeat.setSeatType(Seat.SeatType.PREMIUM);
        seats.add(premiumSeat);
        
        Seat vipSeat = new Seat();
        vipSeat.setSeatId(3);
        vipSeat.setSeatNumber("V1");
        vipSeat.setSeatType(Seat.SeatType.VIP);
        seats.add(vipSeat);
        
        booking.setBookedSeats(seats);
        
        assertEquals(3, booking.getNumberOfSeats());
        assertEquals(Seat.SeatType.REGULAR, booking.getBookedSeats().get(0).getSeatType());
        assertEquals(Seat.SeatType.PREMIUM, booking.getBookedSeats().get(1).getSeatType());
        assertEquals(Seat.SeatType.VIP, booking.getBookedSeats().get(2).getSeatType());
    }
    
    @Test
    @DisplayName("Test different payment methods")
    public void testBooking_DifferentPaymentMethods() {
        booking.setPaymentMethod("Credit Card");
        assertEquals("Credit Card", booking.getPaymentMethod());
        
        booking.setPaymentMethod("Debit Card");
        assertEquals("Debit Card", booking.getPaymentMethod());
        
        booking.setPaymentMethod("Cash");
        assertEquals("Cash", booking.getPaymentMethod());
    }
    
    @Test
    @DisplayName("Test booking status transitions")
    public void testBooking_StatusTransitions() {
        booking.setStatus(Booking.BookingStatus.PENDING);
        assertEquals(Booking.BookingStatus.PENDING, booking.getStatus());
        
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        assertEquals(Booking.BookingStatus.CONFIRMED, booking.getStatus());
        
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        assertEquals(Booking.BookingStatus.CANCELLED, booking.getStatus());
    }
    
    @Test
    @DisplayName("Test booking with zero price")
    public void testBooking_ZeroPrice() {
        booking.setTotalPrice(0.0);
        assertEquals(0.0, booking.getTotalPrice(), 0.01);
    }
    
    @Test
    @DisplayName("Test booking date in the past")
    public void testBooking_PastDate() {
        LocalDateTime pastDate = LocalDateTime.of(2024, 1, 1, 10, 0);
        booking.setBookingDate(pastDate);
        assertEquals(pastDate, booking.getBookingDate());
    }
    
    @Test
    @DisplayName("Test booking date in the future")
    public void testBooking_FutureDate() {
        LocalDateTime futureDate = LocalDateTime.of(2026, 12, 31, 23, 59);
        booking.setBookingDate(futureDate);
        assertEquals(futureDate, booking.getBookingDate());
    }
    
    @Test
    @DisplayName("Test booking with large number of seats")
    public void testBooking_LargeNumberOfSeats() {
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Seat seat = new Seat();
            seat.setSeatId(i);
            seat.setSeatNumber("S" + i);
            seats.add(seat);
        }
        
        booking.setBookedSeats(seats);
        assertEquals(20, booking.getNumberOfSeats());
    }
    
    @Test
    @DisplayName("Test booking with premium seats")
    public void testBooking_PremiumSeats() {
        List<Seat> seats = new ArrayList<>();
        Seat premiumSeat = new Seat();
        premiumSeat.setSeatId(1);
        premiumSeat.setSeatNumber("P1");
        premiumSeat.setSeatType(Seat.SeatType.PREMIUM);
        seats.add(premiumSeat);
        
        booking.setBookedSeats(seats);
        
        assertEquals(1, booking.getNumberOfSeats());
        assertEquals(Seat.SeatType.PREMIUM, booking.getBookedSeats().get(0).getSeatType());
    }
}