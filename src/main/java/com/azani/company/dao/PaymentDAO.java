package com.azani.company.dao;

import java.util.List;

import com.azani.company.model.Payment;

public interface PaymentDAO {
    void save(Payment payment);
    Payment findById(int id);
    List<Payment> findAll();
}
