package com.hospitality.dao;

import com.hospitality.model.Guest;
import com.hospitality.ui.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {

    public void addGuest(Guest guest) throws SQLException {
        String query = "INSERT INTO guests (name, email, phone) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, guest.getName());
            preparedStatement.setString(2, guest.getEmail());
            preparedStatement.setString(3, guest.getPhone());

            preparedStatement.executeUpdate();
        }
    }

    public List<Guest> getAllGuests() throws SQLException {
        String query = "SELECT * FROM guests";
        List<Guest> guestList = new ArrayList<>();

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Guest guest = new Guest(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone")
                );
                guestList.add(guest);
            }
        }
        return guestList;
    }

    public Guest getGuestById(int id) throws SQLException {
        String query = "SELECT * FROM guests WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Guest(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("phone")
                    );
                }
            }
        }
        return null; // Return null if guest not found
    }

    public void updateGuest(Guest guest) throws SQLException {
        String query = "UPDATE guests SET name = ?, email = ?, phone = ? WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, guest.getName());
            preparedStatement.setString(2, guest.getEmail());
            preparedStatement.setString(3, guest.getPhone());
            preparedStatement.setInt(4, guest.getId());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteGuest(int id) throws SQLException {
        String query = "DELETE FROM guests WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
}
