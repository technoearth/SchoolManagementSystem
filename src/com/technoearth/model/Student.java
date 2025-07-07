package com.technoearth.model;

import javafx.beans.property.*;

public class Student {
    private IntegerProperty id;
    private StringProperty name;
    private StringProperty rollNo;
    private StringProperty studentClass;
    private StringProperty section;
    private StringProperty dob;
    private StringProperty gender;

    public Student(int id, String name, String rollNo, String studentClass, String section, String dob, String gender) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.rollNo = new SimpleStringProperty(rollNo);
        this.studentClass = new SimpleStringProperty(studentClass);
        this.section = new SimpleStringProperty(section);
        this.dob = new SimpleStringProperty(dob);
        this.gender = new SimpleStringProperty(gender);
    }

    // ID
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    // Name
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    // Roll No
    public String getRollNo() {
        return rollNo.get();
    }

    public StringProperty rollNoProperty() {
        return rollNo;
    }

    // Class
    public String getStudentClass() {
        return studentClass.get();
    }

    public StringProperty studentClassProperty() {
        return studentClass;
    }

    // Section
    public String getSection() {
        return section.get();
    }

    public StringProperty sectionProperty() {
        return section;
    }

    // DOB
    public String getDob() {
        return dob.get();
    }

    public StringProperty dobProperty() {
        return dob;
    }

    // Gender
    public String getGender() {
        return gender.get();
    }

    public StringProperty genderProperty() {
        return gender;
    }
}
