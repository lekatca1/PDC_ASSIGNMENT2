package com.cinema.cinema.booking.factory;

import com.cinema.cinema.booking.models.Admin;
import com.cinema.cinema.booking.models.Customer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserFactory class
 */
public class UserFactoryTest {
    
    @Test
    @DisplayName("Test creating Customer")
    public void testCreateCustomer() {
        Customer customer = UserFactory.createCustomer(
            "john_doe", "password123", "John", "Doe",
            "john@example.com", "1234567890"
        );
        
        assertNotNull(customer);
        assertEquals("john_doe", customer.getUsername());
        assertEquals("password123", customer.getPassword());
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("john@example.com", customer.getEmail());
        assertEquals("1234567890", customer.getPhone());
    }
    
    @Test
    @DisplayName("Test creating Admin")
    public void testCreateAdmin() {
        Admin admin = UserFactory.createAdmin(
            "admin1", "adminpass", "admin@cinema.com",
            "Jane", "Smith", "MANAGER", "IT"
        );
        
        assertNotNull(admin);
        assertEquals("admin1", admin.getUsername());
        assertEquals("adminpass", admin.getPassword());
        assertEquals("admin@cinema.com", admin.getEmail());
        assertEquals("Jane", admin.getFirstName());
        assertEquals("Smith", admin.getLastName());
        assertEquals("MANAGER", admin.getRole());
        assertEquals("IT", admin.getDepartment());
    }
    
    @Test
    @DisplayName("Test creating multiple customers")
    public void testCreateMultipleCustomers() {
        Customer customer1 = UserFactory.createCustomer(
            "user1", "pass1", "User", "One",
            "user1@test.com", "1111111111"
        );
        
        Customer customer2 = UserFactory.createCustomer(
            "user2", "pass2", "User", "Two",
            "user2@test.com", "2222222222"
        );
        
        assertNotNull(customer1);
        assertNotNull(customer2);
        assertNotSame(customer1, customer2);
        assertNotEquals(customer1.getUsername(), customer2.getUsername());
    }
    
    @Test
    @DisplayName("Test creating multiple admins")
    public void testCreateMultipleAdmins() {
        Admin admin1 = UserFactory.createAdmin(
            "admin1", "pass1", "admin1@cinema.com",
            "Admin", "One", "MANAGER", "HR"
        );
        
        Admin admin2 = UserFactory.createAdmin(
            "admin2", "pass2", "admin2@cinema.com",
            "Admin", "Two", "SUPERVISOR", "Sales"
        );
        
        assertNotNull(admin1);
        assertNotNull(admin2);
        assertNotSame(admin1, admin2);
        assertNotEquals(admin1.getUsername(), admin2.getUsername());
    }
    
    @Test
    @DisplayName("Test customer has all required fields")
    public void testCustomerRequiredFields() {
        Customer customer = UserFactory.createCustomer(
            "testuser", "testpass", "Test", "User",
            "test@example.com", "5555555555"
        );
        
        assertNotNull(customer.getUsername());
        assertNotNull(customer.getPassword());
        assertNotNull(customer.getFirstName());
        assertNotNull(customer.getLastName());
        assertNotNull(customer.getEmail());
        assertNotNull(customer.getPhone());
    }
    
    @Test
    @DisplayName("Test admin has all required fields")
    public void testAdminRequiredFields() {
        Admin admin = UserFactory.createAdmin(
            "testadmin", "testpass", "admin@test.com",
            "Test", "Admin", "ADMIN", "IT"
        );
        
        assertNotNull(admin.getUsername());
        assertNotNull(admin.getPassword());
        assertNotNull(admin.getEmail());
        assertNotNull(admin.getFirstName());
        assertNotNull(admin.getLastName());
        assertNotNull(admin.getRole());
        assertNotNull(admin.getDepartment());
    }
}