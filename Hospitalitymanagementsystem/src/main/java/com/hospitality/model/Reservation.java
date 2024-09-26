package com.hospitality.model;

import java.sql.Date;

public class Reservation {
    private int id;
    private int guestId;
    private int roomId;
    private int hotelId;  // New field for hotel ID
    private String status; // New field for reservation status
    private Date checkIn;
    private Date checkOut;

    public Reservation(int id, int guestId, int roomId, int hotelId, String status, Date checkIn, Date checkOut) {
        this.id = id;
        this.guestId = guestId;
        this.roomId = roomId;
        this.hotelId = hotelId;
        this.status = status;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getHotelId() {
        return hotelId; // Getter for hotel ID
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId; // Setter for hotel ID
    }

    public String getStatus() {
        return status; // Getter for status
    }

    public void setStatus(String status) {
        this.status = status; // Setter for status
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }
}
