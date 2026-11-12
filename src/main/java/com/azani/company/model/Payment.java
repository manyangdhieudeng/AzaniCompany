package com.azani.company.model;

import java.time.LocalDate;

public class Payment {

    private int id;
    private Institution institution;
    private PaymentType type;
    private double amount;
    private LocalDate paymentDate;

    // Used when recording a brand-new payment
    public Payment(Institution institution, PaymentType type, double amount) {
        this(institution, type, amount, LocalDate.now());
    }

    // Used when rebuilding a payment from the database, with its real stored date
    public Payment(Institution institution, PaymentType type, double amount, LocalDate paymentDate) {
        this.institution = institution;
        this.type = type;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    // Getters
    public int getId() { return id; }
    public Institution getInstitution() { return institution; }
    public PaymentType getType() { return type; }
    public double getAmount() { return amount; }
    public LocalDate getPaymentDate() { return paymentDate; }

    // Setters
    public void setId(int id) { this.id = id; }
}
