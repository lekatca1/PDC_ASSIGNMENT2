/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author xps1597
 */
package com.cinema.cinema.booking.models;

public class Screen {
    private int screenId;
    private String screenName;
    private int totalSeats;
    private int regularSeats;
    private int premiumSeats;
    private int vipSeats;
    private boolean isActive;
    
    public Screen() {
        this.isActive = true;
    }
    
    public Screen(String screenName, int regularSeats, int premiumSeats, int vipSeats) {
        this();
        this.screenName = screenName;
        this.regularSeats = regularSeats;
        this.premiumSeats = premiumSeats;
        this.vipSeats = vipSeats;
        this.totalSeats = regularSeats + premiumSeats + vipSeats;
    }
    
    // Getters and Setters
    public int getScreenId() { return screenId; }
    public void setScreenId(int screenId) { this.screenId = screenId; }
    
    public String getScreenName() { return screenName; }
    public void setScreenName(String screenName) { this.screenName = screenName; }
    
    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    
    public int getRegularSeats() { return regularSeats; }
    public void setRegularSeats(int regularSeats) { this.regularSeats = regularSeats; }
    
    public int getPremiumSeats() { return premiumSeats; }
    public void setPremiumSeats(int premiumSeats) { this.premiumSeats = premiumSeats; }
    
    public int getVipSeats() { return vipSeats; }
    public void setVipSeats(int vipSeats) { this.vipSeats = vipSeats; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public void updateTotalSeats() {
        this.totalSeats = regularSeats + premiumSeats + vipSeats;
    }
}
