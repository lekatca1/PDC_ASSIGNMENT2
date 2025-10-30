package com.cinema.cinema.booking.service;

import com.cinema.cinema.booking.models.Seat;
import com.cinema.cinema.booking.models.Showtime;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

/**
 * Unit tests for PriceCalculator class
 */
public class PriceCalculatorTest {
    
    private PriceCalculator priceCalculator;
    
    @BeforeEach
    public void setUp() {
        priceCalculator = new PriceCalculator();
    }
    
    @Test
    @DisplayName("Test regular seat pricing on weekday afternoon")
    public void testCalculatePrice_RegularSeat_Weekday() {
        Seat seat = new Seat();
        seat.setSeatType(Seat.SeatType.REGULAR);
        
        Showtime showtime = new Showtime();
        showtime.setBasePrice(10.0);
        showtime.setStartTime(LocalDateTime.of(2025, 10, 27, 14, 0)); // Monday afternoon
        
        double price = priceCalculator.calculatePrice(seat, showtime);
        
        // Regular seat (1.0x) * weekday afternoon (1.0x) * 10.0 = 10.0
        assertEquals(10.0, price, 0.01);
    }
    
    @Test
    @DisplayName("Test premium seat pricing on weekday")
    public void testCalculatePrice_PremiumSeat_Weekday() {
        Seat seat = new Seat();
        seat.setSeatType(Seat.SeatType.PREMIUM);
        
        Showtime showtime = new Showtime();
        showtime.setBasePrice(10.0);
        showtime.setStartTime(LocalDateTime.of(2025, 10, 27, 14, 0)); // Monday afternoon
        
        double price = priceCalculator.calculatePrice(seat, showtime);
        
        // Premium seat (1.5x) * weekday afternoon (1.0x) * 10.0 = 15.0
        assertEquals(15.0, price, 0.01);
    }
    
    @Test
    @DisplayName("Test VIP seat pricing on weekday")
    public void testCalculatePrice_VIPSeat_Weekday() {
        Seat seat = new Seat();
        seat.setSeatType(Seat.SeatType.VIP);
        
        Showtime showtime = new Showtime();
        showtime.setBasePrice(10.0);
        showtime.setStartTime(LocalDateTime.of(2025, 10, 27, 14, 0)); // Monday afternoon
        
        double price = priceCalculator.calculatePrice(seat, showtime);
        
        // VIP seat (2.0x) * weekday afternoon (1.0x) * 10.0 = 20.0
        assertEquals(20.0, price, 0.01);
    }
    
    @Test
    @DisplayName("Test regular seat pricing on weekend")
    public void testCalculatePrice_RegularSeat_Weekend() {
        Seat seat = new Seat();
        seat.setSeatType(Seat.SeatType.REGULAR);
        
        Showtime showtime = new Showtime();
        showtime.setBasePrice(10.0);
        showtime.setStartTime(LocalDateTime.of(2025, 11, 1, 14, 0)); // Saturday afternoon
        
        double price = priceCalculator.calculatePrice(seat, showtime);
        
        // Regular seat (1.0x) * weekend (1.2x) * 10.0 = 12.0
        assertEquals(12.0, price, 0.01);
    }
    
    @Test
    @DisplayName("Test regular seat pricing evening weekday")
    public void testCalculatePrice_RegularSeat_Evening() {
        Seat seat = new Seat();
        seat.setSeatType(Seat.SeatType.REGULAR);
        
        Showtime showtime = new Showtime();
        showtime.setBasePrice(10.0);
        showtime.setStartTime(LocalDateTime.of(2025, 10, 27, 19, 0)); // Monday evening
        
        double price = priceCalculator.calculatePrice(seat, showtime);
        
        // Regular seat (1.0x) * weekday (1.0x) * evening (1.25x) * 10.0 = 12.5
        assertEquals(12.5, price, 0.01);
    }
    
    @Test
    @DisplayName("Test VIP seat pricing evening weekend")
    public void testCalculatePrice_VIPSeat_EveningWeekend() {
        Seat seat = new Seat();
        seat.setSeatType(Seat.SeatType.VIP);
        
        Showtime showtime = new Showtime();
        showtime.setBasePrice(10.0);
        showtime.setStartTime(LocalDateTime.of(2025, 11, 1, 20, 0)); // Saturday evening
        
        double price = priceCalculator.calculatePrice(seat, showtime);
        
        // VIP seat (2.0x) * weekend (1.2x) * evening (1.25x) * 10.0 = 29.0
        // Actually: 2.0 * (1.0 + 0.2 + 0.25) * 10 = 2.0 * 1.45 * 10 = 29.0
        assertEquals(29.0, price, 0.01);
    }
    
    @Test
    @DisplayName("Test premium seat pricing evening weekend")
    public void testCalculatePrice_PremiumSeat_EveningWeekend() {
        Seat seat = new Seat();
        seat.setSeatType(Seat.SeatType.PREMIUM);
        
        Showtime showtime = new Showtime();
        showtime.setBasePrice(10.0);
        showtime.setStartTime(LocalDateTime.of(2025, 11, 2, 21, 0)); // Sunday evening
        
        double price = priceCalculator.calculatePrice(seat, showtime);
        
        // Premium seat (1.5x) * (weekend 0.2 + evening 0.25 + base 1.0) * 10.0 = 21.75
        assertEquals(21.75, price, 0.01);
    }
    
    @Test
    @DisplayName("Test pricing with different base prices")
    public void testCalculatePrice_DifferentBasePrices() {
        Seat seat = new Seat();
        seat.setSeatType(Seat.SeatType.REGULAR);
        
        Showtime showtime1 = new Showtime();
        showtime1.setBasePrice(5.0);
        showtime1.setStartTime(LocalDateTime.of(2025, 10, 27, 14, 0));
        
        Showtime showtime2 = new Showtime();
        showtime2.setBasePrice(20.0);
        showtime2.setStartTime(LocalDateTime.of(2025, 10, 27, 14, 0));
        
        double price1 = priceCalculator.calculatePrice(seat, showtime1);
        double price2 = priceCalculator.calculatePrice(seat, showtime2);
        
        assertEquals(5.0, price1, 0.01);
        assertEquals(20.0, price2, 0.01);
    }
    
    @Test
    @DisplayName("Test Friday is considered weekend")
    public void testCalculatePrice_Friday() {
        Seat seat = new Seat();
        seat.setSeatType(Seat.SeatType.REGULAR);
        
        Showtime showtime = new Showtime();
        showtime.setBasePrice(10.0);
        showtime.setStartTime(LocalDateTime.of(2025, 10, 31, 14, 0)); // Friday
        
        double price = priceCalculator.calculatePrice(seat, showtime);
        
        // Should include weekend multiplier
        assertEquals(12.0, price, 0.01);
    }
    
    @Test
    @DisplayName("Test morning shows have no evening surcharge")
    public void testCalculatePrice_MorningShow() {
        Seat seat = new Seat();
        seat.setSeatType(Seat.SeatType.REGULAR);
        
        Showtime showtime = new Showtime();
        showtime.setBasePrice(10.0);
        showtime.setStartTime(LocalDateTime.of(2025, 10, 27, 10, 0)); // Monday morning
        
        double price = priceCalculator.calculatePrice(seat, showtime);
        
        // No evening surcharge
        assertEquals(10.0, price, 0.01);
    }
}