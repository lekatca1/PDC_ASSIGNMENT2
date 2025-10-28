package com.cinema.cinema.booking.strategy;

import com.cinema.cinema.booking.models.Seat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class WeekendPricingStrategy implements PricingStrategy {
    
    @Override
    public double calculatePrice(double basePrice, Seat seat, LocalDateTime dateTime) {
        double price = basePrice;
        
        DayOfWeek day = dateTime.getDayOfWeek();
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            price *= 1.20;
        }
        
        if (seat != null && seat.getSeatType() == Seat.SeatType.VIP) {
            price *= 1.5;
        }
        
        return price;
    }
    
    @Override
    public String getStrategyName() {
        return "Weekend Pricing (+20%)";
    }
}