package com.azani.company.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.azani.company.model.BandwidthPlan;
import com.azani.company.util.DBConnection;

public class BandwidthPlanDAOImpl implements BandwidthPlanDAO {

    @Override
    public void save(BandwidthPlan plan) {
        String sql = "INSERT INTO bandwidth_plans (speed_mbps, monthly_cost) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, plan.getSpeedMbps());
            stmt.setDouble(2, plan.getMonthlyCost());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BandwidthPlan findById(int id) {
        String sql = "SELECT * FROM bandwidth_plans WHERE id = ?";
        BandwidthPlan plan = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                plan = new BandwidthPlan(rs.getInt("speed_mbps"), rs.getDouble("monthly_cost"));
                plan.setId(rs.getInt("id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plan;
    }

    @Override
    public List<BandwidthPlan> findAll() {
        List<BandwidthPlan> plans = new ArrayList<>();
        String sql = "SELECT * FROM bandwidth_plans";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                BandwidthPlan plan = new BandwidthPlan(rs.getInt("speed_mbps"), rs.getDouble("monthly_cost"));
                plan.setId(rs.getInt("id"));
                plans.add(plan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plans;
    }
}
