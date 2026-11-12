package com.azani.company.model;

import java.time.LocalDate;

public class Subscription {

    private int id;
    private Institution institution;
    private BandwidthPlan bandwidthPlan;
    private InstitutionStatus status;
    private boolean discountActive;
    private LocalDate startDate;
    private boolean upgraded;

    // Used when creating a brand-new subscription (e.g. at installation time)
    public Subscription(Institution institution, BandwidthPlan bandwidthPlan) {
        this(institution, bandwidthPlan, LocalDate.now());
    }

    // Used when rebuilding a subscription from the database, with its real stored start date
    public Subscription(Institution institution, BandwidthPlan bandwidthPlan, LocalDate startDate) {
        this.institution = institution;
        this.bandwidthPlan = bandwidthPlan;
        this.status = InstitutionStatus.ACTIVE;
        this.discountActive = false;
        this.startDate = startDate;
    }

    // Getters
    public int getId() { return id; }
    public Institution getInstitution() { return institution; }
    public BandwidthPlan getBandwidthPlan() { return bandwidthPlan; }
    public InstitutionStatus getStatus() { return status; }
    public boolean isDiscountActive() { return discountActive; }
    public LocalDate getStartDate() { return startDate; }
    public boolean isUpgraded() { return upgraded; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setBandwidthPlan(BandwidthPlan bandwidthPlan) { this.bandwidthPlan = bandwidthPlan; }
    public void setStatus(InstitutionStatus status) { this.status = status; }
    public void setDiscountActive(boolean discountActive) { this.discountActive = discountActive;}
    public void setUpgraded(boolean upgraded) { this.upgraded = upgraded; }
}