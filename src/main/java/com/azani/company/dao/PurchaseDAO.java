package com.azani.company.dao;

import java.util.List;

import  com.azani.company.model.Purchase;

public interface PurchaseDAO {
    void save(Purchase purchase);
    Purchase findById(int id);
    List<Purchase> findAll();
}
