package app;

import com.technoearth.model.Student;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.sql.*;
import java.time.LocalDate;

public class StudentForm {

    private TextField nameField, rollField;
    private ComboBox<String> classBox, sectionBox;
    private DatePicker dobPicker;
    private ToggleGroup genderGroup;
    private TableView<Student> studentTable;
    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    public Pane getView() {
        Label title = new Label("👩‍🎓 Student Registration");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        nameField = new TextField();
        rollField = new TextField();
        classBox = new ComboBox<>();
        classBox.getItems().addAll("1", "2", "3", "4", "5");
        sectionBox = new ComboBox<>();
        sectionBox.getItems().addAll("A", "B", "C");
        dobPicker = new DatePicker();

        RadioButton male = new RadioButton("Male");
        RadioButton female = new RadioButton("Female");
        genderGroup = new ToggleGroup();
        male.setToggleGroup(genderGroup);
        female.setToggleGroup(genderGroup);

        Button submitBtn = new Button("Submit");
        Button updateBtn = new Button("Update");
        Button editBtn = new Button("Edit Selected");
        Button deleteBtn = new Button("Delete Selected");
        Button dummyBtn = new Button("Add Dummy Data");

        submitBtn.setOnAction(e -> saveStudent());
        dummyBtn.setOnAction(e -> insertDummyStudents());

        HBox buttons = new HBox(15, submitBtn, updateBtn, editBtn, deleteBtn, dummyBtn);

        GridPane form = new GridPane();
        form.setPadding(new Insets(20));
        form.setHgap(10);
        form.setVgap(10);
        form.add(new Label("Full Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("Roll No:"), 0, 1);
        form.add(rollField, 1, 1);
        form.add(new Label("Class:"), 0, 2);
        form.add(classBox, 1, 2);
        form.add(new Label("Section:"), 0, 3);
        form.add(sectionBox, 1, 3);
        form.add(new Label("DOB:"), 0, 4);
        form.add(dobPicker, 1, 4);
        form.add(new Label("Gender:"), 0, 5);
        form.add(new HBox(10, male, female), 1, 5);
        form.add(buttons, 1, 6);

        studentTable = createStudentTableView();
        refreshTable();

        VBox root = new VBox(15, title, form, studentTable);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);
        return root;
    }

    private TableView<Student> createStudentTableView() {
        TableView<Student> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Student, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Student, String> rollCol = new TableColumn<>("Roll No");
        rollCol.setCellValueFactory(new PropertyValueFactory<>("rollNo"));

        TableColumn<Student, String> classCol = new TableColumn<>("Class");
        classCol.setCellValueFactory(new PropertyValueFactory<>("studentClass"));

        TableColumn<Student, String> secCol = new TableColumn<>("Section");
        secCol.setCellValueFactory(new PropertyValueFactory<>("section"));

        TableColumn<Student, String> dobCol = new TableColumn<>("DOB");
        dobCol.setCellValueFactory(new PropertyValueFactory<>("dob"));

        TableColumn<Student, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));

        table.getColumns().addAll(idCol, nameCol, rollCol, classCol, secCol, dobCol, genderCol);
        table.setItems(studentList);
        return table;
    }

    private void refreshTable() {
        studentList.clear();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:school.db");
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");
            while (rs.next()) {
                studentList.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("roll_no"),
                        rs.getString("class"),
                        rs.getString("section"),
                        rs.getString("dob"),
                        rs.getString("gender")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error loading students: " + e.getMessage());
        }
    }

    private void saveStudent() {
        String name = nameField.getText().trim();
        String roll = rollField.getText().trim();
        String studentClass = classBox.getValue();
        String section = sectionBox.getValue();
        LocalDate dob = dobPicker.getValue();
        RadioButton selectedGender = (RadioButton) genderGroup.getSelectedToggle();
        String gender = (selectedGender != null) ? selectedGender.getText() : null;

        if (name.isEmpty() || roll.isEmpty() || studentClass == null || section == null || dob == null || gender == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill all fields.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:school.db")) {
            String sql = "INSERT INTO students (name, roll_no, class, section, dob, gender) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, roll);
            stmt.setString(3, studentClass);
            stmt.setString(4, section);
            stmt.setString(5, dob.toString());
            stmt.setString(6, gender);
            stmt.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Student saved successfully.");
            clearForm();
            refreshTable();
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "DB Error", "Failed to save student.\n" + ex.getMessage());
        }
    }

    private void clearForm() {
        nameField.clear();
        rollField.clear();
        classBox.getSelectionModel().clearSelection();
        sectionBox.getSelectionModel().clearSelection();
        dobPicker.setValue(null);
        genderGroup.selectToggle(null);
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void insertDummyStudents() {
        String[] names = {"Ankit Sharma", "Riya Mehta", "Sahil Verma", "Neha Singh", "Karan Patel"};
        String[] rolls = {"A101", "A102", "A103", "A104", "A105"};
        String[] classes = {"1", "2", "3", "4", "5"};
        String[] sections = {"A", "B", "C", "A", "B"};
        String[] dobs = {"2012-01-15", "2013-05-20", "2011-09-12", "2014-02-18", "2010-07-07"};
        String[] genders = {"Male", "Female", "Male", "Female", "Male"};

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:school.db")) {
            String sql = "INSERT INTO students (name, roll_no, class, section, dob, gender) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < names.length; i++) {
                stmt.setString(1, names[i]);
                stmt.setString(2, rolls[i]);
                stmt.setString(3, classes[i]);
                stmt.setString(4, sections[i]);
                stmt.setString(5, dobs[i]);
                stmt.setString(6, genders[i]);
                stmt.addBatch();
            }

            stmt.executeBatch();
            showAlert(Alert.AlertType.INFORMATION, "Dummy Data", "✅ 5 dummy student records added.");
            refreshTable();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "❌ Failed to insert dummy data.\n" + e.getMessage());
        }
    }
}
