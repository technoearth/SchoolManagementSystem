package app;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AttendanceViewer extends Application {

    private ComboBox<String> classBox;
    private ComboBox<String> sectionBox;
    private DatePicker datePicker;
    private TableView<AttendanceRecord> tableView;

    @Override
    public void start(Stage stage) {
        Label title = new Label("📋 View Attendance Records");
        title.getStyleClass().add("title-label");

        classBox = new ComboBox<>();
        sectionBox = new ComboBox<>();
        datePicker = new DatePicker();

        classBox.getItems().addAll("1", "2", "3", "4", "5");
        sectionBox.getItems().addAll("A", "B", "C");

        Button viewBtn = new Button("View Attendance");
        viewBtn.setOnAction(e -> viewAttendance());

        HBox filters = new HBox(15,
                new Label("Class:"), classBox,
                new Label("Section:"), sectionBox,
                new Label("Date:"), datePicker,
                viewBtn);
        filters.setPadding(new Insets(10));

        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tableView.setPlaceholder(new Label("No records to display"));

        TableColumn<AttendanceRecord, String> idCol = new TableColumn<>("Student ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<AttendanceRecord, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<AttendanceRecord, String> presentCol = new TableColumn<>("Present?");
        presentCol.setCellValueFactory(new PropertyValueFactory<>("present"));

        tableView.getColumns().addAll(idCol, nameCol, presentCol);

        VBox root = new VBox(20, title, filters, tableView);
        root.setPadding(new Insets(20));
        Scene scene = new Scene(root, 800, 500);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setTitle("Attendance Viewer");
        stage.setScene(scene);
        stage.show();
    }

    private void viewAttendance() {
        String studentClass = classBox.getValue();
        String section = sectionBox.getValue();
        LocalDate date = datePicker.getValue();

        if (studentClass == null || section == null || date == null) {
            showAlert(Alert.AlertType.WARNING, "Please select class, section, and date.");
            return;
        }

        List<AttendanceRecord> records = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:school.db");
        		PreparedStatement stmt = conn.prepareStatement(
        			    "SELECT s.id, s.name, a.present " +
        			    "FROM students s " +
        			    "JOIN attendance a ON s.id = a.student_id " +
        			    "WHERE s.student_class = ? AND s.section = ? AND a.date = ?"
        			)) {

            stmt.setString(1, studentClass);
            stmt.setString(2, section);
            stmt.setString(3, date.toString());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                boolean present = rs.getBoolean("present");

                records.add(new AttendanceRecord(
                        String.valueOf(id),
                        name,
                        present ? "Yes" : "No"
                ));
            }

            tableView.getItems().setAll(records);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "DB Error: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static class AttendanceRecord {
        private final SimpleStringProperty id, name, present;

        public AttendanceRecord(String id, String name, String present) {
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.present = new SimpleStringProperty(present);
        }

        public String getId() { return id.get(); }
        public String getName() { return name.get(); }
        public String getPresent() { return present.get(); }
    }

    public static void main(String[] args) {
        launch();
    }
}
