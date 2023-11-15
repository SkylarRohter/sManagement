package net.novahc.smanagement.Database;

import net.novahc.smanagement.functions.Users.Student;

import java.util.ArrayList;

public class StudentManager {
    private ArrayList<Student> students;

    private Database db;
    public Database getDb() {
        return db;
    }

    public void init(){
        students = new ArrayList<>();
        db = new Database();
        for(int i = 1; i < db.getIds().size(); i++){
            students.add(new Student(db.getUsers().get(i),db.getGrades().get(i),true));
        }
    }
    public void addStudent(String name, int grade){
        students.add(new Student(name, grade, false));
    }
    public void removeStudent(String name, int grade){
    }
    public ArrayList<Student> getStudents(){
        return students;
    }
    public void setPresence(int index){
        students.get(index).setPresent(!students.get(index).isPresent());
    }
}
