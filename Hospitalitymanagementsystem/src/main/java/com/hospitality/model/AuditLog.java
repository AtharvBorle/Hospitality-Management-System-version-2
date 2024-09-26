package com.hospitality.model;

import java.sql.Timestamp;

public class AuditLog {
    private int id;
    private int userId;
    private String action;
    private Timestamp timestamp;

    public AuditLog(int id, int userId, String action, Timestamp timestamp) {
        this.id = id;
        this.userId = userId;
        this.action = action;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
