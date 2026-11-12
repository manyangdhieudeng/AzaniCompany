package com.azani.company.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.azani.company.dao.BillDAO;
import com.azani.company.dao.BillDAOImpl;
import com.azani.company.model.Bill;
import com.azani.company.service.BillingService;

public class BillingPanel extends JPanel {

    private static final Color PRIMARY_GREEN = new Color(20, 78, 51);
    private static final Color ACCENT_GREEN = new Color(39, 142, 94);
    private static final Color LIGHT_BG = new Color(236, 245, 240);
    private static final Color WHITE = Color.WHITE;

    private final BillingService billingService = new BillingService();
    private final BillDAO billDAO = new BillDAOImpl();

    private final DefaultTableModel tableModel;
    private final String currentMonth;

    public BillingPanel(MainFrame mainFrame) {
        setLayout(new BorderLayout());
        setBackground(LIGHT_BG);

        currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy"));

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

        JLabel headerLabel = new JLabel("Monthly Billing", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(PRIMARY_GREEN);

        header.add(backBtn, BorderLayout.WEST);
        header.add(headerLabel, BorderLayout.CENTER);

        // --- Controls: month label + generate button ---
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        controls.setBackground(LIGHT_BG);

        JLabel monthLabel = new JLabel("Billing month: " + currentMonth);
        monthLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        monthLabel.setForeground(PRIMARY_GREEN);

        JButton generateBtn = new JButton("Generate Bills for " + currentMonth);
        generateBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        generateBtn.setForeground(WHITE);
        generateBtn.setBackground(ACCENT_GREEN);
        generateBtn.setFocusPainted(false);
        generateBtn.setBorderPainted(false);
        generateBtn.setOpaque(true);
        generateBtn.setPreferredSize(new Dimension(280, 38));
        generateBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        controls.add(monthLabel);
        controls.add(generateBtn);

        // --- Table of all bills ---
        String[] columns = {"Institution", "Bill Month", "Amount Due (KSh)", "Fine Applied", "Status"};
        tableModel = new DefaultTableModel(columns, 0);

        JTable table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setEnabled(false);

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(LIGHT_BG);
        tableWrapper.setBorder(new EmptyBorder(0, 30, 30, 30));
        tableWrapper.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(LIGHT_BG);
        content.add(controls, BorderLayout.NORTH);
        content.add(tableWrapper, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);

        generateBtn.addActionListener(e -> handleGenerate());
        refreshTable();
    }

    // Reloads every bill from the database, newest first
    private void refreshTable() {
        tableModel.setRowCount(0);

        List<Bill> bills = billDAO.findAll();
        for (int i = bills.size() - 1; i >= 0; i--) {
            Bill b = bills.get(i);
            tableModel.addRow(new Object[]{
                    b.getInstitution().getName(),
                    b.getBillMonth(),
                    String.format("%,.2f", b.getAmountDue()),
                    b.isFineApplied() ? "Yes" : "No",
                    b.isPaid() ? "Paid" : "Unpaid"
            });
        }
    }

    private void handleGenerate() {
        try {
            int created = billingService.generateBillsForAllActive();

            String message = (created == 0)
                    ? "No new bills were generated.\nEvery active institution already has a bill for "
                      + currentMonth + ", or none are active yet."
                    : created + " bill(s) generated for " + currentMonth + ".";

            JOptionPane.showMessageDialog(this, message, "Billing", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Something went wrong: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
