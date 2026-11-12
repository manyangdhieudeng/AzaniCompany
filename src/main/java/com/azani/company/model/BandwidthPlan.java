package com.azani.company.model;

public class BandwidthPlan {
    private int id;
    private int speedMbps;
    private double monthlyCost;

    public BandwidthPlan(int speedMbps, double monthlyCost){
        this.speedMbps = speedMbps;
        this.monthlyCost = monthlyCost;
    }
    //getters
    public int getId(){return id;}
    public int getSpeedMbps(){return speedMbps;}
    public double getMonthlyCost(){return monthlyCost;}

    //setters
    public void setId(int id){this.id = id;}

    @Override 
    public String toString(){
        return speedMbps + "Mbps - Ksh" + monthlyCost + "/monthly";
    }

}
