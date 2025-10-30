package com.cinema.cinema.booking.util;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ValidationUtil class
 */
public class ValidationUtilTest {
    
    @Test
    @DisplayName("Test valid email addresses")
    public void testIsValidEmail_ValidEmails() {
        assertTrue(ValidationUtil.isValidEmail("test@example.com"));
        assertTrue(ValidationUtil.isValidEmail("user.name@example.com"));
        assertTrue(ValidationUtil.isValidEmail("user+tag@example.co.uk"));
        assertTrue(ValidationUtil.isValidEmail("test123@gmail.com"));
        assertTrue(ValidationUtil.isValidEmail("admin@cinema.org"));
    }
    
    @Test
    @DisplayName("Test invalid email addresses")
    public void testIsValidEmail_InvalidEmails() {
        assertFalse(ValidationUtil.isValidEmail(""));
        assertFalse(ValidationUtil.isValidEmail("notanemail"));
        assertFalse(ValidationUtil.isValidEmail("@example.com"));
        assertFalse(ValidationUtil.isValidEmail("user@"));
        assertFalse(ValidationUtil.isValidEmail("user @example.com"));
    }
    
    @Test
    @DisplayName("Test null email returns false")
    public void testIsValidEmail_Null() {
        assertFalse(ValidationUtil.isValidEmail(null));
    }
    
    @Test
    @DisplayName("Test valid phone numbers")
    public void testIsValidPhone_ValidPhones() {
        // These formats work because spaces and dashes are stripped
        assertTrue(ValidationUtil.isValidPhone("1234567890"));
        assertTrue(ValidationUtil.isValidPhone("123-456-7890"));
        assertTrue(ValidationUtil.isValidPhone("123 456 7890"));
        assertTrue(ValidationUtil.isValidPhone("12345678901234")); // 14 digits
    }
    
    @Test
    @DisplayName("Test invalid phone numbers")
    public void testIsValidPhone_InvalidPhones() {
        assertFalse(ValidationUtil.isValidPhone(""));
        assertFalse(ValidationUtil.isValidPhone("123"));
        assertFalse(ValidationUtil.isValidPhone("abcdefghij"));
        assertFalse(ValidationUtil.isValidPhone("12345")); // Too short
        // Parentheses are NOT stripped, so this is invalid
        assertFalse(ValidationUtil.isValidPhone("(123) 456-7890"));
    }
    
    @Test
    @DisplayName("Test null phone returns false")
    public void testIsValidPhone_Null() {
        assertFalse(ValidationUtil.isValidPhone(null));
    }
    
    @Test
    @DisplayName("Test valid usernames")
    public void testIsValidUsername_ValidUsernames() {
        assertTrue(ValidationUtil.isValidUsername("john"));
        assertTrue(ValidationUtil.isValidUsername("JohnDoe"));
        assertTrue(ValidationUtil.isValidUsername("user123"));
        assertTrue(ValidationUtil.isValidUsername("testuser2024"));
    }
    
    @Test
    @DisplayName("Test invalid usernames")
    public void testIsValidUsername_InvalidUsernames() {
        assertFalse(ValidationUtil.isValidUsername(""));
        assertFalse(ValidationUtil.isValidUsername("ab")); // Too short
        assertFalse(ValidationUtil.isValidUsername("user_name")); // Underscore not allowed
        assertFalse(ValidationUtil.isValidUsername("user name")); // Space not allowed
        assertFalse(ValidationUtil.isValidUsername("user@name")); // Special char not allowed
    }
    
    @Test
    @DisplayName("Test null username returns false")
    public void testIsValidUsername_Null() {
        assertFalse(ValidationUtil.isValidUsername(null));
    }
    
    @Test
    @DisplayName("Test valid passwords")
    public void testIsValidPassword_ValidPasswords() {
        assertTrue(ValidationUtil.isValidPassword("password"));
        assertTrue(ValidationUtil.isValidPassword("Pass123"));
        assertTrue(ValidationUtil.isValidPassword("MyP@ssw0rd"));
        assertTrue(ValidationUtil.isValidPassword("123456"));
    }
    
    @Test
    @DisplayName("Test invalid passwords")
    public void testIsValidPassword_InvalidPasswords() {
        assertFalse(ValidationUtil.isValidPassword(""));
        assertFalse(ValidationUtil.isValidPassword("12345")); // Too short (< 6)
        assertFalse(ValidationUtil.isValidPassword("pass")); // Too short
    }
    
    @Test
    @DisplayName("Test null password returns false")
    public void testIsValidPassword_Null() {
        assertFalse(ValidationUtil.isValidPassword(null));
    }
    
    @Test
    @DisplayName("Test string is not empty")
    public void testIsNotEmpty_ValidStrings() {
        assertTrue(ValidationUtil.isNotEmpty("Hello"));
        assertTrue(ValidationUtil.isNotEmpty("  text  ")); // With spaces
        assertTrue(ValidationUtil.isNotEmpty("123"));
        assertTrue(ValidationUtil.isNotEmpty("a"));
    }
    
    @Test
    @DisplayName("Test string is empty")
    public void testIsNotEmpty_EmptyStrings() {
        assertFalse(ValidationUtil.isNotEmpty(""));
        assertFalse(ValidationUtil.isNotEmpty("   ")); // Only spaces
        assertFalse(ValidationUtil.isNotEmpty(null));
    }
    
    @Test
    @DisplayName("Test email with multiple dots in domain")
    public void testIsValidEmail_MultipleDots() {
        assertTrue(ValidationUtil.isValidEmail("user@mail.example.com"));
        assertTrue(ValidationUtil.isValidEmail("test@subdomain.example.co.uk"));
    }
    
    @Test
    @DisplayName("Test phone number with dashes")
    public void testIsValidPhone_WithDashes() {
        assertTrue(ValidationUtil.isValidPhone("123-456-7890"));
        assertTrue(ValidationUtil.isValidPhone("1234-567-890"));
    }
    
    @Test
    @DisplayName("Test phone number with spaces")
    public void testIsValidPhone_WithSpaces() {
        assertTrue(ValidationUtil.isValidPhone("123 456 7890"));
        assertTrue(ValidationUtil.isValidPhone("1234 567 890"));
    }
    
    @Test
    @DisplayName("Test username alphanumeric only")
    public void testIsValidUsername_AlphanumericOnly() {
        assertTrue(ValidationUtil.isValidUsername("user123"));
        assertTrue(ValidationUtil.isValidUsername("123user"));
        assertTrue(ValidationUtil.isValidUsername("User"));
        assertFalse(ValidationUtil.isValidUsername("user_123")); // No underscore
        assertFalse(ValidationUtil.isValidUsername("user-123")); // No dash
    }
    
    @Test
    @DisplayName("Test username length constraints")
    public void testIsValidUsername_LengthConstraints() {
        assertFalse(ValidationUtil.isValidUsername("ab")); // 2 chars - too short
        assertTrue(ValidationUtil.isValidUsername("abc")); // 3 chars - minimum
        assertTrue(ValidationUtil.isValidUsername("abcdefghij1234567890")); // 20 chars - maximum
        assertFalse(ValidationUtil.isValidUsername("abcdefghij12345678901")); // 21 chars - too long
    }
    
    @Test
    @DisplayName("Test password minimum length")
    public void testIsValidPassword_MinimumLength() {
        assertFalse(ValidationUtil.isValidPassword("12345")); // 5 chars - too short
        assertTrue(ValidationUtil.isValidPassword("123456")); // 6 chars - minimum
        assertTrue(ValidationUtil.isValidPassword("1234567")); // 7 chars - valid
    }
    
    @Test
    @DisplayName("Test empty string with whitespace")
    public void testIsNotEmpty_Whitespace() {
        assertFalse(ValidationUtil.isNotEmpty("   "));
        assertFalse(ValidationUtil.isNotEmpty("\t"));
        assertFalse(ValidationUtil.isNotEmpty("\n"));
        assertTrue(ValidationUtil.isNotEmpty("  a  ")); // Has non-whitespace
    }
    
    @Test
    @DisplayName("Test phone number strips dashes and spaces only")
    public void testIsValidPhone_StrippingBehavior() {
        // Spaces and dashes are stripped - these work
        assertTrue(ValidationUtil.isValidPhone("123-456-7890"));
        assertTrue(ValidationUtil.isValidPhone("123 456 7890"));
        assertTrue(ValidationUtil.isValidPhone("1234567890"));
        
        // Parentheses are NOT stripped - this doesn't work
        assertFalse(ValidationUtil.isValidPhone("(123)4567890"));
    }
    
    @Test
    @DisplayName("Test phone number length validation")
    public void testIsValidPhone_LengthValidation() {
        // Too short (9 digits)
        assertFalse(ValidationUtil.isValidPhone("123456789"));
        
        // Minimum valid (10 digits)
        assertTrue(ValidationUtil.isValidPhone("1234567890"));
        
        // Maximum valid (15 digits)
        assertTrue(ValidationUtil.isValidPhone("123456789012345"));
        
        // Too long (16 digits)
        assertFalse(ValidationUtil.isValidPhone("1234567890123456"));
    }
}