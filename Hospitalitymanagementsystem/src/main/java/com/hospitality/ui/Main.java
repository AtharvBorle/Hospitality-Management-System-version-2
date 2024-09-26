package com.hospitality.ui;

import com.hospitality.service.AuthService;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        AuthService authService = new AuthService();

        try {
            // Setup the first admin user if no users exist
            authService.setupFirstAdmin(); 

            // Launch the LoginWindow application
            LoginWindow.launch(LoginWindow.class, args);
        } catch (SQLException e) {
            System.err.println("An error occurred while setting up the admin user: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // Catch and handle any exception that might occur during application launch
            System.err.println("An error occurred while starting the application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
