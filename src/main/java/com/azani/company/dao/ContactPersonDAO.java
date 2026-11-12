package com.azani.company.dao;

import java.util.List;

import com.azani.company.model.ContactPerson;

public interface ContactPersonDAO {
    void save(ContactPerson person);
    ContactPerson findById(int id);
    List<ContactPerson> findAll();   
}