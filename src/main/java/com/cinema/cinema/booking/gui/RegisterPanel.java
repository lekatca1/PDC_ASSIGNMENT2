/**
 *
 * @author xps1597
 */
package com.cinema.cinema.booking.gui;

import com.cinema.cinema.booking.models.Customer;
import com.cinema.cinema.booking.service.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterPanel extends JPanel {
    private JTextField usernameField, firstNameField, lastNameField, emailField, phoneField;
    private JPasswordField passwordField;
    private RoundedButton registerButton;
    private JLabel backToLoginLabel;

    private UserManager userManager;
    private PriceCalculator priceCalculator;
    private IncomeTracker incomeTracker;

    // Palette
    private static final Color BACKGROUND = new Color(12, 12, 14);
    private static final Color FIELD_BG = new Color(40, 40, 40);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);
    private static final Color ACCENT_RED = new Color(220, 0, 0);
    private static final Color ACCENT_RED_HOVER = new Color(255, 59, 48);

    public RegisterPanel(UserManager userManager, PriceCalculator priceCalculator, IncomeTracker incomeTracker) {
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
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Logo/Title
        JLabel logo = new JLabel("RCINEMAS", SwingConstants.CENTER);
        logo.setForeground(ACCENT_RED);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        add(logo, gbc);

        // Join us text
        JLabel joinText = new JLabel("Join us", SwingConstants.CENTER);
        joinText.setForeground(Color.WHITE);
        joinText.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(joinText, gbc);

        // Reset insets and gridwidth for form fields
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 0, 5, 15);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(createLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 15, 5, 0);
        usernameField = createTextField();
        add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 0, 5, 15);
        add(createLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 15, 5, 0);
        passwordField = createPasswordField();
        add(passwordField, gbc);

        // First Name
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        add(createLabel("First Name:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 15, 5, 0);
        firstNameField = createTextField();
        add(firstNameField, gbc);

        // Last Name
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        add(createLabel("Last Name:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 15, 5, 0);
        lastNameField = createTextField();
        add(lastNameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        add(createLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 15, 5, 0);
        emailField = createTextField();
        add(emailField, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.EAST;
        add(createLabel("Phone:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 15, 5, 0);
        phoneField = createTextField();
        add(phoneField, gbc);

        // Register button
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(25, 0, 15, 0);

        registerButton = new RoundedButton("CREATE ACCOUNT", 20, ACCENT_RED, ACCENT_RED_HOVER);
        registerButton.setPreferredSize(new Dimension(200, 42));
        add(registerButton, gbc);

        // Back to Sign In link
        gbc.gridy = 9;
        gbc.insets = new Insets(10, 0, 0, 0);
        JPanel linkPanel = new JPanel(new FlowLayout());
        linkPanel.setBackground(BACKGROUND);

        JLabel alreadyLabel = new JLabel("Already have an account? ");
        alreadyLabel.setForeground(Color.LIGHT_GRAY);
        alreadyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        linkPanel.add(alreadyLabel);

        backToLoginLabel = new JLabel("<HTML><U>Sign In</U></HTML>");
        backToLoginLabel.setForeground(ACCENT_RED);
        backToLoginLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backToLoginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkPanel.add(backToLoginLabel);

        add(linkPanel, gbc);

        setupEventHandlers();
    }

    private void setupEventHandlers() {
        // Register action
        registerButton.addActionListener(e -> {
            if (validateFields()) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();

                Customer customer = new Customer(username, password, email, firstName, lastName, phone);
                boolean success = userManager.registerCustomer(customer);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Account created successfully!\nWelcome to ROBS MOVIES!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    navigateToLogin();
                } else {
                    showErrorMessage("Username or email already exists!");
                }
            }
        });

        // Back to login action
        backToLoginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                navigateToLogin();
            }
        });
    }

    private void navigateToLogin() {
        SwingUtilities.getWindowAncestor(this).dispose();
        JFrame loginFrame = new JFrame("Cinema Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(450, 400);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.add(new LoginPanel(userManager, priceCalculator, incomeTracker));
        loginFrame.setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        styleInputField(field);
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        styleInputField(field);
        return field;
    }

    private void styleInputField(JTextField field) {
        field.setPreferredSize(new Dimension(200, 35));
        field.setBackground(FIELD_BG);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Focus effects
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ACCENT_RED, 2),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
        });
    }

    private boolean validateFields() {
        if (usernameField.getText().trim().isEmpty()) {
            showErrorMessage("Username is required!");
            usernameField.requestFocus();
            return false;
        }
        if (passwordField.getPassword().length == 0) {
            showErrorMessage("Password is required!");
            passwordField.requestFocus();
            return false;
        }
        if (firstNameField.getText().trim().isEmpty()) {
            showErrorMessage("First name is required!");
            firstNameField.requestFocus();
            return false;
        }
        if (lastNameField.getText().trim().isEmpty()) {
            showErrorMessage("Last name is required!");
            lastNameField.requestFocus();
            return false;
        }
        if (emailField.getText().trim().isEmpty()) {
            showErrorMessage("Email is required!");
            emailField.requestFocus();
            return false;
        }
        if (!isValidEmail(emailField.getText().trim())) {
            showErrorMessage("Please enter a valid email address!");
            emailField.requestFocus();
            return false;
        }
        if (phoneField.getText().trim().isEmpty()) {
            showErrorMessage("Phone number is required!");
            phoneField.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Registration Error", JOptionPane.ERROR_MESSAGE);
    }


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
