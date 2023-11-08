package net.novahc.smanagement.functions;

import javafx.scene.control.Button;

import java.util.ArrayList;

public class Toggler {
    private ArrayList<Button> buttons;
    public Toggler(ArrayList<Button> buttons){
        this.buttons = buttons;
        init();
    }
    public void init(){
        for(Button b : buttons){
            b.setOnAction(event -> {
                toggleButton(b);
            });
        }
    }
    public void toggleButton(Button button){
        boolean toggled = button.isDisabled();
        button.setDisable(toggled);
    }
}
