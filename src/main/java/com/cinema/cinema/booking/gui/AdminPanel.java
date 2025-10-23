/**
 *
 * @author xps1597
 */
package com.cinema.cinema.booking.gui;

import com.cinema.cinema.booking.models.Booking;
import com.cinema.cinema.booking.service.IncomeTracker;
import com.cinema.cinema.booking.service.PriceCalculator;
import com.cinema.cinema.booking.service.UserManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class AdminPanel extends JFrame {
    private IncomeTracker incomeTracker;
    private JTable reportTable;
    private JLabel totalIncomeLabel;

    private static final List<AdminPanel> openPanels = new ArrayList<>();

    private static final Color BACKGROUND = new Color(12, 12, 14);
    private static final Color FIELD_BG = new Color(40, 40, 40);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);
    private static final Color ACCENT_RED = new Color(220, 0, 0);
    private static final Color ACCENT_RED_HOVER = new Color(255, 59, 48);
    private static final Color TEXT_WHITE = Color.WHITE;

    public AdminPanel(IncomeTracker incomeTracker) {
        this.incomeTracker = incomeTracker;

        setTitle("RCINEMAS - Admin Dashboard");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(BACKGROUND);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Admin Dashboard");
        title.setForeground(TEXT_WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.add(title, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BACKGROUND);

        RoundedButton refreshBtn = new RoundedButton("REFRESH", 20, ACCENT_RED, ACCENT_RED_HOVER);
        RoundedButton logoutBtn = new RoundedButton("LOGOUT", 20, ACCENT_RED, ACCENT_RED_HOVER);

        refreshBtn.setPreferredSize(new Dimension(100, 35));
        logoutBtn.setPreferredSize(new Dimension(100, 35));

        refreshBtn.addActionListener(e -> loadBookings());
        logoutBtn.addActionListener(e -> handleLogout());

        buttonPanel.add(refreshBtn);
        buttonPanel.add(logoutBtn);
        header.add(buttonPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        reportTable = new JTable(new DefaultTableModel(
            new Object[]{"Booking ID", "Customer", "Movie", "Seats", "Revenue", "Date"},
            0
        ));
        reportTable.setBackground(FIELD_BG);
        reportTable.setForeground(TEXT_WHITE);
        reportTable.setGridColor(BORDER_COLOR);
        reportTable.setRowHeight(28);
        reportTable.getTableHeader().setBackground(ACCENT_RED);
        reportTable.getTableHeader().setForeground(TEXT_WHITE);
        reportTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.getViewport().setBackground(BACKGROUND);
        scrollPane.setBorder(new EmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        footer.setBackground(BACKGROUND);
        footer.setBorder(new EmptyBorder(10, 20, 10, 20));

        totalIncomeLabel = new JLabel("Total Income: $0.00");
        totalIncomeLabel.setForeground(TEXT_WHITE);
        totalIncomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        footer.add(totalIncomeLabel);

        add(footer, BorderLayout.SOUTH);

        loadBookings();
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            openPanels.add(this);
        } else {
            openPanels.remove(this);
        }
    }

    public static void refreshAllOpenPanels(boolean showToast) {
        for (AdminPanel panel : openPanels) {
            panel.loadBookings();
            if (showToast) {
                panel.showToast("New booking added!");
            }
        }
    }

    private void loadBookings() {
        List<Booking> bookings = incomeTracker.getBookings();
        DefaultTableModel model = (DefaultTableModel) reportTable.getModel();
        model.setRowCount(0);

        double totalIncome = 0.0;
        for (Booking b : bookings) {
            model.addRow(new Object[]{
                b.getBookingId(),
                "Customer " + b.getCustomerId(),
                "Movie " + b.getShowtimeId(),
                b.getNumberOfSeats(),
                String.format("$%.2f", b.getTotalPrice()),
                b.getBookingDate().toLocalDate().toString()
            });
            totalIncome += b.getTotalPrice();
        }

        totalIncomeLabel.setText("Total Income: $" + String.format("%.2f", totalIncome));
    }

    private void handleLogout() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION
        );
        if (result == JOptionPane.YES_OPTION) {
            this.dispose();

            SwingUtilities.invokeLater(() -> {
                JFrame loginFrame = new JFrame("Cinema Login");
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.setSize(400, 350);
                loginFrame.setLocationRelativeTo(null);

                UserManager userManager = new UserManager();
                PriceCalculator priceCalculator = new PriceCalculator();

                LoginPanel loginPanel = new LoginPanel(userManager, priceCalculator, incomeTracker);
                loginFrame.add(loginPanel);
                loginFrame.setVisible(true);
            });
        }
    }

    private void showToast(String message) {
        JWindow toast = new JWindow(this);
        toast.setLayout(new BorderLayout());

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(ACCENT_RED);
        label.setForeground(TEXT_WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        toast.add(label, BorderLayout.CENTER);
        toast.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width - toast.getWidth() - 20;
        int y = screenSize.height - toast.getHeight() - 60;
        toast.setLocation(x, y);

        new Thread(() -> {
            toast.setVisible(true);
            try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
            toast.setVisible(false);
            toast.dispose();
        }).start();
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

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseEntered(java.awt.event.MouseEvent e) { repaint(); }
                @Override public void mouseExited(java.awt.event.MouseEvent e) { repaint(); }
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
