package com.cinema.cinema.booking.strategy;

import com.cinema.cinema.booking.models.Seat;
import java.time.LocalDateTime;

public class VIPPricingStrategy implements PricingStrategy {
    
    @Override
    public double calculatePrice(double basePrice, Seat seat, LocalDateTime dateTime) {
        double price = basePrice * 0.90; // 10% VIP member discount
        
        if (seat != null && seat.getSeatType() == Seat.SeatType.VIP) {
            price *= 1.5;
        }
        
        return price;
    }
    
    @Override
    public String getStrategyName() {
        return "VIP Member Pricing (-10%)";
    }
}