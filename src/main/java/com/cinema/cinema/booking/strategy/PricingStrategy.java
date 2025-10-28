package com.cinema.cinema.booking.strategy;

import com.cinema.cinema.booking.models.Seat;
import java.time.LocalDateTime;

public interface PricingStrategy {
    double calculatePrice(double basePrice, Seat seat, LocalDateTime dateTime);
    String getStrategyName();
}