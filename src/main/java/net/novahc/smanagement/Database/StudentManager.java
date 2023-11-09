package net.novahc.smanagement.Database;

import net.novahc.smanagement.functions.Users.Student;

import java.util.ArrayList;

public class StudentManager {
    ArrayList<Student> students;
    public StudentManager(){
        students = new ArrayList<>();
        students.add(new Student())
        //TEST STUDENTS ADDITION
        students.add(new Student("Skylar",12,true));
        students.add(new Student("Kurt",12,true));
        students.add(new Student("Ryder",10,false));
        students.add(new Student("John",11,false));
        students.add(new Student("Biden",9,false));

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
