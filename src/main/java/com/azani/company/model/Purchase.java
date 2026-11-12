package com.azani.company.model;

import java.time.LocalDate;

public class Purchase {

    private int id;
    private Institution institution;
    private String type;
    private int quantity;
    private double cost;
    private LocalDate purchaseDate;

    // Used when recording a brand-new purchase
    public Purchase(Institution institution, String type, int quantity, double cost) {
        this(institution, type, quantity, cost, LocalDate.now());
    }

    // Used when rebuilding a purchase from the database, with its real stored date
    public Purchase(Institution institution, String type, int quantity, double cost, LocalDate purchaseDate) {
        this.institution = institution;
        this.type = type;
        this.quantity = quantity;
        this.cost = cost;
        this.purchaseDate = purchaseDate;
    }

    // Getters
    public int getId() { return id; }
    public Institution getInstitution() { return institution; }
    public String getType() { return type; }
    public int getQuantity() { return quantity; }
    public double getCost() { return cost; }
    public LocalDate getPurchaseDate() { return purchaseDate; }

    // Setters
    public void setId(int id) { this.id = id; }
}