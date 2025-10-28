package com.cinema.cinema.booking.models;

public class Admin extends User {
    private String role;
    private String department;
    private boolean canManageUsers;
    private boolean canManageMovies;
    private boolean canViewReports;
    
    public Admin() {
        super();
        this.canManageUsers = false;
        this.canManageMovies = false;
        this.canViewReports = false;
    }
    
    public Admin(String username, String password, String email, String firstName, 
                String lastName, String role, String department) {
        super(username, password, email, firstName, lastName);
        this.role = role;
        this.department = department;
        setPermissionsByRole(role);
    }
    
    public String getRole() { return role; }
    public void setRole(String role) { 
        this.role = role;
        setPermissionsByRole(role);
    }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public boolean isCanManageUsers() { return canManageUsers; }
    public void setCanManageUsers(boolean canManageUsers) { this.canManageUsers = canManageUsers; }
    
    public boolean isCanManageMovies() { return canManageMovies; }
    public void setCanManageMovies(boolean canManageMovies) { this.canManageMovies = canManageMovies; }
    
    public boolean isCanViewReports() { return canViewReports; }
    public void setCanViewReports(boolean canViewReports) { this.canViewReports = canViewReports; }
    
    private void setPermissionsByRole(String role) {
        if (role == null) return;
        
        switch (role.toUpperCase()) {
            case "SUPER_ADMIN" -> {
                this.canManageUsers = true;
                this.canManageMovies = true;
                this.canViewReports = true;
            }
            case "MANAGER" -> {
                this.canManageUsers = false;
                this.canManageMovies = true;
                this.canViewReports = true;
            }
            case "STAFF" -> {
                this.canManageUsers = false;
                this.canManageMovies = false;
                this.canViewReports = false;
            }
        }
    }
}