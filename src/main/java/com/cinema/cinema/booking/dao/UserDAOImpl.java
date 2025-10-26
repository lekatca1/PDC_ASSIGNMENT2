package com.cinema.cinema.booking.dao;

import com.cinema.cinema.booking.database.DatabaseConnection;
import com.cinema.cinema.booking.models.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {
    private final Connection connection;
    
    public UserDAOImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    @Override
    public Optional<User> findById(int userId) {
        String sql = "SELECT * FROM USERS WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapResultSetToUser(rs));
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return Optional.empty();
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM USERS WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapResultSetToUser(rs));
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return Optional.empty();
    }
    
    @Override
    public Optional<User> authenticate(String username, String password) {
        String sql = "SELECT * FROM USERS WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapResultSetToUser(rs));
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return Optional.empty();
    }
    
    @Override
    public boolean save(User user) {
        if (user instanceof Customer) return saveCustomer((Customer) user);
        else if (user instanceof Admin) return saveAdmin((Admin) user);
        return false;
    }
    
    private boolean saveCustomer(Customer customer) {
        String sql = "INSERT INTO USERS (username, password, email, first_name, last_name, user_type, phone) " +
                     "VALUES (?, ?, ?, ?, ?, 'CUSTOMER', ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, customer.getUsername());
            stmt.setString(2, customer.getPassword());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getFirstName());
            stmt.setString(5, customer.getLastName());
            stmt.setString(6, customer.getPhone());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) customer.setUserId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return false;
    }
    
    private boolean saveAdmin(Admin admin) {
        String sql = "INSERT INTO USERS (username, password, email, first_name, last_name, user_type, role, department) " +
                     "VALUES (?, ?, ?, ?, ?, 'ADMIN', ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, admin.getUsername());
            stmt.setString(2, admin.getPassword());
            stmt.setString(3, admin.getEmail());
            stmt.setString(4, admin.getFirstName());
            stmt.setString(5, admin.getLastName());
            stmt.setString(6, admin.getRole());
            stmt.setString(7, admin.getDepartment());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) admin.setUserId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean update(User user) {
        String sql = "UPDATE USERS SET username=?, email=?, first_name=?, last_name=? WHERE user_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setInt(5, user.getUserId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean delete(int userId) {
        String sql = "DELETE FROM USERS WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM USERS WHERE user_type='CUSTOMER'")) {
            while (rs.next()) customers.add((Customer) mapResultSetToUser(rs));
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return customers;
    }
    
    @Override
    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM USERS WHERE user_type='ADMIN'")) {
            while (rs.next()) admins.add((Admin) mapResultSetToUser(rs));
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return admins;
    }
    
    @Override
    public boolean usernameExists(String username) {
        return findByUsername(username).isPresent();
    }
    
    @Override
    public boolean emailExists(String email) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM USERS WHERE email=?")) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return false;
    }
    
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        String userType = rs.getString("user_type");
        if ("CUSTOMER".equals(userType)) {
            Customer c = new Customer();
            c.setUserId(rs.getInt("user_id"));
            c.setUsername(rs.getString("username"));
            c.setPassword(rs.getString("password"));
            c.setEmail(rs.getString("email"));
            c.setFirstName(rs.getString("first_name"));
            c.setLastName(rs.getString("last_name"));
            c.setPhone(rs.getString("phone"));
            return c;
        } else {
            Admin a = new Admin();
            a.setUserId(rs.getInt("user_id"));
            a.setUsername(rs.getString("username"));
            a.setPassword(rs.getString("password"));
            a.setEmail(rs.getString("email"));
            a.setFirstName(rs.getString("first_name"));
            a.setLastName(rs.getString("last_name"));
            a.setRole(rs.getString("role"));
            a.setDepartment(rs.getString("department"));
            return a;
        }
    }
}