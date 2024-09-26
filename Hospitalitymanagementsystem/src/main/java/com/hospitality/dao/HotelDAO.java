package com.hospitality.dao;
import com.hospitality.ui.DatabaseConnector;
import com.hospitality.model.Hotel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelDAO {

    public void addHotel(Hotel hotel) throws SQLException {
        String query = "INSERT INTO hotels (name, address, rating) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, hotel.getName());
            preparedStatement.setString(2, hotel.getAddress());
            preparedStatement.setDouble(3, hotel.getRating());

            preparedStatement.executeUpdate();
        }
    }

    public List<Hotel> getAllHotels() throws SQLException {
        String query = "SELECT * FROM hotels";
        List<Hotel> hotelList = new ArrayList<>();

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Hotel hotel = new Hotel(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("address"),
                        resultSet.getDouble("rating")
                );
                hotelList.add(hotel);
            }
        }
        return hotelList;
    }

     // Method to retrieve a hotel by its ID (new method)
     public Hotel getHotelById(int id) throws SQLException {
        String query = "SELECT * FROM hotels WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Hotel(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("address"),
                            resultSet.getDouble("rating")
                    );
                }
            }
        }
        return null;
    }

    public void updateHotel(Hotel hotel) throws SQLException {
        String query = "UPDATE hotels SET name = ?, address = ?, rating = ? WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, hotel.getName());
            preparedStatement.setString(2, hotel.getAddress());
            preparedStatement.setDouble(3, hotel.getRating());
            preparedStatement.setInt(4, hotel.getId());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteHotel(int id) throws SQLException {
        String query = "DELETE FROM hotels WHERE id = ?";
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
}
