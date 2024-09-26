package com.hospitality.model;

public class Room {
    private int id;
    private int hotelId;
    private int roomNumber;
    private String type;
    private double price;

    public Room(int id, int hotelId, int roomNumber, String type, double price) {
        this.id = id;
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
