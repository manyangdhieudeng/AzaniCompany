package com.azani.company.dao;

import com.azani.company.model.Staff;

public interface StaffDAO {
    Staff login(String username, String password);
}