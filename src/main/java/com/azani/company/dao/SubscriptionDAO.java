package com.azani.company.dao;

import java.util.List;

import com.azani.company.model.Subscription;

public interface SubscriptionDAO {
    void save(Subscription subscription);
    Subscription findById(int id);
    List<Subscription> findAll();
    void updateDiscountActive(Subscription subscription);
    void updateStatus(Subscription subscription);
    Subscription findByInstitutionId(int institutionId);
    void updateBandwidthPlan(Subscription subscription);
}
