package com.azani.company.dao;

import java.util.List;

import com.azani.company.model.LanPricing;

public interface LanPricingDAO {
    void save(LanPricing pricing);
    LanPricing findById(int id);
    List<LanPricing> findAll();
}
