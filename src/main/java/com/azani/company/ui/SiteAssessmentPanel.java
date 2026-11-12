package com.azani.company.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.azani.company.dao.InstitutionDAO;
import com.azani.company.dao.InstitutionDAOImpl;
import com.azani.company.model.Institution;
import com.azani.company.service.RegistrationService;

public class SiteAssessmentPanel extends JPanel {

    private static final Color PRIMARY_GREEN = new Color(20, 78, 51);
    private static final Color ACCENT_GREEN = new Color(39, 142, 94);
    private static final Color LIGHT_BG = new Color(236, 245, 240);
    private static final Color WHITE = Color.WHITE;

    private static final int FIELD_WIDTH = 350;

    private final InstitutionDAO institutionDAO = new InstitutionDAOImpl();
    private final RegistrationService registrationService = new RegistrationService();

    private JComboBox<Institution> institutionBox;
    private JTextField usersField;
    private JTextField computersField;
    private JTextField lanNodesField;
    private JCheckBox readyCheckBox;

    public SiteAssessmentPanel(MainFrame mainFrame) {
        setLayout(new BorderLayout());
        setBackground(LIGHT_BG);

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

        JLabel headerLabel = new JLabel("Site Assessment", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(PRIMARY_GREEN);

        header.add(backBtn, BorderLayout.WEST);
        header.add(headerLabel, BorderLayout.CENTER);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(LIGHT_BG);
        formPanel.setBorder(new EmptyBorder(25, 40, 25, 40));

        List<Institution> institutions = institutionDAO.findAll();
        institutionBox = new JComboBox<>(institutions.toArray(new Institution[0]));
        institutionBox.setMaximumSize(new Dimension(FIELD_WIDTH, 32));
        institutionBox.setPreferredSize(new Dimension(FIELD_WIDTH, 32));
        institutionBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        usersField = new JTextField();
        computersField = new JTextField();
        lanNodesField = new JTextField();

        readyCheckBox = new JCheckBox("Site is ready for installation (computers and LAN both sufficient)");
        readyCheckBox.setBackground(LIGHT_BG);
        readyCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        readyCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        formPanel.add(formLabel("Institution"));
        formPanel.add(institutionBox);
        formPanel.add(Box.createVerticalStrut(14));

        formPanel.add(formLabel("Number of Users"));
        formPanel.add(styledField(usersField));
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(formLabel("Existing Computers"));
        formPanel.add(styledField(computersField));
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(formLabel("Existing LAN Nodes"));
        formPanel.add(styledField(lanNodesField));
        formPanel.add(Box.createVerticalStrut(18));

        formPanel.add(readyCheckBox);
        formPanel.add(Box.createVerticalStrut(25));

        JButton submitBtn = new JButton("Save Assessment");
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

        formPanel.setMaximumSize(new Dimension(420, 460));
        formPanel.setPreferredSize(new Dimension(420, 460));

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
        Institution institution = (Institution) institutionBox.getSelectedItem();

        if (institution == null) {
            JOptionPane.showMessageDialog(this, "No institution selected.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int users = Integer.parseInt(usersField.getText().trim());
            int computers = Integer.parseInt(computersField.getText().trim());
            int lanNodes = Integer.parseInt(lanNodesField.getText().trim());
            boolean ready = readyCheckBox.isSelected();

            registrationService.recordSiteAssessment(institution, users, computers, lanNodes, ready);

            JOptionPane.showMessageDialog(this,
                    "Site assessment saved for " + institution.getName()
                            + "\nReady for installation: " + (ready ? "Yes" : "No"),
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            clearForm();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Users, computers, and LAN nodes must be numbers.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Something went wrong: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        usersField.setText("");
        computersField.setText("");
        lanNodesField.setText("");
        readyCheckBox.setSelected(false);
    }
}