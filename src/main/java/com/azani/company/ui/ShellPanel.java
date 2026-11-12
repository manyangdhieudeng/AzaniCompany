package com.azani.company.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.azani.company.model.Role;

public class ShellPanel extends JPanel {

    private static final Color PRIMARY_GREEN = new Color(20, 78, 51);
    private static final Color ACCENT_GREEN = new Color(39, 142, 94);
    private static final Color LIGHT_BG = new Color(236, 245, 240);
    private static final Color SIDEBAR_BG = new Color(15, 58, 38);
    private static final Color SIDEBAR_HOVER = new Color(28, 96, 64);
    private static final Color WHITE = Color.WHITE;

    private final MainFrame mainFrame;
    private final JPanel contentArea = new JPanel(new BorderLayout());
    private final JPanel menuPanel = new JPanel();
    private final Map<String, JButton> menuButtons = new LinkedHashMap<>();
    private String activeKey = null;

    public ShellPanel(MainFrame mainFrame, Role role) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(LIGHT_BG);

        add(buildHeader(role), BorderLayout.NORTH);
        add(buildSidebar(role), BorderLayout.WEST);

        contentArea.setBackground(LIGHT_BG);
        add(contentArea, BorderLayout.CENTER);

        showWelcome();
    }

    // ---------- Header: logo + AZANI + "Logged in as" ----------

    private JPanel buildHeader(Role role) {
        JPanel header = new JPanel();
        header.setBackground(PRIMARY_GREEN);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setPreferredSize(new Dimension(750, 95));

        JPanel brandingRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        brandingRow.setBackground(PRIMARY_GREEN);
        brandingRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        brandingRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));

        JLabel logo = loadLogo(48);
        if (logo != null) {
            brandingRow.add(logo);
        }

        JLabel titleLabel = new JLabel("AZANI");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(WHITE);
        brandingRow.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Logged in as: " + formatRole(role), SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(220, 230, 240));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(Box.createVerticalGlue());
        header.add(brandingRow);
        header.add(subtitleLabel);
        header.add(Box.createVerticalGlue());

        return header;
    }

    private JLabel loadLogo(int size) {
        URL logoURL = getClass().getResource("/images/azani-logo.png");
        if (logoURL == null) {
            return null;
        }
        Image scaled = new ImageIcon(logoURL).getImage()
                .getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaled));
    }

    // ---------- Sidebar: the menu for this role ----------

    private JPanel buildSidebar(Role role) {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(250, 0));

        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(SIDEBAR_BG);
        menuPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        switch (role) {
            case REGISTRATION_OFFICER -> {
                addItem("Register Institution", MainFrame.REGISTER);
                addItem("Site Assessment", MainFrame.ASSESSMENT);
                addItem("Purchase Infrastructure", MainFrame.PURCHASE);
            }
            case CASHIER -> {
                addItem("Record Payment", MainFrame.PAYMENT);
                addItem("Monthly Billing", MainFrame.BILLING);
                addItem("Process Upgrade", MainFrame.UPGRADE);
                addItem("Run Overdue Check", MainFrame.OVERDUE);
            }
            case MANAGER -> {
                addItem("Register Institution", MainFrame.REGISTER);
                addItem("Site Assessment", MainFrame.ASSESSMENT);
                addItem("Purchase Infrastructure", MainFrame.PURCHASE);
                addItem("Record Payment", MainFrame.PAYMENT);
                addItem("Monthly Billing", MainFrame.BILLING);
                addItem("Process Upgrade", MainFrame.UPGRADE);
                addItem("Run Overdue Check", MainFrame.OVERDUE);
                addDivider();
                addItem("View Reports", MainFrame.REPORTS);
                addItem("Computations", MainFrame.COMPUTATIONS);
                
            }
        }

        JButton logoutBtn = new JButton("Log Out");
        logoutBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        logoutBtn.setForeground(new Color(255, 190, 180));
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> mainFrame.logout());

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 12));
        logoutPanel.setBackground(SIDEBAR_BG);
        logoutPanel.add(logoutBtn);

        sidebar.add(menuPanel, BorderLayout.NORTH);
        sidebar.add(logoutPanel, BorderLayout.SOUTH);
        return sidebar;
    }

    private void addItem(String label, String key) {
        JButton button = new JButton(label);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(WHITE);
        button.setBackground(SIDEBAR_BG);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(0, 22, 0, 10));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        button.setPreferredSize(new Dimension(250, 46));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover colour, except on the item that is currently open
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!key.equals(activeKey)) {
                    button.setBackground(SIDEBAR_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!key.equals(activeKey)) {
                    button.setBackground(SIDEBAR_BG);
                }
            }
        });

        button.addActionListener(e -> mainFrame.showCard(key));

        menuButtons.put(key, button);
        menuPanel.add(button);
    }

    private void addDivider() {
        JPanel line = new JPanel();
        line.setBackground(new Color(60, 110, 85));
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        line.setPreferredSize(new Dimension(250, 1));
        line.setAlignmentX(Component.LEFT_ALIGNMENT);

        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(line);
        menuPanel.add(Box.createVerticalStrut(10));
    }

    // Highlights the menu item for the screen that is open (null = none)
    private void setActive(String key) {
        activeKey = key;
        for (Map.Entry<String, JButton> entry : menuButtons.entrySet()) {
            boolean active = entry.getKey().equals(key);
            entry.getValue().setBackground(active ? ACCENT_GREEN : SIDEBAR_BG);
        }
    }

    // ---------- Content area ----------

    // Opens a screen on the right-hand side
    public void showScreen(JPanel screen, String key) {
        contentArea.removeAll();
        contentArea.add(screen, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
        setActive(key);
    }

    // The message shown right after login
    public void showWelcome() {
        JPanel welcome = new JPanel(new GridBagLayout());
        welcome.setBackground(LIGHT_BG);

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(LIGHT_BG);

        // The logo is light-coloured, so it sits on a dark green card
        JLabel logo = loadLogo(110);
        if (logo != null) {
            JPanel logoCard = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            logoCard.setBackground(PRIMARY_GREEN);
            logoCard.setBorder(new EmptyBorder(20, 30, 20, 30));
            logoCard.add(logo);
            logoCard.setAlignmentX(Component.CENTER_ALIGNMENT);
            logoCard.setMaximumSize(logoCard.getPreferredSize());
            box.add(logoCard);
            box.add(Box.createVerticalStrut(25));
        }

        JLabel welcomeLabel = new JLabel("Welcome to Azani");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(PRIMARY_GREEN);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel hintLabel = new JLabel("Select an activity from the menu on the left.");
        hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        hintLabel.setForeground(new Color(80, 100, 90));
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        box.add(welcomeLabel);
        box.add(Box.createVerticalStrut(8));
        box.add(hintLabel);

        welcome.add(box);

        contentArea.removeAll();
        contentArea.add(welcome, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
        setActive(null);
    }

    private String formatRole(Role role) {
        return switch (role) {
            case REGISTRATION_OFFICER -> "Registration Officer";
            case CASHIER -> "Cashier";
            case MANAGER -> "Manager";
        };
    }
}
