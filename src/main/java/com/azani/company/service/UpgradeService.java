package com.azani.company.service;

import com.azani.company.dao.SubscriptionDAO;
import com.azani.company.dao.SubscriptionDAOImpl;
import com.azani.company.model.BandwidthPlan;
import com.azani.company.model.InstitutionStatus;
import com.azani.company.model.Subscription;

public class UpgradeService {

    private final SubscriptionDAO subscriptionDAO = new SubscriptionDAOImpl();

    // Use Case: Process Bandwidth Upgrade
    public void upgradeBandwidth(Subscription subscription, BandwidthPlan newPlan) {

        if (subscription.getStatus() != InstitutionStatus.ACTIVE) {
            throw new IllegalStateException("Only active subscriptions can be upgraded.");
        }

        if (newPlan.getSpeedMbps() <= subscription.getBandwidthPlan().getSpeedMbps()) {
            throw new IllegalArgumentException("New plan must be a higher bandwidth than the current plan.");
        }

        subscription.setBandwidthPlan(newPlan);
        subscription.setUpgraded(true);
        subscriptionDAO.updateBandwidthPlan(subscription);

        subscription.setDiscountActive(true);
        subscriptionDAO.updateDiscountActive(subscription);
    }
}