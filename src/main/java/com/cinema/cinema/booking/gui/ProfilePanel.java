package com.cinema.cinema.booking.gui;

import com.cinema.cinema.booking.models.Booking;
import com.cinema.cinema.booking.models.Customer;
import com.cinema.cinema.booking.service.IncomeTracker;
import com.cinema.cinema.booking.service.PriceCalculator;
import com.cinema.cinema.booking.service.UserManager;
import com.cinema.cinema.booking.dao.BookingDAO;
import com.cinema.cinema.booking.factory.DAOFactory;
import com.cinema.cinema.booking.exception.DatabaseException;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ProfilePanel extends JFrame {
    private UserManager userManager;
    private PriceCalculator priceCalculator;
    private IncomeTracker incomeTracker;
    private Customer customer;

    // Modern color palette
    private static final Color PRIMARY_DARK = new Color(12, 12, 14);
    private static final Color SECONDARY_DARK = new Color(22, 23, 25);
    private static final Color CARD_BACKGROUND = new Color(28, 29, 32);
    private static final Color ACCENT_RED = new Color(255, 59, 48);
    private static final Color ACCENT_RED_HOVER = new Color(255, 79, 68);
    private static final Color TEXT_PRIMARY = new Color(255, 255, 255);
    private static final Color TEXT_SECONDARY = new Color(174, 174, 178);
    private static final Color BORDER_COLOR = new Color(48, 49, 51);
    private static final Color BUTTON_GREY = new Color(60, 60, 60);
    private static final Color BUTTON_GREY_HOVER = new Color(80, 80, 80);
    private static final Color SUCCESS_GREEN = new Color(52, 199, 89);
    private static final Color WARNING_ORANGE = new Color(255, 149, 0);

    public ProfilePanel(UserManager userManager, PriceCalculator priceCalculator,
                        IncomeTracker incomeTracker, Customer customer) {
        this.userManager = userManager;
        this.priceCalculator = priceCalculator;
        this.incomeTracker = incomeTracker;
        this.customer = customer;

        setupFrame();
        createUI();
    }

    private void setupFrame() {
        setTitle("My Profile");
        setSize(1200, 800);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(PRIMARY_DARK);
        
        // Add window listener to refresh data when window is opened
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                refreshProfileData();
            }
        });
    }

    // Add this new method to refresh all data
    private void refreshProfileData() {
        // Remove old content
        getContentPane().removeAll();
        
        // Recreate UI with fresh data
        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
        
        // Refresh the display
        revalidate();
        repaint();
    }

    private void createUI() {
        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(SECONDARY_DARK);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("My Profile");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);

        RoundedButton backBtn = new RoundedButton("← Back", 18, BUTTON_GREY, BUTTON_GREY_HOVER);
        backBtn.setPreferredSize(new Dimension(100, 40));
        backBtn.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() ->
                    new CinemaBookingGUI(userManager, priceCalculator, incomeTracker, customer).setVisible(true)
            );
        });

        header.add(title, BorderLayout.WEST);
        header.add(backBtn, BorderLayout.EAST);
        return header;
    }

    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(PRIMARY_DARK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(PRIMARY_DARK);
        leftPanel.add(createProfileCard());
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(createStatsCard());

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(PRIMARY_DARK);
        rightPanel.add(createBookingsPanel(), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(380);
        splitPane.setBackground(PRIMARY_DARK);
        splitPane.setBorder(null);
        splitPane.setDividerSize(20);

        mainPanel.add(splitPane, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createProfileCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(new RoundedBorder(20, BORDER_COLOR));
        card.setMaximumSize(new Dimension(360, 300));

        JPanel padding = new JPanel();
        padding.setLayout(new BoxLayout(padding, BoxLayout.Y_AXIS));
        padding.setBackground(CARD_BACKGROUND);
        padding.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel avatarPanel = new JPanel(new BorderLayout());
        avatarPanel.setPreferredSize(new Dimension(80, 80));
        avatarPanel.setMaximumSize(new Dimension(80, 80));
        avatarPanel.setBackground(ACCENT_RED);
        avatarPanel.setBorder(new RoundedBorder(40, ACCENT_RED));
        avatarPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel initials = new JLabel(customer.getFirstName().substring(0, 1).toUpperCase() +
                customer.getLastName().substring(0, 1).toUpperCase());
        initials.setForeground(Color.WHITE);
        initials.setFont(new Font("Segoe UI", Font.BOLD, 32));
        initials.setHorizontalAlignment(SwingConstants.CENTER);
        avatarPanel.add(initials, BorderLayout.CENTER);

        padding.add(avatarPanel);
        padding.add(Box.createVerticalStrut(20));

        JLabel name = new JLabel(customer.getFirstName() + " " + customer.getLastName());
        name.setFont(new Font("Segoe UI", Font.BOLD, 24));
        name.setForeground(TEXT_PRIMARY);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        padding.add(name);
        padding.add(Box.createVerticalStrut(5));

        JLabel email = new JLabel(customer.getEmail());
        email.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        email.setForeground(TEXT_SECONDARY);
        email.setAlignmentX(Component.CENTER_ALIGNMENT);
        padding.add(email);
        padding.add(Box.createVerticalStrut(20));

        JLabel memberSince = new JLabel("Member since: " +
                (customer.getDateJoined() != null ?
                        customer.getDateJoined().format(DateTimeFormatter.ofPattern("MMM yyyy")) : "N/A"));
        memberSince.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        memberSince.setForeground(TEXT_SECONDARY);
        memberSince.setAlignmentX(Component.CENTER_ALIGNMENT);
        padding.add(memberSince);

        padding.add(Box.createVerticalStrut(25));
        RoundedButton editBtn = new RoundedButton("Edit Profile", 18, BUTTON_GREY, BUTTON_GREY_HOVER);
        editBtn.setPreferredSize(new Dimension(200, 40));
        editBtn.setMaximumSize(new Dimension(200, 40));
        editBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        editBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Profile editing will be available soon!", "Coming Soon", JOptionPane.INFORMATION_MESSAGE));
        padding.add(editBtn);

        card.add(padding);
        return card;
    }

    private JPanel createStatsCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(new RoundedBorder(20, BORDER_COLOR));
        card.setMaximumSize(new Dimension(360, 200));

        JPanel padding = new JPanel();
        padding.setLayout(new BoxLayout(padding, BoxLayout.Y_AXIS));
        padding.setBackground(CARD_BACKGROUND);
        padding.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel statsTitle = new JLabel("Quick Stats");
        statsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        statsTitle.setForeground(TEXT_PRIMARY);
        statsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        padding.add(statsTitle);
        padding.add(Box.createVerticalStrut(15));

        List<Booking> bookings = getCustomerBookings();
        int totalBookings = bookings.size();
        double totalSpent = bookings.stream().mapToDouble(Booking::getTotalPrice).sum();
        long confirmedBookings = bookings.stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.CONFIRMED).count();

        padding.add(createStatRow("Total Bookings", String.valueOf(totalBookings)));
        padding.add(Box.createVerticalStrut(10));
        padding.add(createStatRow("Total Spent", "$" + String.format("%.2f", totalSpent)));
        padding.add(Box.createVerticalStrut(10));
        padding.add(createStatRow("Confirmed", String.valueOf(confirmedBookings)));

        card.add(padding);
        return card;
    }

    private JPanel createStatRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(CARD_BACKGROUND);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelText.setForeground(TEXT_SECONDARY);

        JLabel valueText = new JLabel(value);
        valueText.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueText.setForeground(TEXT_PRIMARY);

        row.add(labelText, BorderLayout.WEST);
        row.add(valueText, BorderLayout.EAST);
        return row;
    }

    private JPanel createBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_DARK);

        JLabel title = new JLabel("My Bookings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel bookingsContainer = new JPanel();
        bookingsContainer.setLayout(new BoxLayout(bookingsContainer, BoxLayout.Y_AXIS));
        bookingsContainer.setBackground(PRIMARY_DARK);

        List<Booking> bookings = getCustomerBookings();

        if (bookings.isEmpty()) {
            JLabel noBookings = new JLabel("No bookings yet");
            noBookings.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            noBookings.setForeground(TEXT_SECONDARY);
            noBookings.setAlignmentX(Component.CENTER_ALIGNMENT);
            bookingsContainer.add(Box.createVerticalStrut(50));
            bookingsContainer.add(noBookings);
        } else {
            for (Booking booking : bookings) {
                bookingsContainer.add(createBookingCard(booking));
                bookingsContainer.add(Box.createVerticalStrut(15));
            }
        }

        JScrollPane scrollPane = new JScrollPane(bookingsContainer);
        scrollPane.setBorder(null);
        scrollPane.setBackground(PRIMARY_DARK);
        scrollPane.getViewport().setBackground(PRIMARY_DARK);

        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBookingCard(Booking booking) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(new RoundedBorder(16, BORDER_COLOR));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        JPanel padding = new JPanel(new BorderLayout(15, 10));
        padding.setBackground(CARD_BACKGROUND);
        padding.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(CARD_BACKGROUND);

        JLabel movieTitle = new JLabel(getMovieTitleForBooking(booking));
        movieTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        movieTitle.setForeground(TEXT_PRIMARY);

        JLabel bookingDate = new JLabel("Booked: " +
                booking.getBookingDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));
        bookingDate.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bookingDate.setForeground(TEXT_SECONDARY);

        JLabel seatsInfo = new JLabel("Seats: " + booking.getNumberOfSeats() +
                " | Total: $" + String.format("%.2f", booking.getTotalPrice()));
        seatsInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        seatsInfo.setForeground(TEXT_SECONDARY);

        leftPanel.add(movieTitle);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(bookingDate);
        leftPanel.add(Box.createVerticalStrut(3));
        leftPanel.add(seatsInfo);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(CARD_BACKGROUND);

        JLabel status = new JLabel(booking.getStatus().toString());
        status.setFont(new Font("Segoe UI", Font.BOLD, 13));
        status.setForeground(getStatusColor(booking.getStatus()));
        status.setAlignmentX(Component.RIGHT_ALIGNMENT);

        RoundedButton viewBtn = new RoundedButton("View Details", 12, BUTTON_GREY, BUTTON_GREY_HOVER);
        viewBtn.setPreferredSize(new Dimension(110, 32));
        viewBtn.setMaximumSize(new Dimension(110, 32));
        viewBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);
        viewBtn.addActionListener(e -> showBookingDetails(booking));

        rightPanel.add(status);
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(viewBtn);

        padding.add(leftPanel, BorderLayout.CENTER);
        padding.add(rightPanel, BorderLayout.EAST);

        card.add(padding, BorderLayout.CENTER);
        return card;
    }

    private Color getStatusColor(Booking.BookingStatus status) {
        switch (status) {
            case CONFIRMED:
                return SUCCESS_GREEN;
            case PENDING:
                return WARNING_ORANGE;
            case CANCELLED:
            case EXPIRED:
            default:
                return TEXT_SECONDARY;
        }
    }

    private String getMovieTitleForBooking(Booking booking) {
        String[] movies = {"Roofman", "One Battle After Another", "Regretting You", "Chainsaw Man"};
        int index = booking.getShowtimeId() % movies.length;
        return movies[index];
    }

    private void showBookingDetails(Booking booking) {
        StringBuilder details = new StringBuilder();
        details.append("Booking ID: ").append(booking.getBookingId()).append("\n");
        details.append("Movie: ").append(getMovieTitleForBooking(booking)).append("\n");
        details.append("Date: ").append(booking.getBookingDate()
                .format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"))).append("\n");
        details.append("Seats: ").append(booking.getNumberOfSeats()).append("\n");
        details.append("Total Price: $").append(String.format("%.2f", booking.getTotalPrice())).append("\n");
        details.append("Status: ").append(booking.getStatus()).append("\n");
        if (booking.getPaymentMethod() != null) {
            details.append("Payment Method: ").append(booking.getPaymentMethod()).append("\n");
        }
        if (booking.getTransactionId() != null) {
            details.append("Transaction ID: ").append(booking.getTransactionId()).append("\n");
        }

        JOptionPane.showMessageDialog(this, details.toString(),
                "Booking Details", JOptionPane.INFORMATION_MESSAGE);
    }

    // --- Database-backed booking fetch ---
    private List<Booking> getCustomerBookings() {
        try {
            BookingDAO bookingDAO = DAOFactory.getBookingDAO();
            return bookingDAO.getBookingsByCustomerId(customer.getUserId());
        } catch (DatabaseException e) {
            System.err.println("Error loading bookings: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Error loading bookings: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    // --- Custom UI helpers ---
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
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { repaint(); }
                @Override public void mouseExited(MouseEvent e) { repaint(); }
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

    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color borderColor;

        public RoundedBorder(int radius, Color borderColor) {
            this.radius = radius;
            this.borderColor = borderColor;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(borderColor);
            g2.draw(new RoundRectangle2D.Float(x, y, width - 1, height - 1, radius, radius));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(1, 1, 1, 1);
        }
    }
}