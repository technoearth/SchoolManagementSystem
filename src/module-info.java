module SchoolManagementSystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires java.sql; // ✅ Add this line if missing

    opens app to javafx.fxml;
    exports app;
}
