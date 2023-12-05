package net.novahc.smanagement;


import com.github.sarxos.webcam.Webcam;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import net.novahc.smanagement.utils.WebcamManager;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

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
    private WebcamManager webcamManager;

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
    @FXML private TextField addUserStudentIdField;
    @FXML private TextField addUserGradeField;
    @FXML private Button addUserButton;

    @FXML private AnchorPane updateUserPane;
    @FXML private TextField updateUserNameField;
    @FXML private TextField updateUserIdField;
    @FXML private TextField updateUserChangeField;
    @FXML private TextField updateUserGradeField;
    @FXML private Button updateUserButton;

    @FXML private AnchorPane removeUserPane;
    @FXML private TextField removeUserNameField;
    @FXML private TextField removeUserGradeField;
    @FXML private Button removeUserButton;

    //Settings Pane
    @FXML private TextField urlField;
    @FXML private TextField usernameFieldDB;
    @FXML private TextField passwordFieldDB;
    @FXML private TextField tableNameField;
    @FXML private Button databaseButton;

    @FXML private CheckBox usePassword;
    @FXML private CheckBox usePasswordForAdd;
    @FXML private CheckBox usePasswordForUpdate;
    @FXML private CheckBox usePasswordForRemove;

    @FXML private ChoiceBox<String> cameraSelector;
    @FXML private Button updateCamera;

    //Scanner Pane;
    @FXML private ImageView webcamWrapper;
    @FXML private Button stopScanningButton;
    @FXML private Label decodeResultLabel;



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
        webcamManager = new WebcamManager();
        studentManager = new StudentManager();
        tableManager = new TableManager(tableView,studentManager);
        chartManager = new ChartManager();
        invokePromptPane("Please configure application.", "Okay");
        databaseButton.setOnAction(actionEvent -> {
            String[] initInputValues = new String[4];
            initInputValues[0] = usernameFieldDB.getText();
            initInputValues[1] = passwordFieldDB.getText();
            initInputValues[2] = urlField.getText();
            initInputValues[3] = tableNameField.getText();
            studentManager.setInitInputValues(initInputValues);
            tableManager.init(name,age,present);
            chartManager.initBarChart(presentChart,"Grade","# Present", studentManager.getStudentTotals());
            chartManager.initPieChart(pieChart,studentManager.getStudents());
            urlField.setDisable(true);
            usernameFieldDB.setDisable(true);
            passwordFieldDB.setDisable(true);
            tableNameField.setDisable(true);
            databaseButton.setDisable(true);
        });
        ObservableList<String> labelList = FXCollections.observableArrayList();
        for(Webcam webcam : webcamManager.getWebcams()){
            labelList.add(webcam.getName());
        }
        cameraSelector.setItems(labelList);
        updateCamera.setOnAction(actionEvent -> {
            webcamManager.setCamera(cameraSelector.getSelectionModel().getSelectedIndex());
            System.out.println(cameraSelector.getSelectionModel().getSelectedIndex());
            webcamManager.init(webcamWrapper,decodeResultLabel);
            cameraSelector.setDisable(true);
            updateCamera.setDisable(true);
        });
    }
    // UTILS
    public boolean isNumeric(String str){
        if(str == null){
            return false;
        }
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        return pattern.matcher(str).matches();
    }
    //updaters
    public void updateChart(){
        chartManager.updateBarChart(presentChart, studentManager.getStudentTotals());
    }
    public void updatePieChart(){
        chartManager.updatePieChart(pieChart,studentManager.getStudents());
    }

    //binding
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
    private boolean promptPassword(){
        dialoguePane.setVisible(true);
        promptPane.setVisible(false);
        confirmPane.setVisible(true);
        System.out.println("start");
        AtomicBoolean returnable = new AtomicBoolean(false);
        confirmPasswordButton.setOnAction(actionEvent -> {
            System.out.println("helo");
            correctPassword = checkPassword(confirmPasswordField.getText());
            if (correctPassword) {
                dialoguePane.setVisible(false);
                returnable.set(true);
            } else {
                confirmPasswordField.setText("");
                dialoguePane.setVisible(false);
            }
        });
        System.out.println(returnable.get());
        return returnable.get();
    }

    //  EVENT HANDLERS

    //Dialogue Pane
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
            studentManager.setPresence(
                    studentManager.getDb().getStudentIds().get(tableView.getSelectionModel().getSelectedIndex())
            );
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
        //TODO Change if statements to be formatted same as updateUser()
        if(!Objects.equals(addUserNameField.getText(), "") && !Objects.equals(addUserNameField.getText(), "")) {
            if(promptPassword()) {
                String name = addUserNameField.getText();
                int grade = Integer.parseInt(addUserGradeField.getText());
                int studentid = Integer.parseInt(addUserStudentIdField.getText());

                //Clear Table and Data
                tableManager.getTable().getItems().removeAll();
                tableManager.getData().removeAll(studentManager.getStudents());

                //Insert Record into Database and Array
                studentManager.getDb().insertRecord(name, grade, studentid);
                studentManager.addStudent(name, grade, studentid);

                //Re-add to table and database
                tableManager.getData().addAll(studentManager.getStudents());
                tableManager.getTable().setItems(tableManager.getData());
            } else {
                //invokePromptPane("Incorrect password", "Try Again");
            }
        } else {
            invokePromptPane("Please fill in the boxes.", "Okay");
        }
        updateChart();
    }
    public void onUpdateUserButtonClick(){
        System.out.println("hello");
        if(Objects.equals(updateUserNameField.getText(), "") && Objects.equals(updateUserIdField.getText(),"")){
            invokePromptPane("Please enter a user to be changed", "Okay");
        }
        String name = updateUserNameField.getText();
        int id = Integer.parseInt(updateUserIdField.getText());
        System.out.println(id);
        String updatedInfo = updateUserChangeField.getText();
        if(!Objects.equals(updatedInfo, "")){
            tableManager.getTable().getItems().removeAll();
            tableManager.getData().removeAll(studentManager.getStudents());
            int key = studentManager.getDb().getStudentIds().indexOf(id);
            if(isNumeric(updatedInfo) && updatedInfo.length() <= 2){
                studentManager.getDb().updateRecord("grade",updatedInfo,true,key);
                studentManager.updateStudent(key,
                        studentManager.getDb().getUsers().get(key),
                        Integer.parseInt(updatedInfo),
                        studentManager.getDb().getStudentIds().get(key),
                        studentManager.getStudents().get(key).isPresent()
                );
            } else if (isNumeric(updatedInfo)){
                studentManager.getDb().updateRecord("studentid",updatedInfo,true,key);
                studentManager.updateStudent(key,
                        studentManager.getDb().getUsers().get(key),
                        studentManager.getDb().getGrades().get(key),
                        Integer.parseInt(updatedInfo),
                        studentManager.getStudents().get(key).isPresent()
                );
            } else{
                studentManager.getDb().updateRecord("name",updatedInfo,false,key);
                studentManager.updateStudent(key,
                        updatedInfo,
                        studentManager.getDb().getGrades().get(key),
                        studentManager.getDb().getStudentIds().get(key),
                        studentManager.getStudents().get(key).isPresent()
                );
            }
            tableManager.getData().addAll(studentManager.getStudents());
            tableManager.getTable().setItems(tableManager.getData());
        } else {
            invokePromptPane("Please include updated information", "Okay");
        }
    }

    //Settings Pane
    public void onUsePasswordChecked(){
        if(usePassword.isSelected()){
            usePasswordForAdd.setDisable(false);
            usePasswordForUpdate.setDisable(false);
            usePasswordForRemove.setDisable(false);
        } else {
            usePasswordForAdd.setDisable(true);
            usePasswordForUpdate.setDisable(true);
            usePasswordForRemove.setDisable(true);
            usePasswordForAdd.setSelected(false);
            usePasswordForUpdate.setSelected(false);
            usePasswordForRemove.setSelected(false);
        }
    }

    //Scanner Pane
    public void onStopScanningButtonClick(){
        webcamManager.getDecoder().invokeSingleScan();
        if(webcamManager.getDecoder().getResult() != null){
            int id = Integer.parseInt(webcamManager.getDecoder().getResult());
            if(studentManager.isRegisteredId(id)) {
                studentManager.setPresence(id);
                tableView.getSelectionModel().clearSelection();
                tableView.refresh();
                updatePieChart();
            }
        }
    }
}