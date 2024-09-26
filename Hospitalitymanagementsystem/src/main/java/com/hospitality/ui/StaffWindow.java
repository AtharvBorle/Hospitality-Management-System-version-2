package com.hospitality.ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.hospitality.dao.ReservationDAO;
import com.hospitality.dao.GuestDAO;
import com.hospitality.dao.RoomDAO;
import com.hospitality.model.Reservation;
import com.hospitality.model.Guest;
import com.hospitality.model.Room;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class StaffWindow extends Application {
    private ReservationDAO reservationDAO;
    private GuestDAO guestDAO;
    private RoomDAO roomDAO;

    public StaffWindow() {
        this.reservationDAO = new ReservationDAO();
        this.guestDAO = new GuestDAO();
        this.roomDAO = new RoomDAO();
    }

    @SuppressWarnings("exports")
    @Override
    public void start(Stage primaryStage) {
        // Set up the layout
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        // Add a background image
        BackgroundImage bgImage = new BackgroundImage(
            new Image(getClass().getResourceAsStream("/com/hospitality/images/room.jpg")),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        layout.setBackground(new Background(bgImage));

        // Create buttons
        Button viewReservationsBtn = new Button("View Reservations");
        Button addReservationBtn = new Button("Add Reservation");
        Button updateReservationBtn = new Button("Update Reservation");
        Button deleteReservationBtn = new Button("Delete Reservation");
        Button manageGuestsBtn = new Button("Manage Guests");
        Button manageRoomsBtn = new Button("Manage Rooms");
        Button exitBtn = new Button("Exit");

        // Add buttons to a VBox
        VBox centerLayout = new VBox(15);
        centerLayout.setPadding(new Insets(20));
        centerLayout.getChildren().addAll(
                viewReservationsBtn, addReservationBtn, updateReservationBtn,
                deleteReservationBtn, manageGuestsBtn, manageRoomsBtn, exitBtn);
        layout.setCenter(centerLayout);

        // Event handlers for buttons
        viewReservationsBtn.setOnAction(e -> {
            try {
                viewReservations();
            } catch (SQLException ex) {
                showError(ex.getMessage());
            }
        });

        addReservationBtn.setOnAction(e -> showAddReservationDialog());

        updateReservationBtn.setOnAction(e -> showUpdateReservationDialog());

        deleteReservationBtn.setOnAction(e -> showDeleteReservationDialog());

        manageGuestsBtn.setOnAction(e -> showManageGuestsDialog());

        manageRoomsBtn.setOnAction(e -> showManageRoomsDialog());

        exitBtn.setOnAction(e -> primaryStage.close());

        // Set up the scene and stage
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setTitle("Staff Panel");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to view all reservations
    private void viewReservations() throws SQLException {
        List<Reservation> reservations = reservationDAO.getAllReservations();
        StringBuilder reservationInfo = new StringBuilder();
        for (Reservation reservation : reservations) {
            reservationInfo.append("Reservation ID: ").append(reservation.getId())
                    .append(", Guest ID: ").append(reservation.getGuestId())
                    .append(", Room ID: ").append(reservation.getRoomId())
                    .append(", Hotel ID: ").append(reservation.getHotelId())
                    .append(", Status: ").append(reservation.getStatus())
                    .append(", Check-in: ").append(reservation.getCheckIn())
                    .append(", Check-out: ").append(reservation.getCheckOut()).append("\n");
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reservations");
        alert.setHeaderText("All Reservations");
        alert.setContentText(reservationInfo.toString());
        alert.showAndWait();
    }

    // Method to show dialog for adding a new reservation
    private void showAddReservationDialog() {
        Dialog<Reservation> dialog = new Dialog<>();
        dialog.setTitle("Add Reservation");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField guestIdField = new TextField();
        guestIdField.setPromptText("Guest ID");
        TextField roomIdField = new TextField();
        roomIdField.setPromptText("Room ID");
        TextField hotelIdField = new TextField();
        hotelIdField.setPromptText("Hotel ID");
        TextField statusField = new TextField();
        statusField.setPromptText("Status");
        DatePicker checkInField = new DatePicker();
        DatePicker checkOutField = new DatePicker();

        grid.add(new Label("Guest ID:"), 0, 0);
        grid.add(guestIdField, 1, 0);
        grid.add(new Label("Room ID:"), 0, 1);
        grid.add(roomIdField, 1, 1);
        grid.add(new Label("Hotel ID:"), 0, 2);
        grid.add(hotelIdField, 1, 2);
        grid.add(new Label("Status:"), 0, 3);
        grid.add(statusField, 1, 3);
        grid.add(new Label("Check-in:"), 0, 4);
        grid.add(checkInField, 1, 4);
        grid.add(new Label("Check-out:"), 0, 5);
        grid.add(checkOutField, 1, 5);

        dialog.getDialogPane().setContent(grid);
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                int guestId = Integer.parseInt(guestIdField.getText());
                int roomId = Integer.parseInt(roomIdField.getText());
                int hotelId = Integer.parseInt(hotelIdField.getText());
                String status = statusField.getText();
                Date checkIn = Date.valueOf(checkInField.getValue());
                Date checkOut = Date.valueOf(checkOutField.getValue());
                return new Reservation(0, guestId, roomId, hotelId, status, checkIn, checkOut);
            }
            return null;
        });

        dialog.showAndWait().ifPresent(reservation -> {
            try {
                reservationDAO.addReservation(reservation);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Reservation Added");
                alert.setContentText("Reservation added successfully!");
                alert.showAndWait();
            } catch (SQLException e) {
                showError(e.getMessage());
            }
        });
    }

    // Method to show dialog for updating an existing reservation
    private void showUpdateReservationDialog() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Update Reservation");
        idDialog.setHeaderText("Enter Reservation ID to Update");
        Integer reservationId = idDialog.showAndWait().map(Integer::parseInt).orElse(null);

        if (reservationId != null) {
            try {
                Reservation existingReservation = reservationDAO.getReservationById(reservationId);
                if (existingReservation != null) {
                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(10);
                    grid.setPadding(new Insets(20, 150, 10, 10));

                    TextField guestIdField = new TextField(String.valueOf(existingReservation.getGuestId()));
                    TextField roomIdField = new TextField(String.valueOf(existingReservation.getRoomId()));
                    TextField hotelIdField = new TextField(String.valueOf(existingReservation.getHotelId()));
                    TextField statusField = new TextField(existingReservation.getStatus());
                    DatePicker checkInField = new DatePicker(existingReservation.getCheckIn().toLocalDate());
                    DatePicker checkOutField = new DatePicker(existingReservation.getCheckOut().toLocalDate());

                    grid.add(new Label("Guest ID:"), 0, 0);
                    grid.add(guestIdField, 1, 0);
                    grid.add(new Label("Room ID:"), 0, 1);
                    grid.add(roomIdField, 1, 1);
                    grid.add(new Label("Hotel ID:"), 0, 2);
                    grid.add(hotelIdField, 1, 2);
                    grid.add(new Label("Status:"), 0, 3);
                    grid.add(statusField, 1, 3);
                    grid.add(new Label("Check-in:"), 0, 4);
                    grid.add(checkInField, 1, 4);
                    grid.add(new Label("Check-out:"), 0, 5);
                    grid.add(checkOutField, 1, 5);

                    Dialog<Reservation> dialog = new Dialog<>();
                    dialog.setTitle("Update Reservation");
                    dialog.getDialogPane().setContent(grid);
                    ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
                    dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == updateButtonType) {
                            int guestId = Integer.parseInt(guestIdField.getText());
                            int roomId = Integer.parseInt(roomIdField.getText());
                            int hotelId = Integer.parseInt(hotelIdField.getText());
                            String status = statusField.getText();
                            Date checkIn = Date.valueOf(checkInField.getValue());
                            Date checkOut = Date.valueOf(checkOutField.getValue());
                            return new Reservation(reservationId, guestId, roomId, hotelId, status, checkIn, checkOut);
                        }
                        return null;
                    });

                    dialog.showAndWait().ifPresent(updatedReservation -> {
                        try {
                            reservationDAO.updateReservation(updatedReservation);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Success");
                            alert.setHeaderText("Reservation Updated");
                            alert.setContentText("Reservation updated successfully!");
                            alert.showAndWait();
                        } catch (SQLException e) {
                            showError(e.getMessage());
                        }
                    });
                } else {
                    showError("Reservation not found.");
                }
            } catch (SQLException e) {
                showError(e.getMessage());
            }
        }
    }

    // Method to show dialog for deleting a reservation
    private void showDeleteReservationDialog() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Delete Reservation");
        idDialog.setHeaderText("Enter Reservation ID to Delete");
        Integer reservationId = idDialog.showAndWait().map(Integer::parseInt).orElse(null);

        if (reservationId != null) {
            try {
                reservationDAO.deleteReservation(reservationId);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Reservation Deleted");
                alert.setContentText("Reservation deleted successfully!");
                alert.showAndWait();
            } catch (SQLException e) {
                showError(e.getMessage());
            }
        }
    }

    // Method to show dialog for managing guests
   

    // Method to show dialog for managing rooms
   

    private void showManageGuestsDialog() {
        Dialog<String> actionDialog = new Dialog<>();
        actionDialog.setTitle("Manage Guests");
    
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
    
        Button addGuestBtn = new Button("Add Guest");
        Button viewGuestsBtn = new Button("View Guests");
        Button updateGuestBtn = new Button("Update Guest");
        Button deleteGuestBtn = new Button("Delete Guest");
    
        layout.getChildren().addAll(addGuestBtn, viewGuestsBtn, updateGuestBtn, deleteGuestBtn);
        actionDialog.getDialogPane().setContent(layout);
        actionDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
    
        // Add Guest Action
        addGuestBtn.setOnAction(e -> {
            showGuestForm(null);  // Open the form to add a new guest (Guest ID is null for new guests)
        });
    
        // View Guests Action
        viewGuestsBtn.setOnAction(e -> {
            try {
                viewGuests();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    
        // Update Guest Action
        updateGuestBtn.setOnAction(e -> {
            TextInputDialog idDialog = new TextInputDialog();
            idDialog.setTitle("Update Guest");
            idDialog.setHeaderText("Enter Guest ID to Update");
            idDialog.showAndWait().ifPresent(idString -> {
                int guestId = Integer.parseInt(idString);
                try {
                    Guest existingGuest = guestDAO.getGuestById(guestId);
                    if (existingGuest != null) {
                        showGuestForm(existingGuest);  // Open the form with guest details for updating
                    } else {
                        showAlert("Guest Not Found", "No guest found with ID: " + guestId);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
        });
    
        // Delete Guest Action
        deleteGuestBtn.setOnAction(e -> {
            TextInputDialog idDialog = new TextInputDialog();
            idDialog.setTitle("Delete Guest");
            idDialog.setHeaderText("Enter Guest ID to Delete");
            idDialog.showAndWait().ifPresent(idString -> {
                int guestId = Integer.parseInt(idString);
                try {
                    guestDAO.deleteGuest(guestId);
                    showAlert("Guest Deleted", "Guest with ID " + guestId + " has been deleted.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showAlert("Error", "Could not delete guest with ID: " + guestId);
                }
            });
        });
    
        actionDialog.showAndWait();
    }
    
    // Method to show guest form for adding or updating a guest
    private void showGuestForm(Guest existingGuest) {
        Dialog<Guest> dialog = new Dialog<>();
        dialog.setTitle(existingGuest == null ? "Add Guest" : "Update Guest");
    
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
    
        TextField guestIdField = new TextField();
        guestIdField.setPromptText("Guest ID (Leave empty to add new guest)");
        if (existingGuest != null) {
            guestIdField.setText(String.valueOf(existingGuest.getId()));
            guestIdField.setEditable(false); // Guest ID cannot be changed during update
        }
    
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        if (existingGuest != null) nameField.setText(existingGuest.getName());
    
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        if (existingGuest != null) emailField.setText(existingGuest.getEmail());
    
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");
        if (existingGuest != null) phoneField.setText(existingGuest.getPhone());
    
        grid.add(new Label("Guest ID:"), 0, 0);
        grid.add(guestIdField, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Phone:"), 0, 3);
        grid.add(phoneField, 1, 3);
    
        ButtonType submitButtonType = new ButtonType(existingGuest == null ? "Add" : "Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);
    
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                int guestId = guestIdField.getText().isEmpty() ? 0 : Integer.parseInt(guestIdField.getText());
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                return new Guest(guestId, name, email, phone);
            }
            return null;
        });
    
        dialog.showAndWait().ifPresent(guest -> {
            try {
                if (existingGuest == null) {
                    guestDAO.addGuest(guest);  // Add new guest
                    showAlert("Success", "Guest added successfully.");
                } else {
                    guestDAO.updateGuest(guest);  // Update existing guest
                    showAlert("Success", "Guest updated successfully.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Operation failed.");
            }
        });
    }
    
    // View all guests method
    private void viewGuests() throws SQLException {
        List<Guest> guests = guestDAO.getAllGuests();
        StringBuilder guestInfo = new StringBuilder();
        for (Guest guest : guests) {
            guestInfo.append("Guest ID: ").append(guest.getId())
                    .append(", Name: ").append(guest.getName())
                    .append(", Email: ").append(guest.getEmail())
                    .append(", Phone: ").append(guest.getPhone()).append("\n");
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Guest Information");
        alert.setHeaderText("All Guests");
        alert.setContentText(guestInfo.toString());
        alert.showAndWait();
    }
    
   
    
    private void showManageRoomsDialog() {
    Dialog<Room> dialog = new Dialog<>();
    dialog.setTitle("Manage Rooms");

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    // Text fields for input
    TextField roomIdField = new TextField();
    roomIdField.setPromptText("Room ID (Leave empty to add new room)");
    TextField hotelIdField = new TextField();
    hotelIdField.setPromptText("Hotel ID");
    TextField roomNumberField = new TextField();
    roomNumberField.setPromptText("Room Number");
    TextField typeField = new TextField();
    typeField.setPromptText("Room Type");
    TextField priceField = new TextField();
    priceField.setPromptText("Price");

    // Add all fields to the grid
    grid.add(new Label("Room ID:"), 0, 0);
    grid.add(roomIdField, 1, 0);
    grid.add(new Label("Hotel ID:"), 0, 1);
    grid.add(hotelIdField, 1, 1);
    grid.add(new Label("Room Number:"), 0, 2);
    grid.add(roomNumberField, 1, 2);
    grid.add(new Label("Type:"), 0, 3);
    grid.add(typeField, 1, 3);
    grid.add(new Label("Price:"), 0, 4);
    grid.add(priceField, 1, 4);

    // Button types for different operations
    ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
    ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
    ButtonType viewButtonType = new ButtonType("View", ButtonBar.ButtonData.LEFT);
    ButtonType deleteButtonType = new ButtonType("Delete", ButtonBar.ButtonData.OTHER);

    // Set dialog content and buttons
    dialog.getDialogPane().setContent(grid);
    dialog.getDialogPane().getButtonTypes().addAll(addButtonType, updateButtonType, viewButtonType, deleteButtonType, ButtonType.CANCEL);

    // Handle dialog button clicks
    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == addButtonType || dialogButton == updateButtonType) {
            try {
                int roomId = roomIdField.getText().isEmpty() ? 0 : Integer.parseInt(roomIdField.getText());
                int hotelId = Integer.parseInt(hotelIdField.getText());
                int roomNumber = Integer.parseInt(roomNumberField.getText());
                String type = typeField.getText();
                double price = Double.parseDouble(priceField.getText());
                return new Room(roomId, hotelId, roomNumber, type, price);
            } catch (NumberFormatException e) {
                showError("Invalid input. Please enter valid data.");
            }
        }
        return null;
    });

    // Handle room operations
    dialog.showAndWait().ifPresent(room -> {
        try {
            if (room.getId() == 0) {
                roomDAO.addRoom(room);  // Add new room
                showAlert("Room Added", "Room added successfully!");
            } else {
                roomDAO.updateRoom(room);  // Update existing room
                showAlert("Room Updated", "Room updated successfully!");
            }
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    });
    // View Rooms action
    dialog.getDialogPane().lookupButton(viewButtonType).addEventFilter(ActionEvent.ACTION, event -> {
        try {
            List<Room> rooms = roomDAO.getAllRooms();
            StringBuilder roomList = new StringBuilder();
            for (Room room : rooms) {
                roomList.append("ID: ").append(room.getId()).append(", Room Number: ").append(room.getRoomNumber())
                        .append(", Type: ").append(room.getType()).append(", Price: ").append(room.getPrice()).append("\n");
            }
            showAlert("Room List", roomList.toString());
        } catch (SQLException e) {
            showError(e.getMessage());
        }
        event.consume();
    });

    // Delete Room action
    dialog.getDialogPane().lookupButton(deleteButtonType).addEventFilter(ActionEvent.ACTION, event -> {
        try {
            int roomId = Integer.parseInt(roomIdField.getText());
            roomDAO.deleteRoom(roomId);
            showAlert("Room Deleted", "Room deleted successfully!");
        } catch (SQLException | NumberFormatException e) {
            showError(e.getMessage());
        }
        event.consume();
    });
}

// Utility method to show an informational message


    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    

    // Utility method to show error messages
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
