package com.cinema.cinema.booking.strategy;

import com.cinema.cinema.booking.models.Seat;
import java.time.LocalDateTime;

public class StandardPricingStrategy implements PricingStrategy {
    
    @Override
    public double calculatePrice(double basePrice, Seat seat, LocalDateTime dateTime) {
        double price = basePrice;
        if (seat != null && seat.getSeatType() == Seat.SeatType.VIP) {
            price *= 1.5;
        }
        return price;
    }
    
    @Override
    public String getStrategyName() {
        return "Standard Pricing";
    }
}