/**
 *
 * @author xps1597
 */

package com.cinema.cinema.booking.service;

import com.cinema.cinema.booking.models.Booking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class IncomeTracker {

    private List<Booking> bookings;
    private double manualIncome; // for income added without a booking
    private List<Runnable> listeners; // GUI listeners

    public IncomeTracker() {
        this.bookings = new ArrayList<>();
        this.manualIncome = 0.0;
        this.listeners = new ArrayList<>();
    }

    // Add booking to revenue tracking
    public void addBooking(Booking booking) {
        if (booking.getStatus() == Booking.BookingStatus.CONFIRMED) {
            bookings.add(booking);
            notifyListeners();
        }
    }

    // Add raw income (without a booking)
    public void addIncome(double amount) {
        if (amount > 0) {
            manualIncome += amount;
            notifyListeners();
        }
    }

    // Calculate total income (bookings + manual income)
    public double getTotalIncome() {
        double bookingIncome = bookings.stream()
                .mapToDouble(Booking::getTotalPrice)
                .sum();
        return bookingIncome + manualIncome;
    }

    // Daily income
    public double getIncomeForDate(LocalDate date) {
        return bookings.stream()
                .filter(b -> {
                    LocalDate d = extractBookingDate(b);
                    return d != null && d.equals(date);
                })
                .mapToDouble(Booking::getTotalPrice)
                .sum();
    }

    // Range income
    public double getIncomeForRange(LocalDate start, LocalDate end) {
        return bookings.stream()
                .filter(b -> {
                    LocalDate d = extractBookingDate(b);
                    return d != null && !d.isBefore(start) && !d.isAfter(end);
                })
                .mapToDouble(Booking::getTotalPrice)
                .sum();
    }

    // Helper to safely extract LocalDate from Booking
    private LocalDate extractBookingDate(Booking b) {
        LocalDateTime dt = b.getBookingDate();
        return (dt != null) ? dt.toLocalDate() : null;
    }

    // Generate simple financial report
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== Cinema Revenue Report ===\n");
        report.append("Total Bookings: ").append(bookings.size()).append("\n");
        report.append("Manual Income: $").append(String.format("%.2f", manualIncome)).append("\n");
        report.append("Total Income: $").append(String.format("%.2f", getTotalIncome())).append("\n");
        report.append("----------------------------\n");

        for (Booking b : bookings) {
            report.append("Booking ID: ").append(b.getBookingId())
                  .append(" | Customer ID: ").append(b.getCustomerId())
                  .append(" | Showtime ID: ").append(b.getShowtimeId())
                  .append(" | Seats: ").append(b.getNumberOfSeats())
                  .append(" | Total: $").append(String.format("%.2f", b.getTotalPrice()))
                  .append(" | Date: ").append(extractBookingDate(b))
                  .append("\n");
        }

        return report.toString();
    }

    // Listener mechanism for GUI refresh
    public void addChangeListener(Runnable listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    private void notifyListeners() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }

    // Provide a method that AdminPanel expects
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings); // safe copy
    }

    // Keep original getter for backward compatibility
    public List<Booking> getBookings() {
        return bookings;
    }
}
