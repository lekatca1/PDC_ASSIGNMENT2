/**
 *
 * @author xps1597
 */
package com.cinema.cinema.booking.models;

public class Seat {
    public enum SeatType {
        STANDARD, REGULAR, PREMIUM, VIP
    }
    
    public enum SeatStatus {
        AVAILABLE, BOOKED, RESERVED, MAINTENANCE
    }
    
    private int seatId;
    private String seatNumber; // e.g., "A1", "B5"
    private int row;
    private int column;
    private SeatType seatType;
    private SeatStatus status;
    private int showtimeId;
    
    public Seat() {
        this.status = SeatStatus.AVAILABLE;
        this.seatType = SeatType.STANDARD;
    }
    
    public Seat(String seatNumber, int row, int column, SeatType seatType) {
        this();
        this.seatNumber = seatNumber;
        this.row = row;
        this.column = column;
        this.seatType = seatType;
    }
    
    // Getters and Setters
    public int getSeatId() { return seatId; }
    public void setSeatId(int seatId) { this.seatId = seatId; }
    
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    
    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }
    
    public int getColumn() { return column; }
    public void setColumn(int column) { this.column = column; }
    
    public SeatType getSeatType() { return seatType; }
    public void setSeatType(SeatType seatType) { this.seatType = seatType; }
    
    public SeatStatus getStatus() { return status; }
    public void setStatus(SeatStatus status) { this.status = status; }
    
    public int getShowtimeId() { return showtimeId; }
    public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }
    
    public boolean isAvailable() {
        return status == SeatStatus.AVAILABLE;
    }
}