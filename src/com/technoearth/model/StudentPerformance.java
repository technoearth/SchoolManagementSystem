package com.technoearth.model;

import javafx.beans.property.*;

public class StudentPerformance {

    private final StringProperty name;
    private final IntegerProperty homeworkScore;
    private final IntegerProperty participationScore;
    private final IntegerProperty dailyTaskScore;

    public StudentPerformance(String name, int homeworkScore, int participationScore, int dailyTaskScore) {
        this.name = new SimpleStringProperty(name);
        this.homeworkScore = new SimpleIntegerProperty(homeworkScore);
        this.participationScore = new SimpleIntegerProperty(participationScore);
        this.dailyTaskScore = new SimpleIntegerProperty(dailyTaskScore);
    }

    // Getters for property binding
    public StringProperty nameProperty() {
        return name;
    }

    public IntegerProperty homeworkScoreProperty() {
        return homeworkScore;
    }

    public IntegerProperty participationScoreProperty() {
        return participationScore;
    }

    public IntegerProperty dailyTaskScoreProperty() {
        return dailyTaskScore;
    }

    // Getters for direct use
    public String getName() {
        return name.get();
    }

    public int getHomeworkScore() {
        return homeworkScore.get();
    }

    public int getParticipationScore() {
        return participationScore.get();
    }

    public int getDailyTaskScore() {
        return dailyTaskScore.get();
    }

    // Setters (optional if needed)
    public void setName(String name) {
        this.name.set(name);
    }

    public void setHomeworkScore(int score) {
        this.homeworkScore.set(score);
    }

    public void setParticipationScore(int score) {
        this.participationScore.set(score);
    }

    public void setDailyTaskScore(int score) {
        this.dailyTaskScore.set(score);
    }
}
