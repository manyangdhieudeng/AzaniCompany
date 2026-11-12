package com.azani.company.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.azani.company.model.LanPricing;
import com.azani.company.util.DBConnection;

public class LanPricingDAOImpl implements LanPricingDAO {

    @Override
    public void save(LanPricing pricing) {
        String sql = "INSERT INTO lan_pricing (min_nodes, max_nodes, cost) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pricing.getMinNodes());
            stmt.setInt(2, pricing.getMaxNodes());
            stmt.setDouble(3, pricing.getCost());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public LanPricing findById(int id) {
        String sql = "SELECT * FROM lan_pricing WHERE id = ?";
        LanPricing pricing = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                pricing = new LanPricing(rs.getInt("min_nodes"), rs.getInt("max_nodes"), rs.getDouble("cost"));
                pricing.setId(rs.getInt("id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pricing;
    }

    @Override
    public List<LanPricing> findAll() {
        List<LanPricing> pricingList = new ArrayList<>();
        String sql = "SELECT * FROM lan_pricing";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LanPricing pricing = new LanPricing(rs.getInt("min_nodes"), rs.getInt("max_nodes"), rs.getDouble("cost"));
                pricing.setId(rs.getInt("id"));
                pricingList.add(pricing);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pricingList;
    }
}
