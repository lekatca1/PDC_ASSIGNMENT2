/**
 *
 * @author xps1597
 */
package com.cinema.cinema.booking.gui;

import com.cinema.cinema.booking.models.Customer;
import com.cinema.cinema.booking.service.*;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class CinemaBookingGUI extends JFrame {
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

    public CinemaBookingGUI(UserManager userManager, PriceCalculator priceCalculator, IncomeTracker incomeTracker, Customer customer) {
        this.userManager = userManager;
        this.priceCalculator = priceCalculator;
        this.incomeTracker = incomeTracker;
        this.customer = customer;

        setupModernFrame();
        createModernUI();
    }

    private void setupModernFrame() {
        setTitle("Cinema Booking");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(PRIMARY_DARK);
    }

    private void createModernUI() {
        add(createModernSidebar(), BorderLayout.WEST);
        add(createModernHeader(), BorderLayout.NORTH);
        add(createModernContentArea(), BorderLayout.CENTER);
    }

    private JPanel createModernSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SECONDARY_DARK);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));

        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        brandPanel.setBackground(SECONDARY_DARK);
        JLabel brandLabel = new JLabel("CINEMA");
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        brandLabel.setForeground(ACCENT_RED);
        brandPanel.add(brandLabel);
        sidebar.add(brandPanel);
        sidebar.add(Box.createVerticalStrut(40));

        String[] menuItems = {"Movies", "Session Times"};
        for (int i = 0; i < menuItems.length; i++) {
            RoundedButton btn = new RoundedButton(menuItems[i], 14,
                    (i == 0 ? new Color(32, 34, 37) : SECONDARY_DARK),
                    new Color(48, 50, 53));
            btn.setMaximumSize(new Dimension(180, 48));
            btn.setForeground(i == 0 ? TEXT_PRIMARY : TEXT_SECONDARY);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(8));
        }

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(createUserPanel());

        return sidebar;
    }

    private JPanel createModernHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(SECONDARY_DARK);
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        welcomePanel.setBackground(SECONDARY_DARK);
        String userName = (customer != null) ? customer.getFirstName() : "Guest";
        JLabel welcomeLabel = new JLabel("Welcome, " + userName);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        welcomeLabel.setForeground(TEXT_PRIMARY);
        welcomePanel.add(welcomeLabel);

        JPanel headerButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        headerButtons.setBackground(SECONDARY_DARK);

        RoundedButton profileBtn = new RoundedButton("Profile", 18, new Color(32, 34, 37), new Color(48, 50, 53));
        profileBtn.setPreferredSize(new Dimension(90, 36));
        RoundedButton logoutBtn = new RoundedButton("Logout", 18, new Color(32, 34, 37), new Color(48, 50, 53));
        logoutBtn.setPreferredSize(new Dimension(90, 36));

        logoutBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (result == JOptionPane.YES_OPTION) {
                this.dispose();
                SwingUtilities.invokeLater(() -> {
                    JFrame loginFrame = new JFrame("Cinema Login");
                    loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    loginFrame.setSize(400, 350);
                    loginFrame.setLocationRelativeTo(null);
                    loginFrame.add(new LoginPanel(userManager, priceCalculator, incomeTracker));
                    loginFrame.setVisible(true);
                });
            }
        });

        headerButtons.add(profileBtn);
        headerButtons.add(logoutBtn);

        header.add(welcomePanel, BorderLayout.WEST);
        header.add(headerButtons, BorderLayout.EAST);
        return header;
    }

    private JPanel createUserPanel() {
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBackground(CARD_BACKGROUND);
        userPanel.setMaximumSize(new Dimension(180, 65));
        userPanel.setBorder(new RoundedBorder(16, BORDER_COLOR));

        JPanel avatarCircle = new JPanel(new BorderLayout());
        avatarCircle.setPreferredSize(new Dimension(32, 32));
        avatarCircle.setBackground(ACCENT_RED);
        avatarCircle.setBorder(new RoundedBorder(16, ACCENT_RED));

        JLabel initials = new JLabel(customer != null
                ? customer.getFirstName().substring(0, 1).toUpperCase()
                : "G");
        initials.setForeground(Color.WHITE);
        initials.setFont(new Font("Segoe UI", Font.BOLD, 14));
        initials.setHorizontalAlignment(SwingConstants.CENTER);
        avatarCircle.add(initials, BorderLayout.CENTER);

        JPanel userInfo = new JPanel();
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));
        userInfo.setBackground(CARD_BACKGROUND);

        JLabel userName = new JLabel(customer != null ? customer.getFirstName() + " " + customer.getLastName() : "Guest User");
        userName.setForeground(TEXT_PRIMARY);
        userName.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel userStatus = new JLabel("Member");
        userStatus.setForeground(TEXT_SECONDARY);
        userStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        userInfo.add(userName);
        userInfo.add(userStatus);

        userPanel.add(avatarCircle, BorderLayout.WEST);
        userPanel.add(Box.createHorizontalStrut(12), BorderLayout.CENTER);
        userPanel.add(userInfo, BorderLayout.EAST);
        return userPanel;
    }

    private JPanel createModernContentArea() {
        JPanel contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(PRIMARY_DARK);

        contentArea.add(createContentHeader(), BorderLayout.NORTH);

        JPanel moviesPanel = new JPanel();
        moviesPanel.setLayout(new BoxLayout(moviesPanel, BoxLayout.Y_AXIS));
        moviesPanel.setBackground(PRIMARY_DARK);
        moviesPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));

        moviesPanel.add(createModernMovieCard(
                "The Fantastic Four: First Steps",
                "115 min • Action, Adventure",
                "Reed Richards, a brilliant but arrogant scientist, leads a team of astronauts on an unauthorized space mission that goes terribly wrong when they are exposed to cosmic radiation.",
                "images/fantasicfour.jpg",
                "The Fantastic Four: First Steps"
        ));
        moviesPanel.add(Box.createVerticalStrut(24));

        moviesPanel.add(createModernMovieCard(
                "Superman",
                "129 min • Action, Sci-Fi",
                "The Man of Steel returns in an epic adventure that explores his origins and his commitment to protecting Earth from both terrestrial and cosmic threats.",
                "images/superman.jpg",
                "Superman"
        ));
        moviesPanel.add(Box.createVerticalStrut(24));

        moviesPanel.add(createModernMovieCard(
                "The Bad Guys 2",
                "95 min • Animation, Comedy",
                "The reformed criminal animals are back for another hilarious adventure filled with heists, heart, and lots of laughs in this highly anticipated sequel.",
                "images/badguys2.jpg",
                "The Bad Guys 2"
        ));

        JScrollPane scrollPane = new JScrollPane(moviesPanel);
        scrollPane.setBackground(PRIMARY_DARK);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(PRIMARY_DARK);

        contentArea.add(scrollPane, BorderLayout.CENTER);
        return contentArea;
    }

    private JPanel createContentHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_DARK);
        header.setBorder(BorderFactory.createEmptyBorder(25, 30, 20, 30));

        JLabel title = new JLabel("NOW PLAYING");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.setBackground(PRIMARY_DARK);

        String[] filters = {"All", "Action", "Comedy", "Drama", "Sci-Fi"};
        for (int i = 0; i < filters.length; i++) {
            RoundedButton filterBtn = new RoundedButton(filters[i], 20,
                    (i == 0 ? ACCENT_RED : new Color(32, 34, 37)),
                    (i == 0 ? ACCENT_RED_HOVER : new Color(48, 50, 53)));
            filterBtn.setPreferredSize(new Dimension(75, 32));
            filterPanel.add(filterBtn);
        }

        header.add(title, BorderLayout.WEST);
        header.add(filterPanel, BorderLayout.EAST);
        return header;
    }

    private JPanel createModernMovieCard(String title, String meta, String description, String imagePath, String movieTitle) {
        JPanel card = new JPanel(new BorderLayout(24, 0));
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(new RoundedBorder(20, BORDER_COLOR));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        JPanel paddedCard = new JPanel(new BorderLayout());
        paddedCard.setBackground(CARD_BACKGROUND);
        paddedCard.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // Poster
        JPanel posterPanel = new JPanel(new BorderLayout());
        posterPanel.setBackground(CARD_BACKGROUND);
        posterPanel.setPreferredSize(new Dimension(180, 260));

        JLabel poster = new JLabel();
        poster.setPreferredSize(new Dimension(180, 260));
        poster.setOpaque(true);
        poster.setHorizontalAlignment(SwingConstants.CENTER);
        poster.setBackground(new Color(40, 41, 44));
        poster.setBorder(new RoundedBorder(16, BORDER_COLOR));

        try {
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(imagePath));
            Image scaledImage = icon.getImage().getScaledInstance(180, 260, Image.SCALE_SMOOTH);
            poster.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            poster.setText("Image not found");
            poster.setForeground(TEXT_SECONDARY);
        }

        posterPanel.add(poster, BorderLayout.CENTER);

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(CARD_BACKGROUND);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(TEXT_PRIMARY);

        JLabel lblMeta = new JLabel(meta);
        lblMeta.setForeground(TEXT_SECONDARY);
        lblMeta.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTextArea lblDesc = new JTextArea(description);
        lblDesc.setWrapStyleWord(true);
        lblDesc.setLineWrap(true);
        lblDesc.setEditable(false);
        lblDesc.setForeground(TEXT_SECONDARY);
        lblDesc.setBackground(CARD_BACKGROUND);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDesc.setBorder(BorderFactory.createEmptyBorder(8, 0, 16, 0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setBackground(CARD_BACKGROUND);

        RoundedButton bookNowBtn = new RoundedButton("Book Now", 20, ACCENT_RED, ACCENT_RED_HOVER);
        bookNowBtn.setPreferredSize(new Dimension(110, 40));
        RoundedButton trailerBtn = new RoundedButton("Trailer", 20, new Color(32, 34, 37), new Color(48, 50, 53));
        trailerBtn.setPreferredSize(new Dimension(90, 40));
        RoundedButton favoriteBtn = new RoundedButton("☆", 20, new Color(32, 34, 37), new Color(48, 50, 53));
        favoriteBtn.setPreferredSize(new Dimension(42, 40));

        favoriteBtn.addActionListener(e -> favoriteBtn.setText(favoriteBtn.getText().equals("☆") ? "★" : "☆"));
        bookNowBtn.addActionListener(e -> {
            this.setVisible(false);
            SwingUtilities.invokeLater(() -> new CinemaSeatBookingGUI(userManager, priceCalculator, incomeTracker, customer, movieTitle).setVisible(true));
        });
        trailerBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Trailer for " + movieTitle + " will be available soon!", "Coming Soon", JOptionPane.INFORMATION_MESSAGE));

        buttonPanel.add(bookNowBtn);
        buttonPanel.add(trailerBtn);
        buttonPanel.add(favoriteBtn);

        infoPanel.add(lblTitle);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(lblMeta);
        infoPanel.add(lblDesc);
        infoPanel.add(buttonPanel);

        paddedCard.add(posterPanel, BorderLayout.WEST);
        paddedCard.add(infoPanel, BorderLayout.CENTER);
        card.add(paddedCard, BorderLayout.CENTER);
        return card;
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
        public Insets getBorderInsets(Component c) { return new Insets(1, 1, 1, 1); }
    }
}
