package com.azani.company.service;

import com.azani.company.dao.*;
import com.azani.company.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BillingService {

    private final BillDAO billDAO = new BillDAOImpl();
    private final PaymentDAO paymentDAO = new PaymentDAOImpl();
    private final SubscriptionDAO subscriptionDAO = new SubscriptionDAOImpl();
    private final InstitutionDAO institutionDAO = new InstitutionDAOImpl();

    private static final double FINE_RATE = 0.15;
    private static final double RECONNECTION_FEE = 1000;

    private static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("MMMM yyyy");

    // Generates this month's bill for a subscription, applying the first-month discount if active
    public Bill generateMonthlyBill(Subscription subscription) {
        double amount = subscription.getBandwidthPlan().getMonthlyCost();

        if (subscription.isDiscountActive()) {
            amount = amount * 0.90; // 10% off
            subscription.setDiscountActive(false); // discount only applies once
            subscriptionDAO.updateDiscountActive(subscription);
        }

        String billMonth = LocalDate.now().format(MONTH_FORMAT);
        Bill bill = new Bill(subscription.getInstitution(), amount, billMonth);
        billDAO.save(bill);

        return bill;
    }

    // Generates this month's bill for every active subscription that doesn't have one yet.
// Returns how many new bills were created.
public int generateBillsForAllActive() {
    String billMonth = LocalDate.now().format(MONTH_FORMAT);
    int created = 0;

    for (Subscription subscription : subscriptionDAO.findAll()) {
        if (subscription.getStatus() != InstitutionStatus.ACTIVE) {
            continue; // not installed yet, or disconnected
        }

        int institutionId = subscription.getInstitution().getId();
        if (billDAO.existsForMonth(institutionId, billMonth)) {
            continue; // already billed this month
        }

        generateMonthlyBill(subscription);
        created++;
    }
    return created;
}

    // Use Case: Pay Monthly Bill
    public void payMonthlyBill(Bill bill, double amountPaid) {
        if (bill.isPaid()) {
            throw new IllegalStateException("This bill has already been paid.");
        }

        Payment payment = new Payment(bill.getInstitution(), PaymentType.MONTHLY, amountPaid);
        paymentDAO.save(payment);

        bill.setPaid(true);
        billDAO.updatePaidStatus(bill);
    }

    // Use Case: Check Overdue Accounts / Apply Fine / Disconnect Service
    // Manually triggered by Manager/Cashier
    public void runOverdueCheck() {
        List<Bill> unpaidBills = billDAO.findAll().stream()
                .filter(b -> !b.isPaid() && !b.isFineApplied())
                .toList();

        for (Bill bill : unpaidBills) {
            // Apply the 15% fine
            double finedAmount = bill.getAmountDue() * (1 + FINE_RATE);
            bill.setAmountDue(finedAmount);
            bill.setFineApplied(true);
            billDAO.updateFineAndAmount(bill);

            // Disconnect the subscription
            Institution institution = bill.getInstitution();
            Subscription subscription = subscriptionDAO.findByInstitutionId(institution.getId());
            if (subscription != null) {
                subscription.setStatus(InstitutionStatus.DISCONNECTED);
                subscriptionDAO.updateStatus(subscription);
            }

            institution.setStatus(InstitutionStatus.DISCONNECTED);
            institutionDAO.updateStatus(institution);
        }
    }

    // Use Case: Pay Reconnection Fee
    public void payReconnectionFee(Bill bill, double amountPaid) {
        Institution institution = bill.getInstitution();

        if (institution.getStatus() != InstitutionStatus.DISCONNECTED) {
            throw new IllegalStateException("Institution is not currently disconnected.");
        }

        double totalOwed = bill.getAmountDue() + RECONNECTION_FEE;

        if (amountPaid < totalOwed) {
            throw new IllegalArgumentException("Full payment required. Amount owed: KSh " + totalOwed);
        }

        Payment payment = new Payment(institution, PaymentType.RECONNECTION, amountPaid);
        paymentDAO.save(payment);

        bill.setPaid(true);
        billDAO.updatePaidStatus(bill);

        institution.setStatus(InstitutionStatus.ACTIVE);
        institutionDAO.updateStatus(institution);

        Subscription subscription = subscriptionDAO.findByInstitutionId(institution.getId());
        if (subscription != null) {
            subscription.setStatus(InstitutionStatus.ACTIVE);
            subscriptionDAO.updateStatus(subscription);
        }
    }
}