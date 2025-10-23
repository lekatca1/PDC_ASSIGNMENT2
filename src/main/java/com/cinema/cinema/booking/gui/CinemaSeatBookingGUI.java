/**
 *
 * @author xps1597
 */
package com.cinema.cinema.booking.gui;

import com.cinema.cinema.booking.models.Customer;
import com.cinema.cinema.booking.models.Booking;
import com.cinema.cinema.booking.models.Seat;
import com.cinema.cinema.booking.service.*;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CinemaSeatBookingGUI extends JFrame {
    private UserManager userManager;
    private PriceCalculator priceCalculator;
    private IncomeTracker incomeTracker;
    private Customer customer;
    private String movieTitle;
    private ShowTime selectedShowTime;
    
    // RCINEMAS brand colors
    private static final Color RC_RED = new Color(139, 0, 0);
    private static final Color RC_RED_DARK = new Color(139, 0, 0);
    private static final Color RC_BLACK = new Color(0, 0, 0);
    private static final Color RC_DARK_GRAY = new Color(25, 25, 25);
    private static final Color RC_GRAY = new Color(45, 45, 45);
    private static final Color RC_LIGHT_GRAY = new Color(65, 65, 65);
    private static final Color RC_WHITE = new Color(255, 255, 255);
    private static final Color RC_SILVER = new Color(192, 192, 192);
    
    // UI Colors
    private static final Color BACKGROUND_PRIMARY = RC_BLACK;
    private static final Color BACKGROUND_SECONDARY = RC_DARK_GRAY;
    private static final Color CARD_BACKGROUND = RC_GRAY;
    private static final Color TEXT_PRIMARY = RC_WHITE;
    private static final Color TEXT_SECONDARY = new Color(200, 200, 200);
    private static final Color TEXT_MUTED = new Color(150, 150, 150);
    private static final Color BORDER_COLOR = RC_LIGHT_GRAY;
    
    // Seat colors
    private static final Color SEAT_AVAILABLE = RC_LIGHT_GRAY;
    private static final Color SEAT_AVAILABLE_HOVER = new Color(100, 100, 100);
    private static final Color SEAT_RESERVED = new Color(150, 150, 150);
    private static final Color SEAT_SELECTED = RC_RED;
    private static final Color SEAT_SELECTED_HOVER = RC_RED_DARK;
    private static final Color SEAT_UNAVAILABLE = new Color(35, 35, 35);
    
    private List<SeatButton> selectedSeats;
    private JLabel selectedSeatsLabel;
    private JLabel totalPriceLabel;
    private JLabel selectedTimeLabel;
    private double baseSeatPrice = 15.00;
    private List<ShowTime> availableShowTimes;
    private JPanel showtimeScrollPanel;
    private int currentShowtimeIndex = 0;
    
    public CinemaSeatBookingGUI(UserManager userManager, PriceCalculator priceCalculator, 
                               IncomeTracker incomeTracker, Customer customer, String movieTitle) {
        this.userManager = userManager;
        this.priceCalculator = priceCalculator;
        this.incomeTracker = incomeTracker;
        this.customer = customer;
        this.movieTitle = movieTitle != null ? movieTitle : "Selected Movie";
        this.selectedSeats = new ArrayList<>();
        this.availableShowTimes = generateShowTimes();
        
        setupFrame();
        createBookingUI();
    }
    
    public CinemaSeatBookingGUI(UserManager userManager, PriceCalculator priceCalculator, 
                               IncomeTracker incomeTracker, Customer customer) {
        this(userManager, priceCalculator, incomeTracker, customer, "Selected Movie");
    }
    
    private List<ShowTime> generateShowTimes() {
        List<ShowTime> showTimes = new ArrayList<>();
        String[] times = {"10:00 AM", "12:30 PM", "3:00 PM", "5:30 PM", "8:00 PM", "10:30 PM", "11:59 PM"};
        String[] formats = {"RECLINED", "XTREME SCREEN", "RECLINED", "RECLINED", "RECLINED", "RECLINED", "LATE NIGHT"};
        double[] prices = {25.00, 28.00, 19.50, 22.00, 25.00, 19.50, 16.50};
        
        for (int i = 0; i < times.length; i++) {
            showTimes.add(new ShowTime(i + 1, times[i], formats[i], prices[i]));
        }
        return showTimes;
    }
    
    private void setupFrame() {
        setTitle("RCINEMAS - " + movieTitle);
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_PRIMARY);
        
        // Add RCINEMAS icon
        try {
            Image icon = createRCinemasIcon();
            if (icon != null) {
                setIconImage(icon);
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not create window icon: " + e.getMessage());
        }
    }
    
    private Image createRCinemasIcon() {
        try {
            int size = 32;
            java.awt.image.BufferedImage icon = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = icon.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw RCINEMAS blue background
            g.setColor(RC_RED);
            g.fillRoundRect(0, 0, size, size, 8, 8);
            
            // Draw RC for RCINEMAS
            g.setColor(TEXT_PRIMARY);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fm = g.getFontMetrics();
            String text = "RC";
            int x = (size - fm.stringWidth(text)) / 2;
            int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
            g.drawString(text, x, y);
            
            g.dispose();
            return icon;
        } catch (Exception e) {
            System.err.println("Error creating RCINEMAS icon: " + e.getMessage());
            return null;
        }
    }
    
    private void createBookingUI() {
        setLayout(new BorderLayout(0, 0));
        
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(BACKGROUND_PRIMARY);
        
        // RCINEMAS header with movie info and compact showtime selection
        JPanel headerPanel = createRCinemasHeader();
        mainContent.add(headerPanel, BorderLayout.NORTH);
        
        // Seat selection area
        JPanel seatPanel = createSeatSelectionPanel();
        mainContent.add(seatPanel, BorderLayout.CENTER);
        
        // Booking summary and controls
        JPanel bottomPanel = createBottomPanel();
        mainContent.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainContent, BorderLayout.CENTER);
    }
    
    private JPanel createRCinemasHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_SECONDARY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 35, 20, 35));
        
        // Top section with RCINEMAS logo and navigation
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BACKGROUND_SECONDARY);
        
        // RCINEMAS logo
        JLabel rcinemasLogo = new JLabel("RCINEMAS");
        rcinemasLogo.setFont(new Font("Arial", Font.BOLD, 28));
        rcinemasLogo.setForeground(RC_RED);
        
        // Back button
        JButton backBtn = new JButton("← Back");
        styleRCSecondaryButton(backBtn);
        backBtn.setPreferredSize(new Dimension(100, 35));
        backBtn.addActionListener(e -> {
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                CinemaBookingGUI dashboard = new CinemaBookingGUI(userManager, priceCalculator, incomeTracker, customer);
                dashboard.setVisible(true);
            });
        });
        
        topBar.add(rcinemasLogo, BorderLayout.WEST);
        topBar.add(backBtn, BorderLayout.EAST);
        
        // Movie info section
        JPanel movieInfoPanel = new JPanel(new BorderLayout());
        movieInfoPanel.setBackground(BACKGROUND_SECONDARY);
        movieInfoPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Movie details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(BACKGROUND_SECONDARY);
        
        JLabel titleLabel = new JLabel(movieTitle);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel ratingLabel = new JLabel("M • 125 min • Action, Adventure");
        ratingLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        ratingLabel.setForeground(TEXT_SECONDARY);
        ratingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        detailsPanel.add(titleLabel);
        detailsPanel.add(Box.createVerticalStrut(5));
        detailsPanel.add(ratingLabel);
        
        movieInfoPanel.add(detailsPanel, BorderLayout.WEST);
        
        // Compact horizontal showtime selector
        JPanel showtimeSelector = createCompactShowtimeSelector();
        movieInfoPanel.add(showtimeSelector, BorderLayout.CENTER);
        
        // Assemble header
        JPanel fullHeader = new JPanel();
        fullHeader.setLayout(new BoxLayout(fullHeader, BoxLayout.Y_AXIS));
        fullHeader.setBackground(BACKGROUND_SECONDARY);
        fullHeader.add(topBar);
        fullHeader.add(movieInfoPanel);
        
        headerPanel.add(fullHeader, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createCompactShowtimeSelector() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_SECONDARY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Title
        JLabel titleLabel = new JLabel("SESSION TIMES");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(TEXT_SECONDARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        
        // Initialize selectedTimeLabel first
        selectedTimeLabel = new JLabel("");
        selectedTimeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        selectedTimeLabel.setForeground(RC_WHITE);
        selectedTimeLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        // Scrollable showtime container
        JPanel scrollContainer = new JPanel(new BorderLayout());
        scrollContainer.setBackground(BACKGROUND_SECONDARY);
        
        // Left arrow button
        JButton leftArrow = new JButton("<");
        styleArrowButton(leftArrow);
        leftArrow.setPreferredSize(new Dimension(40, 60));
        
        // Right arrow button
        JButton rightArrow = new JButton(">");
        styleArrowButton(rightArrow);
        rightArrow.setPreferredSize(new Dimension(40, 60));
        
        // Showtime cards panel
        showtimeScrollPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        showtimeScrollPanel.setBackground(BACKGROUND_SECONDARY);
        
        ButtonGroup timeGroup = new ButtonGroup();
        
        // Create showtime cards (show only 4 at a time)
        updateVisibleShowtimes(timeGroup);
        
        leftArrow.addActionListener(e -> {
            if (currentShowtimeIndex > 0) {
                currentShowtimeIndex--;
                updateVisibleShowtimes(timeGroup);
            }
        });
        
        rightArrow.addActionListener(e -> {
            if (currentShowtimeIndex < availableShowTimes.size() - 4) {
                currentShowtimeIndex++;
                updateVisibleShowtimes(timeGroup);
            }
        });
        
        scrollContainer.add(leftArrow, BorderLayout.WEST);
        scrollContainer.add(showtimeScrollPanel, BorderLayout.CENTER);
        scrollContainer.add(rightArrow, BorderLayout.EAST);
        
        JPanel fullPanel = new JPanel();
        fullPanel.setLayout(new BoxLayout(fullPanel, BoxLayout.Y_AXIS));
        fullPanel.setBackground(BACKGROUND_SECONDARY);
        fullPanel.add(titleLabel);
        fullPanel.add(scrollContainer);
        fullPanel.add(selectedTimeLabel);
        
        mainPanel.add(fullPanel, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private void updateVisibleShowtimes(ButtonGroup timeGroup) {
        showtimeScrollPanel.removeAll();
        
        int endIndex = Math.min(currentShowtimeIndex + 4, availableShowTimes.size());
        for (int i = currentShowtimeIndex; i < endIndex; i++) {
            ShowTime showTime = availableShowTimes.get(i);
            JToggleButton timeButton = createCompactShowtimeButton(showTime);
            timeGroup.add(timeButton);
            showtimeScrollPanel.add(timeButton);
            
            // Select first showtime by default
            if (i == currentShowtimeIndex && selectedShowTime == null) {
                timeButton.setSelected(true);
                selectedShowTime = showTime;
                updateSeatPricing();
                selectedTimeLabel.setText("Selected: " + showTime.getTime() + " - " + showTime.getFormat());
            }
        }
        
        showtimeScrollPanel.revalidate();
        showtimeScrollPanel.repaint();
    }
    
    private JToggleButton createCompactShowtimeButton(ShowTime showTime) {
        JToggleButton button = new JToggleButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(150, 60));
        button.setBackground(CARD_BACKGROUND);
        button.setBorder(new RoundedBorder(8, BORDER_COLOR, 1));
        button.setFocusPainted(false);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(CARD_BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        
        JLabel timeLabel = new JLabel(showTime.getTime());
        timeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        timeLabel.setForeground(TEXT_PRIMARY);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel formatLabel = new JLabel(showTime.getFormat());
        formatLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        formatLabel.setForeground(showTime.getFormat().contains("PREMIUM") ? RC_SILVER : TEXT_SECONDARY);
        formatLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel priceLabel = new JLabel("$" + String.format("%.2f", showTime.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 12));
        priceLabel.setForeground(RC_RED);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(timeLabel);
        contentPanel.add(formatLabel);
        contentPanel.add(Box.createVerticalStrut(2));
        contentPanel.add(priceLabel);
        
        button.add(contentPanel, BorderLayout.CENTER);
        
        button.addActionListener(e -> {
            selectedShowTime = showTime;
            selectedTimeLabel.setText("Selected: " + showTime.getTime() + " - " + showTime.getFormat());
            updateSeatPricing();
            updateBookingSummary();
            
            // Update button styling
            for (Component comp : showtimeScrollPanel.getComponents()) {
                if (comp instanceof JToggleButton) {
                    JToggleButton btn = (JToggleButton) comp;
                    JPanel panel = (JPanel) btn.getComponent(0);
                    if (btn.isSelected()) {
                        btn.setBackground(RC_RED);
                        panel.setBackground(RC_RED);
                        btn.setBorder(new RoundedBorder(8, RC_RED, 2));
                    } else {
                        btn.setBackground(CARD_BACKGROUND);
                        panel.setBackground(CARD_BACKGROUND);
                        btn.setBorder(new RoundedBorder(8, BORDER_COLOR, 1));
                    }
                }
            }
        });
        
        return button;
    }
    
    private void styleArrowButton(JButton button) {
        button.setBackground(RC_GRAY);
        button.setForeground(TEXT_PRIMARY);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(RC_RED);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(RC_GRAY);
            }
        });
    }
    
    private void updateSeatPricing() {
        if (selectedShowTime != null) {
            baseSeatPrice = selectedShowTime.getPrice();
        }
    }
    
    private JPanel createSeatSelectionPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_PRIMARY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 35, 20, 35));
        
        JLabel titleLabel = new JLabel("SELECT YOUR SEATS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Seat grid
        JPanel seatGridPanel = createSeatGrid();
        
        // Legend
        JPanel legendPanel = createLegend();
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BACKGROUND_PRIMARY);
        
        contentPanel.add(titleLabel);
        contentPanel.add(seatGridPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(legendPanel);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private JPanel createSeatGrid() {
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new BoxLayout(gridPanel, BoxLayout.Y_AXIS));
        gridPanel.setBackground(BACKGROUND_PRIMARY);
        
        // Screen
        JPanel screenPanel = new JPanel(new BorderLayout());
        screenPanel.setBackground(BACKGROUND_PRIMARY);
        screenPanel.setPreferredSize(new Dimension(0, 50));
        
        JPanel screenContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        screenContainer.setBackground(BACKGROUND_PRIMARY);
        
        JLabel screenLabel = new JLabel("SCREEN");
        screenLabel.setFont(new Font("Arial", Font.BOLD, 12));
        screenLabel.setForeground(TEXT_PRIMARY);
        screenLabel.setHorizontalAlignment(SwingConstants.CENTER);
        screenLabel.setOpaque(true);
        screenLabel.setBackground(new Color(100, 100, 100));
        screenLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        screenLabel.setPreferredSize(new Dimension(400, 30));
        
        screenContainer.add(screenLabel);
        screenPanel.add(screenContainer, BorderLayout.CENTER);
        
        gridPanel.add(screenPanel);
        gridPanel.add(Box.createVerticalStrut(25));
        
        // Seat rows - all standard seats now
        String[] rowLabels = {"A", "B", "C", "D", "E", "F", "G", "H", "J", "K"};
        int[] seatsPerRow = {12, 14, 16, 16, 18, 18, 18, 18, 20, 20};
        
        for (int rowIndex = 0; rowIndex < rowLabels.length; rowIndex++) {
            JPanel rowPanel = createSeatRow(rowLabels[rowIndex], seatsPerRow[rowIndex], rowIndex);
            gridPanel.add(rowPanel);
            gridPanel.add(Box.createVerticalStrut(6));
        }
        
        return gridPanel;
    }
    
    private JPanel createSeatRow(String rowLabel, int seatCount, int rowIndex) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 0));
        rowPanel.setBackground(BACKGROUND_PRIMARY);
        
        // Row label
        JLabel label = new JLabel(rowLabel);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(TEXT_SECONDARY);
        label.setPreferredSize(new Dimension(25, 30));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        rowPanel.add(label);
        
        rowPanel.add(Box.createHorizontalStrut(15));
        
        // Create seats
        for (int seatNum = 1; seatNum <= seatCount; seatNum++) {
            SeatButton seat = createSeat(rowLabel + seatNum, rowIndex, seatNum);
            rowPanel.add(seat);
            
            // Aisle spacing
            if (seatNum == seatCount / 2) {
                rowPanel.add(Box.createHorizontalStrut(25));
            }
        }
        
        return rowPanel;
    }
    
    private SeatButton createSeat(String seatId, int rowIndex, int seatNum) {
        SeatButton seat = new SeatButton(seatId);
        seat.setPreferredSize(new Dimension(30, 30));
        seat.setFont(new Font("Arial", Font.PLAIN, 10));
        seat.setFocusPainted(false);
        seat.setBorderPainted(false);
        
        // All seats are standard now
        if ((rowIndex <= 1) && (seatNum <= 2 || seatNum >= 11)) {
            seat.setSeatType(SeatType.WHEELCHAIR);
        } else {
            seat.setSeatType(SeatType.STANDARD);
        }
        
        // Seat availability simulation
        double random = Math.random();
        if (random < 0.65) {
            seat.setSeatState(SeatState.AVAILABLE);
        } else if (random < 0.85) {
            seat.setSeatState(SeatState.RESERVED);
        } else {
            seat.setSeatState(SeatState.UNAVAILABLE);
        }
        
        updateSeatAppearance(seat);
        
        seat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (seat.getSeatState() == SeatState.AVAILABLE) {
                    seat.setBackground(SEAT_AVAILABLE_HOVER);
                } else if (seat.getSeatState() == SeatState.SELECTED) {
                    seat.setBackground(SEAT_SELECTED_HOVER);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                updateSeatAppearance(seat);
            }
        });
        
        seat.addActionListener(e -> handleSeatClick(seat));
        
        return seat;
    }
    
    private void handleSeatClick(SeatButton seat) {
        if (selectedShowTime == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select a session time first.", 
                "No Session Selected", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (seat.getSeatState() != SeatState.AVAILABLE && seat.getSeatState() != SeatState.SELECTED) {
            return;
        }
        
        if (seat.getSeatState() == SeatState.SELECTED) {
            seat.setSeatState(SeatState.AVAILABLE);
            selectedSeats.remove(seat);
        } else {
            if (selectedSeats.size() >= 10) {
                JOptionPane.showMessageDialog(this, 
                    "Maximum 10 seats can be selected per booking.", 
                    "Seat Limit", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            seat.setSeatState(SeatState.SELECTED);
            selectedSeats.add(seat);
        }
        
        updateSeatAppearance(seat);
        updateBookingSummary();
    }
    
    private void updateSeatAppearance(SeatButton seat) {
        switch (seat.getSeatState()) {
            case AVAILABLE:
                seat.setBackground(SEAT_AVAILABLE);
                seat.setForeground(TEXT_PRIMARY);
                break;
            case RESERVED:
                seat.setBackground(SEAT_RESERVED);
                seat.setForeground(TEXT_MUTED);
                break;
            case SELECTED:
                seat.setBackground(SEAT_SELECTED);
                seat.setForeground(TEXT_PRIMARY);
                break;
            case UNAVAILABLE:
                seat.setBackground(SEAT_UNAVAILABLE);
                seat.setForeground(TEXT_MUTED);
                break;
        }
        seat.repaint();
    }
    
    private JPanel createLegend() {
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        legendPanel.setBackground(BACKGROUND_PRIMARY);
        legendPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        legendPanel.add(createLegendItem("Available", SEAT_AVAILABLE));
        legendPanel.add(createLegendItem("Selected", SEAT_SELECTED));
        legendPanel.add(createLegendItem("Sold", SEAT_RESERVED));
        legendPanel.add(createLegendItem("Unavailable", SEAT_UNAVAILABLE));
        
        return legendPanel;
    }
    
    private JPanel createLegendItem(String text, Color seatColor) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        item.setBackground(BACKGROUND_PRIMARY);
        
        JLabel colorBox = new JLabel();
        colorBox.setOpaque(true);
        colorBox.setBackground(seatColor);
        colorBox.setPreferredSize(new Dimension(18, 18));
        colorBox.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        
        JLabel textLabel = new JLabel(text);
        textLabel.setForeground(TEXT_SECONDARY);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        
        item.add(colorBox);
        item.add(textLabel);
        
        return item;
    }
    
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(RC_RED);
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, RC_RED_DARK),
            BorderFactory.createEmptyBorder(20, 35, 20, 35)
        ));
        
        // Booking summary
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBackground(RC_RED);
        
        selectedSeatsLabel = new JLabel("Selected Seats: None");
        selectedSeatsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        selectedSeatsLabel.setForeground(TEXT_PRIMARY);
        selectedSeatsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        totalPriceLabel = new JLabel("Total: $0.00");
        totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalPriceLabel.setForeground(TEXT_PRIMARY);
        totalPriceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        summaryPanel.add(selectedSeatsLabel);
        summaryPanel.add(Box.createVerticalStrut(6));
        summaryPanel.add(totalPriceLabel);
        
        // Book button
        JButton bookButton = new JButton("PROCEED TO PAYMENT");
        styleRCPrimaryButton(bookButton);
        bookButton.setPreferredSize(new Dimension(180, 45));
        bookButton.setEnabled(false);
        
        bookButton.addActionListener(e -> handleBooking());
        
        bottomPanel.add(summaryPanel, BorderLayout.WEST);
        bottomPanel.add(bookButton, BorderLayout.EAST);
        
        return bottomPanel;
    }
    
    private void updateBookingSummary() {
        JButton bookButton = findBookButton();
        
        if (selectedSeats.isEmpty()) {
            selectedSeatsLabel.setText("Selected Seats: None");
            totalPriceLabel.setText("Total: $0.00");
            if (bookButton != null) {
                bookButton.setEnabled(false);
                bookButton.setBackground(RC_GRAY);
                bookButton.setForeground(TEXT_MUTED);
            }
        } else {
            StringBuilder seatList = new StringBuilder();
            double total = 0;
            for (int i = 0; i < selectedSeats.size(); i++) {
                SeatButton seat = selectedSeats.get(i);
                seatList.append(seat.getSeatId());
                if (i < selectedSeats.size() - 1) {
                    seatList.append(", ");
                }
                // All seats use base price now (no LUX surcharge)
                total += baseSeatPrice;
            }
            
            selectedSeatsLabel.setText("Selected Seats: " + seatList.toString());
            totalPriceLabel.setText("Total: $" + String.format("%.2f", total));
            
            if (bookButton != null && selectedShowTime != null) {
                bookButton.setEnabled(true);
                styleRCPrimaryButton(bookButton);
            }
        }
    }
    
    private JButton findBookButton() {
        Container parent = getContentPane();
        return findButtonInContainer(parent, "PROCEED TO PAYMENT");
    }
    
    private JButton findButtonInContainer(Container container, String text) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton && ((JButton) comp).getText().equals(text)) {
                return (JButton) comp;
            } else if (comp instanceof Container) {
                JButton found = findButtonInContainer((Container) comp, text);
                if (found != null) return found;
            }
        }
        return null;
    }
    
    private void handleBooking() {
        if (selectedSeats.isEmpty() || selectedShowTime == null) {
            JOptionPane.showMessageDialog(this,
                "Please select seats and session time before proceeding.",
                "Incomplete Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        double totalAmount = 0;
        for (SeatButton seat : selectedSeats) {
            totalAmount += baseSeatPrice;
        }
        
        // Create RCINEMAS-styled confirmation dialog
        JPanel confirmPanel = new JPanel();
        confirmPanel.setLayout(new BoxLayout(confirmPanel, BoxLayout.Y_AXIS));
        confirmPanel.setBackground(BACKGROUND_SECONDARY);
        confirmPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel rcinemasLabel = new JLabel("RCINEMAS");
        rcinemasLabel.setFont(new Font("Arial", Font.BOLD, 20));
        rcinemasLabel.setForeground(RC_RED);
        rcinemasLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("CONFIRM YOUR BOOKING");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel movieLabel = new JLabel("Movie: " + movieTitle);
        movieLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        movieLabel.setForeground(TEXT_SECONDARY);
        movieLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel timeLabel = new JLabel("Session: " + selectedShowTime.getTime() + " - " + selectedShowTime.getFormat());
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        timeLabel.setForeground(TEXT_SECONDARY);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel seatsLabel = new JLabel("Seats: " + getSelectedSeatsString());
        seatsLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        seatsLabel.setForeground(TEXT_SECONDARY);
        seatsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel totalLabel = new JLabel("Total Amount: $" + String.format("%.2f", totalAmount));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 15));
        totalLabel.setForeground(RC_RED);
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        confirmPanel.add(rcinemasLabel);
        confirmPanel.add(Box.createVerticalStrut(10));
        confirmPanel.add(titleLabel);
        confirmPanel.add(Box.createVerticalStrut(15));
        confirmPanel.add(movieLabel);
        confirmPanel.add(Box.createVerticalStrut(5));
        confirmPanel.add(timeLabel);
        confirmPanel.add(Box.createVerticalStrut(5));
        confirmPanel.add(seatsLabel);
        confirmPanel.add(Box.createVerticalStrut(10));
        confirmPanel.add(totalLabel);
        
        int result = JOptionPane.showConfirmDialog(
            this,
            confirmPanel,
            "RCINEMAS - Confirm Booking",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            try {
                // Create Seat objects for the selected seats
                List<Seat> bookedSeats = new ArrayList<>();
                for (SeatButton seatButton : selectedSeats) {
                    Seat seat = new Seat();
                    bookedSeats.add(seat);
                }
                
                // Generate booking ID
                int bookingId = (int) (System.currentTimeMillis() % 100000);
                
                // Create booking
                Booking booking = new Booking(
                    bookingId,
                    selectedShowTime.getId(),
                    bookedSeats,
                    totalAmount
                );
                
                // Add booking to income tracker
                incomeTracker.addBooking(booking);
                
                // Show success dialog
                showRCinemasSuccessDialog(totalAmount);
                
                // Return to dashboard
                this.dispose();
                SwingUtilities.invokeLater(() -> {
                    CinemaBookingGUI dashboard = new CinemaBookingGUI(userManager, priceCalculator, incomeTracker, customer);
                    dashboard.setVisible(true);
                });
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error processing booking. Please try again.\nError: " + ex.getMessage(),
                    "Booking Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    private void showRCinemasSuccessDialog(double totalAmount) {
        JPanel successPanel = new JPanel();
        successPanel.setLayout(new BoxLayout(successPanel, BoxLayout.Y_AXIS));
        successPanel.setBackground(BACKGROUND_SECONDARY);
        successPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        JLabel rcinemasLabel = new JLabel("RCINEMAS");
        rcinemasLabel.setFont(new Font("Arial", Font.BOLD, 24));
        rcinemasLabel.setForeground(RC_RED);
        rcinemasLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel successIcon = new JLabel("✓");
        successIcon.setFont(new Font("Arial", Font.BOLD, 36));
        successIcon.setForeground(RC_RED);
        successIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("BOOKING CONFIRMED");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel detailsLabel = new JLabel("<html><center>" +
            movieTitle + "<br>" +
            selectedShowTime.getTime() + " - " + selectedShowTime.getFormat() + "<br>" +
            "Seats: " + getSelectedSeatsString() + "<br>" +
            "Total: $" + String.format("%.2f", totalAmount) +
            "</center></html>");
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        detailsLabel.setForeground(TEXT_SECONDARY);
        detailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel enjoyLabel = new JLabel("Thank you for choosing RCINEMAS!");
        enjoyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        enjoyLabel.setForeground(TEXT_PRIMARY);
        enjoyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel membersLabel = new JLabel("RC Rewards members earn points!");
        membersLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        membersLabel.setForeground(RC_SILVER);
        membersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        successPanel.add(rcinemasLabel);
        successPanel.add(Box.createVerticalStrut(8));
        successPanel.add(successIcon);
        successPanel.add(Box.createVerticalStrut(8));
        successPanel.add(titleLabel);
        successPanel.add(Box.createVerticalStrut(15));
        successPanel.add(detailsLabel);
        successPanel.add(Box.createVerticalStrut(15));
        successPanel.add(enjoyLabel);
        successPanel.add(Box.createVerticalStrut(5));
        successPanel.add(membersLabel);
        
        JOptionPane.showMessageDialog(
            this,
            successPanel,
            "RCINEMAS - Booking Confirmed",
            JOptionPane.PLAIN_MESSAGE
        );
    }
    
    private String getSelectedSeatsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selectedSeats.size(); i++) {
            sb.append(selectedSeats.get(i).getSeatId());
            if (i < selectedSeats.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    
    // Button styling methods
    private void styleRCPrimaryButton(JButton button) {
        button.setBackground(RC_BLACK);
        button.setForeground(TEXT_PRIMARY);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBorder(BorderFactory.createLineBorder(TEXT_PRIMARY, 2));
        button.setFocusPainted(false);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(TEXT_PRIMARY);
                    button.setForeground(RC_BLACK);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(RC_BLACK);
                    button.setForeground(TEXT_PRIMARY);
                }
            }
        });
    }
    
    private void styleRCSecondaryButton(JButton button) {
        button.setBackground(CARD_BACKGROUND);
        button.setForeground(TEXT_PRIMARY);
        button.setFont(new Font("Arial", Font.BOLD, 11));
        button.setBorder(new RoundedBorder(6, BORDER_COLOR, 1));
        button.setFocusPainted(false);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(220, 53, 69));
                button.setBorder(new RoundedBorder(6, new Color(220, 53, 69), 1));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(CARD_BACKGROUND);
                button.setBorder(new RoundedBorder(6, BORDER_COLOR, 1));
            }
        });
    }
    
    // ShowTime data class
    private static class ShowTime {
        private final int id;
        private final String time;
        private final String format;
        private final double price;
        
        public ShowTime(int id, String time, String format, double price) {
            this.id = id;
            this.time = time;
            this.format = format;
            this.price = price;
        }
        
        public int getId() { return id; }
        public String getTime() { return time; }
        public String getFormat() { return format; }
        public double getPrice() { return price; }
    }
    
    // SeatButton class
    private static class SeatButton extends JButton {
        private String seatId;
        private SeatType seatType;
        private SeatState seatState;
        
        public SeatButton(String seatId) {
            this.seatId = seatId;
        }
        
        public String getSeatId() { return seatId; }
        public SeatType getSeatType() { return seatType; }
        public void setSeatType(SeatType seatType) { this.seatType = seatType; }
        public SeatState getSeatState() { return seatState; }
        public void setSeatState(SeatState seatState) { this.seatState = seatState; }
    }
    
    // Enums
    private enum SeatType {
        STANDARD, WHEELCHAIR
    }
    
    private enum SeatState {
        AVAILABLE, RESERVED, SELECTED, UNAVAILABLE
    }
    
    // Rounded border class
    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color borderColor;
        private final int thickness;

        public RoundedBorder(int radius, Color borderColor, int thickness) {
            this.radius = radius;
            this.borderColor = borderColor;
            this.thickness = thickness;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            if (thickness > 0) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(thickness));
                g2.draw(new RoundRectangle2D.Float(
                    x + thickness/2f, 
                    y + thickness/2f, 
                    width - thickness, 
                    height - thickness, 
                    radius, 
                    radius
                ));
                g2.dispose();
            }
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness);
        }
    }
}