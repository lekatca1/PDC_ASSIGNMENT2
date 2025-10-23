/**
 *
 * @author xps1597
 */
package com.cinema.cinema.booking.service;

import com.cinema.cinema.booking.models.Customer;
import com.cinema.cinema.booking.models.Admin;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private List<Customer> customers;
    private List<Admin> admins;

    public UserManager() {
        this.customers = new ArrayList<>();
        this.admins = new ArrayList<>();

        // Add default admin
        admins.add(new Admin(
                "admin", "admin123", "admin@cinema.com",
                "Default", "Admin", "SUPER_ADMIN", "Management"
        ));

    // Add default customer rob/rob
customers.add(new Customer(
        "rob",          // username
        "rob",          // password
        "Rob",          // first name
        "rob",        // last name (example)
        "rob@cinema.com", // email
        "1234567890"    // phone
));

    }

    // Register a new customer
    public boolean registerCustomer(Customer customer) {
        if (findCustomerByUsername(customer.getUsername()) != null) {
            return false; // username already exists
        }
        customers.add(customer);
        return true;
    }

    // Login a customer or admin
    public Object login(String username, String password) {
        // Check admin first
        for (Admin admin : admins) {
            if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                return admin;
            }
        }

        // Check customers
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username) && customer.getPassword().equals(password)) {
                return customer;
            }
        }

        return null; // login failed
    }

    public Customer findCustomerByUsername(String username) {
        for (Customer c : customers) {
            if (c.getUsername().equals(username)) return c;
        }
        return null;
    }

    public Admin findAdminByUsername(String username) {
        for (Admin a : admins) {
            if (a.getUsername().equals(username)) return a;
        }
        return null;
    }

    public List<Customer> getAllCustomers() {
        return customers;
    }

    public List<Admin> getAllAdmins() {
        return admins;
    }
}
