package com.cinema.cinema.booking.factory;

import com.cinema.cinema.booking.models.*;

public class UserFactory {
    
    public static Customer createCustomer(String username, String password, 
                                         String firstName, String lastName,
                                         String email, String phone) {
        return new Customer(username, password, firstName, lastName, email, phone);
    }
    
    public static Admin createAdmin(String username, String password,
                                   String email, String firstName, String lastName,
                                   String role, String department) {
        return new Admin(username, password, email, firstName, lastName, role, department);
    }
}