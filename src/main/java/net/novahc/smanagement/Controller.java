package net.novahc.smanagement;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import net.novahc.smanagement.Database.StudentManager;
import net.novahc.smanagement.functions.charts.TableManager;
import net.novahc.smanagement.functions.Users.Student;
import net.novahc.smanagement.functions.charts.ChartManager;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    // FIELDS
    private int currentlyEnabledIndex = 0;
    private Button[] buttons;
    private FlowPane[] windowPanes;
    private String tempPassword = "root"; //TODO Replace with password handler (settings pane)
    private boolean correctPassword = false;

    //MANAGERS
    private StudentManager studentManager;
    private TableManager tableManager;
    private ChartManager chartManager;

    // MAIN PANES
    @FXML private AnchorPane mainPane;
    @FXML private AnchorPane sidePane;
    @FXML private FlowPane buttonPane;

    // SECONDARY PANES
    @FXML private FlowPane scanPane;
    @FXML private FlowPane umPane;
    @FXML private FlowPane setPane;

    // SIDEBAR BUTTONS
    @FXML private Button sbButton;
    @FXML private Button aButton;
    @FXML private Button umButton;
    @FXML private Button sButton;

    //ATTENDANCE PANE
    @FXML private FlowPane attPane;
    @FXML private TableView<Student> tableView;
    @FXML TableColumn<Student, String> name;
    @FXML TableColumn<Student, Integer> age;
    @FXML TableColumn<Student, Boolean> present;

    @FXML private Button toggleIsPresent;
    @FXML private Button updateTable;
    @FXML private Button resetDay;

    @FXML private PieChart pieChart;
    @FXML private BarChart<String,Number> presentChart;


    // DIALOGUE PANE

    @FXML private AnchorPane dialoguePane;

    @FXML private ImageView quitButton;

    @FXML private AnchorPane confirmPane;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button confirmPasswordButton;

    @FXML private AnchorPane promptPane;
    @FXML private Label promptLabel;
    @FXML private Button promptButton;



    //UserManagement Pane
    @FXML private AnchorPane addUserPane;
    @FXML private TextField addUserNameField;
    @FXML private TextField addUserGradeField;
    @FXML private Button addUserButton;

    @FXML private AnchorPane updateUserPane;
    @FXML private TextField updateUserNameField;
    @FXML private TextField updateUserGradeField;
    @FXML private Button updateUserButton;

    @FXML private AnchorPane removeUserPane;
    @FXML private TextField removeUserNameField;
    @FXML private TextField removeUserGradeField;
    @FXML private Button removeUserButton;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // FXML element array inits
        buttons = new Button[]{sbButton, aButton, umButton, sButton};
        windowPanes = new FlowPane[]{scanPane, attPane, umPane, setPane};

        // Bind the X,Y Coordinates of the buttonPane (flow) to the AnchorPane within sidePane
        buttonPane.prefWidthProperty().bind(sidePane.widthProperty());
        buttonPane.prefHeightProperty().bind(sidePane.heightProperty());

        // Bind Buttons to buttonPane;
        for(Button b: buttons){
            bindButton(b);
        }
        // Bind windows
        for(FlowPane w :windowPanes){
            bindWindow(w);
        }

        for(Button button : buttons){
            button.setOnAction(event -> handleButtonClick((Button) event.getSource()));
        }
        studentManager = new StudentManager();

        tableManager = new TableManager(tableView, studentManager);
        tableManager.init(name,age,present);

        chartManager = new ChartManager();
        chartManager.initBarChart(presentChart,"Grade","# Present", studentManager.getStudentTotals());
        chartManager.initPieChart(pieChart,studentManager.getStudents());
    }
    //  UPDATERS
    public void updateChart(){
        chartManager.updateBarChart(presentChart, studentManager.getStudentTotals());
    }
    public void updatePieChart(){
        chartManager.updatePieChart(pieChart,studentManager.getStudents());
    }

    //  BINDING
    public void bindButton(Button b){
        b.prefWidthProperty().bind(sidePane.widthProperty());
        b.getStyleClass().add("sidebar-buttons");
    }
    public void bindWindow(Pane p){
        p.prefWidthProperty().bind(mainPane.widthProperty());
        p.getStyleClass().add("windows");
    }

    private void handleButtonClick(Button clickedButton) {
        // Enable the previously disabled button
        buttons[currentlyEnabledIndex].setDisable(false);

        // Find the index of the clicked button and disable it
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == clickedButton) {
                currentlyEnabledIndex = i;

                //set selected button to disabled to prevent double registry
                buttons[i].setDisable(true);

                //set selected pane to visible
                windowPanes[i].setVisible(true);
            }
            // Set all other windows other than the selected to invisible
            for(int j = 0; j < windowPanes.length; j++){
                if(j != currentlyEnabledIndex){
                    windowPanes[j].setVisible(false);
                }
            }
        }
    }
    private void invokePromptPane(String labelText, String buttonText){
        dialoguePane.setVisible(true);
        confirmPane.setVisible(false);
        promptPane.setVisible(true);

        promptLabel.setText(labelText);
        promptButton.setText(buttonText);
    }
    private void promptPassword(){
        dialoguePane.setVisible(true);
        promptPane.setVisible(false);
        confirmPane.setVisible(true);
        System.out.println("start");
        confirmPasswordButton.setOnAction(actionEvent -> {
            System.out.println("helo");
            correctPassword = checkPassword(confirmPasswordField.getText());
            if (correctPassword) {
                dialoguePane.setVisible(false);
            } else {
                confirmPasswordField.setText("");
                try {
                    for(int i = 0; i < 3; i++) {
                        confirmPasswordField.setStyle("-fx-border-color: -red");
                        Thread.sleep(500);
                        confirmPasswordField.setStyle("-fx-border-color: -fx-timberwolf");
                        Thread.sleep(500);
                    }
                    dialoguePane.setVisible(false);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        System.out.println("end");
    }

    //  EVENT HANDLERS

    //DialoguePane
    private boolean checkPassword(String enteredPassword){
        return enteredPassword.equals(tempPassword);
    }
    public void onPromptButtonClick(){
        dialoguePane.setVisible(false);
    }
    public void onDQuitButtonClick(){
        dialoguePane.setVisible(false);
        confirmPasswordField.setText("");
    }
    //Attendance Pane
    public void onToggleIsPresentClick(){
        if(tableView.getSelectionModel().getSelectedIndex()!=-1) {
            studentManager.setPresence(tableView.getSelectionModel().getSelectedIndex());
            tableView.getSelectionModel().clearSelection();
            tableView.refresh();
            updatePieChart();
        } else{
            invokePromptPane("Please highlight a student.", "Okay");
        }
    }
    public void onUpdateTableClick(){
        for(Student student : studentManager.getStudents()){
            student.setPresent(true);
        }
        tableView.refresh();
        updatePieChart();
    }
    public void onResetButtonClick(){
        for(Student student : studentManager.getStudents()){
            student.setPresent(false);
        }
        tableView.refresh();
        updatePieChart();
    }
    public void onAddUserButtonClick(){
        if(!Objects.equals(addUserNameField.getText(), "") && !Objects.equals(addUserNameField.getText(), "")) {
            //promptPassword();
            String name = addUserNameField.getText();
            int grade = Integer.parseInt(addUserGradeField.getText());

            //Clear Table and Data
            tableManager.getTable().getItems().removeAll();
            tableManager.getData().removeAll(studentManager.getStudents());

            //Insert Record into Database and Array
            studentManager.getDb().insertRecord(name, grade);
            studentManager.addStudent(name, grade);

            //Re-add to table and database
            tableManager.getData().addAll(studentManager.getStudents());
            tableManager.getTable().setItems(tableManager.getData());
        } else {
            invokePromptPane("Please fill in the boxes.", "Okay");
        }
        updateChart();
    }
}