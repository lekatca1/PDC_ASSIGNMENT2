/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cinema.cinema.booking.service;

/**
 *
 * @author xps1597
 */
import com.cinema.cinema.booking.models.Seat;
import com.cinema.cinema.booking.models.Showtime;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class PriceCalculator {

    // Base multipliers for seat type
    private static final double REGULAR_MULTIPLIER = 1.0;
    private static final double PREMIUM_MULTIPLIER = 1.5;
    private static final double VIP_MULTIPLIER = 2.0;

    public double calculatePrice(Seat seat, Showtime showtime) {
        double basePrice = showtime.getBasePrice();
        double multiplier = getSeatMultiplier(seat);
        multiplier *= getTimeMultiplier(showtime.getStartTime());
        return basePrice * multiplier;
    }

    private double getSeatMultiplier(Seat seat) {
        switch (seat.getSeatType()) {
            case PREMIUM: return PREMIUM_MULTIPLIER;
            case VIP: return VIP_MULTIPLIER;
            default: return REGULAR_MULTIPLIER;
        }
    }

    // Increase price for peak times (evening/weekend)
    private double getTimeMultiplier(LocalDateTime startTime) {
        DayOfWeek day = startTime.getDayOfWeek();
        int hour = startTime.getHour();

        double multiplier = 1.0;

        // Weekend pricing
        if (day == DayOfWeek.FRIDAY || day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            multiplier += 0.2; // +20%
        }

        // Evening peak time: 18:00 - 22:00
        if (hour >= 18 && hour <= 22) {
            multiplier += 0.25; // +25%
        }

        return multiplier;
    }
}

