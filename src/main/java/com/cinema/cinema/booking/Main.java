package com.cinema.cinema.booking;

import com.cinema.cinema.booking.database.DatabaseConnection;
import com.cinema.cinema.booking.gui.LoginPanel;
import com.cinema.cinema.booking.service.*;

import javax.swing.*;

/**
 * Main application entry point
 * Demonstrates: MVC Pattern, Singleton Pattern usage
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Cinema Booking System - Project 2 ===");
        System.out.println("Initializing application...\n");
        
        // Initialize database (Singleton pattern)
        try {
            DatabaseConnection.getInstance();
            System.out.println("\n✓ System initialization complete\n");
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize database: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Failed to initialize database. Please check the logs.",
                "Startup Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Initialize services
        UserManager userManager = new UserManager();
        PriceCalculator priceCalculator = new PriceCalculator();
        IncomeTracker incomeTracker = new IncomeTracker();
        
        // Launch GUI
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Use default look and feel if system not available
            }
            
            JFrame loginFrame = new JFrame("Cinema Booking System - Login");
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setSize(400, 350);
            loginFrame.setLocationRelativeTo(null);
            
            // Create login panel with services
            LoginPanel loginPanel = new LoginPanel(userManager, priceCalculator, incomeTracker);
            loginFrame.add(loginPanel);
            
            loginFrame.setVisible(true);
            
            System.out.println("✓ GUI launched successfully");
            System.out.println("\nDefault Login Credentials:");
            System.out.println("Admin: username='admin', password='admin123'");
            System.out.println("Customer: username='rob', password='rob'");
        });
        
        // Add shutdown hook for graceful database shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down application...");
            DatabaseConnection.shutdown();
            System.out.println("✓ Application closed successfully");
        }));
    }
}