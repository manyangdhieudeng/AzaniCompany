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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.azani.company.dao.BandwidthPlanDAO;
import com.azani.company.dao.BandwidthPlanDAOImpl;
import com.azani.company.dao.InstitutionDAO;
import com.azani.company.dao.InstitutionDAOImpl;
import com.azani.company.dao.SubscriptionDAO;
import com.azani.company.dao.SubscriptionDAOImpl;
import com.azani.company.model.BandwidthPlan;
import com.azani.company.model.Institution;
import com.azani.company.model.Subscription;
import com.azani.company.service.UpgradeService;

public class ProcessUpgradePanel extends JPanel {

    private static final Color PRIMARY_GREEN = new Color(20, 78, 51);
    private static final Color ACCENT_GREEN = new Color(39, 142, 94);
    private static final Color LIGHT_BG = new Color(236, 245, 240);
    private static final Color WHITE = Color.WHITE;

    private static final int FIELD_WIDTH = 350;

    private final InstitutionDAO institutionDAO = new InstitutionDAOImpl();
    private final SubscriptionDAO subscriptionDAO = new SubscriptionDAOImpl();
    private final BandwidthPlanDAO bandwidthPlanDAO = new BandwidthPlanDAOImpl();
    private final UpgradeService upgradeService = new UpgradeService();

    private JComboBox<Institution> institutionBox;
    private JLabel currentPlanLabel;
    private JComboBox<BandwidthPlan> newPlanBox;

    public ProcessUpgradePanel(MainFrame mainFrame) {
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

        JLabel headerLabel = new JLabel("Process Bandwidth Upgrade", SwingConstants.CENTER);
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

        currentPlanLabel = new JLabel("Select an institution to see their current plan.");
        currentPlanLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        currentPlanLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentPlanLabel.setMaximumSize(new Dimension(FIELD_WIDTH, 20));
        currentPlanLabel.setPreferredSize(new Dimension(FIELD_WIDTH, 20));

        List<BandwidthPlan> plans = bandwidthPlanDAO.findAll();
        newPlanBox = new JComboBox<>(plans.toArray(new BandwidthPlan[0]));
        newPlanBox.setMaximumSize(new Dimension(FIELD_WIDTH, 32));
        newPlanBox.setPreferredSize(new Dimension(FIELD_WIDTH, 32));
        newPlanBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(formLabel("Institution"));
        formPanel.add(institutionBox);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(currentPlanLabel);
        formPanel.add(Box.createVerticalStrut(18));

        formPanel.add(formLabel("Upgrade To"));
        formPanel.add(newPlanBox);
        formPanel.add(Box.createVerticalStrut(10));

        JLabel discountNote = new JLabel("A 10% discount applies to the first bill after upgrading.");
        discountNote.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        discountNote.setAlignmentX(Component.CENTER_ALIGNMENT);
        discountNote.setMaximumSize(new Dimension(FIELD_WIDTH, 20));
        discountNote.setPreferredSize(new Dimension(FIELD_WIDTH, 20));
        formPanel.add(discountNote);
        formPanel.add(Box.createVerticalStrut(25));

        JButton submitBtn = new JButton("Process Upgrade");
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

        institutionBox.addActionListener(e -> updateCurrentPlanLabel());
        updateCurrentPlanLabel();

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

    private void updateCurrentPlanLabel() {
        Institution institution = (Institution) institutionBox.getSelectedItem();
        if (institution == null) return;

        Subscription subscription = subscriptionDAO.findByInstitutionId(institution.getId());
        if (subscription == null) {
            currentPlanLabel.setText("No active subscription found for this institution.");
        } else {
            currentPlanLabel.setText("Current plan: " + subscription.getBandwidthPlan().getSpeedMbps() + " Mbps");
        }
    }

    private void handleSubmit() {
        Institution institution = (Institution) institutionBox.getSelectedItem();
        BandwidthPlan newPlan = (BandwidthPlan) newPlanBox.getSelectedItem();

        if (institution == null || newPlan == null) {
            JOptionPane.showMessageDialog(this, "Please select both an institution and a new plan.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Subscription subscription = subscriptionDAO.findByInstitutionId(institution.getId());
            if (subscription == null) {
                JOptionPane.showMessageDialog(this, "This institution has no active subscription to upgrade.", "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            upgradeService.upgradeBandwidth(subscription, newPlan);

            JOptionPane.showMessageDialog(this,
                    institution.getName() + " upgraded to " + newPlan.getSpeedMbps() + " Mbps.\n"
                            + "10% discount will apply to their next bill.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            updateCurrentPlanLabel();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Upgrade failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}