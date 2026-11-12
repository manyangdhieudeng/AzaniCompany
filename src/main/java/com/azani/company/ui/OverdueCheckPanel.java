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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.azani.company.service.BillingService;

public class OverdueCheckPanel extends JPanel {

    private static final Color PRIMARY_GREEN = new Color(20, 78, 51);
    private static final Color ACCENT_GREEN = new Color(39, 142, 94);
    private static final Color LIGHT_BG = new Color(236, 245, 240);
    private static final Color WHITE = Color.WHITE;

    private final BillingService billingService = new BillingService();

    public OverdueCheckPanel(MainFrame mainFrame) {
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

        JLabel headerLabel = new JLabel("Run Overdue Check", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(PRIMARY_GREEN);

        header.add(backBtn, BorderLayout.WEST);
        header.add(headerLabel, BorderLayout.CENTER);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(LIGHT_BG);
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel explanationLabel = new JLabel("<html><div style='text-align:center;'>"
                + "This checks every unpaid bill in the system.<br><br>"
                + "Any bill not yet paid will have a 15% fine applied, "
                + "and the institution's service will be disconnected.</div></html>");
        explanationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        explanationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton runBtn = new JButton("Run Overdue Check Now");
        runBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        runBtn.setForeground(WHITE);
        runBtn.setBackground(ACCENT_GREEN);
        runBtn.setFocusPainted(false);
        runBtn.setBorderPainted(false);
        runBtn.setOpaque(true);
        runBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        runBtn.setMaximumSize(new Dimension(260, 40));
        runBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        contentPanel.add(explanationLabel);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(runBtn);

        contentPanel.setMaximumSize(new Dimension(400, 220));
        contentPanel.setPreferredSize(new Dimension(400, 220));

        JPanel centeringWrapper = new JPanel(new GridBagLayout());
        centeringWrapper.setBackground(LIGHT_BG);
        centeringWrapper.add(contentPanel);

        add(header, BorderLayout.NORTH);
        add(centeringWrapper, BorderLayout.CENTER);

        runBtn.addActionListener(e -> handleRunCheck());
    }

    private void handleRunCheck() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to run the overdue check?\nThis may disconnect institutions with unpaid bills.",
                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            billingService.runOverdueCheck();
            JOptionPane.showMessageDialog(this,
                    "Overdue check complete. Fines applied and institutions disconnected where applicable.",
                    "Done", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Something went wrong: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}