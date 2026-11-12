package com.azani.company.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.azani.company.model.College;
import com.azani.company.model.ContactPerson;
import com.azani.company.model.Institution;
import com.azani.company.model.InstitutionStatus;
import com.azani.company.model.JuniorSchool;
import com.azani.company.model.PrimarySchool;
import com.azani.company.model.SeniorSchool;
import com.azani.company.util.DBConnection;

public class InstitutionDAOImpl implements InstitutionDAO {

    private final ContactPersonDAO contactPersonDAO = new ContactPersonDAOImpl();

    @Override
    public void save(Institution institution) {
        // Step 1: save the contact person first, so it has a real id
        ContactPerson cp = institution.getContactPerson();
        contactPersonDAO.save(cp);

        // Step 2: insert the institution row
        String sql = "INSERT INTO institutions (name, category, contact_person_id, status, registration_date) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, institution.getName());
            stmt.setString(2, institution.getCategory());
            stmt.setInt(3, cp.getId());
            stmt.setString(4, institution.getStatus().name());
            stmt.setDate(5, Date.valueOf(institution.getRegistrationDate()));
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                institution.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
public void updateStatus(Institution institution) {
    String sql = "UPDATE institutions SET status = ? WHERE id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, institution.getStatus().name());
        stmt.setInt(2, institution.getId());
        stmt.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    @Override
    public Institution findById(int id) {
        String sql = "SELECT * FROM institutions WHERE id = ?";
        Institution institution = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                institution = buildInstitutionFromRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return institution;
    }

    @Override
    public List<Institution> findAll() {
        List<Institution> institutions = new ArrayList<>();
        String sql = "SELECT * FROM institutions";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                institutions.add(buildInstitutionFromRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return institutions;
    }

    // Shared helper: turns one database row into the correct Institution subclass
    private Institution buildInstitutionFromRow(ResultSet rs) throws SQLException {
        String category = rs.getString("category");
        int contactPersonId = rs.getInt("contact_person_id");
        ContactPerson cp = contactPersonDAO.findById(contactPersonId);
        String name = rs.getString("name");

        Institution institution = switch (category) {
            case "Primary School" -> new PrimarySchool(name, cp);
            case "Junior School" -> new JuniorSchool(name, cp);
            case "Senior School" -> new SeniorSchool(name, cp);
            case "College" -> new College(name, cp);
            default -> throw new IllegalStateException("Unknown category: " + category);
        };

        institution.setId(rs.getInt("id"));
        institution.setStatus(InstitutionStatus.valueOf(rs.getString("status")));
        return institution;
    }
}
