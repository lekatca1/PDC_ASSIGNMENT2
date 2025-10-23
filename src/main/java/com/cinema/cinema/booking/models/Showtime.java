/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cinema.cinema.booking.models;

/**
 *
 * @author xps1597
 */

import java.time.LocalDateTime;
import java.util.List;

public class Showtime {
    private int showtimeId;
    private int movieId;
    private int screenId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double basePrice;
    private int availableSeats;
    private List<Seat> seats;
    
    public Showtime() {}
    
    public Showtime(int movieId, int screenId, LocalDateTime startTime, double basePrice) {
        this.movieId = movieId;
        this.screenId = screenId;
        this.startTime = startTime;
        this.basePrice = basePrice;
        // endTime will be calculated based on movie duration
    }
    
    // Getters and Setters
    public int getShowtimeId() { return showtimeId; }
    public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }
    
    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }
    
    public int getScreenId() { return screenId; }
    public void setScreenId(int screenId) { this.screenId = screenId; }
    
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    
    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
    
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    
    public List<Seat> getSeats() { return seats; }
    public void setSeats(List<Seat> seats) { this.seats = seats; }
}
