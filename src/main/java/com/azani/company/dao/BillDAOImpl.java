package com.azani.company.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.azani.company.model.Bill;
import com.azani.company.model.Institution;
import com.azani.company.util.DBConnection;

public class BillDAOImpl implements BillDAO {

    private final InstitutionDAO institutionDAO = new InstitutionDAOImpl();

    @Override
    public void save(Bill bill) {
        String sql = "INSERT INTO bills (institution_id, amount_due, fine_applied, bill_month, paid) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, bill.getInstitution().getId());
            stmt.setDouble(2, bill.getAmountDue());
            stmt.setBoolean(3, bill.isFineApplied());
            stmt.setString(4, bill.getBillMonth());
            stmt.setBoolean(5, bill.isPaid());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                bill.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePaidStatus(Bill bill) {
        String sql = "UPDATE bills SET paid = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, bill.isPaid());
            stmt.setInt(2, bill.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateFineAndAmount(Bill bill) {
        String sql = "UPDATE bills SET amount_due = ?, fine_applied = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, bill.getAmountDue());
            stmt.setBoolean(2, bill.isFineApplied());
            stmt.setInt(3, bill.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bill findById(int id) {
        String sql = "SELECT * FROM bills WHERE id = ?";
        Bill bill = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                bill = buildBillFromRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bill;
    }

    @Override
    public List<Bill> findAll() {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bills.add(buildBillFromRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    @Override
    public List<Bill> findDefaulters() {
        List<Bill> defaulters = new ArrayList<>();
        String sql = "SELECT * FROM bills WHERE fine_applied = TRUE AND paid = FALSE";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                defaulters.add(buildBillFromRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return defaulters;
    }

    @Override
public boolean existsForMonth(int institutionId, String billMonth) {
    String sql = "SELECT COUNT(*) FROM bills WHERE institution_id = ? AND bill_month = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, institutionId);
        stmt.setString(2, billMonth);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getInt(1) > 0;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    private Bill buildBillFromRow(ResultSet rs) throws SQLException {
        int institutionId = rs.getInt("institution_id");
        Institution institution = institutionDAO.findById(institutionId);

        Bill bill = new Bill(institution, rs.getDouble("amount_due"), rs.getString("bill_month"));
        bill.setId(rs.getInt("id"));
        bill.setFineApplied(rs.getBoolean("fine_applied"));
        bill.setPaid(rs.getBoolean("paid"));

        return bill;
    }
    @Override
public Bill findUnpaidByInstitution(int institutionId) {
    String sql = "SELECT * FROM bills WHERE institution_id = ? AND paid = FALSE ORDER BY id DESC LIMIT 1";
    Bill bill = null;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, institutionId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            bill = buildBillFromRow(rs);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return bill;
}
}