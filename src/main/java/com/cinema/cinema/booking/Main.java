/**
 *
 * @author xps1597
 */
package com.cinema.cinema.booking;

import com.cinema.cinema.booking.gui.CinemaBookingGUI;
import com.cinema.cinema.booking.gui.LoginPanel;
import com.cinema.cinema.booking.service.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        PriceCalculator priceCalculator = new PriceCalculator();
        IncomeTracker incomeTracker = new IncomeTracker();

        SwingUtilities.invokeLater(() -> {
            JFrame loginFrame = new JFrame("Cinema Login");
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setSize(400, 350);
            loginFrame.setLocationRelativeTo(null);

            // Pass services into login panel
            LoginPanel loginPanel = new LoginPanel(userManager, priceCalculator, incomeTracker);
            loginFrame.add(loginPanel);

            loginFrame.setVisible(true);
        });
    }
}

