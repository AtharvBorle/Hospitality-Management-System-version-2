package com.hospitality.dao;

import com.hospitality.ui.DatabaseConnector;
import com.hospitality.model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    public void addReservation(Reservation reservation) throws SQLException {
        String query = "INSERT INTO reservations (guest_id, room_id, hotel_id, status, check_in, check_out) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, reservation.getGuestId());
            preparedStatement.setInt(2, reservation.getRoomId());
            preparedStatement.setInt(3, reservation.getHotelId()); // Set hotel ID
            preparedStatement.setString(4, reservation.getStatus()); // Set status
            preparedStatement.setDate(5, reservation.getCheckIn());
            preparedStatement.setDate(6, reservation.getCheckOut());

            preparedStatement.executeUpdate();
        }
    }

    public List<Reservation> getAllReservations() throws SQLException {
        String query = "SELECT * FROM reservations";
        List<Reservation> reservationList = new ArrayList<>();

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Reservation reservation = new Reservation(
                        resultSet.getInt("id"),
                        resultSet.getInt("guest_id"),
                        resultSet.getInt("room_id"),
                        resultSet.getInt("hotel_id"), // Get hotel ID
                        resultSet.getString("status"), // Get status
                        resultSet.getDate("check_in"),
                        resultSet.getDate("check_out")
                );
                reservationList.add(reservation);
            }
        }
        return reservationList;
    }

    public void updateReservation(Reservation reservation) throws SQLException {
        String query = "UPDATE reservations SET guest_id = ?, room_id = ?, hotel_id = ?, status = ?, check_in = ?, check_out = ? WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, reservation.getGuestId());
            preparedStatement.setInt(2, reservation.getRoomId());
            preparedStatement.setInt(3, reservation.getHotelId()); // Set hotel ID
            preparedStatement.setString(4, reservation.getStatus()); // Set status
            preparedStatement.setDate(5, reservation.getCheckIn());
            preparedStatement.setDate(6, reservation.getCheckOut());
            preparedStatement.setInt(7, reservation.getId());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteReservation(int id) throws SQLException {
        String query = "DELETE FROM reservations WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    public Reservation getReservationById(int id) throws SQLException {
        String query = "SELECT * FROM reservations WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Reservation(
                            resultSet.getInt("id"),
                            resultSet.getInt("guest_id"),
                            resultSet.getInt("room_id"),
                            resultSet.getInt("hotel_id"), // Get hotel ID
                            resultSet.getString("status"), // Get status
                            resultSet.getDate("check_in"),
                            resultSet.getDate("check_out")
                    );
                }
            }
        }
        return null; // Return null if reservation not found
    }
}
