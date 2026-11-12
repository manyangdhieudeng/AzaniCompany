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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.azani.company.dao.InstitutionDAO;
import com.azani.company.dao.InstitutionDAOImpl;
import com.azani.company.model.Institution;
import com.azani.company.service.PurchaseService;

public class PurchasePanel extends JPanel {

    private static final Color PRIMARY_GREEN = new Color(20, 78, 51);
    private static final Color ACCENT_GREEN = new Color(39, 142, 94);
    private static final Color LIGHT_BG = new Color(236, 245, 240);
    private static final Color WHITE = Color.WHITE;

    private static final int FIELD_WIDTH = 350;

    private final InstitutionDAO institutionDAO = new InstitutionDAOImpl();
    private final PurchaseService purchaseService = new PurchaseService();

    private JComboBox<Institution> institutionBox;
    private JTextField computerQtyField;
    private JTextField lanQtyField;
    private JLabel computerTotalLabel;
    private JLabel lanTotalLabel;
    private JLabel grandTotalLabel;

    public PurchasePanel(MainFrame mainFrame) {
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

        JLabel headerLabel = new JLabel("Purchase Infrastructure", SwingConstants.CENTER);
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

        computerQtyField = new JTextField();
        computerQtyField.setMaximumSize(new Dimension(FIELD_WIDTH, 32));
        computerQtyField.setPreferredSize(new Dimension(FIELD_WIDTH, 32));
        computerQtyField.setAlignmentX(Component.CENTER_ALIGNMENT);

        computerTotalLabel = new JLabel("Subtotal: KSh 0");
        computerTotalLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        computerTotalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        computerTotalLabel.setMaximumSize(new Dimension(FIELD_WIDTH, 20));
        computerTotalLabel.setPreferredSize(new Dimension(FIELD_WIDTH, 20));

        lanQtyField = new JTextField();
        lanQtyField.setMaximumSize(new Dimension(FIELD_WIDTH, 32));
        lanQtyField.setPreferredSize(new Dimension(FIELD_WIDTH, 32));
        lanQtyField.setAlignmentX(Component.CENTER_ALIGNMENT);

        lanTotalLabel = new JLabel("Subtotal: KSh 0");
        lanTotalLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lanTotalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        lanTotalLabel.setMaximumSize(new Dimension(FIELD_WIDTH, 20));
        lanTotalLabel.setPreferredSize(new Dimension(FIELD_WIDTH, 20));

        grandTotalLabel = new JLabel("Grand Total: KSh 0");
        grandTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        grandTotalLabel.setForeground(PRIMARY_GREEN);
        grandTotalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        grandTotalLabel.setMaximumSize(new Dimension(FIELD_WIDTH, 24));
        grandTotalLabel.setPreferredSize(new Dimension(FIELD_WIDTH, 24));

        formPanel.add(formLabel("Institution"));
        formPanel.add(institutionBox);
        formPanel.add(Box.createVerticalStrut(18));

        formPanel.add(formLabel("Number of Computers (leave blank if none needed)"));
        formPanel.add(computerQtyField);
        formPanel.add(computerTotalLabel);
        formPanel.add(Box.createVerticalStrut(16));

        formPanel.add(formLabel("Number of LAN Nodes (leave blank if none needed)"));
        formPanel.add(lanQtyField);
        formPanel.add(lanTotalLabel);
        formPanel.add(Box.createVerticalStrut(18));

        formPanel.add(grandTotalLabel);
        formPanel.add(Box.createVerticalStrut(25));

        JButton submitBtn = new JButton("Confirm Purchase");
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

        formPanel.setMaximumSize(new Dimension(420, 540));
        formPanel.setPreferredSize(new Dimension(420, 540));

        JPanel centeringWrapper = new JPanel(new GridBagLayout());
        centeringWrapper.setBackground(LIGHT_BG);
        centeringWrapper.add(formPanel);

        add(header, BorderLayout.NORTH);
        add(centeringWrapper, BorderLayout.CENTER);

        DocumentListener liveUpdateListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateTotals(); }
            public void removeUpdate(DocumentEvent e) { updateTotals(); }
            public void changedUpdate(DocumentEvent e) { updateTotals(); }
        };
        computerQtyField.getDocument().addDocumentListener(liveUpdateListener);
        lanQtyField.getDocument().addDocumentListener(liveUpdateListener);

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

    private void updateTotals() {
        double computerTotal = 0;
        double lanTotal = 0;

        String computerText = computerQtyField.getText().trim();
        if (!computerText.isEmpty()) {
            try {
                int qty = Integer.parseInt(computerText);
                computerTotal = purchaseService.calculateComputerCost(qty);
                computerTotalLabel.setText("Subtotal: KSh " + computerTotal);
            } catch (NumberFormatException ex) {
                computerTotalLabel.setText("Enter a valid number");
            }
        } else {
            computerTotalLabel.setText("Subtotal: KSh 0");
        }

        String lanText = lanQtyField.getText().trim();
        if (!lanText.isEmpty()) {
            try {
                int qty = Integer.parseInt(lanText);
                lanTotal = purchaseService.calculateLanCost(qty);
                lanTotalLabel.setText("Subtotal: KSh " + lanTotal);
            } catch (NumberFormatException ex) {
                lanTotalLabel.setText("Enter a valid number");
            } catch (IllegalArgumentException ex) {
                lanTotalLabel.setText("No pricing bracket for that quantity");
            }
        } else {
            lanTotalLabel.setText("Subtotal: KSh 0");
        }

        grandTotalLabel.setText("Grand Total: KSh " + (computerTotal + lanTotal));
    }

    private void handleSubmit() {
        Institution institution = (Institution) institutionBox.getSelectedItem();

        if (institution == null) {
            JOptionPane.showMessageDialog(this, "No institution selected.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String computerText = computerQtyField.getText().trim();
        String lanText = lanQtyField.getText().trim();

        if (computerText.isEmpty() && lanText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a quantity for at least one item.", "Nothing to Purchase", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder summary = new StringBuilder("Purchase recorded for " + institution.getName() + "\n\n");

        try {
            if (!computerText.isEmpty()) {
                int qty = Integer.parseInt(computerText);
                var purchase = purchaseService.recordComputerPurchase(institution, qty);
                summary.append(qty).append(" Computers - KSh ").append(purchase.getCost()).append("\n");
            }

            if (!lanText.isEmpty()) {
                int qty = Integer.parseInt(lanText);
                var purchase = purchaseService.recordLanPurchase(institution, qty);
                summary.append(qty).append(" LAN Nodes - KSh ").append(purchase.getCost()).append("\n");
            }

            JOptionPane.showMessageDialog(this, summary.toString(), "Success", JOptionPane.INFORMATION_MESSAGE);

            computerQtyField.setText("");
            lanQtyField.setText("");
            updateTotals();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantities must be valid numbers.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Something went wrong: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}