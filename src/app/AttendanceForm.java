package app;

import javafx.beans.property.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.*;

import java.sql.*;
import java.time.LocalDate;

public class AttendanceForm {

    private ObservableList<StudentAttendance> attendanceList = FXCollections.observableArrayList();
    private DatePicker datePicker;
    private TableView<StudentAttendance> table;

    public Pane getView() {
        Label title = new Label("✅ Student Attendance");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        datePicker = new DatePicker(LocalDate.now());
        Button loadBtn = new Button("Load Students");
        Button saveBtn = new Button("Save Attendance");

        loadBtn.setOnAction(e -> loadStudents());
        saveBtn.setOnAction(e -> saveAttendance());

        HBox topControls = new HBox(10, new Label("Date:"), datePicker, loadBtn, saveBtn);
        topControls.setPadding(new Insets(10));
        topControls.setAlignment(Pos.CENTER_LEFT);

        table = createTable();

        VBox root = new VBox(15, title, topControls, table);
        root.setPadding(new Insets(20));
        return root;
    }

    private TableView<StudentAttendance> createTable() {
        TableView<StudentAttendance> tableView = new TableView<>();
        tableView.setItems(attendanceList);
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<StudentAttendance, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());

        TableColumn<StudentAttendance, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cell -> cell.getValue().nameProperty());

        TableColumn<StudentAttendance, Boolean> statusCol = new TableColumn<>("Present?");
        statusCol.setCellValueFactory(cell -> {
            StudentAttendance sa = cell.getValue();
            SimpleBooleanProperty property = sa.presentProperty();
            return property;
        });
        statusCol.setCellFactory(CheckBoxTableCell.forTableColumn(statusCol));
        statusCol.setEditable(true);

        tableView.getColumns().addAll(idCol, nameCol, statusCol);
        return tableView;
    }

    private void loadStudents() {
        attendanceList.clear();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:school.db");
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id, name FROM students");
            while (rs.next()) {
                attendanceList.add(new StudentAttendance(rs.getInt("id"), rs.getString("name")));
            }
            System.out.println("✅ Students loaded for attendance.");
        } catch (SQLException e) {
            showAlert("Database Error", "❌ Could not load students.\n" + e.getMessage());
        }
    }

    private void saveAttendance() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            showAlert("Validation Error", "Please select a date.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:school.db")) {
            String sql = "INSERT INTO attendance (student_id, date, status) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (StudentAttendance sa : attendanceList) {
                stmt.setInt(1, sa.getId());
                stmt.setString(2, selectedDate.toString());
                stmt.setString(3, sa.isPresent() ? "Present" : "Absent");
                stmt.addBatch();
            }

            stmt.executeBatch();
            showAlert("Success", "✅ Attendance saved for " + selectedDate);
        } catch (SQLException e) {
            showAlert("Database Error", "❌ Could not save attendance.\n" + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Inner class for attendance data
    public static class StudentAttendance {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty name;
        private final SimpleBooleanProperty present;

        public StudentAttendance(int id, String name) {
            this.id = new SimpleIntegerProperty(id);
            this.name = new SimpleStringProperty(name);
            this.present = new SimpleBooleanProperty(false);
        }

        public int getId() {
            return id.get();
        }

        public String getName() {
            return name.get();
        }

        public boolean isPresent() {
            return present.get();
        }

        public void setPresent(boolean present) {
            this.present.set(present);
        }

        public SimpleIntegerProperty idProperty() {
            return id;
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public SimpleBooleanProperty presentProperty() {
            return present;
        }
    }
}
