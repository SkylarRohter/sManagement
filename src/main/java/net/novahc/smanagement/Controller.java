package net.novahc.smanagement;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import net.novahc.smanagement.Database.StudentManager;
import net.novahc.smanagement.functions.TableManager;
import net.novahc.smanagement.functions.Users.Student;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Flow;

public class Controller implements Initializable {
    // FIELDS
    private int currentlyEnabledIndex = 0;
    private Button[] buttons;
    private FlowPane[] windowPanes;
    private StudentManager studentManager;

    // MAIN PANES
    @FXML private AnchorPane mainPane;
    @FXML private AnchorPane sidePane;
    @FXML private FlowPane buttonPane;

    // SECONDARY PANES
    @FXML private FlowPane staffPane;
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


    // DIALOGUE PANE
    @FXML
    private AnchorPane dialoguePane;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // FXML element array inits
        buttons = new Button[]{sbButton, aButton, umButton, sButton};
        windowPanes = new FlowPane[]{staffPane, attPane, umPane, setPane};

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
        TableManager tableManager = new TableManager(tableView, studentManager);
        tableManager.init(name,age,present);
    }
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
    public void onToggleIsPresentClick(){
        studentManager.setPresence(tableView.getSelectionModel().getSelectedIndex());
        tableView.refresh();
    }
    public void onUpdateTableClick(){
        for(Student student : studentManager.getStudents()){
            student.setPresent(true);
        }
        tableView.refresh();
    }
    public void onResetButtonClick(){
        for(Student student : studentManager.getStudents()){
            student.setPresent(false);
        }
        tableView.refresh();
    }
}