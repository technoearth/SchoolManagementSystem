package app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Dashboard extends Application {

    private BorderPane mainLayout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        mainLayout = new BorderPane();

        // Create Sidebar 
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #2C3E50;");

        Button studentBtn = new Button("👩‍🎓 Student Registration");
        Button attendanceBtn = new Button("✅ Attendance");
        Button reportBtn = new Button("📊 Performance Report");

        studentBtn.setMaxWidth(Double.MAX_VALUE);
        attendanceBtn.setMaxWidth(Double.MAX_VALUE);
        reportBtn.setMaxWidth(Double.MAX_VALUE);

        // Add Buttons to Sidebar
        sidebar.getChildren().addAll(studentBtn, attendanceBtn, reportBtn);

        // Default center content
        mainLayout.setLeft(sidebar);
        mainLayout.setCenter(new Text("📌 Welcome to the School Management System"));

        // Button Actions
        studentBtn.setOnAction(e -> loadStudentForm());
        attendanceBtn.setOnAction(e -> loadAttendanceForm());
        reportBtn.setOnAction(e -> loadPerformanceReport());

        // Finalize scene
        Scene scene = new Scene(mainLayout, 1000, 700);
        stage.setTitle("School Management Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    private void loadStudentForm() {
        StudentForm studentForm = new StudentForm();
        Pane view = studentForm.getView();
        mainLayout.setCenter(view);
    }

    private void loadAttendanceForm() {
        AttendanceForm attendanceForm = new AttendanceForm();
        Pane view = attendanceForm.getView();
        mainLayout.setCenter(view);
    }

    private void loadPerformanceReport() {
        PerformanceReport report = new PerformanceReport();
        Pane view = report.getView();
        mainLayout.setCenter(view);
    }
}
