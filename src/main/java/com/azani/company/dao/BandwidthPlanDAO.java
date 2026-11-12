package com.azani.company.dao;

import java.util.List;

import com.azani.company.model.BandwidthPlan;

public interface BandwidthPlanDAO {
    void save(BandwidthPlan plan);
    BandwidthPlan findById(int id);
    List<BandwidthPlan> findAll();
}
