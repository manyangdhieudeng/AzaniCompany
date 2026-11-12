package com.azani.company.model;

public class LanPricing {

    private int id;
    private int minNodes;
    private int maxNodes;
    private double cost;

    public LanPricing(int minNodes, int maxNodes, double cost) {
        this.minNodes = minNodes;
        this.maxNodes = maxNodes;
        this.cost = cost;
    }

    // Getters
    public int getId() { return id; }
    public int getMinNodes() { return minNodes; }
    public int getMaxNodes() { return maxNodes; }
    public double getCost() { return cost; }

    // A handy helper method: does a given node count fall in this bracket?
    public boolean coversNodeCount(int nodeCount) {
        return nodeCount >= minNodes && nodeCount <= maxNodes;
    }

    // Setters
    public void setId(int id) { this.id = id; }
}