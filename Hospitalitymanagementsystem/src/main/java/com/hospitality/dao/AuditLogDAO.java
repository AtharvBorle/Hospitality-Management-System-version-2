package com.hospitality.dao;

import com.hospitality.model.AuditLog;
import com.hospitality.ui.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditLogDAO {

    public void addAuditLog(AuditLog log) throws SQLException {
        String query = "INSERT INTO audit_log (user_id, action, timestamp) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, log.getUserId());
            statement.setString(2, log.getAction());
            statement.setTimestamp(3, log.getTimestamp());
            statement.executeUpdate();
        }
    }

    public List<AuditLog> getAllAuditLogs() throws SQLException {
        String query = "SELECT * FROM audit_log";
        List<AuditLog> logs = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                AuditLog log = new AuditLog(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("action"),
                        resultSet.getTimestamp("timestamp")
                );
                logs.add(log);
            }
        }
        return logs;
    }

    public void deleteAuditLog(int id) throws SQLException {
        String query = "DELETE FROM audit_log WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}

