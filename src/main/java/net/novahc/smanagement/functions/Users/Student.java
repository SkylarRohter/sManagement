package net.novahc.smanagement.functions.Users;

import javafx.beans.property.*;

public class Student {
    private String name;
    private int grade;
    private boolean present;

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public Student(String name, int grade, boolean present) {
        this.name = name;
        this.grade = grade;
        this.present = present;
    }
    public StringProperty nameProperty() {
        return new SimpleStringProperty(name);
    }
    public IntegerProperty gradeProperty() {
        return new SimpleIntegerProperty(grade);
    }
    public BooleanProperty presentProperty() {
        return new SimpleBooleanProperty(present);
    }
    public String toString() {
        return getName()+" "+getGrade();
    }
}
