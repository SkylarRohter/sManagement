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
        // FXML element array inits
        buttons = new Button[]{sbButton, aButton, umButton, sButton};
        windowPanes = new Pane[]{staffPane, attPane, umPane, setPane};

        // Bind the X,Y Coordinates of the buttonPane (flow) to the AnchorPane within sidePane
        buttonPane.prefWidthProperty().bind(sidePane.widthProperty());
        buttonPane.prefHeightProperty().bind(sidePane.heightProperty());

        // Bind Buttons to buttonPane;
        for(Button b: buttons){
            bindButton(b);
        }
        // Bind windows
        for(Pane w :windowPanes){
            bindWindow(w);
        }

        for(Button button : buttons){
            button.setOnAction(event -> handleButtonClick((Button) event.getSource()));
        }


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
}