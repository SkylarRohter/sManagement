package net.novahc.smanagement;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    //MAIN PANES
    @FXML
    private AnchorPane mainPane;
    @FXML
    private AnchorPane sidePane;
    @FXML
    private FlowPane buttonPane;

    //SIDEBAR BUTTONS
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
        //Bind the X,Y Coordinates of the buttonPane (flow) to the AnchorPane within sidePane
        buttonPane.prefWidthProperty().bind(sidePane.widthProperty());
        buttonPane.prefHeightProperty().bind(sidePane.heightProperty());
        //Bind Buttons to buttonPane;
        bindButton(sbButton);
        bindButton(aButton);
        bindButton(umButton);
        bindButton(sButton);

    }
    public void bindButton(Button b){
        b.prefWidthProperty().bind(sidePane.widthProperty());
        b.getStyleClass().add("sidebar-buttons");
    }
}