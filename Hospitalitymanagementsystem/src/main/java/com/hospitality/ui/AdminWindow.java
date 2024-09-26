package com.hospitality.ui;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.hospitality.dao.HotelDAO;
import com.hospitality.dao.UserDAO;
import com.hospitality.model.Hotel;
import com.hospitality.model.User;
import com.hospitality.service.EncryptionUtil;

import java.sql.SQLException;
import java.util.List;

public class AdminWindow extends Application {
    private HotelDAO hotelDAO;
    private UserDAO userDAO;

    @SuppressWarnings("exports")
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.hotelDAO = new HotelDAO();
        this.userDAO = new UserDAO();

        // Set up the layout
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        // Add a background image
        BackgroundImage bgImage = new BackgroundImage(
            new Image(getClass().getResourceAsStream("/com/hospitality/images/admin.jpg")),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        layout.setBackground(new Background(bgImage));

        // Top: Menu Bar
        MenuBar menuBar = new MenuBar();
        Menu hotelMenu = new Menu("Hotel");
        Menu userMenu = new Menu("User");
        menuBar.getMenus().addAll(hotelMenu, userMenu);
        layout.setTop(menuBar);

        // Center: Admin Options (buttons)
        VBox centerLayout = new VBox(15);
        centerLayout.setPadding(new Insets(20));
        Button viewHotelsBtn = new Button("View Hotels");
        Button addHotelBtn = new Button("Add Hotel");
        Button updateHotelBtn = new Button("Update Hotel");
        Button deleteHotelBtn = new Button("Delete Hotel");
        Button viewUsersBtn = new Button("View Users");
        Button addUserBtn = new Button("Add User");
        Button updateUserBtn = new Button("Update User");
        Button deleteUserBtn = new Button("Delete User");
        centerLayout.getChildren().addAll(viewHotelsBtn, addHotelBtn, updateHotelBtn, deleteHotelBtn, viewUsersBtn, addUserBtn, updateUserBtn, deleteUserBtn);
        layout.setCenter(centerLayout);

        // Event handling for buttons
        viewHotelsBtn.setOnAction(e -> {
            try {
                viewHotels();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        addHotelBtn.setOnAction(e -> showAddHotelDialog());
        updateHotelBtn.setOnAction(e -> showUpdateHotelDialog());
        deleteHotelBtn.setOnAction(e -> showDeleteHotelDialog());

        // User management
        viewUsersBtn.setOnAction(e -> {
            try {
                viewUsers();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        addUserBtn.setOnAction(e -> showAddUserDialog());
        updateUserBtn.setOnAction(e -> showUpdateUserDialog());
        deleteUserBtn.setOnAction(e -> showDeleteUserDialog());

        // Set up the scene and stage
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setTitle("Admin Panel");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // View hotels
    private void viewHotels() throws SQLException {
        List<Hotel> hotels = hotelDAO.getAllHotels();
        StringBuilder hotelInfo = new StringBuilder();
        for (Hotel hotel : hotels) {
            hotelInfo.append("Hotel: ").append(hotel.getName())
                    .append(", Address: ").append(hotel.getAddress())
                    .append(", Rating: ").append(hotel.getRating()).append("\n");
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hotel Information");
        alert.setHeaderText("All Hotels");
        alert.setContentText(hotelInfo.toString());
        alert.showAndWait();
    }

    // Dialog for adding a hotel
    private void showAddHotelDialog() {
        Dialog<Hotel> dialog = new Dialog<>();
        dialog.setTitle("Add Hotel");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Hotel Name");
        TextField addressField = new TextField();
        addressField.setPromptText("Hotel Address");
        TextField ratingField = new TextField();
        ratingField.setPromptText("Hotel Rating");

        grid.add(new Label("Hotel Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Hotel Address:"), 0, 1);
        grid.add(addressField, 1, 1);
        grid.add(new Label("Hotel Rating:"), 0, 2);
        grid.add(ratingField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Hotel(0, nameField.getText(), addressField.getText(), Double.parseDouble(ratingField.getText()));
            }
            return null;
        });

        dialog.showAndWait().ifPresent(hotel -> {
            try {
                hotelDAO.addHotel(hotel);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    // Dialog for updating a hotel
    private void showUpdateHotelDialog() {
        Dialog<Hotel> dialog = new Dialog<>();
        dialog.setTitle("Update Hotel");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField idField = new TextField();
        idField.setPromptText("Hotel ID");
        TextField nameField = new TextField();
        nameField.setPromptText("New Hotel Name");
        TextField addressField = new TextField();
        addressField.setPromptText("New Hotel Address");
        TextField ratingField = new TextField();
        ratingField.setPromptText("New Hotel Rating");

        grid.add(new Label("Hotel ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("New Hotel Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("New Hotel Address:"), 0, 2);
        grid.add(addressField, 1, 2);
        grid.add(new Label("New Hotel Rating:"), 0, 3);
        grid.add(ratingField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                return new Hotel(Integer.parseInt(idField.getText()), nameField.getText(), addressField.getText(), Double.parseDouble(ratingField.getText()));
            }
            return null;
        });

        dialog.showAndWait().ifPresent(hotel -> {
            try {
                hotelDAO.updateHotel(hotel);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    // Dialog for deleting a hotel
    private void showDeleteHotelDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Delete Hotel");

        TextField idField = new TextField();
        idField.setPromptText("Hotel ID");

        dialog.getDialogPane().setContent(idField);
        ButtonType deleteButtonType = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(deleteButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == deleteButtonType) {
                return idField.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(id -> {
            try {
                hotelDAO.deleteHotel(Integer.parseInt(id));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    // View users
    private void viewUsers() throws SQLException {
        List<User> users = userDAO.getAllUsers();
        StringBuilder userInfo = new StringBuilder();
        for (User user : users) {
            userInfo.append("User: ").append(user.getUsername())
                    .append(", Role: ").append(user.getRole()).append("\n");
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User Information");
        alert.setHeaderText("All Users");
        alert.setContentText(userInfo.toString());
        alert.showAndWait();
    }

    // Dialog for adding a user
    private void showAddUserDialog() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add User");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        TextField passwordField = new TextField();
        passwordField.setPromptText("Password");
        TextField roleField = new TextField();
        roleField.setPromptText("Role");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Role:"), 0, 2);
        grid.add(roleField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new User(0, usernameField.getText(), EncryptionUtil.hashPassword(passwordField.getText()) , roleField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(user -> {
            try {
                userDAO.addUser(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    // Dialog for updating a user
    private void showUpdateUserDialog() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Update User");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField idField = new TextField();
        idField.setPromptText("User ID");
        TextField usernameField = new TextField();
        usernameField.setPromptText("New Username");
        TextField passwordField = new TextField();
        passwordField.setPromptText("New Password");
        TextField roleField = new TextField();
        roleField.setPromptText("New Role");

        grid.add(new Label("User ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("New Username:"), 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(new Label("New Password:"), 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(new Label("New Role:"), 0, 3);
        grid.add(roleField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                return new User(Integer.parseInt(idField.getText()), usernameField.getText(), EncryptionUtil.hashPassword(passwordField.getText()), roleField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(user -> {
            try {
                userDAO.updateUser(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    // Dialog for deleting a user
    private void showDeleteUserDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Delete User");

        TextField idField = new TextField();
        idField.setPromptText("User ID");

        dialog.getDialogPane().setContent(idField);
        ButtonType deleteButtonType = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(deleteButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == deleteButtonType) {
                return idField.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(id -> {
            try {
                userDAO.deleteUser(Integer.parseInt(id));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
