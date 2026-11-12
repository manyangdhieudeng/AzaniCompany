package com.azani.company.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.azani.company.dao.StaffDAO;
import com.azani.company.dao.StaffDAOImpl;
import com.azani.company.model.Role;
import com.azani.company.model.Staff;

public class LoginPanel extends JPanel {

    private static final Color PRIMARY_GREEN = new Color(20, 78, 51);
    private static final Color ACCENT_GREEN = new Color(39, 142, 94);
    private static final Color LIGHT_BG = new Color(236, 245, 240);
    private static final Color WHITE = Color.WHITE;

    

    private final StaffDAO staffDAO = new StaffDAOImpl();

    private CardLayout innerCardLayout;
    private JPanel innerCardPanel;

    private Role selectedRole;
    private JLabel roleHeaderLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPanel(MainFrame mainFrame) {

        setLayout(new BorderLayout());
        setBackground(LIGHT_BG);

        // HEADER: COMPANY LOGO, NAME AND SUBTITLE

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_GREEN);
        header.setPreferredSize(new Dimension(750, 85));

        // Panel containing the logo and company name
        JPanel brandingPanel = new JPanel();
        brandingPanel.setLayout(
                new BoxLayout(brandingPanel, BoxLayout.X_AXIS)
        );
        brandingPanel.setBackground(PRIMARY_GREEN);

        URL logoURL = getClass().getResource("/images/azani-logo.png");

        if (logoURL != null) {

            ImageIcon originalIcon = new ImageIcon(logoURL);

            Image scaledImage = originalIcon.getImage()
                    .getScaledInstance(55, 55, Image.SCALE_SMOOTH);

            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));

            brandingPanel.add(logoLabel);
            brandingPanel.add(Box.createHorizontalStrut(15));

        } else {
            System.err.println(
                    "ERROR: Could not find /images/azani-logo.png"
            );
        }

        // Company name
        JLabel titleLabel = new JLabel("AZANI");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(WHITE);

        brandingPanel.add(titleLabel);

        // Center the logo and company name horizontally
        JPanel brandingWrapper = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 0, 0)
        );
        brandingWrapper.setBackground(PRIMARY_GREEN);
        brandingWrapper.add(brandingPanel);

        // Company subtitle
        JLabel subtitleLabel = new JLabel(
                "Internet Service Provider - Information System",
                SwingConstants.CENTER
        );

        subtitleLabel.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );
        subtitleLabel.setForeground(new Color(220, 230, 240));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Arrange the branding and subtitle vertically
        JPanel headerContent = new JPanel();
        headerContent.setLayout(
                new BoxLayout(headerContent, BoxLayout.Y_AXIS)
        );
        headerContent.setBackground(PRIMARY_GREEN);

        headerContent.add(brandingWrapper);
        headerContent.add(Box.createVerticalStrut(0));
        headerContent.add(subtitleLabel);

        // Add some space around the header content
        headerContent.setBorder(
                new EmptyBorder(0, 5, 0, 5)
        );

        header.add(headerContent, BorderLayout.CENTER);

        // LOGIN CONTENT: ROLE SELECTION AND LOGIN

        innerCardLayout = new CardLayout();
        innerCardPanel = new JPanel(innerCardLayout);
        innerCardPanel.setBackground(LIGHT_BG);

        innerCardPanel.add(
                buildRoleChoicePanel(),
                "ROLE_CHOICE"
        );

        innerCardPanel.add(
                buildCredentialsPanel(mainFrame),
                "CREDENTIALS"
        );

        add(header, BorderLayout.NORTH);
        add(innerCardPanel, BorderLayout.CENTER);
    }


    // STEP 1: CHOOSE A ROLE

    private JPanel buildRoleChoicePanel() {

        JPanel panel = new JPanel();
        panel.setLayout(
                new BoxLayout(panel, BoxLayout.Y_AXIS)
        );
        panel.setBackground(LIGHT_BG);
        panel.setBorder(
                new EmptyBorder(30, 50, 30, 50)
        );

        JLabel instructionLabel = new JLabel("Log in as:");
        instructionLabel.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton officerBtn =
                createRoleButton("Registration Officer");

        JButton cashierBtn =
                createRoleButton("Cashier");

        JButton managerBtn =
                createRoleButton("Manager");

        panel.add(instructionLabel);
        panel.add(Box.createVerticalStrut(20));

        panel.add(officerBtn);
        panel.add(Box.createVerticalStrut(14));

        panel.add(cashierBtn);
        panel.add(Box.createVerticalStrut(14));

        panel.add(managerBtn);

        panel.setMaximumSize(new Dimension(400, 300));
        panel.setPreferredSize(new Dimension(400, 300));

        JPanel centeringWrapper = new JPanel(
                new GridBagLayout()
        );
        centeringWrapper.setBackground(LIGHT_BG);
        centeringWrapper.add(panel);

        officerBtn.addActionListener(e ->
                goToCredentials(
                        Role.REGISTRATION_OFFICER,
                        "Registration Officer"
                )
        );

        cashierBtn.addActionListener(e ->
                goToCredentials(
                        Role.CASHIER,
                        "Cashier"
                )
        );

        managerBtn.addActionListener(e ->
                goToCredentials(
                        Role.MANAGER,
                        "Manager"
                )
        );

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(LIGHT_BG);
        wrapper.add(
                centeringWrapper,
                BorderLayout.CENTER
        );

        return wrapper;
    }

    
    // CREATE ROLE BUTTONS


    private JButton createRoleButton(String text) {

        JButton button = new JButton(text);

        button.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );
        button.setForeground(WHITE);
        button.setBackground(ACCENT_GREEN);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 42));
        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    // MOVE TO LOGIN CREDENTIALS

    private void goToCredentials(
            Role role,
            String displayName
    ) {

        selectedRole = role;

        roleHeaderLabel.setText(
                "Log in as " + displayName
        );

        usernameField.setText("");
        passwordField.setText("");

        innerCardLayout.show(
                innerCardPanel,
                "CREDENTIALS"
        );
    }

    // STEP 2: ENTER LOGIN CREDENTIALS

    private JPanel buildCredentialsPanel(MainFrame mainFrame) {

    final int FIELD_WIDTH = 300;

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(LIGHT_BG);
    panel.setBorder(new EmptyBorder(30, 50, 30, 50));

    roleHeaderLabel = new JLabel("Log in");
    roleHeaderLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
    roleHeaderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel usernameLabel = new JLabel("Username");
    usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    usernameLabel.setMaximumSize(new Dimension(FIELD_WIDTH, 20));
    usernameLabel.setPreferredSize(new Dimension(FIELD_WIDTH, 20));

    usernameField = new JTextField();
    usernameField.setMaximumSize(new Dimension(FIELD_WIDTH, 32));
    usernameField.setPreferredSize(new Dimension(FIELD_WIDTH, 32));
    usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel passwordLabel = new JLabel("Password");
    passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    passwordLabel.setMaximumSize(new Dimension(FIELD_WIDTH, 20));
    passwordLabel.setPreferredSize(new Dimension(FIELD_WIDTH, 20));

    passwordField = new JPasswordField();
    passwordField.setMaximumSize(new Dimension(FIELD_WIDTH, 32));
    passwordField.setPreferredSize(new Dimension(FIELD_WIDTH, 32));
    passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

    JButton loginBtn = new JButton("Log In");
    loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
    loginBtn.setForeground(WHITE);
    loginBtn.setBackground(ACCENT_GREEN);
    loginBtn.setFocusPainted(false);
    loginBtn.setBorderPainted(false);
    loginBtn.setOpaque(true);
    loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    loginBtn.setMaximumSize(new Dimension(FIELD_WIDTH, 40));
    loginBtn.setPreferredSize(new Dimension(FIELD_WIDTH, 40));
    loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    JButton backBtn = new JButton("← Choose a different role");
    backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    backBtn.setForeground(PRIMARY_GREEN);
    backBtn.setContentAreaFilled(false);
    backBtn.setBorderPainted(false);
    backBtn.setFocusPainted(false);
    backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    panel.add(roleHeaderLabel);
    panel.add(Box.createVerticalStrut(20));
    panel.add(usernameLabel);
    panel.add(usernameField);
    panel.add(Box.createVerticalStrut(12));
    panel.add(passwordLabel);
    panel.add(passwordField);
    panel.add(Box.createVerticalStrut(20));
    panel.add(loginBtn);
    panel.add(Box.createVerticalStrut(10));
    panel.add(backBtn);

    panel.setMaximumSize(new Dimension(380, 320));
    panel.setPreferredSize(new Dimension(380, 320));

    JPanel centeringWrapper = new JPanel(new GridBagLayout());
    centeringWrapper.setBackground(LIGHT_BG);
    centeringWrapper.add(panel);

    backBtn.addActionListener(e -> innerCardLayout.show(innerCardPanel, "ROLE_CHOICE"));
    loginBtn.addActionListener(e -> handleLogin(mainFrame));

    JPanel wrapper = new JPanel(new BorderLayout());
    wrapper.setBackground(LIGHT_BG);
    wrapper.add(centeringWrapper, BorderLayout.CENTER);

    return wrapper;
}

    // HANDLE LOGIN AUTHENTICATION

    private void handleLogin(MainFrame mainFrame) {

        String username = usernameField.getText().trim();

        String password = new String(
                passwordField.getPassword()
        );

        if (username.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter both username and password.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Staff staff = staffDAO.login(username, password);

        if (staff == null || staff.getRole() != selectedRole) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password for this role.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        mainFrame.loginSuccessful(staff);
    }
}