package com.azani.company.ui;

import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.azani.company.model.Staff;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final Map<String, JPanel> cards = new HashMap<>();

    private Staff loggedInStaff;
    private ShellPanel shell;

    // The two top-level screens of the window
    public static final String LOGIN = "LOGIN";
    public static final String SHELL = "SHELL";

    // Activities that open inside the shell
    public static final String DASHBOARD = "DASHBOARD"; // now means "back to the welcome message"
    public static final String REGISTER = "REGISTER";
    public static final String PAYMENT = "PAYMENT";
    public static final String ASSESSMENT = "ASSESSMENT";
    public static final String UPGRADE = "UPGRADE";
    public static final String OVERDUE = "OVERDUE";
    public static final String REPORTS = "REPORTS";
    public static final String PURCHASE = "PURCHASE";
    public static final String BILLING = "BILLING";
    public static final String COMPUTATIONS = "COMPUTATIONS";

    public MainFrame() {
        setTitle("Azani Internet Service Provider - Information System");

        var logoURL = getClass().getResource("/images/azani-logo.png");
        if (logoURL != null) {
            setIconImage(new ImageIcon(logoURL).getImage());
        }

        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        addCard(LOGIN, new LoginPanel(this));

        add(cardPanel);
        cardLayout.show(cardPanel, LOGIN);
    }

    // Registers a top-level screen under a name, replacing any older copy
    private void addCard(String name, JPanel panel) {
        JPanel old = cards.put(name, panel);
        if (old != null) {
            cardPanel.remove(old);
        }
        cardPanel.add(panel, name);
    }

    // Builds a brand-new copy of an activity screen, so its dropdowns
    // and tables always reflect what is in the database right now.
    private JPanel buildScreen(String cardName) {
        return switch (cardName) {
            case REGISTER -> new RegisterInstitutionPanel(this);
            case PAYMENT -> new RecordPaymentPanel(this);
            case ASSESSMENT -> new SiteAssessmentPanel(this);
            case UPGRADE -> new ProcessUpgradePanel(this);
            case OVERDUE -> new OverdueCheckPanel(this);
            case REPORTS -> new ReportsPanel(this);
            case PURCHASE -> new PurchasePanel(this);
            case BILLING -> new BillingPanel(this);
            case COMPUTATIONS -> new ComputationsPanel(this);
            default -> null;
        };
    }

    // Called by the menu (and by the screens' old Back buttons)
    public void showCard(String cardName) {
        if (cardName.equals(LOGIN)) {
            cardLayout.show(cardPanel, LOGIN);
            return;
        }

        if (shell == null) {
            return; // nobody is logged in
        }

        if (cardName.equals(DASHBOARD)) {
            shell.showWelcome();
            return;
        }

        JPanel screen = buildScreen(cardName);
        if (screen != null) {
            shell.showScreen(screen, cardName);
        }
    }

    public void loginSuccessful(Staff staff) {
        this.loggedInStaff = staff;

        shell = new ShellPanel(this, staff.getRole());
        addCard(SHELL, shell);
        cardLayout.show(cardPanel, SHELL);
    }

    public void logout() {
        this.loggedInStaff = null;
        this.shell = null;

        JPanel oldShell = cards.remove(SHELL);
        if (oldShell != null) {
            cardPanel.remove(oldShell);
        }

        addCard(LOGIN, new LoginPanel(this)); // fresh login screen, no leftover credentials
        cardLayout.show(cardPanel, LOGIN);
    }

    public Staff getLoggedInStaff() {
        return loggedInStaff;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}