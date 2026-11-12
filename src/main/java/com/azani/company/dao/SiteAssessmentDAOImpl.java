package com.azani.company.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.azani.company.model.Institution;
import com.azani.company.model.SiteAssessment;
import com.azani.company.util.DBConnection;

public class SiteAssessmentDAOImpl implements SiteAssessmentDAO {

    private final InstitutionDAO institutionDAO = new InstitutionDAOImpl();

    @Override
    public void save(SiteAssessment assessment) {
        String sql = "INSERT INTO site_assessments (institution_id, number_of_users, existing_computers, existing_lan_nodes, ready) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, assessment.getInstitution().getId());
            stmt.setInt(2, assessment.getNumberOfUsers());
            stmt.setInt(3, assessment.getExistingComputers());
            stmt.setInt(4, assessment.getExistingLanNodes());
            stmt.setBoolean(5, assessment.isReady());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                assessment.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SiteAssessment findById(int id) {
        String sql = "SELECT * FROM site_assessments WHERE id = ?";
        SiteAssessment assessment = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                assessment = buildAssessmentFromRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assessment;
    }

    @Override
    public List<SiteAssessment> findAll() {
        List<SiteAssessment> assessments = new ArrayList<>();
        String sql = "SELECT * FROM site_assessments";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                assessments.add(buildAssessmentFromRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assessments;
    }

    private SiteAssessment buildAssessmentFromRow(ResultSet rs) throws SQLException {
        int institutionId = rs.getInt("institution_id");
        Institution institution = institutionDAO.findById(institutionId);

        SiteAssessment assessment = new SiteAssessment(
                institution,
                rs.getInt("number_of_users"),
                rs.getInt("existing_computers"),
                rs.getInt("existing_lan_nodes")
        );
        assessment.setId(rs.getInt("id"));
        assessment.setReady(rs.getBoolean("ready"));

        return assessment;
    }
}