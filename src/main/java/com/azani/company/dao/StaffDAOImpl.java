package com.azani.company.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.azani.company.model.Role;
import com.azani.company.model.Staff;
import com.azani.company.util.DBConnection;

public class StaffDAOImpl implements StaffDAO {

    @Override
    public Staff login(String username, String password) {
        String sql = "SELECT * FROM staff WHERE username = ? AND password = ?";
        Staff staff = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                staff = new Staff(rs.getString("username"), rs.getString("password"), Role.valueOf(rs.getString("role")));
                staff.setId(rs.getInt("id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staff;
    }
}