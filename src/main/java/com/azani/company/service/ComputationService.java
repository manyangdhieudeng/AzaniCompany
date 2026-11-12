package com.azani.company.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.azani.company.dao.BillDAO;
import com.azani.company.dao.BillDAOImpl;
import com.azani.company.dao.InstitutionDAO;
import com.azani.company.dao.InstitutionDAOImpl;
import com.azani.company.dao.PaymentDAO;
import com.azani.company.dao.PaymentDAOImpl;
import com.azani.company.dao.PurchaseDAO;
import com.azani.company.dao.PurchaseDAOImpl;
import com.azani.company.dao.SubscriptionDAO;
import com.azani.company.dao.SubscriptionDAOImpl;
import com.azani.company.model.Bill;
import com.azani.company.model.Institution;
import com.azani.company.model.Payment;
import com.azani.company.model.PaymentType;
import com.azani.company.model.Purchase;
import com.azani.company.model.Subscription;

public class ComputationService {

    private final InstitutionDAO institutionDAO = new InstitutionDAOImpl();
    private final PaymentDAO paymentDAO = new PaymentDAOImpl();
    private final PurchaseDAO purchaseDAO = new PurchaseDAOImpl();
    private final BillDAO billDAO = new BillDAOImpl();
    private final SubscriptionDAO subscriptionDAO = new SubscriptionDAOImpl();

    private static final double FINE_RATE = 0.15;
    private static final double RECONNECTION_FEE = 1000;

    // ---------- Result shapes ----------

    public record InstallationCost(String institution, double installationFee, double computers, double lanNodes) {
        public double total() { return installationFee + computers + lanNodes; }
    }

    public record InfrastructureCost(String institution, int computerQty, double computerCost, int lanQty, double lanCost) {
        public double total() { return computerCost + lanCost; }
        public String services() {
            if (computerQty > 0 && lanQty > 0) return "Computers + LAN";
            return computerQty > 0 ? "Computers only" : "LAN only";
        }
    }

    public record UpgradedService(String institution, int speedMbps, double monthlyCharge) { }

    public record CategoryTotals(String category, double monthlyCharges, double fines, double reconnectionFees) {
        public double total() { return monthlyCharges + fines + reconnectionFees; }
    }

    public record InstitutionAggregate(String institution, double registration, double installation,
                                       double computers, double lanNodes, double monthlyCharges,
                                       double fines, double reconnectionFees) {
        public double total() {
            return registration + installation + computers + lanNodes + monthlyCharges + fines + reconnectionFees;
        }
    }

    // ---------- 4(a) ----------

    public List<InstallationCost> getInstallationCosts() {
        List<Payment> payments = paymentDAO.findAll();
        List<Purchase> purchases = purchaseDAO.findAll();

        List<InstallationCost> result = new ArrayList<>();
        for (Institution i : sortedInstitutions()) {
            result.add(new InstallationCost(
                    i.getName(),
                    paymentTotal(payments, i.getId(), PaymentType.INSTALLATION),
                    purchaseCost(purchases, i.getId(), "Computer"),
                    purchaseCost(purchases, i.getId(), "LAN")));
        }
        return result;
    }

    // ---------- 4(b) ----------

    public List<InfrastructureCost> getInfrastructureCosts() {
        List<Purchase> purchases = purchaseDAO.findAll();

        List<InfrastructureCost> result = new ArrayList<>();
        for (Institution i : sortedInstitutions()) {
            int computerQty = purchaseQty(purchases, i.getId(), "Computer");
            int lanQty = purchaseQty(purchases, i.getId(), "LAN");

            if (computerQty == 0 && lanQty == 0) {
                continue; // bought nothing
            }

            result.add(new InfrastructureCost(
                    i.getName(),
                    computerQty, purchaseCost(purchases, i.getId(), "Computer"),
                    lanQty, purchaseCost(purchases, i.getId(), "LAN")));
        }
        return result;
    }

    // ---------- 4(c) ----------

    public List<UpgradedService> getUpgradedServices() {
        List<UpgradedService> result = new ArrayList<>();

        for (Subscription s : subscriptionDAO.findAll()) {
            if (s.isUpgraded()) {
                result.add(new UpgradedService(
                        s.getInstitution().getName(),
                        s.getBandwidthPlan().getSpeedMbps(),
                        s.getBandwidthPlan().getMonthlyCost()));
            }
        }

        result.sort(Comparator.comparing(UpgradedService::institution, String.CASE_INSENSITIVE_ORDER));
        return result;
    }

    // ---------- 4(d) ----------

    public List<CategoryTotals> getTotalsByCategory() {
        // category -> { monthly charges, fines, reconnection fees }
        Map<String, double[]> totals = new LinkedHashMap<>();
        for (String category : List.of("Primary School", "Junior School", "Senior School", "College")) {
            totals.put(category, new double[3]);
        }

        for (Bill b : billDAO.findAll()) {
            double[] t = totals.computeIfAbsent(b.getInstitution().getCategory(), k -> new double[3]);
            t[0] += billBase(b);
            t[1] += billFine(b);
        }

        for (Payment p : paymentDAO.findAll()) {
            if (p.getType() == PaymentType.RECONNECTION) {
                double[] t = totals.computeIfAbsent(p.getInstitution().getCategory(), k -> new double[3]);
                t[2] += RECONNECTION_FEE;
            }
        }

        List<CategoryTotals> result = new ArrayList<>();
        for (Map.Entry<String, double[]> e : totals.entrySet()) {
            result.add(new CategoryTotals(e.getKey(),
                    round2(e.getValue()[0]), round2(e.getValue()[1]), round2(e.getValue()[2])));
        }
        return result;
    }

    // ---------- 4(e) ----------

    public List<InstitutionAggregate> getAggregatesPerInstitution() {
        List<Payment> payments = paymentDAO.findAll();
        List<Purchase> purchases = purchaseDAO.findAll();
        List<Bill> bills = billDAO.findAll();

        List<InstitutionAggregate> result = new ArrayList<>();
        for (Institution i : sortedInstitutions()) {
            int id = i.getId();

            double monthly = 0;
            double fines = 0;
            for (Bill b : bills) {
                if (b.getInstitution().getId() == id) {
                    monthly += billBase(b);
                    fines += billFine(b);
                }
            }

            result.add(new InstitutionAggregate(
                    i.getName(),
                    paymentTotal(payments, id, PaymentType.REGISTRATION),
                    paymentTotal(payments, id, PaymentType.INSTALLATION),
                    purchaseCost(purchases, id, "Computer"),
                    purchaseCost(purchases, id, "LAN"),
                    round2(monthly),
                    round2(fines),
                    paymentCount(payments, id, PaymentType.RECONNECTION) * RECONNECTION_FEE));
        }
        return result;
    }

    // ---------- Helpers ----------

    private List<Institution> sortedInstitutions() {
        List<Institution> list = institutionDAO.findAll();
        list.sort(Comparator.comparing(Institution::getName, String.CASE_INSENSITIVE_ORDER));
        return list;
    }

    private double paymentTotal(List<Payment> payments, int institutionId, PaymentType type) {
        double sum = 0;
        for (Payment p : payments) {
            if (p.getInstitution().getId() == institutionId && p.getType() == type) {
                sum += p.getAmount();
            }
        }
        return sum;
    }

    private int paymentCount(List<Payment> payments, int institutionId, PaymentType type) {
        int count = 0;
        for (Payment p : payments) {
            if (p.getInstitution().getId() == institutionId && p.getType() == type) {
                count++;
            }
        }
        return count;
    }

    private double purchaseCost(List<Purchase> purchases, int institutionId, String type) {
        double sum = 0;
        for (Purchase p : purchases) {
            if (p.getInstitution().getId() == institutionId && p.getType().equals(type)) {
                sum += p.getCost();
            }
        }
        return sum;
    }

    private int purchaseQty(List<Purchase> purchases, int institutionId, String type) {
        int sum = 0;
        for (Purchase p : purchases) {
            if (p.getInstitution().getId() == institutionId && p.getType().equals(type)) {
                sum += p.getQuantity();
            }
        }
        return sum;
    }

    // A fined bill stores base + 15% in amountDue, so the base is amountDue / 1.15
    private double billBase(Bill b) {
        return b.isFineApplied() ? b.getAmountDue() / (1 + FINE_RATE) : b.getAmountDue();
    }

    private double billFine(Bill b) {
        return b.getAmountDue() - billBase(b);
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}