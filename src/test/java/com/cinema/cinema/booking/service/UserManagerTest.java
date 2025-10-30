package com.cinema.cinema.booking.service;

import com.cinema.cinema.booking.models.Customer;
import com.cinema.cinema.booking.models.Admin;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserManager class
 * Tests user authentication, registration, and retrieval from database
 * @author xps1597
 */
public class UserManagerTest {
    
    private UserManager userManager;
    
    /**
     * Set up test environment before each test
     * Creates a new UserManager instance
     */
    @BeforeEach
    public void setUp() {
        userManager = new UserManager();
    }
    
    /**
     * Test that default admin exists in database and can login
     */
    @Test
    @DisplayName("Test default admin exists")
    public void testDefaultAdminExists() {
        Object result = userManager.login("admin", "admin123");
        
        assertNotNull(result, "Admin login should return user object");
        assertTrue(result instanceof Admin, "Should return Admin instance");
        
        Admin admin = (Admin) result;
        assertEquals("admin", admin.getUsername());
        assertTrue(admin.getUserId() > 0, "Admin should have valid userId from database");
    }
    
    /**
     * Test that default customer 'rob' exists in database and can login
     */
    @Test
    @DisplayName("Test default customer rob exists")
    public void testDefaultCustomerExists() {
        Object result = userManager.login("rob", "rob");
        
        assertNotNull(result, "Customer login should return user object");
        assertTrue(result instanceof Customer, "Should return Customer instance");
        
        Customer customer = (Customer) result;
        assertEquals("rob", customer.getUsername());
        assertTrue(customer.getUserId() > 0, "Customer should have valid userId from database");
    }
    
    /**
     * Test successful registration of a new customer
     */
    @Test
    @DisplayName("Test register new customer")
    public void testRegisterCustomer_Success() {
        Customer newCustomer = new Customer(
            "newuser", "password123", "New", "User",
            "new@example.com", "1234567890"
        );
        
        boolean result = userManager.registerCustomer(newCustomer);
        
        assertTrue(result, "Registration should succeed");
        assertTrue(newCustomer.getUserId() > 0, "Registered customer should have userId set");
        
        // Verify customer can be found
        Customer found = userManager.findCustomerByUsername("newuser");
        assertNotNull(found, "Registered customer should be findable");
        assertEquals("newuser", found.getUsername());
    }
    
    /**
     * Test that registering duplicate username fails
     */
    @Test
    @DisplayName("Test register duplicate username fails")
    public void testRegisterCustomer_DuplicateUsername() {
        Customer customer1 = new Customer(
            "duplicate", "pass1", "User", "One",
            "user1@example.com", "1111111111"
        );
        
        boolean firstResult = userManager.registerCustomer(customer1);
        assertTrue(firstResult, "First registration should succeed");
        
        Customer customer2 = new Customer(
            "duplicate", "pass2", "User", "Two",
            "user2@example.com", "2222222222"
        );
        
        boolean secondResult = userManager.registerCustomer(customer2);
        
        assertFalse(secondResult, "Duplicate username registration should fail");
    }
    
    /**
     * Test login with valid customer credentials
     * FIXED: Now expects userId to be set from database
     */
    @Test
    @DisplayName("Test login with valid customer credentials")
    public void testLogin_ValidCustomer() {
        // Register a test customer
        Customer customer = new Customer(
            "testuser", "testpass", "Test", "User",
            "test@example.com", "5555555555"
        );
        boolean registered = userManager.registerCustomer(customer);
        assertTrue(registered, "Customer registration should succeed");
        
        // Login with the registered customer
        Object result = userManager.login("testuser", "testpass");
        
        // Verify login successful
        assertNotNull(result, "Login should return user object");
        assertTrue(result instanceof Customer, "Should return Customer instance");
        
        Customer loggedIn = (Customer) result;
        assertEquals("testuser", loggedIn.getUsername());
        assertTrue(loggedIn.getUserId() > 0, "Logged in customer should have valid userId from database");
    }
    
    /**
     * Test login with non-existent credentials returns null
     */
    @Test
    @DisplayName("Test login with invalid credentials")
    public void testLogin_InvalidCredentials() {
        Object result = userManager.login("nonexistent", "wrongpass");
        
        assertNull(result, "Login with invalid credentials should return null");
    }
    
    /**
     * Test login with wrong password fails
     */
    @Test
    @DisplayName("Test login with wrong password")
    public void testLogin_WrongPassword() {
        Customer customer = new Customer(
            "testuser2", "correctpass", "Test", "User",
            "test2@example.com", "5555555555"
        );
        userManager.registerCustomer(customer);
        
        Object result = userManager.login("testuser2", "wrongpass");
        
        assertNull(result, "Login with wrong password should return null");
    }
    
    /**
     * Test finding a customer by username
     */
    @Test
    @DisplayName("Test find customer by username")
    public void testFindCustomerByUsername() {
        Customer customer = new Customer(
            "findme", "password", "Find", "Me",
            "findme@example.com", "1234567890"
        );
        userManager.registerCustomer(customer);
        
        Customer found = userManager.findCustomerByUsername("findme");
        
        assertNotNull(found, "Customer should be found");
        assertEquals("findme", found.getUsername());
        assertTrue(found.getUserId() > 0, "Found customer should have valid userId");
    }
    
    /**
     * Test finding non-existent customer returns null
     */
    @Test
    @DisplayName("Test find non-existent customer returns null")
    public void testFindCustomerByUsername_NotFound() {
        Customer found = userManager.findCustomerByUsername("nonexistent");
        
        assertNull(found, "Non-existent customer should return null");
    }
    
    /**
     * Test finding admin by username
     */
    @Test
    @DisplayName("Test find admin by username")
    public void testFindAdminByUsername() {
        Admin found = userManager.findAdminByUsername("admin");
        
        assertNotNull(found, "Admin should be found");
        assertEquals("admin", found.getUsername());
        assertTrue(found.getUserId() > 0, "Found admin should have valid userId");
    }
    
    /**
     * Test finding non-existent admin returns null
     */
    @Test
    @DisplayName("Test find non-existent admin returns null")
    public void testFindAdminByUsername_NotFound() {
        Admin found = userManager.findAdminByUsername("nonexistentadmin");
        
        assertNull(found, "Non-existent admin should return null");
    }
    
    /**
     * Test getting all customers from database
     */
    @Test
    @DisplayName("Test get all customers")
    public void testGetAllCustomers() {
        Customer customer1 = new Customer(
            "user1", "pass1", "User", "One",
            "user1@example.com", "1111111111"
        );
        Customer customer2 = new Customer(
            "user2", "pass2", "User", "Two",
            "user2@example.com", "2222222222"
        );
        
        userManager.registerCustomer(customer1);
        userManager.registerCustomer(customer2);
        
        // Should have at least 3 customers (2 new + 1 default "rob")
        assertTrue(userManager.getAllCustomers().size() >= 3, 
            "Should have at least 3 customers in database");
    }
    
    /**
     * Test getting all admins from database
     */
    @Test
    @DisplayName("Test get all admins")
    public void testGetAllAdmins() {
        // Should have at least 1 default admin
        assertTrue(userManager.getAllAdmins().size() >= 1, 
            "Should have at least 1 admin in database");
    }
    
    /**
     * Test that admin login works correctly
     */
    @Test
    @DisplayName("Test admin login returns Admin object")
    public void testLogin_AdminPriority() {
        // The default admin should be found
        Object result = userManager.login("admin", "admin123");
        
        assertNotNull(result, "Admin login should succeed");
        assertTrue(result instanceof Admin, "Should return Admin instance");
        
        Admin admin = (Admin) result;
        assertTrue(admin.getUserId() > 0, "Admin should have valid userId");
    }
}