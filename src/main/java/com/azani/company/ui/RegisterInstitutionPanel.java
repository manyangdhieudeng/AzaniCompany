package com.azani.company.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.azani.company.dao.BandwidthPlanDAO;
import com.azani.company.dao.BandwidthPlanDAOImpl;
import com.azani.company.model.BandwidthPlan;
import com.azani.company.model.ContactPerson;
import com.azani.company.model.Institution;
import com.azani.company.service.RegistrationService;

public class RegisterInstitutionPanel extends JPanel {

    private static final Color PRIMARY_GREEN = new Color(20, 78, 51);
    private static final Color ACCENT_GREEN = new Color(30, 142, 94);
    private static final Color LIGHT_BG = new Color(236, 245, 240);
    private static final Color WHITE = Color.WHITE;

    private static final int FIELD_WIDTH = 350;

    private final RegistrationService registrationService = new RegistrationService();
    private final BandwidthPlanDAO bandwidthPlanDAO = new BandwidthPlanDAOImpl();

    private JTextField nameField;
    private JComboBox<String> categoryBox;
    private JComboBox<BandwidthPlan> planBox;
    private JTextField contactNameField;
    private JTextField phoneField;
    private JTextField emailField;

    public RegisterInstitutionPanel(MainFrame mainFrame) {
        setLayout(new BorderLayout());
        setBackground(LIGHT_BG);

        // --- Header with Back button ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(LIGHT_BG);
        header.setPreferredSize(new Dimension(750, 60));

        JButton backBtn = new JButton("← Back");
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        backBtn.setForeground(PRIMARY_GREEN);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> mainFrame.showCard(MainFrame.DASHBOARD));

        JLabel headerLabel = new JLabel("Register Institution", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(PRIMARY_GREEN);

        header.add(backBtn, BorderLayout.WEST);
        header.add(headerLabel, BorderLayout.CENTER);

        // --- Form panel ---
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(LIGHT_BG);
        formPanel.setBorder(new EmptyBorder(25, 40, 25, 40));

        nameField = new JTextField();
        categoryBox = new JComboBox<>(new String[]{"Primary School", "Junior School", "Senior School", "College"});
        contactNameField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();

        formPanel.add(formLabel("Institution Name"));
        formPanel.add(styledField(nameField));
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(formLabel("Category"));
        categoryBox.setMaximumSize(new Dimension(FIELD_WIDTH, 32));
        categoryBox.setPreferredSize(new Dimension(FIELD_WIDTH, 32));
        categoryBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(categoryBox);
        formPanel.add(Box.createVerticalStrut(12));

        java.util.List<BandwidthPlan> plans = bandwidthPlanDAO.findAll();
        planBox = new JComboBox<>(plans.toArray(new BandwidthPlan[0]));
        planBox.setMaximumSize(new Dimension(FIELD_WIDTH, 32));
        planBox.setPreferredSize(new Dimension(FIELD_WIDTH, 32));
        planBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(formLabel("Desired Bandwidth Plan"));
        formPanel.add(planBox);
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(formLabel("Contact Person Name"));
        formPanel.add(styledField(contactNameField));
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(formLabel("Phone Number"));
        formPanel.add(styledField(phoneField));
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(formLabel("Email"));
        formPanel.add(styledField(emailField));
        formPanel.add(Box.createVerticalStrut(25));

        JButton submitBtn = new JButton("Register Institution");
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitBtn.setForeground(WHITE);
        submitBtn.setBackground(ACCENT_GREEN);
        submitBtn.setFocusPainted(false);
        submitBtn.setBorderPainted(false);
        submitBtn.setOpaque(true);
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitBtn.setMaximumSize(new Dimension(FIELD_WIDTH, 40));
        submitBtn.setPreferredSize(new Dimension(FIELD_WIDTH, 40));
        submitBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        formPanel.add(submitBtn);

        formPanel.setMaximumSize(new Dimension(420, 560));
        formPanel.setPreferredSize(new Dimension(420, 560));

        JPanel centeringWrapper = new JPanel(new GridBagLayout());
        centeringWrapper.setBackground(LIGHT_BG);
        centeringWrapper.add(formPanel);

        add(header, BorderLayout.NORTH);
        add(centeringWrapper, BorderLayout.CENTER);

        submitBtn.addActionListener(e -> handleSubmit());
    }

    private JLabel formLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setMaximumSize(new Dimension(FIELD_WIDTH, 20));
        label.setPreferredSize(new Dimension(FIELD_WIDTH, 20));
        return label;
    }

    private JTextField styledField(JTextField field) {
        field.setMaximumSize(new Dimension(FIELD_WIDTH, 32));
        field.setPreferredSize(new Dimension(FIELD_WIDTH, 32));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }

    private void handleSubmit() {
        String name = nameField.getText().trim();
        String category = (String) categoryBox.getSelectedItem();
        String contactName = contactNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || contactName.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in the institution name, contact name, and phone number.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ContactPerson contactPerson = new ContactPerson(contactName, phone, email);
            BandwidthPlan selectedPlan = (BandwidthPlan) planBox.getSelectedItem();
            Institution institution = registrationService.registerInstitution(category, name, contactPerson, selectedPlan);

            JOptionPane.showMessageDialog(this,
                    "Institution registered successfully!\n\nID: " + institution.getId()
                            + "\nName: " + institution.getName()
                            + "\nCategory: " + institution.getCategory()
                            + "\nStatus: " + institution.getStatus(),
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            clearForm();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Something went wrong: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        nameField.setText("");
        contactNameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        categoryBox.setSelectedIndex(0);
    }
}