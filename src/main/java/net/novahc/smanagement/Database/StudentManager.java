package net.novahc.smanagement.Database;

import net.novahc.smanagement.functions.Users.Student;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentManager {


    public void setInitInputValues(String[] initInputValues) {
        this.initInputValues = initInputValues;
    }

    private String[] initInputValues;

    private ArrayList<Student> students;

    private Database db;
    public Database getDb() {
        return db;
    }

    public void init(){
        students = new ArrayList<>();
        db = new Database(initInputValues[0],initInputValues[1],initInputValues[2],initInputValues[3]);
        for(int i = 0; i < db.getIds().size(); i++){
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
    public HashMap<Integer,Integer> getStudentTotals(){
        HashMap<Integer,Integer> totals =  new HashMap<>();
        int[] grades = new int[7];
        for(Student student : getStudents()){
            if(student.getGrade() == 6){
                grades[0] +=1;
            } else if(student.getGrade() == 7){
                grades[1] +=1;
            } else if(student.getGrade() == 8){
                grades[2] +=1;
            } else if(student.getGrade() == 9){
                grades[3] +=1;
            } else if(student.getGrade() == 10){
                grades[4] +=1;
            } else if(student.getGrade() == 11){
                grades[5] +=1;
            } else if(student.getGrade() == 12){
                grades[6] +=1;
            }
            for(int i = 0; i < grades.length; i ++){
                totals.put(i+6,grades[i]);
            }
        }
        return totals;
    }
    public void setPresence(int index){
        students.get(index).setPresent(!students.get(index).isPresent());
    }
}
