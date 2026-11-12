package com.azani.company.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.azani.company.model.ContactPerson;
import com.azani.company.util.DBConnection;

public class ContactPersonDAOImpl implements ContactPersonDAO {

    @Override
    public void save(ContactPerson person) {
        String sql = "INSERT INTO contact_persons (name, phone_number, email) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, person.getName());
            stmt.setString(2, person.getPhoneNumber());
            stmt.setString(3, person.getEmail());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                person.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ContactPerson findById(int id) {
        String sql = "SELECT * FROM contact_persons WHERE id = ?";
        ContactPerson person = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                person = new ContactPerson(rs.getString("name"), rs.getString("phone_number"), rs.getString("email"));
                person.setId(rs.getInt("id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }

    @Override
    public List<ContactPerson> findAll() {
        List<ContactPerson> people = new ArrayList<>();
        String sql = "SELECT * FROM contact_persons";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ContactPerson person = new ContactPerson(rs.getString("name"), rs.getString("phone_number"), rs.getString("email"));
                person.setId(rs.getInt("id"));
                people.add(person);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return people;
    }
}
