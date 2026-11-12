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
import com.azani.company.model.Payment;
import com.azani.company.model.PaymentType;
import com.azani.company.util.DBConnection;

public class PaymentDAOImpl implements PaymentDAO {

    private final InstitutionDAO institutionDAO = new InstitutionDAOImpl();

    @Override
    public void save(Payment payment) {
        String sql = "INSERT INTO payments (institution_id, type, amount, payment_date) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, payment.getInstitution().getId());
            stmt.setString(2, payment.getType().name());
            stmt.setDouble(3, payment.getAmount());
            stmt.setDate(4, Date.valueOf(payment.getPaymentDate()));
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                payment.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Payment findById(int id) {
        String sql = "SELECT * FROM payments WHERE id = ?";
        Payment payment = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                payment = buildPaymentFromRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payment;
    }

    @Override
    public List<Payment> findAll() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                payments.add(buildPaymentFromRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    private Payment buildPaymentFromRow(ResultSet rs) throws SQLException {
        int institutionId = rs.getInt("institution_id");
        Institution institution = institutionDAO.findById(institutionId);
        PaymentType type = PaymentType.valueOf(rs.getString("type"));
        LocalDate paymentDate = rs.getDate("payment_date").toLocalDate();

        Payment payment = new Payment(institution, type, rs.getDouble("amount"), paymentDate);
        payment.setId(rs.getInt("id"));

        return payment;
    }
}
