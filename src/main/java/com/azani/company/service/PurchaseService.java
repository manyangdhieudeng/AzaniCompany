package com.azani.company.service;

import java.util.List;

import com.azani.company.dao.LanPricingDAO;
import com.azani.company.dao.LanPricingDAOImpl;
import com.azani.company.dao.PurchaseDAO;
import com.azani.company.dao.PurchaseDAOImpl;
import com.azani.company.model.Institution;
import com.azani.company.model.LanPricing;
import com.azani.company.model.Purchase;

public class PurchaseService {

    private final PurchaseDAO purchaseDAO = new PurchaseDAOImpl();
    private final LanPricingDAO lanPricingDAO = new LanPricingDAOImpl();

    private static final double COMPUTER_COST = 40000;

    // Fixed price per computer - just quantity x price
    public double calculateComputerCost(int quantity) {
        return quantity * COMPUTER_COST;
    }

    public Purchase recordComputerPurchase(Institution institution, int quantity) {
        double cost = calculateComputerCost(quantity);
        Purchase purchase = new Purchase(institution, "Computer", quantity, cost);
        purchaseDAO.save(purchase);
        return purchase;
    }

    // Looks up which bracket the node count falls into, from the lan_pricing table
    public double calculateLanCost(int nodeCount) {
        List<LanPricing> brackets = lanPricingDAO.findAll();
        for (LanPricing bracket : brackets) {
            if (bracket.coversNodeCount(nodeCount)) {
                return bracket.getCost();
            }
        }
        throw new IllegalArgumentException("No pricing bracket found for " + nodeCount + " nodes.");
    }

    public Purchase recordLanPurchase(Institution institution, int nodeCount) {
        double cost = calculateLanCost(nodeCount);
        Purchase purchase = new Purchase(institution, "LAN", nodeCount, cost);
        purchaseDAO.save(purchase);
        return purchase;
    }
}