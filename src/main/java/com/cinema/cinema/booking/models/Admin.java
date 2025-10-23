/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author xps1597
 */
package com.cinema.cinema.booking.models;

public class Admin extends User {
    private String adminLevel; // e.g., "SUPER_ADMIN", "MANAGER", "STAFF"
    private String department;
    
    public Admin() {
        super();
        this.adminLevel = "STAFF";
    }
    
    public Admin(String username, String password, String email, 
                String firstName, String lastName, String adminLevel, String department) {
        super(username, password, email, firstName, lastName);
        this.adminLevel = adminLevel;
        this.department = department;
    }
    
    public String getAdminLevel() { return adminLevel; }
    public void setAdminLevel(String adminLevel) { this.adminLevel = adminLevel; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public boolean canViewReports() {
        return "SUPER_ADMIN".equals(adminLevel) || "MANAGER".equals(adminLevel);
    }
    
    public boolean canManageMovies() {
        return "SUPER_ADMIN".equals(adminLevel) || "MANAGER".equals(adminLevel);
    }
}
