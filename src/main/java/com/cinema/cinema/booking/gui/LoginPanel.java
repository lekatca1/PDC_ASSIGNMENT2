/**
 *
 * @author xps1597
 */
package com.cinema.cinema.booking.gui;

import com.cinema.cinema.booking.models.Admin;
import com.cinema.cinema.booking.models.Customer;
import com.cinema.cinema.booking.service.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginPanel extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private RoundedButton signInButton;
    private JLabel joinLabel;

    private final UserManager userManager;
    private final PriceCalculator priceCalculator;
    private final IncomeTracker incomeTracker;

    // Color palette
    private static final Color BACKGROUND = new Color(12, 12, 14);
    private static final Color FIELD_BG = new Color(40, 40, 40);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);
    private static final Color ACCENT_RED = new Color(220, 0, 0);
    private static final Color ACCENT_RED_HOVER = new Color(255, 59, 48);

    public LoginPanel(UserManager userManager, PriceCalculator priceCalculator, IncomeTracker incomeTracker) {
        this.userManager = userManager;
        this.priceCalculator = priceCalculator;
        this.incomeTracker = incomeTracker;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Logo
        JLabel logo = new JLabel("RCinemas", SwingConstants.CENTER);
        logo.setForeground(ACCENT_RED);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(logo, gbc);

        // Sign in text
        JLabel signInText = new JLabel("Sign in", SwingConstants.CENTER);
        signInText.setForeground(Color.WHITE);
        signInText.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 20, 30, 20);
        add(signInText, gbc);

        // Email field
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 20, 5, 20);
        emailField = createTextField("Enter your email");
        add(emailField, gbc);

        // Password field
        gbc.gridy = 3;
        passwordField = createPasswordField("Enter your password");
        add(passwordField, gbc);

        // Sign in button
        gbc.gridy = 4;
        gbc.insets = new Insets(25, 20, 15, 20);
        signInButton = new RoundedButton("SIGN IN", 20, ACCENT_RED, ACCENT_RED_HOVER);
        signInButton.setPreferredSize(new Dimension(200, 42));
        add(signInButton, gbc);

        // Join Now link
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 20, 0, 20);
        JPanel linkPanel = new JPanel(new FlowLayout());
        linkPanel.setBackground(BACKGROUND);

        JLabel newUserLabel = new JLabel("New to ROBS MOVIES? ");
        newUserLabel.setForeground(Color.LIGHT_GRAY);
        newUserLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        linkPanel.add(newUserLabel);

        joinLabel = new JLabel("<HTML><U>Join Now</U></HTML>");
        joinLabel.setForeground(ACCENT_RED);
        joinLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        joinLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkPanel.add(joinLabel);

        add(linkPanel, gbc);

        setupEventHandlers();
    }

    private void setupEventHandlers() {
        // Sign in action
        signInButton.addActionListener((ActionEvent e) -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                showErrorMessage("Please enter both email and password!");
                return;
            }

            Object user = userManager.login(email, password);
            if (user != null) {
                showSuccessMessage("Welcome back!");

                if (user instanceof Admin) {
                    onSuccessfulAdminLogin();
                } else if (user instanceof Customer) {
                    onSuccessfulCustomerLogin((Customer) user);
                }
            } else {
                showErrorMessage("Invalid email or password. Please try again.");
            }
        });

        // Join Now action
        joinLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                navigateToRegister();
            }
        });

        // Enter key support
        emailField.addActionListener(e -> passwordField.requestFocus());
        passwordField.addActionListener(e -> signInButton.doClick());
    }

    private void onSuccessfulCustomerLogin(Customer customer) {
        Window loginWindow = SwingUtilities.getWindowAncestor(this);
        if (loginWindow != null) {
            loginWindow.dispose();
        }

        SwingUtilities.invokeLater(() -> {
            CinemaBookingGUI customerDashboard =
                new CinemaBookingGUI(userManager, priceCalculator, incomeTracker, customer);
            customerDashboard.setVisible(true);
        });
    }

    private void onSuccessfulAdminLogin() {
        // Do NOT close login window, keep it open
        SwingUtilities.invokeLater(() -> {
            AdminPanel adminPanel = new AdminPanel(incomeTracker);
            adminPanel.setVisible(true); // Opens in its own window
        });
    }

    private void navigateToRegister() {
        SwingUtilities.getWindowAncestor(this).dispose();
        JFrame registerFrame = new JFrame("Register - ROBS MOVIES");
        registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registerFrame.setSize(500, 600);
        registerFrame.setLocationRelativeTo(null);

        registerFrame.add(new RegisterPanel(userManager, priceCalculator, incomeTracker));
        registerFrame.setVisible(true);
    }

    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(280, 35));
        field.setBackground(FIELD_BG);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        field.setText(placeholder);
        field.setForeground(Color.GRAY);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.WHITE);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_RED, 2),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }
        });

        return field;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setPreferredSize(new Dimension(280, 35));
        field.setBackground(FIELD_BG);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_RED, 2),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }
        });

        return field;
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Error", JOptionPane.ERROR_MESSAGE);
    }

    /** -----------------------------
     *  RoundedButton class
     *  ----------------------------- */
    private static class RoundedButton extends JButton {
        private final int radius;
        private final Color normalColor;
        private final Color hoverColor;

        public RoundedButton(String text, int radius, Color normalColor, Color hoverColor) {
            super(text);
            this.radius = radius;
            this.normalColor = normalColor;
            this.hoverColor = hoverColor;

            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) { repaint(); }
                @Override
                public void mouseExited(MouseEvent e) { repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color bg = getModel().isRollover() ? hoverColor : normalColor;
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            FontMetrics fm = g2.getFontMetrics();
            int textX = (getWidth() - fm.stringWidth(getText())) / 2;
            int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.setColor(getForeground());
            g2.drawString(getText(), textX, textY);
            g2.dispose();
        }
    }
}
