package com.azani.company.model;

public class Bill {

    private int id;
    private Institution institution;
    private double amountDue;
    private boolean fineApplied;
    private String billMonth;
    private boolean paid;

    public Bill(Institution institution, double amountDue, String billMonth) {
        this.institution = institution;
        this.amountDue = amountDue;
        this.billMonth = billMonth;
        this.fineApplied = false;
        this.paid = false;
    }

    // Getters
    public int getId() { return id; }
    public Institution getInstitution() { return institution; }
    public double getAmountDue() { return amountDue; }
    public boolean isFineApplied() { return fineApplied; }
    public String getBillMonth() { return billMonth; }
    public boolean isPaid() { return paid; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setAmountDue(double amountDue) { this.amountDue = amountDue; }
    public void setFineApplied(boolean fineApplied) { this.fineApplied = fineApplied; }
    public void setPaid(boolean paid) { this.paid = paid; }
}
