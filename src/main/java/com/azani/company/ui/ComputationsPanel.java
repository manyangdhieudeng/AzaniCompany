package com.azani.company.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.azani.company.service.ComputationService;

public class ComputationsPanel extends JPanel {

    private static final Color PRIMARY_GREEN = new Color(20, 78, 51);
    private static final Color LIGHT_BG = new Color(236, 245, 240);
    private static final Color WHITE = Color.WHITE;

    private final ComputationService computationService = new ComputationService();

    public ComputationsPanel(MainFrame mainFrame) {
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

        JLabel headerLabel = new JLabel("Computations", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(PRIMARY_GREEN);

        header.add(backBtn, BorderLayout.WEST);
        header.add(headerLabel, BorderLayout.CENTER);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabs.addTab("Installation Cost", buildInstallationTab());
        tabs.addTab("Infrastructure", buildInfrastructureTab());
        tabs.addTab("4Upgraded Services", buildUpgradedTab());
        tabs.addTab("By Category", buildCategoryTab());
        tabs.addTab("4Per Institution", buildAggregateTab());

        add(header, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    // ---------- 4(a) ----------
    private JScrollPane buildInstallationTab() {
        String[] columns = {"Institution", "Installation Fee (KSh)", "Computers (KSh)",
                "LAN Nodes (KSh)", "Total Installation Cost (KSh)"};
        List<Object[]> rows = new ArrayList<>();
        double fee = 0, computers = 0, lan = 0, total = 0;

        for (var r : computationService.getInstallationCosts()) {
            rows.add(new Object[]{r.institution(), money(r.installationFee()), money(r.computers()),
                    money(r.lanNodes()), money(r.total())});
            fee += r.installationFee();
            computers += r.computers();
            lan += r.lanNodes();
            total += r.total();
        }
        rows.add(new Object[]{"TOTAL", money(fee), money(computers), money(lan), money(total)});
        return buildTable(columns, rows);
    }

    // ---------- 4(b) ----------
    private JScrollPane buildInfrastructureTab() {
        String[] columns = {"Institution", "Computers (Qty)", "Computers (KSh)", "LAN Nodes (Qty)",
                "LAN Nodes (KSh)", "Total (KSh)", "Services"};
        List<Object[]> rows = new ArrayList<>();
        int computerQty = 0, lanQty = 0;
        double computerCost = 0, lanCost = 0, total = 0;

        for (var r : computationService.getInfrastructureCosts()) {
            rows.add(new Object[]{r.institution(), r.computerQty(), money(r.computerCost()),
                    r.lanQty(), money(r.lanCost()), money(r.total()), r.services()});
            computerQty += r.computerQty();
            computerCost += r.computerCost();
            lanQty += r.lanQty();
            lanCost += r.lanCost();
            total += r.total();
        }
        rows.add(new Object[]{"TOTAL", computerQty, money(computerCost), lanQty, money(lanCost), money(total), ""});
        return buildTable(columns, rows);
    }

    // ---------- 4(c) ----------
    private JScrollPane buildUpgradedTab() {
        String[] columns = {"Institution", "Current Plan", "Monthly Charge (KSh)"};
        List<Object[]> rows = new ArrayList<>();
        double total = 0;

        for (var r : computationService.getUpgradedServices()) {
            rows.add(new Object[]{r.institution(), r.speedMbps() + " Mbps", money(r.monthlyCharge())});
            total += r.monthlyCharge();
        }
        rows.add(new Object[]{"TOTAL", "", money(total)});
        return buildTable(columns, rows);
    }

    // ---------- 4(d) ----------
    private JScrollPane buildCategoryTab() {
        String[] columns = {"Category", "Monthly Charges (KSh)", "Overdue Fines (KSh)",
                "Reconnection Fees (KSh)", "Total (KSh)"};
        List<Object[]> rows = new ArrayList<>();
        double monthly = 0, fines = 0, reconnection = 0, total = 0;

        for (var r : computationService.getTotalsByCategory()) {
            rows.add(new Object[]{r.category(), money(r.monthlyCharges()), money(r.fines()),
                    money(r.reconnectionFees()), money(r.total())});
            monthly += r.monthlyCharges();
            fines += r.fines();
            reconnection += r.reconnectionFees();
            total += r.total();
        }
        rows.add(new Object[]{"TOTAL", money(monthly), money(fines), money(reconnection), money(total)});
        return buildTable(columns, rows);
    }

    // ---------- 4(e) ----------
    private JScrollPane buildAggregateTab() {
        String[] columns = {"Institution", "Registration", "Installation", "Computers", "LAN Nodes",
                "Monthly Charges", "Fines", "Reconnection", "Total (KSh)"};
        List<Object[]> rows = new ArrayList<>();
        double registration = 0, installation = 0, computers = 0, lan = 0,
                monthly = 0, fines = 0, reconnection = 0, total = 0;

        for (var r : computationService.getAggregatesPerInstitution()) {
            rows.add(new Object[]{r.institution(), money(r.registration()), money(r.installation()),
                    money(r.computers()), money(r.lanNodes()), money(r.monthlyCharges()),
                    money(r.fines()), money(r.reconnectionFees()), money(r.total())});
            registration += r.registration();
            installation += r.installation();
            computers += r.computers();
            lan += r.lanNodes();
            monthly += r.monthlyCharges();
            fines += r.fines();
            reconnection += r.reconnectionFees();
            total += r.total();
        }
        rows.add(new Object[]{"TOTAL", money(registration), money(installation), money(computers),
                money(lan), money(monthly), money(fines), money(reconnection), money(total)});
        return buildTable(columns, rows);
    }

    // ---------- Shared helpers ----------
    private JScrollPane buildTable(String[] columns, List<Object[]> rows) {
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (Object[] row : rows) {
            model.addRow(row);
        }

        JTable table = new JTable(model);
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setEnabled(false);

        return new JScrollPane(table);
    }

    private String money(double value) {
        return String.format("%,.2f", value);
    }
}