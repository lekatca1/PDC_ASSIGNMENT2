package com.cinema.cinema.booking.dao;

import com.cinema.cinema.booking.database.DatabaseConnection;
import com.cinema.cinema.booking.exception.DatabaseException;
import com.cinema.cinema.booking.models.Admin;
import com.cinema.cinema.booking.models.Customer;
import com.cinema.cinema.booking.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of UserDAO interface
 * Handles all database operations for User entities
 * @author xps1597
 */
public class UserDAOImpl implements UserDAO {
    
    /**
     * Find a user by their ID
     * @param userId User ID to search for
     * @return Optional containing User if found, empty otherwise
     */
    @Override
    public Optional<User> findById(int userId) {
        String sql = "SELECT * FROM USERS WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String userType = rs.getString("user_type");
                
                if ("CUSTOMER".equals(userType)) {
                    Customer customer = new Customer(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone")
                    );
                    customer.setUserType(userType);
                    return Optional.of(customer);
                    
                } else if ("ADMIN".equals(userType)) {
                    Admin admin = new Admin(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getString("department")
                    );
                    admin.setUserType(userType);
                    return Optional.of(admin);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
    
    /**
     * Find a user by username
     * @param username Username to search for
     * @return Optional containing User if found, empty otherwise
     */
    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM USERS WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String userType = rs.getString("user_type");
                int userId = rs.getInt("user_id");
                
                if ("CUSTOMER".equals(userType)) {
                    Customer customer = new Customer(
                        userId,
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone")
                    );
                    customer.setUserType(userType);
                    System.out.println("✓ Loaded customer from database with ID: " + userId);
                    return Optional.of(customer);
                    
                } else if ("ADMIN".equals(userType)) {
                    Admin admin = new Admin(
                        userId,
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getString("department")
                    );
                    admin.setUserType(userType);
                    System.out.println("✓ Loaded admin from database with ID: " + userId);
                    return Optional.of(admin);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding user by username: " + e.getMessage());
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
    
    /**
     * Authenticate user credentials
     * @param username Username to authenticate
     * @param password Password to verify
     * @return Optional containing User if credentials are valid, empty otherwise
     */
    @Override
    public Optional<User> authenticate(String username, String password) {
        Optional<User> userOpt = findByUsername(username);
        
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            System.out.println("✓ User authenticated successfully: " + username);
            return userOpt;
        }
        
        System.out.println("✗ Authentication failed for username: " + username);
        return Optional.empty();
    }
    
    /**
     * Save a new user to the database
     * @param user User object to save (Customer or Admin)
     * @return true if save was successful, false otherwise
     */
    @Override
    public boolean save(User user) {
        String sql = "INSERT INTO USERS (username, password, email, first_name, last_name, user_type, phone, role, department) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Set common fields
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getUserType());
            
            // Set type-specific fields
            if (user instanceof Customer) {
                Customer customer = (Customer) user;
                stmt.setString(7, customer.getPhone());
                stmt.setNull(8, Types.VARCHAR);  // role is null for customers
                stmt.setNull(9, Types.VARCHAR);  // department is null for customers
            } else if (user instanceof Admin) {
                Admin admin = (Admin) user;
                stmt.setNull(7, Types.VARCHAR);  // phone is null for admins
                stmt.setString(8, admin.getRole());
                stmt.setString(9, admin.getDepartment());
            } else {
                // Default user
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.VARCHAR);
                stmt.setNull(9, Types.VARCHAR);
            }
            
            // Execute insert
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return false;
            }
            
            // Get generated user ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    user.setUserId(userId);  // Set the ID in the user object
                    
                    System.out.println("✓ Created user in database with ID: " + userId);
                    return true;
                } else {
                    return false;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update an existing user in the database
     * @param user User object with updated information
     * @return true if update was successful, false otherwise
     */
    @Override
    public boolean update(User user) {
        String sql = "UPDATE USERS SET username = ?, password = ?, email = ?, " +
                     "first_name = ?, last_name = ?, phone = ?, role = ?, department = ? " +
                     "WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            
            // Set type-specific fields
            if (user instanceof Customer) {
                Customer customer = (Customer) user;
                stmt.setString(6, customer.getPhone());
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.VARCHAR);
            } else if (user instanceof Admin) {
                Admin admin = (Admin) user;
                stmt.setNull(6, Types.VARCHAR);
                stmt.setString(7, admin.getRole());
                stmt.setString(8, admin.getDepartment());
            } else {
                stmt.setNull(6, Types.VARCHAR);
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.VARCHAR);
            }
            
            stmt.setInt(9, user.getUserId());
            
            int affectedRows = stmt.executeUpdate();
            
            System.out.println("✓ Updated user in database with ID: " + user.getUserId());
            
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete a user from the database
     * @param userId ID of user to delete
     * @return true if deletion was successful, false otherwise
     */
    @Override
    public boolean delete(int userId) {
        String sql = "DELETE FROM USERS WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            int affectedRows = stmt.executeUpdate();
            
            System.out.println("✓ Deleted user from database with ID: " + userId);
            
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Check if a username already exists in the database
     * @param username Username to check
     * @return true if username exists, false otherwise
     */
    @Override
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM USERS WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking username: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Check if an email already exists in the database
     * @param email Email to check
     * @return true if email exists, false otherwise
     */
    @Override
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM USERS WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking email: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get all admin users from the database
     * @return List of Admin objects
     */
    @Override
    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        String sql = "SELECT * FROM USERS WHERE user_type = 'ADMIN' ORDER BY username";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                int userId = rs.getInt("user_id");
                
                Admin admin = new Admin(
                    userId,
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getString("department")
                );
                
                admin.setUserType("ADMIN");
                admins.add(admin);
            }
            
            System.out.println("✓ Loaded " + admins.size() + " admins from database");
            
        } catch (SQLException e) {
            System.err.println("Error retrieving admins: " + e.getMessage());
            e.printStackTrace();
        }
        
        return admins;
    }
    
    /**
     * Get all customer users from the database
     * @return List of Customer objects
     */
    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM USERS WHERE user_type = 'CUSTOMER' ORDER BY username";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                int userId = rs.getInt("user_id");
                
                Customer customer = new Customer(
                    userId,
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("phone")
                );
                
                customer.setUserType("CUSTOMER");
                customers.add(customer);
            }
            
            System.out.println("✓ Loaded " + customers.size() + " customers from database");
            
        } catch (SQLException e) {
            System.err.println("Error retrieving customers: " + e.getMessage());
            e.printStackTrace();
        }
        
        return customers;
    }
}