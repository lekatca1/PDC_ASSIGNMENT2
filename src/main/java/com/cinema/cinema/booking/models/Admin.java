package com.cinema.cinema.booking.models;

/**
 * Admin class representing a cinema administrator
 * Extends User to inherit basic user properties
 * Includes role-based permissions for system management
 * @author xps1597
 */
public class Admin extends User {
    private String role;
    private String department;
    private boolean canManageUsers;
    private boolean canManageMovies;
    private boolean canViewReports;
    
    /**
     * Default constructor
     * Initializes admin with no permissions
     */
    public Admin() {
        super();
        this.canManageUsers = false;
        this.canManageMovies = false;
        this.canViewReports = false;
    }
    
    /**
     * Constructor without userId (for registration before database insert)
     * @param username Admin's username
     * @param password Admin's password
     * @param email Admin's email
     * @param firstName Admin's first name
     * @param lastName Admin's last name
     * @param role Admin's role (SUPER_ADMIN, MANAGER, STAFF)
     * @param department Admin's department
     */
    public Admin(String username, String password, String email, String firstName, 
                String lastName, String role, String department) {
        super(username, password, email, firstName, lastName);
        this.role = role;
        this.department = department;
        setPermissionsByRole(role);
    }
    
    /**
     * Constructor with userId for database operations
     * CRITICAL: This constructor is needed for proper database integration
     * @param userId Database user ID
     * @param username Admin's username
     * @param password Admin's password
     * @param firstName Admin's first name
     * @param lastName Admin's last name
     * @param email Admin's email
     * @param role Admin's role (SUPER_ADMIN, MANAGER, STAFF)
     * @param department Admin's department
     */
    public Admin(int userId, String username, String password, String firstName, 
                String lastName, String email, String role, String department) {
        super(userId, username, password, email, firstName, lastName, "ADMIN");
        this.role = role;
        this.department = department;
        setPermissionsByRole(role);
    }
    
    // Getters and Setters
    
    /**
     * Get admin's role
     * @return Role as String (SUPER_ADMIN, MANAGER, STAFF)
     */
    public String getRole() { 
        return role; 
    }
    
    /**
     * Set admin's role and update permissions accordingly
     * @param role Role to set
     */
    public void setRole(String role) { 
        this.role = role;
        setPermissionsByRole(role);
    }
    
    /**
     * Get admin's department
     * @return Department as String
     */
    public String getDepartment() { 
        return department; 
    }
    
    /**
     * Set admin's department
     * @param department Department to set
     */
    public void setDepartment(String department) { 
        this.department = department; 
    }
    
    /**
     * Check if admin can manage users
     * @return true if permission granted, false otherwise
     */
    public boolean isCanManageUsers() { 
        return canManageUsers; 
    }
    
    /**
     * Set user management permission
     * @param canManageUsers Permission to set
     */
    public void setCanManageUsers(boolean canManageUsers) { 
        this.canManageUsers = canManageUsers; 
    }
    
    /**
     * Check if admin can manage movies
     * @return true if permission granted, false otherwise
     */
    public boolean isCanManageMovies() { 
        return canManageMovies; 
    }
    
    /**
     * Set movie management permission
     * @param canManageMovies Permission to set
     */
    public void setCanManageMovies(boolean canManageMovies) { 
        this.canManageMovies = canManageMovies; 
    }
    
    /**
     * Check if admin can view reports
     * @return true if permission granted, false otherwise
     */
    public boolean isCanViewReports() { 
        return canViewReports; 
    }
    
    /**
     * Set report viewing permission
     * @param canViewReports Permission to set
     */
    public void setCanViewReports(boolean canViewReports) { 
        this.canViewReports = canViewReports; 
    }
    
    /**
     * Set permissions based on role
     * SUPER_ADMIN: All permissions
     * MANAGER: Manage movies and view reports
     * STAFF: No special permissions
     * @param role Role to set permissions for
     */
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