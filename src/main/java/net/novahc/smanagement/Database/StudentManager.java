package net.novahc.smanagement.Database;

import net.novahc.smanagement.functions.Users.Student;

import java.util.ArrayList;

public class StudentManager {
    ArrayList<Student> students;
    public StudentManager(){
        students = new ArrayList<>();
        Database db = new Database();
        for(int i = 1; i < db.getIds().size(); i++){
            students.add(new Student(db.getUsers().get(i),db.getGrades().get(i),false));
        }
    }
    public void addStudent(String name, int grade){
        students.add(new Student(name, grade, false));
    }
    public void removeStudent(String name, int grade){
        //TODO finish removeStudent
    }
    public ArrayList<Student> getStudents(){
        return students;
    }
}
