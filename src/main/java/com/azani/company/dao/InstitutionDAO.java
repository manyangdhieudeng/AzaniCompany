package com.azani.company.dao;

import java.util.List;

import com.azani.company.model.Institution;

public interface InstitutionDAO {
    void save(Institution institution);
    Institution findById(int id);
    List<Institution> findAll();
    void updateStatus(Institution institution);
}
