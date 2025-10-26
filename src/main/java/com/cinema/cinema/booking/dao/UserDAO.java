package com.cinema.cinema.booking.dao;  // CORRECTED

import com.cinema.cinema.booking.models.User;  // CORRECTED
import com.cinema.cinema.booking.models.Customer;  // CORRECTED
import com.cinema.cinema.booking.models.Admin;  // CORRECTED
import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Optional<User> findById(int userId);
    Optional<User> findByUsername(String username);
    Optional<User> authenticate(String username, String password);
    boolean save(User user);
    boolean update(User user);
    boolean delete(int userId);
    List<Customer> getAllCustomers();
    List<Admin> getAllAdmins();
    boolean usernameExists(String username);
    boolean emailExists(String email);
}