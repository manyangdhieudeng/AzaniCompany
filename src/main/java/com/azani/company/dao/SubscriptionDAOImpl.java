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

import com.azani.company.model.BandwidthPlan;
import com.azani.company.model.Institution;
import com.azani.company.model.InstitutionStatus;
import com.azani.company.model.Subscription;
import com.azani.company.util.DBConnection;

public class SubscriptionDAOImpl implements SubscriptionDAO {

    private final InstitutionDAO institutionDAO = new InstitutionDAOImpl();
    private final BandwidthPlanDAO bandwidthPlanDAO = new BandwidthPlanDAOImpl();

    @Override
    public void save(Subscription subscription) {
        String sql = "INSERT INTO subscriptions (institution_id, bandwidth_plan_id, status, discount_active, start_date) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, subscription.getInstitution().getId());
            stmt.setInt(2, subscription.getBandwidthPlan().getId());
            stmt.setString(3, subscription.getStatus().name());
            stmt.setBoolean(4, subscription.isDiscountActive());
            stmt.setDate(5, Date.valueOf(subscription.getStartDate()));
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                subscription.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateDiscountActive(Subscription subscription) {
        String sql = "UPDATE subscriptions SET discount_active = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, subscription.isDiscountActive());
            stmt.setInt(2, subscription.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateStatus(Subscription subscription) {
        String sql = "UPDATE subscriptions SET status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, subscription.getStatus().name());
            stmt.setInt(2, subscription.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Subscription findByInstitutionId(int institutionId) {
        String sql = "SELECT * FROM subscriptions WHERE institution_id = ?";
        Subscription subscription = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, institutionId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                subscription = buildSubscriptionFromRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscription;
    }

    @Override
    public Subscription findById(int id) {
        String sql = "SELECT * FROM subscriptions WHERE id = ?";
        Subscription subscription = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                subscription = buildSubscriptionFromRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscription;
    }

    @Override
    public List<Subscription> findAll() {
        List<Subscription> subscriptions = new ArrayList<>();
        String sql = "SELECT * FROM subscriptions";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                subscriptions.add(buildSubscriptionFromRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscriptions;
    }

    private Subscription buildSubscriptionFromRow(ResultSet rs) throws SQLException {
        int institutionId = rs.getInt("institution_id");
        int bandwidthPlanId = rs.getInt("bandwidth_plan_id");

        Institution institution = institutionDAO.findById(institutionId);
        BandwidthPlan plan = bandwidthPlanDAO.findById(bandwidthPlanId);
        LocalDate startDate = rs.getDate("start_date").toLocalDate();

        Subscription subscription = new Subscription(institution, plan, startDate);
        subscription.setId(rs.getInt("id"));
        subscription.setStatus(InstitutionStatus.valueOf(rs.getString("status")));
        subscription.setDiscountActive(rs.getBoolean("discount_active"));
        subscription.setUpgraded(rs.getBoolean("upgraded"));

        return subscription;
    }
    @Override
public void updateBandwidthPlan(Subscription subscription) {
    String sql = "UPDATE subscriptions SET bandwidth_plan_id = ?, upgraded = ? WHERE id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, subscription.getBandwidthPlan().getId());
        stmt.setBoolean(2, subscription.isUpgraded());
        stmt.setInt(3, subscription.getId());
        stmt.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}
