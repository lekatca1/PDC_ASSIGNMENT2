package com.cinema.cinema.booking.dao;  
/**
 *
 * @author xps1597
 */

import com.cinema.cinema.booking.models.User;   
import com.cinema.cinema.booking.models.Customer;  
import com.cinema.cinema.booking.models.Admin;  
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