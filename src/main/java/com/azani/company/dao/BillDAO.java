package com.azani.company.dao;

import java.util.List;

import com.azani.company.model.Bill;

public interface BillDAO {
    void save(Bill bill);
    Bill findById(int id);
    List<Bill> findAll();
    List<Bill> findDefaulters();
    void updatePaidStatus(Bill bill);
    void updateFineAndAmount(Bill bill);
    Bill findUnpaidByInstitution(int institutionId);
    boolean existsForMonth(int institutionId, String billMonth);
}
