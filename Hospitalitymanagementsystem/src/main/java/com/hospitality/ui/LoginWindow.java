package com.hospitality.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.hospitality.service.AuthService;
import java.sql.Timestamp;
import com.hospitality.model.AuditLog;
import com.hospitality.model.User;
import com.hospitality.dao.AuditLogDAO;

public class LoginWindow extends Application {
    private AuthService authService;
    private AuditLogDAO auditLogDAO;

    public LoginWindow() {
        this.authService = new AuthService();
        this.auditLogDAO = new AuditLogDAO();
    }

    @SuppressWarnings("exports")
    @Override
    public void start(Stage primaryStage) {
        // Create a GridPane for the login form
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        // Add background image
        BackgroundImage bgImage = new BackgroundImage(
                new Image(getClass().getResourceAsStream("/com/hospitality/images/login.jpg")),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        gridPane.setBackground(new Background(bgImage));

        // Add UI controls
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #0073e6; -fx-text-fill: white;");

        Label statusLabel = new Label();

        // Add components to the grid
        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(loginButton, 1, 2);
        gridPane.add(statusLabel, 1, 3);

        // Handle login button click
        loginButton.setOnAction(e -> handleLogin(usernameField, passwordField, statusLabel, primaryStage));

        // Setup the scene and stage
        Scene scene = new Scene(gridPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

    // Handle login logic
    private void handleLogin(TextField usernameField, PasswordField passwordField, Label statusLabel, Stage primaryStage) {
        // Trim whitespace from inputs
        String username = usernameField.getText().trim();
        
        String password = passwordField.getText().trim();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    
        try {
            // Attempt to login
            User user = authService.login(username, password);
            if (user != null) {
                AuditLog successLog = new AuditLog(0, user.getId(), "Login Success", timestamp);
                auditLogDAO.addAuditLog(successLog);
                statusLabel.setText("Welcome, " + user.getUsername() + "!");
                navigateBasedOnRole(user, primaryStage);
            } else {
                AuditLog failedLog = new AuditLog(0, 0, "Login Failed", timestamp);
                auditLogDAO.addAuditLog(failedLog); 
                statusLabel.setText("Invalid credentials. Please try again.");
                clearFields(usernameField, passwordField);
            }
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            clearFields(usernameField, passwordField);
        }
    }
    

    // Method to navigate to different windows based on the user's role
    private void navigateBasedOnRole(User user, Stage primaryStage) throws Exception {
        String role = user.getRole();

        switch (role.toLowerCase()) {
            case "admin":
                new AdminWindow().start(primaryStage);
                break;
            case "manager":
                new ManagerWindow().start(primaryStage);
                break;
            case "staff":
                new StaffWindow().start(primaryStage);
                break;
            default:
                System.out.println("Unknown role: " + role);
                break;
        }
    }

    // Clear input fields
    private void clearFields(TextField usernameField, PasswordField passwordField) {
        usernameField.clear();
        passwordField.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
