package net.novahc.smanagement;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    // FIELDS
    private int currentlyEnabledIndex = 0;
    private Button[] buttons;
    private Pane[] windowPanes;

    // MAIN PANES
    @FXML
    private AnchorPane mainPane;
    @FXML
    private AnchorPane sidePane;
    @FXML
    private FlowPane buttonPane;

    // SECONDARY PANES
    @FXML
    private Pane staffPane;
    @FXML
    private Pane attPane;
    @FXML
    private Pane umPane;
    @FXML
    private Pane setPane;

    // SIDEBAR BUTTONS
    @FXML
    private Button sbButton;
    @FXML
    private Button aButton;
    @FXML
    private Button umButton;
    @FXML
    private Button sButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Bind the X,Y Coordinates of the buttonPane (flow) to the AnchorPane within sidePane
        buttonPane.prefWidthProperty().bind(sidePane.widthProperty());
        buttonPane.prefHeightProperty().bind(sidePane.heightProperty());
        // Bind Buttons to buttonPane;
        bindButton(sbButton);
        bindButton(aButton);
        bindButton(umButton);
        bindButton(sButton);
        buttons = new Button[]{sbButton, aButton, umButton, sButton};
        windowPanes = new Pane[]{staffPane, attPane, umPane, setPane};
        for(Button button : buttons){
            button.setOnAction(event -> handleButtonClick((Button) event.getSource()));
        }


    }
    public void bindButton(Button b){
        b.prefWidthProperty().bind(sidePane.widthProperty());
        b.getStyleClass().add("sidebar-buttons");
    }

    private void handleButtonClick(Button clickedButton) {
        // Enable the previously disabled button
        buttons[currentlyEnabledIndex].setDisable(false);

        // Find the index of the clicked button and disable it
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == clickedButton) {
                currentlyEnabledIndex = i;
                buttons[i].setDisable(true);
                windowPanes[i].setVisible(true);
            }
            for(int j = 0; j < windowPanes.length; j++){
                if(j != currentlyEnabledIndex){
                    windowPanes[j].setVisible(false);
                }
            }
        }
    }
}