package com.azani.company.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.azani.company.dao.SubscriptionDAO;
import com.azani.company.dao.SubscriptionDAOImpl;
import com.azani.company.model.Bill;
import com.azani.company.model.Institution;
import com.azani.company.model.SiteAssessment;
import com.azani.company.model.Subscription;
import com.azani.company.service.ReportService;

public class ReportsPanel extends JPanel {

    private static final Color PRIMARY_GREEN = new Color(20, 78, 51);
    private static final Color LIGHT_BG = new Color(236, 245, 240);
    private static final Color WHITE = Color.WHITE;

    private final ReportService reportService = new ReportService();
    private final SubscriptionDAO subscriptionDAO = new SubscriptionDAOImpl();

    public ReportsPanel(MainFrame mainFrame) {
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

        JLabel headerLabel = new JLabel("Reports", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(PRIMARY_GREEN);

        header.add(backBtn, BorderLayout.WEST);
        header.add(headerLabel, BorderLayout.CENTER);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        tabbedPane.addTab("Registered Institutions", buildRegisteredInstitutionsTable());
        tabbedPane.addTab("Defaulters", buildDefaultersTable());
        tabbedPane.addTab("Disconnected", buildDisconnectedTable());
        tabbedPane.addTab("Infrastructure", buildInfrastructureTable());

        add(header, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JScrollPane buildRegisteredInstitutionsTable() {
    String[] columns = {"ID", "Name", "Category", "Status", "Bandwidth Plan", "Registered On"};
    DefaultTableModel model = new DefaultTableModel(columns, 0);

    List<Institution> institutions = reportService.getRegisteredInstitutions();
    for (Institution i : institutions) {
        Subscription subscription = subscriptionDAO.findByInstitutionId(i.getId());
        String planText = (subscription != null)
                ? subscription.getBandwidthPlan().getSpeedMbps() + " Mbps"
                : "No subscription yet";

        model.addRow(new Object[]{
                i.getId(), i.getName(), i.getCategory(), i.getStatus(), planText, i.getRegistrationDate()
        });
    }

    return wrapInScrollPane(model);
}

    private JScrollPane buildDefaultersTable() {
        String[] columns = {"Institution", "Bill Month", "Amount Due (KSh)"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        List<Bill> defaulters = reportService.getDefaulters();
        for (Bill b : defaulters) {
            model.addRow(new Object[]{
                    b.getInstitution().getName(), b.getBillMonth(), b.getAmountDue()
            });
        }

        return wrapInScrollPane(model);
    }

    private JScrollPane buildDisconnectedTable() {
        String[] columns = {"ID", "Name", "Category"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        List<Institution> disconnected = reportService.getDisconnectedInstitutions();
        for (Institution i : disconnected) {
            model.addRow(new Object[]{
                    i.getId(), i.getName(), i.getCategory()
            });
        }

        return wrapInScrollPane(model);
    }

    private JScrollPane buildInfrastructureTable() {
        String[] columns = {"Institution", "Users", "Computers", "LAN Nodes", "Ready"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        List<SiteAssessment> assessments = reportService.getInfrastructureReport();
        for (SiteAssessment sa : assessments) {
            model.addRow(new Object[]{
                    sa.getInstitution().getName(),
                    sa.getNumberOfUsers(),
                    sa.getExistingComputers(),
                    sa.getExistingLanNodes(),
                    sa.isReady() ? "Yes" : "No"
            });
        }

        return wrapInScrollPane(model);
    }

    private JScrollPane wrapInScrollPane(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setEnabled(false);

        return new JScrollPane(table);
    }
}