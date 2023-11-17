package net.novahc.smanagement.functions.charts;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import net.novahc.smanagement.Database.StudentManager;
import net.novahc.smanagement.functions.Users.Student;

import java.util.Arrays;

public class TableManager {
    private TableView<Student> table;
    public TableView<Student> getTable() {
        return table;
    }

    private StudentManager studentManager;
    private ObservableList<Student> data;

    public ObservableList<Student> getData() {
        return data;
    }


    public TableManager(TableView<Student> table, StudentManager studentManager){
        this.table = table;
        table = new TableView<Student>();
        data = FXCollections.observableArrayList();
        this.studentManager = studentManager;
    }
    public void init(TableColumn<Student, String> nameColumn, TableColumn<Student, Integer> gradeColumn, TableColumn<Student, Boolean> presentColumn){
        nameColumn.setCellValueFactory(data ->
                Bindings.createStringBinding(() -> data.getValue().getName())
        );
        gradeColumn.setCellValueFactory(data ->
                Bindings.createObjectBinding(() -> data.getValue().getGrade(), data.getValue().gradeProperty())
        );
        presentColumn.setCellValueFactory(data ->
                Bindings.createObjectBinding(() -> data.getValue().presentProperty().get(), data.getValue().presentProperty())
        );
        if (!table.getColumns().contains(nameColumn)) {
            table.getColumns().add(nameColumn);
        }
        if (!table.getColumns().contains(gradeColumn)) {
            table.getColumns().add(gradeColumn);
        }
        if (!table.getColumns().contains(presentColumn)) {
            table.getColumns().add(presentColumn);
        }
        studentManager.init();
        data.addAll(studentManager.getStudents());
        table.setItems(data);

    }
}
