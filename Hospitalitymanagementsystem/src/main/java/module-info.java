module com.hospitality {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires com.google.protobuf;
    opens com.hospitality.ui to javafx.fxml;
    exports com.hospitality.ui;
}
