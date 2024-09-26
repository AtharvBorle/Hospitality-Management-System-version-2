package com.hospitality.dao;

import com.hospitality.ui.DatabaseConnector;
import com.hospitality.model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    public void addRoom(Room room) throws SQLException {
        String query = "INSERT INTO rooms (hotel_id, room_number, type, price) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, room.getHotelId());
            preparedStatement.setInt(2, room.getRoomNumber());
            preparedStatement.setString(3, room.getType());
            preparedStatement.setDouble(4, room.getPrice());

            preparedStatement.executeUpdate();
        }
    }

    public List<Room> getAllRooms() throws SQLException {
        String query = "SELECT * FROM rooms";
        List<Room> roomList = new ArrayList<>();

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Room room = new Room(
                        resultSet.getInt("id"),
                        resultSet.getInt("hotel_id"),
                        resultSet.getInt("room_number"),
                        resultSet.getString("type"),
                        resultSet.getDouble("price")
                );
                roomList.add(room);
            }
        }
        return roomList;
    }

    public Room getRoomById(int id) throws SQLException {
        String query = "SELECT * FROM rooms WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Room(
                        resultSet.getInt("id"),
                        resultSet.getInt("hotel_id"),
                        resultSet.getInt("room_number"),
                        resultSet.getString("type"),
                        resultSet.getDouble("price")
                );
            } else {
                return null; // Room not found
            }
        }
    }

    public void updateRoom(Room room) throws SQLException {
        String query = "UPDATE rooms SET hotel_id = ?, room_number = ?, type = ?, price = ? WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, room.getHotelId());
            preparedStatement.setInt(2, room.getRoomNumber());
            preparedStatement.setString(3, room.getType());
            preparedStatement.setDouble(4, room.getPrice());
            preparedStatement.setInt(5, room.getId());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteRoom(int id) throws SQLException {
        String query = "DELETE FROM rooms WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
}
