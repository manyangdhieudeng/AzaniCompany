package com.azani.company.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.azani.company.model.Institution;
import com.azani.company.model.Purchase;
import com.azani.company.util.DBConnection;

public class PurchaseDAOImpl implements PurchaseDAO {

    private final InstitutionDAO institutionDAO = new InstitutionDAOImpl();

    @Override
    public void save(Purchase purchase) {
        String sql = "INSERT INTO purchases (institution_id, type, quantity, cost, purchase_date) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, purchase.getInstitution().getId());
            stmt.setString(2, purchase.getType());
            stmt.setInt(3, purchase.getQuantity());
            stmt.setDouble(4, purchase.getCost());
            stmt.setDate(5, Date.valueOf(purchase.getPurchaseDate()));
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                purchase.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Purchase findById(int id) {
        String sql = "SELECT * FROM purchases WHERE id = ?";
        Purchase purchase = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                purchase = buildPurchaseFromRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return purchase;
    }

    @Override
    public List<Purchase> findAll() {
        List<Purchase> purchases = new ArrayList<>();
        String sql = "SELECT * FROM purchases";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                purchases.add(buildPurchaseFromRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return purchases;
    }

    private Purchase buildPurchaseFromRow(ResultSet rs) throws SQLException {
        int institutionId = rs.getInt("institution_id");
        Institution institution = institutionDAO.findById(institutionId);
        LocalDate purchaseDate = rs.getDate("purchase_date").toLocalDate();

        Purchase purchase = new Purchase(
                institution,
                rs.getString("type"),
                rs.getInt("quantity"),
                rs.getDouble("cost"),
                purchaseDate
        );
        purchase.setId(rs.getInt("id"));

        return purchase;
    }
}
