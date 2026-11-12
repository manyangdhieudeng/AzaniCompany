package com.azani.company.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.azani.company.model.Role;

public class DashboardPanel extends JPanel {

    private static final Color PRIMARY_GREEN = new Color(20, 78, 51);
    private static final Color ACCENT_GREEN = new Color(39, 142, 94);
    private static final Color LIGHT_BG = new Color(236, 245, 240);
    private static final Color WHITE = Color.WHITE;

    public DashboardPanel(MainFrame mainFrame, Role role) {
        setLayout(new BorderLayout());
        setBackground(LIGHT_BG);

        // --- Header ---
        JPanel header = new JPanel();
        header.setBackground(PRIMARY_GREEN);
        header.setPreferredSize(new Dimension(750, 70));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("AZANI", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Logged in as: " + formatRole(role), SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(220, 230, 240));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(Box.createVerticalStrut(12));
        header.add(titleLabel);
        header.add(subtitleLabel);

        // --- Button panel (built based on role) ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(LIGHT_BG);

        int calculatedWidth;
        int calculatedHeight;

        if (role == Role.REGISTRATION_OFFICER) {
            buttonPanel.setLayout(new GridLayout(0, 1, 0, 14));
            buttonPanel.setBorder(new EmptyBorder(30, 50, 30, 50));

            buttonPanel.add(createStyledButton("Register Institution", () -> mainFrame.showCard(MainFrame.REGISTER)));
            buttonPanel.add(createStyledButton("Site Assessment", () -> mainFrame.showCard(MainFrame.ASSESSMENT)));
            buttonPanel.add(createStyledButton("Purchase Infrastructure", () -> mainFrame.showCard(MainFrame.PURCHASE)));

            calculatedWidth = 500;
            calculatedHeight = (buttonPanel.getComponentCount() * 60) + 60;

        } else if (role == Role.CASHIER) {
            buttonPanel.setLayout(new GridLayout(0, 1, 0, 14));
            buttonPanel.setBorder(new EmptyBorder(30, 50, 30, 50));

            buttonPanel.add(createStyledButton("Record Payment", () -> mainFrame.showCard(MainFrame.PAYMENT)));
            buttonPanel.add(createStyledButton("Monthly Billing", () -> mainFrame.showCard(MainFrame.BILLING)));
            buttonPanel.add(createStyledButton("Process Upgrade", () -> mainFrame.showCard(MainFrame.UPGRADE)));
            buttonPanel.add(createStyledButton("Run Overdue Check", () -> mainFrame.showCard(MainFrame.OVERDUE)));

            calculatedWidth = 500;
            calculatedHeight = (buttonPanel.getComponentCount() * 60) + 60;

        } else {
            // Manager: everything, in two columns (5 on the left, 4 on the right)
        buttonPanel.setLayout(new GridLayout(1, 2, 20, 0));
        buttonPanel.setBorder(new EmptyBorder(30, 0, 30, 0));

        JPanel leftColumn = new JPanel(new GridLayout(5, 1, 0, 14));
        leftColumn.setBackground(LIGHT_BG);
        leftColumn.add(createStyledButton("Register Institution", () -> mainFrame.showCard(MainFrame.REGISTER)));
        leftColumn.add(createStyledButton("Record Payment", () -> mainFrame.showCard(MainFrame.PAYMENT)));
        leftColumn.add(createStyledButton("Site Assessment", () -> mainFrame.showCard(MainFrame.ASSESSMENT)));
        leftColumn.add(createStyledButton("Purchase Infrastructure", () -> mainFrame.showCard(MainFrame.PURCHASE)));
        leftColumn.add(createStyledButton("Monthly Billing", () -> mainFrame.showCard(MainFrame.BILLING)));

        JPanel rightColumn = new JPanel(new GridLayout(5, 1, 0, 14));
        rightColumn.setBackground(LIGHT_BG);
        rightColumn.add(createStyledButton("Process Upgrade", () -> mainFrame.showCard(MainFrame.UPGRADE)));
        rightColumn.add(createStyledButton("Run Overdue Check", () -> mainFrame.showCard(MainFrame.OVERDUE)));
        rightColumn.add(createStyledButton("View Reports", () -> mainFrame.showCard(MainFrame.REPORTS)));
        rightColumn.add(createStyledButton("Computations", () -> mainFrame.showCard(MainFrame.COMPUTATIONS)));

        buttonPanel.add(leftColumn);
        buttonPanel.add(rightColumn);

        calculatedWidth = 700;
        calculatedHeight = (5 * 60) + 60;
        }

        buttonPanel.setMaximumSize(new Dimension(calculatedWidth, calculatedHeight));
        buttonPanel.setPreferredSize(new Dimension(calculatedWidth, calculatedHeight));

        JPanel centeringWrapper = new JPanel(new GridBagLayout());
        centeringWrapper.setBackground(LIGHT_BG);
        centeringWrapper.add(buttonPanel);

        // --- Footer with Logout ---
        JPanel footer = new JPanel();
        footer.setBackground(LIGHT_BG);
        JButton logoutBtn = new JButton("Log Out");
        logoutBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        logoutBtn.setForeground(new Color(150, 40, 40));
        logoutBtn.setBorderPainted(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> mainFrame.logout());
        footer.add(logoutBtn);

        add(header, BorderLayout.NORTH);
        add(centeringWrapper, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    private String formatRole(Role role) {
        return switch (role) {
            case REGISTRATION_OFFICER -> "Registration Officer";
            case CASHIER -> "Cashier";
            case MANAGER -> "Manager";
        };
    }

    private JButton createStyledButton(String text, Runnable onClick) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(WHITE);
        button.setBackground(ACCENT_GREEN);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_GREEN);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_GREEN);
            }
        });

        button.addActionListener(e -> onClick.run());
        return button;
    }
}