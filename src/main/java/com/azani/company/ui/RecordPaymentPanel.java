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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.azani.company.dao.BillDAO;
import com.azani.company.dao.BillDAOImpl;
import com.azani.company.dao.InstitutionDAO;
import com.azani.company.dao.InstitutionDAOImpl;
import com.azani.company.model.Bill;
import com.azani.company.model.Institution;
import com.azani.company.service.BillingService;
import com.azani.company.service.RegistrationService;

public class RecordPaymentPanel extends JPanel {

    private static final Color PRIMARY_GREEN = new Color(20, 78, 51);
    private static final Color ACCENT_GREEN = new Color(39, 142, 94);
    private static final Color LIGHT_BG = new Color(236, 245, 240);
    private static final Color WHITE = Color.WHITE;

    private static final int FIELD_WIDTH = 350;

    private final InstitutionDAO institutionDAO = new InstitutionDAOImpl();
    private final BillDAO billDAO = new BillDAOImpl();
    private final RegistrationService registrationService = new RegistrationService();
    private final BillingService billingService = new BillingService();

    private JComboBox<Institution> institutionBox;
    private JComboBox<String> typeBox;
    private JLabel amountInfoLabel;
    private JTextField amountField;

    public RecordPaymentPanel(MainFrame mainFrame) {
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
        JLabel headerLabel = new JLabel("Record Payment", SwingConstants.CENTER);
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

        typeBox = new JComboBox<>(new String[]{"Registration Fee", "Installation Fee", "Monthly Bill", "Reconnection Fee"});
        typeBox.setMaximumSize(new Dimension(FIELD_WIDTH, 32));
        typeBox.setPreferredSize(new Dimension(FIELD_WIDTH, 32));
        typeBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        amountInfoLabel = new JLabel(" ");
        amountInfoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        amountInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        amountInfoLabel.setMaximumSize(new Dimension(FIELD_WIDTH, 20));
        amountInfoLabel.setPreferredSize(new Dimension(FIELD_WIDTH, 20));

        amountField = new JTextField();
        amountField.setMaximumSize(new Dimension(FIELD_WIDTH, 32));
        amountField.setPreferredSize(new Dimension(FIELD_WIDTH, 32));
        amountField.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(formLabel("Institution"));
        formPanel.add(institutionBox);
        formPanel.add(Box.createVerticalStrut(14));

        formPanel.add(formLabel("Payment Type"));
        formPanel.add(typeBox);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(amountInfoLabel);
        formPanel.add(Box.createVerticalStrut(6));

        formPanel.add(formLabel("Amount to Pay (KSh)"));
        formPanel.add(amountField);
        formPanel.add(Box.createVerticalStrut(25));

        JButton submitBtn = new JButton("Submit Payment");
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

        formPanel.setMaximumSize(new Dimension(420, 400));
        formPanel.setPreferredSize(new Dimension(420, 400));

        JPanel centeringWrapper = new JPanel(new GridBagLayout());
        centeringWrapper.setBackground(LIGHT_BG);
        centeringWrapper.add(formPanel);

        add(header, BorderLayout.NORTH);
        add(centeringWrapper, BorderLayout.CENTER);

        typeBox.addActionListener(e -> updateAmountInfo());
        updateAmountInfo();

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

    private void updateAmountInfo() {
        String type = (String) typeBox.getSelectedItem();
        switch (type) {
            case "Registration Fee" -> {
                amountInfoLabel.setText("Fixed fee: KSh 8,500 (amount field not needed)");
                amountField.setEnabled(false);
                amountField.setText("");
            }
            case "Installation Fee" -> {
                amountInfoLabel.setText("Fixed fee: KSh 10,000 (amount field not needed)");
                amountField.setEnabled(false);
                amountField.setText("");
            }
            case "Monthly Bill" -> {
                amountInfoLabel.setText("Enter the amount shown on the institution's current bill.");
                amountField.setEnabled(true);
            }
            case "Reconnection Fee" -> {
                amountInfoLabel.setText("Must pay the FULL amount owed (bill + fine + KSh 1,000).");
                amountField.setEnabled(true);
            }
        }
    }

    private void handleSubmit() {
        Institution institution = (Institution) institutionBox.getSelectedItem();
        String type = (String) typeBox.getSelectedItem();

        if (institution == null) {
            JOptionPane.showMessageDialog(this, "No institution selected.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            switch (type) {
                case "Registration Fee" -> {
                    registrationService.payRegistrationFee(institution);
                    showSuccess("Registration fee paid for " + institution.getName());
                }
                case "Installation Fee" -> {
                    registrationService.payInstallationFee(institution);
                    showSuccess("Installation fee paid for " + institution.getName());
                }
                case "Monthly Bill" -> {
                    Bill bill = billDAO.findUnpaidByInstitution(institution.getId());
                    if (bill == null) {
                        JOptionPane.showMessageDialog(this, "No unpaid monthly bill found for this institution.", "Not Found", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    double amount = parseAmount();
                    billingService.payMonthlyBill(bill, amount);
                    showSuccess("Monthly bill paid for " + institution.getName());
                }
                case "Reconnection Fee" -> {
                    Bill bill = billDAO.findUnpaidByInstitution(institution.getId());
                    if (bill == null) {
                        JOptionPane.showMessageDialog(this, "No unpaid bill found for this institution.", "Not Found", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    double amount = parseAmount();
                    billingService.payReconnectionFee(bill, amount);
                    showSuccess("Reconnection fee paid - " + institution.getName() + " is now reconnected.");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for the amount.", "Invalid Amount", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Payment failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double parseAmount() {
        return Double.parseDouble(amountField.getText().trim());
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
        amountField.setText("");
    }
}