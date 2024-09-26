package com.hospitality.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.hospitality.dao.AuditLogDAO;
import com.hospitality.dao.GuestDAO;
import com.hospitality.dao.HotelDAO;
import com.hospitality.dao.ReservationDAO;
import com.hospitality.dao.RoomDAO;
import com.hospitality.model.AuditLog;
import com.hospitality.model.Guest;
import com.hospitality.model.Hotel;
import com.hospitality.model.Reservation;
import com.hospitality.model.Room;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ManagerWindow extends Application {
    private AuditLogDAO auditLogDAO;
    private HotelDAO hotelDAO;
    private ReservationDAO reservationDAO;
    private GuestDAO guestDAO;
    private RoomDAO roomDAO;

    @SuppressWarnings("exports")
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.auditLogDAO = new AuditLogDAO();
        this.hotelDAO = new HotelDAO();
        this.reservationDAO = new ReservationDAO();
        this.guestDAO = new GuestDAO();
        this.roomDAO = new RoomDAO();

        // Set up the layout
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        // Add a background image
        BackgroundImage bgImage = new BackgroundImage(
            new Image(getClass().getResourceAsStream("/com/hospitality/images/manager.jpg")),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        layout.setBackground(new Background(bgImage));

        // Top: Menu Bar
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        menuBar.getMenus().addAll(fileMenu);
        layout.setTop(menuBar);

        // Center: Manager Options (buttons)
        VBox centerLayout = new VBox(15);
        centerLayout.setPadding(new Insets(20));
        Button viewAuditLogsBtn = new Button("View Audit Logs");
        Button manageHotelsButton = new Button("Manage Hotel");
        Button manageGuestBtn = new Button("Manage Guests");
        Button manageReservationsBtn = new Button("Manage Reservations");
        Button manageRoomsbtn = new Button("Manage Rooms");

        centerLayout.getChildren().addAll(viewAuditLogsBtn, manageHotelsButton, manageGuestBtn, manageReservationsBtn, manageRoomsbtn);
        layout.setCenter(centerLayout);

        // Event handling for buttons
        viewAuditLogsBtn.setOnAction(e -> {
            try {
                viewAuditLogs();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        manageHotelsButton.setOnAction(e -> openManageHotelsWindow());

        manageGuestBtn.setOnAction(e -> openManageGuestsWindow());

        manageReservationsBtn.setOnAction(e -> openManageReservationsWindow());

        manageRoomsbtn.setOnAction(e -> openManageRoomsWindow());

        // Set up the scene and stage
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setTitle("Manager Panel");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Opens a new window (stage) for managing hotels
    private void openManageHotelsWindow() {
        Stage manageHotelsStage = new Stage();
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        // Add buttons and actions for managing hotels (this can be expanded with more functionality)
        Button addHotelBtn = new Button("Add Hotel");
        Button updateHotelBtn = new Button("Update Hotel");
        Button viewHotelsBtn = new Button("View Hotels");
        Button deleteHotelsBtn = new Button("delete Hotels");

        layout.getChildren().addAll(addHotelBtn, updateHotelBtn, viewHotelsBtn, deleteHotelsBtn);

        // Button actions
        addHotelBtn.setOnAction(e -> showAddHotelDialog());
        updateHotelBtn.setOnAction(e -> showUpdateHotelDialog());
        viewHotelsBtn.setOnAction(e -> {
            try {
                viewHotels();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        deleteHotelsBtn.setOnAction(e -> showdeleteHotelDialog());


        Scene scene = new Scene(layout, 400, 300);
        manageHotelsStage.setTitle("Manage Hotels");
        manageHotelsStage.setScene(scene);
        manageHotelsStage.show();
    }

    // Opens a new window (stage) for managing guests
   // Opens a new window (stage) for managing guests
private void openManageGuestsWindow() {
    Stage manageGuestsStage = new Stage();
    VBox layout = new VBox(15);
    layout.setPadding(new Insets(20));

    Button addGuestBtn = new Button("Add Guest");
    Button viewGuestsBtn = new Button("View Guests");
    Button updateGuestBtn = new Button("Update Guest");
    Button deleteGuestBtn = new Button("Delete Guest");

    layout.getChildren().addAll(addGuestBtn, viewGuestsBtn, updateGuestBtn, deleteGuestBtn);

    // Event handling
    addGuestBtn.setOnAction(e -> showAddGuestDialog());
    viewGuestsBtn.setOnAction(e -> {
        try {
            viewGuests();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    });
    updateGuestBtn.setOnAction(e -> showUpdateGuestDialog());
    deleteGuestBtn.setOnAction(e -> showDeleteGuestDialog());

    Scene scene = new Scene(layout, 400, 300);
    manageGuestsStage.setTitle("Manage Guests");
    manageGuestsStage.setScene(scene);
    manageGuestsStage.show();
}

// Dialog to add a guest
private void showAddGuestDialog() {
    Dialog<Guest> dialog = new Dialog<>();
    dialog.setTitle("Add Guest");

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    TextField nameField = new TextField();
    nameField.setPromptText("Name");
    TextField emailField = new TextField();
    emailField.setPromptText("Email");
    TextField phoneField = new TextField();
    phoneField.setPromptText("Phone");

    grid.add(new Label("Name:"), 0, 0);
    grid.add(nameField, 1, 0);
    grid.add(new Label("Email:"), 0, 1);
    grid.add(emailField, 1, 1);
    grid.add(new Label("Phone:"), 0, 2);
    grid.add(phoneField, 1, 2);

    dialog.getDialogPane().setContent(grid);
    ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == addButtonType) {
            return new Guest(0, nameField.getText(), emailField.getText(), phoneField.getText());
        }
        return null;
    });

    dialog.showAndWait().ifPresent(guest -> {
        try {
            guestDAO.addGuest(guest);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    });
}

// Dialog to update a guest
private void showUpdateGuestDialog() {
    TextInputDialog idDialog = new TextInputDialog();
    idDialog.setTitle("Update Guest");
    idDialog.setHeaderText("Enter Guest ID to Update");
    idDialog.showAndWait().ifPresent(idString -> {
        int id = Integer.parseInt(idString);
        try {
            Guest existingGuest = guestDAO.getGuestById(id);
            if (existingGuest != null) {
                Dialog<Guest> dialog = new Dialog<>();
                dialog.setTitle("Update Guest");

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField nameField = new TextField(existingGuest.getName());
                TextField emailField = new TextField(existingGuest.getEmail());
                TextField phoneField = new TextField(existingGuest.getPhone());

                grid.add(new Label("Name:"), 0, 0);
                grid.add(nameField, 1, 0);
                grid.add(new Label("Email:"), 0, 1);
                grid.add(emailField, 1, 1);
                grid.add(new Label("Phone:"), 0, 2);
                grid.add(phoneField, 1, 2);

                dialog.getDialogPane().setContent(grid);
                ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == updateButtonType) {
                        return new Guest(id, nameField.getText(), emailField.getText(), phoneField.getText());
                    }
                    return null;
                });

                dialog.showAndWait().ifPresent(guest -> {
                    try {
                        guestDAO.updateGuest(guest);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                showAlert("Guest Not Found", "No guest found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    });
}

// View all guests
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

// Dialog to delete a guest
private void showDeleteGuestDialog() {
    Dialog<String> dialog = new Dialog<>();
    dialog.setTitle("Delete Guest");

    TextField idField = new TextField();
    idField.setPromptText("Guest ID");

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
            guestDAO.deleteGuest(Integer.parseInt(id));
            showAlert("Success", "Guest with ID " + id + " has been deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Could not delete guest with ID: " + id);
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid guest ID.");
        }
    });
}



    // Opens a new window (stage) for managing reservations
   // Opens a new window (stage) for managing reservations
private void openManageReservationsWindow() {
    Stage manageReservationsStage = new Stage();
    VBox layout = new VBox(15);
    layout.setPadding(new Insets(20));

    // Add buttons for managing reservations
    Button addReservationBtn = new Button("Add Reservation");
    Button updateReservationBtn = new Button("Update Reservation");
    Button viewReservationsBtn = new Button("View Reservations");
    Button deleteReservationBtn = new Button("Delete Reservation");

    layout.getChildren().addAll(addReservationBtn, updateReservationBtn, viewReservationsBtn, deleteReservationBtn);

    // Button actions
    addReservationBtn.setOnAction(e -> showAddReservationDialog());
    updateReservationBtn.setOnAction(e -> showUpdateReservationDialog());
    viewReservationsBtn.setOnAction(e -> {
        try {
            viewReservations();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    });
    deleteReservationBtn.setOnAction(e -> showDeleteReservationDialog());

    Scene scene = new Scene(layout, 400, 300);
    manageReservationsStage.setTitle("Manage Reservations");
    manageReservationsStage.setScene(scene);
    manageReservationsStage.show();
}

// Show dialog for adding a reservation
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
    DatePicker checkInDatePicker = new DatePicker();
    DatePicker checkOutDatePicker = new DatePicker();

    grid.add(new Label("Guest ID:"), 0, 0);
    grid.add(guestIdField, 1, 0);
    grid.add(new Label("Room ID:"), 0, 1);
    grid.add(roomIdField, 1, 1);
    grid.add(new Label("Hotel ID:"), 0, 2);
    grid.add(hotelIdField, 1, 2);
    grid.add(new Label("Status:"), 0, 3);
    grid.add(statusField, 1, 3);
    grid.add(new Label("Check-In Date:"), 0, 4);
    grid.add(checkInDatePicker, 1, 4);
    grid.add(new Label("Check-Out Date:"), 0, 5);
    grid.add(checkOutDatePicker, 1, 5);

    dialog.getDialogPane().setContent(grid);
    ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == addButtonType) {
            return new Reservation(0, 
                                   Integer.parseInt(guestIdField.getText()), 
                                   Integer.parseInt(roomIdField.getText()),
                                   Integer.parseInt(hotelIdField.getText()), 
                                   statusField.getText(), 
                                   java.sql.Date.valueOf(checkInDatePicker.getValue()), 
                                   java.sql.Date.valueOf(checkOutDatePicker.getValue()));
        }
        return null;
    });

    dialog.showAndWait().ifPresent(reservation -> {
        try {
            reservationDAO.addReservation(reservation);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    });
}

// Show dialog for updating a reservation
private void showUpdateReservationDialog() {
    TextInputDialog idDialog = new TextInputDialog();
    idDialog.setTitle("Update Reservation");
    idDialog.setHeaderText("Enter Reservation ID to Update");
    
    idDialog.showAndWait().ifPresent(idString -> {
        try {
            int id = Integer.parseInt(idString);
            Reservation existingReservation = reservationDAO.getReservationById(id);
            if (existingReservation != null) {
                Dialog<Reservation> dialog = new Dialog<>();
                dialog.setTitle("Update Reservation");

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField guestIdField = new TextField(String.valueOf(existingReservation.getGuestId()));
                TextField roomIdField = new TextField(String.valueOf(existingReservation.getRoomId()));
                TextField hotelIdField = new TextField(String.valueOf(existingReservation.getHotelId()));
                TextField statusField = new TextField(existingReservation.getStatus());
                DatePicker checkInDatePicker = new DatePicker(existingReservation.getCheckIn().toLocalDate());
                DatePicker checkOutDatePicker = new DatePicker(existingReservation.getCheckOut().toLocalDate());

                grid.add(new Label("Guest ID:"), 0, 0);
                grid.add(guestIdField, 1, 0);
                grid.add(new Label("Room ID:"), 0, 1);
                grid.add(roomIdField, 1, 1);
                grid.add(new Label("Hotel ID:"), 0, 2);
                grid.add(hotelIdField, 1, 2);
                grid.add(new Label("Status:"), 0, 3);
                grid.add(statusField, 1, 3);
                grid.add(new Label("Check-In Date:"), 0, 4);
                grid.add(checkInDatePicker, 1, 4);
                grid.add(new Label("Check-Out Date:"), 0, 5);
                grid.add(checkOutDatePicker, 1, 5);

                dialog.getDialogPane().setContent(grid);
                ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == updateButtonType) {
                        LocalDate checkInLocalDate = checkInDatePicker.getValue();
                        LocalDate checkOutLocalDate = checkOutDatePicker.getValue();

                        return new Reservation(id, 
                                               Integer.parseInt(guestIdField.getText()), 
                                               Integer.parseInt(roomIdField.getText()), 
                                               Integer.parseInt(hotelIdField.getText()), 
                                               statusField.getText(), 
                                               (checkInLocalDate != null) ? java.sql.Date.valueOf(checkInLocalDate) : null, 
                                               (checkOutLocalDate != null) ? java.sql.Date.valueOf(checkOutLocalDate) : null);
                    }
                    return null;
                });

                dialog.showAndWait().ifPresent(reservation -> {
                    try {
                        reservationDAO.updateReservation(reservation);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        showAlert("Error", "Failed to update reservation.");
                    }
                });
            } else {
                showAlert("Reservation Not Found", "No reservation found with ID: " + id);
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid numeric Reservation ID.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error occurred while retrieving the reservation.");
        }
    });
}

// View reservations
private void viewReservations() throws SQLException {
    List<Reservation> reservations = reservationDAO.getAllReservations();
    StringBuilder reservationInfo = new StringBuilder();
    for (Reservation reservation : reservations) {
        reservationInfo.append("Reservation ID: ").append(reservation.getId())
                .append(", Guest ID: ").append(reservation.getGuestId())
                .append(", Room ID: ").append(reservation.getRoomId())
                .append(", Hotel ID: ").append(reservation.getHotelId())
                .append(", Status: ").append(reservation.getStatus())
                .append(", Check-In: ").append(reservation.getCheckIn())
                .append(", Check-Out: ").append(reservation.getCheckOut()).append("\n");
    }
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Reservation Information");
    alert.setHeaderText("All Reservations");
    alert.setContentText(reservationInfo.toString());
    alert.showAndWait();
}

// Show dialog for deleting a reservation
private void showDeleteReservationDialog() {
    Dialog<String> dialog = new Dialog<>();
    dialog.setTitle("Delete Reservation");

    TextField idField = new TextField();
    idField.setPromptText("Reservation ID");

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
            reservationDAO.deleteReservation(Integer.parseInt(id));
            showAlert("Success", "Reservation with ID " + id + " has been deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Could not delete reservation with ID: " + id);
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid reservation ID.");
        }
    });
}


    // Opens a new window (stage) for managing rooms
    private void openManageRoomsWindow() {
    Stage manageRoomsStage = new Stage();
    VBox layout = new VBox(15);
    layout.setPadding(new Insets(20));

    Button addRoomBtn = new Button("Add Room");
    Button viewRoomsBtn = new Button("View Rooms");
    Button updateRoomBtn = new Button("Update Room");
    Button deleteRoomBtn = new Button("Delete Room");

    layout.getChildren().addAll(addRoomBtn, viewRoomsBtn, updateRoomBtn, deleteRoomBtn);

    // Event handling
    addRoomBtn.setOnAction(e -> showAddRoomDialog());
    viewRoomsBtn.setOnAction(e -> {
        try {
            viewRooms();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    });
    updateRoomBtn.setOnAction(e -> showUpdateRoomDialog());
    deleteRoomBtn.setOnAction(e -> showDeleteRoomDialog());

    Scene scene = new Scene(layout, 400, 300);
    manageRoomsStage.setTitle("Manage Rooms");
    manageRoomsStage.setScene(scene);
    manageRoomsStage.show();
}

// Dialog to add a room
private void showAddRoomDialog() {
    Dialog<Room> dialog = new Dialog<>();
    dialog.setTitle("Add Room");

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    TextField hotelIdField = new TextField();
    hotelIdField.setPromptText("Hotel ID");
    TextField roomNumberField = new TextField();
    roomNumberField.setPromptText("Room Number");
    TextField typeField = new TextField();
    typeField.setPromptText("Room Type");
    TextField priceField = new TextField();
    priceField.setPromptText("Price");

    grid.add(new Label("Hotel ID:"), 0, 0);
    grid.add(hotelIdField, 1, 0);
    grid.add(new Label("Room Number:"), 0, 1);
    grid.add(roomNumberField, 1, 1);
    grid.add(new Label("Room Type:"), 0, 2);
    grid.add(typeField, 1, 2);
    grid.add(new Label("Price:"), 0, 3);
    grid.add(priceField, 1, 3);

    dialog.getDialogPane().setContent(grid);
    ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == addButtonType) {
            return new Room(0, Integer.parseInt(hotelIdField.getText()), 
                            Integer.parseInt(roomNumberField.getText()), 
                            typeField.getText(), 
                            Double.parseDouble(priceField.getText()));
        }
        return null;
    });

    dialog.showAndWait().ifPresent(room -> {
        try {
            roomDAO.addRoom(room);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    });
}

// Dialog to update a room
private void showUpdateRoomDialog() {
    TextInputDialog idDialog = new TextInputDialog();
    idDialog.setTitle("Update Room");
    idDialog.setHeaderText("Enter Room ID to Update");
    idDialog.showAndWait().ifPresent(idString -> {
        int id = Integer.parseInt(idString);
        try {
            Room existingRoom = roomDAO.getRoomById(id);
            if (existingRoom != null) {
                Dialog<Room> dialog = new Dialog<>();
                dialog.setTitle("Update Room");

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField hotelIdField = new TextField(String.valueOf(existingRoom.getHotelId()));
                TextField roomNumberField = new TextField(String.valueOf(existingRoom.getRoomNumber()));
                TextField typeField = new TextField(existingRoom.getType());
                TextField priceField = new TextField(String.valueOf(existingRoom.getPrice()));

                grid.add(new Label("Hotel ID:"), 0, 0);
                grid.add(hotelIdField, 1, 0);
                grid.add(new Label("Room Number:"), 0, 1);
                grid.add(roomNumberField, 1, 1);
                grid.add(new Label("Room Type:"), 0, 2);
                grid.add(typeField, 1, 2);
                grid.add(new Label("Price:"), 0, 3);
                grid.add(priceField, 1, 3);

                dialog.getDialogPane().setContent(grid);
                ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == updateButtonType) {
                        return new Room(id, Integer.parseInt(hotelIdField.getText()), 
                                        Integer.parseInt(roomNumberField.getText()), 
                                        typeField.getText(), 
                                        Double.parseDouble(priceField.getText()));
                    }
                    return null;
                });

                dialog.showAndWait().ifPresent(room -> {
                    try {
                        roomDAO.updateRoom(room);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                showAlert("Room Not Found", "No room found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    });
}

// View all rooms
private void viewRooms() throws SQLException {
    List<Room> rooms = roomDAO.getAllRooms();
    StringBuilder roomInfo = new StringBuilder();
    for (Room room : rooms) {
        roomInfo.append("Room ID: ").append(room.getId())
                .append(", Hotel ID: ").append(room.getHotelId())
                .append(", Room Number: ").append(room.getRoomNumber())
                .append(", Type: ").append(room.getType())
                .append(", Price: ").append(room.getPrice()).append("\n");
    }
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Room Information");
    alert.setHeaderText("All Rooms");
    alert.setContentText(roomInfo.toString());
    alert.showAndWait();
}

// Dialog to delete a room
private void showDeleteRoomDialog() {
    Dialog<String> dialog = new Dialog<>();
    dialog.setTitle("Delete Room");

    TextField idField = new TextField();
    idField.setPromptText("Room ID");

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
            roomDAO.deleteRoom(Integer.parseInt(id));
            showAlert("Success", "Room with ID " + id + " has been deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Could not delete room with ID: " + id);
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid room ID.");
        }
    });
}

    // View audit logs in an alert
    private void viewAuditLogs() throws SQLException {
        List<AuditLog> logs = auditLogDAO.getAllAuditLogs();
        StringBuilder logInfo = new StringBuilder();
        for (AuditLog log : logs) {
            logInfo.append("User ID: ").append(log.getUserId())
                    .append(", Action: ").append(log.getAction())
                    .append(", Timestamp: ").append(log.getTimestamp()).append("\n");
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Audit Logs");
        alert.setHeaderText("All Audit Logs");
        alert.setContentText(logInfo.toString());
        alert.showAndWait();
    }

    // View hotels
    private void viewHotels() throws SQLException {
        List<Hotel> hotels = hotelDAO.getAllHotels();
        StringBuilder hotelInfo = new StringBuilder();
        for (Hotel hotel : hotels) {
            hotelInfo.append("Hotel ID: ").append(hotel.getId())
                    .append(", Name: ").append(hotel.getName())
                    .append(", Address: ").append(hotel.getAddress())
                    .append(", Rating: ").append(hotel.getRating()).append("\n");
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hotel Information");
        alert.setHeaderText("All Hotels");
        alert.setContentText(hotelInfo.toString());
        alert.showAndWait();
    }

    // Show dialog for adding a hotel
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

    // Show dialog for updating a hotel
    private void showUpdateHotelDialog() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Update Hotel");
        idDialog.setHeaderText("Enter Hotel ID to Update");
        idDialog.showAndWait().ifPresent(idString -> {
            int id = Integer.parseInt(idString);
            try {
                Hotel existingHotel = hotelDAO.getHotelById(id);
                if (existingHotel != null) {
                    Dialog<Hotel> dialog = new Dialog<>();
                    dialog.setTitle("Update Hotel");

                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(10);
                    grid.setPadding(new Insets(20, 150, 10, 10));

                    TextField nameField = new TextField(existingHotel.getName());
                    TextField addressField = new TextField(existingHotel.getAddress());
                    TextField ratingField = new TextField(String.valueOf(existingHotel.getRating()));

                    grid.add(new Label("Hotel Name:"), 0, 0);
                    grid.add(nameField, 1, 0);
                    grid.add(new Label("Hotel Address:"), 0, 1);
                    grid.add(addressField, 1, 1);
                    grid.add(new Label("Hotel Rating:"), 0, 2);
                    grid.add(ratingField, 1, 2);

                    dialog.getDialogPane().setContent(grid);
                    ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
                    dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == updateButtonType) {
                            return new Hotel(id, nameField.getText(), addressField.getText(), Double.parseDouble(ratingField.getText()));
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
                } else {
                    showAlert("Hotel Not Found", "No hotel found with ID: " + id);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    // Show dialog for deleting a hotel
private void showdeleteHotelDialog() {
    Dialog<String> dialog = new Dialog<>();
    dialog.setTitle("Delete Hotel");

    // Create a text field for hotel ID input
    TextField idField = new TextField();
    idField.setPromptText("Hotel ID");

    // Set the text field as the content of the dialog
    dialog.getDialogPane().setContent(idField);

    // Add 'Delete' and 'Cancel' buttons
    ButtonType deleteButtonType = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(deleteButtonType, ButtonType.CANCEL);

    // Handle the result of the dialog
    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == deleteButtonType) {
            return idField.getText();  // Return the hotel ID entered
        }
        return null;
    });

    // Show the dialog and process the result
    dialog.showAndWait().ifPresent(id -> {
        try {
            int hotelId = Integer.parseInt(id);
            hotelDAO.deleteHotel(hotelId);  // Call your existing deleteHotel method
            showAlert("Success", "Hotel with ID " + id + " has been deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Could not delete hotel with ID: " + id);
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid hotel ID.");
        }
    });
}


    // Helper method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);  // launch only once for the entire application
    }
}
