/**
 * UserManager - Manages user authentication and registration
 * Integrates with database to persist and retrieve user data
 * @author xps1597
 */
package com.cinema.cinema.booking.service;

import com.cinema.cinema.booking.models.Customer;
import com.cinema.cinema.booking.models.Admin;
import com.cinema.cinema.booking.models.User;
import com.cinema.cinema.booking.dao.UserDAO;
import com.cinema.cinema.booking.factory.DAOFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * UserManager handles all user-related operations
 * Now uses database for persistence instead of in-memory lists
 */
public class UserManager {
    private UserDAO userDAO;
    
    /**
     * Constructor - Initializes UserManager with database access
     * No longer creates default users in memory
     * Default users (admin/admin123 and rob/rob) are in the database
     */
    public UserManager() {
        // Get the UserDAO from factory for database operations
        this.userDAO = DAOFactory.getUserDAO();
        
        System.out.println("✓ UserManager initialized with database connection");
    }
    
    /**
     * Register a new customer in the database
     * @param customer Customer object to register
     * @return true if registration successful, false if username already exists
     */
    public boolean registerCustomer(Customer customer) {
        try {
            // Check if username already exists in database
            if (userDAO.usernameExists(customer.getUsername())) {
                System.out.println("✗ Registration failed - username already exists: " + customer.getUsername());
                return false;
            }
            
            // Check if email already exists in database
            if (userDAO.emailExists(customer.getEmail())) {
                System.out.println("✗ Registration failed - email already exists: " + customer.getEmail());
                return false;
            }
            
            // Set user type to CUSTOMER
            customer.setUserType("CUSTOMER");
            
            // Save customer to database
            boolean success = userDAO.save(customer);
            
            if (success) {
                System.out.println("✓ Customer registered successfully with ID: " + customer.getUserId());
            } else {
                System.out.println("✗ Failed to register customer");
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("Error registering customer: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Login a customer or admin
     * CRITICAL: Returns User object from database with userId properly set
     * @param username Username to authenticate
     * @param password Password to verify
     * @return User object (Customer or Admin) if login successful, null otherwise
     */
    public Object login(String username, String password) {
        try {
            // Authenticate user through database
            Optional<User> userOpt = userDAO.authenticate(username, password);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                // CRITICAL: User object from database has userId set
                System.out.println("✓ Login successful - User: " + username + ", ID: " + user.getUserId() + ", Type: " + user.getUserType());
                
                return user;
            }
            
            System.out.println("✗ Login failed - Invalid credentials for username: " + username);
            return null;
            
        } catch (Exception e) {
            System.err.println("Error during login: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Find a customer by username from database
     * @param username Username to search for
     * @return Customer object if found, null otherwise
     */
    public Customer findCustomerByUsername(String username) {
        try {
            Optional<User> userOpt = userDAO.findByUsername(username);
            
            if (userOpt.isPresent() && userOpt.get() instanceof Customer) {
                Customer customer = (Customer) userOpt.get();
                System.out.println("✓ Found customer: " + username + " with ID: " + customer.getUserId());
                return customer;
            }
            
            System.out.println("✗ Customer not found: " + username);
            return null;
            
        } catch (Exception e) {
            System.err.println("Error finding customer: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Find an admin by username from database
     * @param username Username to search for
     * @return Admin object if found, null otherwise
     */
    public Admin findAdminByUsername(String username) {
        try {
            Optional<User> userOpt = userDAO.findByUsername(username);
            
            if (userOpt.isPresent() && userOpt.get() instanceof Admin) {
                Admin admin = (Admin) userOpt.get();
                System.out.println("✓ Found admin: " + username + " with ID: " + admin.getUserId());
                return admin;
            }
            
            System.out.println("✗ Admin not found: " + username);
            return null;
            
        } catch (Exception e) {
            System.err.println("Error finding admin: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get all customers from database
     * @return List of all Customer objects
     */
    public List<Customer> getAllCustomers() {
        try {
            List<Customer> customers = userDAO.getAllCustomers();
            System.out.println("✓ Retrieved " + customers.size() + " customers from database");
            return customers;
            
        } catch (Exception e) {
            System.err.println("Error getting all customers: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Return empty list on error
        }
    }
    
    /**
     * Get all admins from database
     * @return List of all Admin objects
     */
    public List<Admin> getAllAdmins() {
        try {
            List<Admin> admins = userDAO.getAllAdmins();
            System.out.println("✓ Retrieved " + admins.size() + " admins from database");
            return admins;
            
        } catch (Exception e) {
            System.err.println("Error getting all admins: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Return empty list on error
        }
    }
    
    /**
     * Update a user in the database
     * @param user User object with updated information
     * @return true if update successful, false otherwise
     */
    public boolean updateUser(User user) {
        try {
            boolean success = userDAO.update(user);
            
            if (success) {
                System.out.println("✓ User updated successfully: " + user.getUsername());
            } else {
                System.out.println("✗ Failed to update user: " + user.getUsername());
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete a user from the database
     * @param userId ID of user to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteUser(int userId) {
        try {
            boolean success = userDAO.delete(userId);
            
            if (success) {
                System.out.println("✓ User deleted successfully with ID: " + userId);
            } else {
                System.out.println("✗ Failed to delete user with ID: " + userId);
            }
            
            return success;
            
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Check if a username exists in the database
     * @param username Username to check
     * @return true if username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        try {
            return userDAO.usernameExists(username);
        } catch (Exception e) {
            System.err.println("Error checking username: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Check if an email exists in the database
     * @param email Email to check
     * @return true if email exists, false otherwise
     */
    public boolean emailExists(String email) {
        try {
            return userDAO.emailExists(email);
        } catch (Exception e) {
            System.err.println("Error checking email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
